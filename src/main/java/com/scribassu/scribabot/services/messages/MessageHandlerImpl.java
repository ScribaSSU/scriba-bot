package com.scribassu.scribabot.services.messages;

import com.scribassu.scribabot.dto.BotMessage;
import com.scribassu.scribabot.dto.Command;
import com.scribassu.scribabot.dto.InnerBotUser;
import com.scribassu.scribabot.dto.rest.FullTimeLessonDto;
import com.scribassu.scribabot.entities.TgBotUser;
import com.scribassu.scribabot.entities.TgUnrecognizedMessage;
import com.scribassu.scribabot.entities.VkBotUser;
import com.scribassu.scribabot.entities.VkUnrecognizedMessage;
import com.scribassu.scribabot.generators.TgKeyboardGenerator;
import com.scribassu.scribabot.generators.VkKeyboardGenerator;
import com.scribassu.scribabot.repositories.TgBotUserRepository;
import com.scribassu.scribabot.repositories.TgUnrecognizedMessageRepository;
import com.scribassu.scribabot.repositories.VkBotUserRepository;
import com.scribassu.scribabot.repositories.VkUnrecognizedMessageRepository;
import com.scribassu.scribabot.services.CallRestService;
import com.scribassu.scribabot.services.bot.*;
import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.text.MessageText;
import com.scribassu.scribabot.util.BotMessageUtils;
import com.scribassu.scribabot.util.BotUserSource;
import com.scribassu.scribabot.util.Constants;
import com.scribassu.scribabot.util.DepartmentConverter;
import com.scribassu.tracto.domain.EducationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.scribassu.scribabot.text.MessageText.*;

@Service
public class MessageHandlerImpl implements MessageHandler {

    private final CallRestService callRestService;
    private final HelpService helpService;
    private final FullTimeLessonService fullTimeLessonService;
    private final ExamPeriodService examPeriodService;
    private final ExtramuralEventService extramuralEventService;
    private final VkBotUserRepository vkBotUserRepository;
    private final TgBotUserRepository tgBotUserRepository;
    private final SettingsService settingsService;
    private final StudentGroupService studentGroupService;
    private final TeacherService teacherService;
    private final VkUnrecognizedMessageRepository vkUnrecognizedMessageRepository;
    private final TgUnrecognizedMessageRepository tgUnrecognizedMessageRepository;
    private final VkKeyboardGenerator vkKeyboardGenerator;
    private final TgKeyboardGenerator tgKeyboardGenerator;

    @Value("#{'${scriba-bot.vk.mentioned-names}'.split(',')}")
    private List<String> mentionedNames;

    @Autowired
    public MessageHandlerImpl(CallRestService callRestService,
                              HelpService helpService,
                              FullTimeLessonService fullTimeLessonService,
                              ExamPeriodService examPeriodService,
                              ExtramuralEventService extramuralEventService,
                              VkBotUserRepository vkBotUserRepository,
                              TgBotUserRepository tgBotUserRepository,
                              SettingsService settingsService,
                              StudentGroupService studentGroupService,
                              TeacherService teacherService,
                              VkUnrecognizedMessageRepository vkUnrecognizedMessageRepository,
                              TgUnrecognizedMessageRepository tgUnrecognizedMessageRepository,
                              VkKeyboardGenerator vkKeyboardGenerator,
                              TgKeyboardGenerator tgKeyboardGenerator) {
        this.callRestService = callRestService;
        this.helpService = helpService;
        this.fullTimeLessonService = fullTimeLessonService;
        this.examPeriodService = examPeriodService;
        this.extramuralEventService = extramuralEventService;
        this.vkBotUserRepository = vkBotUserRepository;
        this.tgBotUserRepository = tgBotUserRepository;
        this.settingsService = settingsService;
        this.studentGroupService = studentGroupService;
        this.teacherService = teacherService;
        this.vkUnrecognizedMessageRepository = vkUnrecognizedMessageRepository;
        this.tgUnrecognizedMessageRepository = tgUnrecognizedMessageRepository;
        this.vkKeyboardGenerator = vkKeyboardGenerator;
        this.tgKeyboardGenerator = tgKeyboardGenerator;
    }

    @Override
    public BotMessage getBotMessage(Command command) {
        BotMessage botMessage = new BotMessage(
                MessageText.DEFAULT_MESSAGE,
                VkKeyboardGenerator.mainMenu,
                TgKeyboardGenerator.mainMenu());
        String message = command.getMessage(); // NO LOWER CASE HERE!
        String payload = command.getPayload().toLowerCase();
        String userId = command.getUserId();
        BotUserSource source = command.getBotUserSource();
        InnerBotUser botUser;
        VkBotUser vkBotUser = null;
        TgBotUser tgBotUser = null;
        boolean registered = false;
        if (BotUserSource.VK.equals(source)) {
            vkBotUser = vkBotUserRepository.findOneById(userId);
            if (null == vkBotUser) {
                botUser = new InnerBotUser(source, userId);
            } else {
                botUser = new InnerBotUser(vkBotUser);
                registered = true;
            }
        } else {
            tgBotUser = tgBotUserRepository.findOneById(userId);
            if (null == tgBotUser) {
                botUser = new InnerBotUser(source, userId);
            } else {
                botUser = new InnerBotUser(tgBotUser);
                registered = true;
            }
        }

        boolean isMentioned = false;

        for (String mentionedName : mentionedNames) {
            if (message.contains(mentionedName)) {
                // 1 for ] char
                message = message.substring(message.indexOf(mentionedName) + mentionedName.length() + 1);
                isMentioned = true;
                break;
            }
        }
        message = message.trim().toLowerCase();

        long userIdLong = Long.parseLong(userId);

        // It is mandatory to mention bot name in chats
        if (!isMentioned && userIdLong > Constants.PEER_ID_SHIFT) {
            return new BotMessage(DO_NOT_SEND);
        }

        if (!registered && !message.equals(CommandText.HELLO)
                && !message.equals(CommandText.MAIN_MENU)
                && !message.equals(CommandText.SHORT_MAIN_MENU)
                && !message.equals(CommandText.TG_START)) {
            botMessage = new BotMessage(GREETING_WITH_CHOOSE_DEPARTMENT);
            botUser.setUserId(userId); //DON'T SAVE! It is only for unrecognized messages check
            return botMessage;
        }

        if (registered
                && null != botUser.getPreviousUserMessage()
                && (botUser.getPreviousUserMessage().equalsIgnoreCase(CommandText.TEACHER_SCHEDULE)
                || botUser.getPreviousUserMessage().startsWith(Constants.TEACHER_ID)
                || botUser.getPreviousUserMessage().equalsIgnoreCase(CHOOSE_TEACHER_TO_GET_SCHEDULE))) {
            botMessage = teacherService.getBotMessage(message, botUser);
        }

        switch (message) {
            case CommandText.TG_START:
            case CommandText.HELLO:
                if (!registered) {
                    if (botUser.fromVk()) {
                        vkBotUser = new VkBotUser(userId);
                        vkBotUser = vkBotUserRepository.save(vkBotUser);
                        botMessage = new BotMessage(GREETING_WITH_CHOOSE_DEPARTMENT, VkKeyboardGenerator.departments);
                        vkBotUserRepository.updatePreviousUserMessage(GREETING_WITH_CHOOSE_DEPARTMENT, vkBotUser.getUserId());
                    } else {
                        tgBotUser = new TgBotUser(userId);
                        tgBotUser = tgBotUserRepository.save(tgBotUser);
                        botMessage = new BotMessage(GREETING_WITH_CHOOSE_DEPARTMENT, TgKeyboardGenerator.departments());
                        tgBotUserRepository.updatePreviousUserMessage(GREETING_WITH_CHOOSE_DEPARTMENT, tgBotUser.getUserId());
                    }
                } else {
                    if (botUser.fromVk()) {
                        botMessage = new BotMessage(GREETING, VkKeyboardGenerator.mainMenu);
                    } else {
                        botMessage = new BotMessage(GREETING, TgKeyboardGenerator.mainMenu());
                    }
                }
                break;
            case CommandText.MAIN_MENU:
            case CommandText.SHORT_MAIN_MENU:
                if (!registered) {
                    if (botUser.fromVk()) {
                        vkBotUser = new VkBotUser(userId);
                        vkBotUser = vkBotUserRepository.save(vkBotUser);
                        botMessage = new BotMessage(GREETING_WITH_CHOOSE_DEPARTMENT, VkKeyboardGenerator.departments);
                        vkBotUserRepository.updatePreviousUserMessage(GREETING_WITH_CHOOSE_DEPARTMENT, vkBotUser.getUserId());
                    } else {
                        tgBotUser = new TgBotUser(userId);
                        tgBotUser = tgBotUserRepository.save(tgBotUser);
                        botMessage = new BotMessage(GREETING_WITH_CHOOSE_DEPARTMENT, TgKeyboardGenerator.departments());
                        tgBotUserRepository.updatePreviousUserMessage(GREETING_WITH_CHOOSE_DEPARTMENT, tgBotUser.getUserId());
                    }
                } else {
                    if (botUser.fromVk()) {
                        vkBotUserRepository.updatePreviousUserMessage("", botUser.getUserId());
                        botMessage = new BotMessage(RETURN_MAIN_MENU, VkKeyboardGenerator.mainMenu);
                    } else {
                        tgBotUserRepository.updatePreviousUserMessage("", botUser.getUserId());
                        botMessage = new BotMessage(RETURN_MAIN_MENU, TgKeyboardGenerator.mainMenu());
                    }
                }
                break;
            case CommandText.HELP:
                botMessage = helpService.getBotMessage(message, botUser);
                break;
            case CommandText.THANKS:
                if (botUser.fromVk()) {
                    botMessage = new BotMessage(YOU_ARE_WELCOME, VkKeyboardGenerator.mainMenu);
                } else {
                    botMessage = new BotMessage(YOU_ARE_WELCOME, TgKeyboardGenerator.mainMenu());
                }
                break;
            case CommandText.TEACHER_SCHEDULE:
                botMessage = teacherService.getBotMessage(message, botUser);
                break;
            case CommandText.STUDENTS_SCHEDULE:
                if (BotMessageUtils.isBotUserFullTime(botUser)) {
                    botMessage = botUser.fromVk() ?
                            new BotMessage(CHOOSE_WANTED_SCHEDULE, VkKeyboardGenerator.fullTimeSchedule)
                            : new BotMessage(CHOOSE_WANTED_SCHEDULE, TgKeyboardGenerator.fullTimeSchedule());
                } else if (BotMessageUtils.isBotUserExtramural(botUser)) {
                    botMessage = botUser.fromVk() ?
                            new BotMessage(CHOOSE_WANTED_SCHEDULE, VkKeyboardGenerator.extramuralSchedule)
                            : new BotMessage(CHOOSE_WANTED_SCHEDULE, TgKeyboardGenerator.extramuralSchedule());
                } else {
                    botMessage = botUser.fromVk() ?
                            new BotMessage(CANNOT_GET_SCHEDULE_GROUP_NOT_SET, VkKeyboardGenerator.departments)
                            : new BotMessage(CANNOT_GET_SCHEDULE_GROUP_NOT_SET, TgKeyboardGenerator.departments());
                }
                break;
            case CommandText.CHOOSE_DEPARTMENT:
                botMessage = botUser.fromVk() ?
                        new BotMessage(CHOOSE_DEPARTMENT, VkKeyboardGenerator.departments)
                        : new BotMessage(CHOOSE_DEPARTMENT, TgKeyboardGenerator.departments());
                break;
            case CommandText.FULL_TIME:
                if (botUser.fromVk()) {
                    vkBotUserRepository.updateEducationForm(EducationForm.DO.getGroupType(), userId);
                    botMessage = new BotMessage(CHOOSE_COURSE, VkKeyboardGenerator.courses);
                } else {
                    tgBotUserRepository.updateEducationForm(EducationForm.DO.getGroupType(), userId);
                    botMessage = new BotMessage(CHOOSE_COURSE, TgKeyboardGenerator.courses());
                }
                break;
            case CommandText.EXTRAMURAL:
                if (botUser.fromVk()) {
                    vkBotUserRepository.updateEducationForm(EducationForm.ZO.getGroupType(), userId);
                    botMessage = new BotMessage(CHOOSE_COURSE, VkKeyboardGenerator.courses);
                } else {
                    tgBotUserRepository.updateEducationForm(EducationForm.ZO.getGroupType(), userId);
                    botMessage = new BotMessage(CHOOSE_COURSE, TgKeyboardGenerator.courses());
                }
                break;
            case CommandText.EVENING:
                if (botUser.fromVk()) {
                    vkBotUserRepository.updateEducationForm(EducationForm.VO.getGroupType(), userId);
                    botMessage = new BotMessage(CHOOSE_COURSE, VkKeyboardGenerator.courses);
                } else {
                    tgBotUserRepository.updateEducationForm(EducationForm.VO.getGroupType(), userId);
                    botMessage = new BotMessage(CHOOSE_COURSE, TgKeyboardGenerator.courses());
                }
                break;
            case CommandText.MONDAY:
            case CommandText.TUESDAY:
            case CommandText.WEDNESDAY:
            case CommandText.THURSDAY:
            case CommandText.FRIDAY:
            case CommandText.SATURDAY:
                botMessage = fullTimeLessonService.getBotMessage(message, botUser);
                break;
            case CommandText.TODAY:
            case CommandText.TOMORROW:
            case CommandText.YESTERDAY:
                if (BotMessageUtils.isBotUserFullTime(botUser)) {
                    botMessage = fullTimeLessonService.getBotMessage(message, botUser);
                } else {
                    botMessage = extramuralEventService.getBotMessage(message, botUser);
                }
                break;
            case CommandText.ALL_LESSONS:
                if (BotMessageUtils.isBotUserExtramural(botUser)) {
                    botMessage = extramuralEventService.getBotMessage(message, botUser);
                } else {
                    botMessage = fullTimeLessonService.getBotMessage(message, botUser);
                }
                break;
            case CommandText.TEACHER_SCHEDULE_FOR_EXTRAMURAL:
                if (botUser.wantTeacherSchedule()) {
                    botMessage = teacherService.getBotMessage(message, botUser);
                }
                break;
            case CommandText.SETTINGS:
                botMessage = botUser.fromVk() ?
                        new BotMessage(SETTINGS_MENU, vkKeyboardGenerator.settings(botUser))
                        : new BotMessage(SETTINGS_MENU, tgKeyboardGenerator.settings(botUser));
                break;
            case CommandText.SET_SEND_SCHEDULE_TIME_TODAY:
            case CommandText.ENABLE_SEND_SCHEDULE_TODAY:
            case CommandText.DISABLE_SEND_SCHEDULE_TODAY:
            case CommandText.SET_SEND_SCHEDULE_TIME_TOMORROW:
            case CommandText.ENABLE_SEND_SCHEDULE_TOMORROW:
            case CommandText.DISABLE_SEND_SCHEDULE_TOMORROW:
            case CommandText.SET_SEND_EXAM_PERIOD_TIME_TODAY:
            case CommandText.ENABLE_SEND_EXAM_PERIOD_TODAY:
            case CommandText.DISABLE_SEND_EXAM_PERIOD_TODAY:
            case CommandText.SET_SEND_EXAM_PERIOD_TIME_TOMORROW:
            case CommandText.ENABLE_SEND_EXAM_PERIOD_TOMORROW:
            case CommandText.DISABLE_SEND_EXAM_PERIOD_TOMORROW:
            case CommandText.SET_SEND_EXAM_PERIOD_TIME_AFTER_TOMORROW:
            case CommandText.ENABLE_SEND_EXAM_PERIOD_AFTER_TOMORROW:
            case CommandText.DISABLE_SEND_EXAM_PERIOD_AFTER_TOMORROW:
            case CommandText.SEND_EXAM_PERIOD:
            case CommandText.SEND_SCHEDULE:
            case CommandText.ENABLE_FILTER_WEEK_TYPE:
            case CommandText.DISABLE_FILTER_WEEK_TYPE:
            case CommandText.ENABLE_SEND_EMPTY_SCHEDULE_NOTIFICATION:
            case CommandText.DISABLE_SEND_EMPTY_SCHEDULE_NOTIFICATION:
            case CommandText.CURRENT_USER_SETTINGS:
            case CommandText.ENABLE_SEND_KEYBOARD:
            case CommandText.DISABLE_SEND_KEYBOARD:
            case CommandText.DELETE_PROFILE:
            case CommandText.YES:
            case CommandText.NO:
                botMessage = settingsService.getBotMessage(message, botUser);
                break;
            case CommandText.EXAMS:
                botMessage = examPeriodService.getBotMessage(message, botUser);
                break;
        }

        if (CommandText.HOUR_PATTERN.matcher(message).matches()) {
            botMessage = settingsService.getBotMessage(message, botUser);
        }

        if (CommandText.DEPARTMENT_PAYLOAD.equalsIgnoreCase(payload)
                || CommandText.DEPARTMENT_PATTERN.matcher(message).matches()) {
            if (botUser.fromVk()) {
                vkBotUserRepository.updateDepartment(DepartmentConverter.convertToUrl(message), userId);
                botMessage = new BotMessage(MessageText.CHOOSE_EDUCATION_FORM, VkKeyboardGenerator.educationForms);
            } else {
                tgBotUserRepository.updateDepartment(DepartmentConverter.convertToUrl(message), userId);
                botMessage = new BotMessage(MessageText.CHOOSE_EDUCATION_FORM, TgKeyboardGenerator.educationForms());
            }
        }

        if (CommandText.COURSE_PAYLOAD.equalsIgnoreCase(payload)) {
            botMessage = studentGroupService.getBotMessage(message, botUser);
        }

        if (CommandText.CHOOSE_STUDENT_GROUP.equalsIgnoreCase(payload)) {
            if (botUser.fromVk()) {
                vkBotUserRepository.updateGroupNumber(message, userId);
            } else {
                tgBotUserRepository.updateGroupNumber(message, userId);
            }
            if (null != botUser.getPreviousUserMessage()
                    && botUser.getPreviousUserMessage().equalsIgnoreCase(GREETING_WITH_CHOOSE_DEPARTMENT)) {
                if (botUser.fromVk()) {
                    botMessage = new BotMessage(THIS_IS_MAIN_MENU, VkKeyboardGenerator.mainMenu);
                    vkBotUserRepository.updatePreviousUserMessage("", botUser.getUserId());
                } else {
                    botMessage = new BotMessage(THIS_IS_MAIN_MENU, TgKeyboardGenerator.mainMenu());
                    tgBotUserRepository.updatePreviousUserMessage("", botUser.getUserId());
                }

            } else {
                if (botUser.fromVk()) {
                    if (BotMessageUtils.isBotUserFullTime(botUser)) {
                        botMessage = new BotMessage(MessageText.FINISH_SET_GROUP, VkKeyboardGenerator.fullTimeSchedule);
                    } else {
                        botMessage = new BotMessage(FINISH_SET_GROUP, VkKeyboardGenerator.extramuralSchedule);
                    }
                } else {
                    if (BotMessageUtils.isBotUserFullTime(botUser)) {
                        botMessage = new BotMessage(MessageText.FINISH_SET_GROUP, TgKeyboardGenerator.fullTimeSchedule());
                    } else {
                        botMessage = new BotMessage(FINISH_SET_GROUP, TgKeyboardGenerator.extramuralSchedule());
                    }
                }
            }
        }

        if (message.startsWith(CommandText.GROUP_NUMBER_INPUT)) {
            if (botUser.fromVk()) {
                vkBotUserRepository.updateGroupNumber(message.substring(2), userId);
            } else {
                tgBotUserRepository.updateGroupNumber(message.substring(2), userId);
            }
            if (null != botUser.getPreviousUserMessage()
                    && botUser.getPreviousUserMessage().equalsIgnoreCase(GREETING_WITH_CHOOSE_DEPARTMENT)) {
                if (botUser.fromVk()) {
                    botMessage = new BotMessage(THIS_IS_MAIN_MENU, VkKeyboardGenerator.mainMenu);
                    vkBotUserRepository.updatePreviousUserMessage("", botUser.getUserId());
                } else {
                    botMessage = new BotMessage(THIS_IS_MAIN_MENU, TgKeyboardGenerator.mainMenu());
                    tgBotUserRepository.updatePreviousUserMessage("", botUser.getUserId());
                }

            } else {
                if (botUser.fromVk()) {
                    botMessage = new BotMessage(MessageText.FINISH_SET_GROUP, VkKeyboardGenerator.fullTimeSchedule);
                } else {
                    botMessage = new BotMessage(MessageText.FINISH_SET_GROUP, TgKeyboardGenerator.fullTimeSchedule());
                }
            }
        }

        if (message.startsWith("р ")) {
            String[] params = message.split(" ");
            FullTimeLessonDto lessons = callRestService.getFullTimeLessonsByDay(params[1], params[2], params[3]);
            botMessage = BotMessageUtils.getBotMessageForFullTimeLessons(lessons, "", botUser.isFilterNomDenom(), botUser);
        }

        if (payload.startsWith(CommandText.TEACHER_ID_PAYLOAD)) {
            botMessage = teacherService.getBotMessage(payload, botUser);
        }

        if (CommandText.COURSE_PATTERN.matcher(message).matches()) {
            botMessage = studentGroupService.getBotMessage(message, botUser);
        }

        if (botMessage.isDefault()) {
            if (botUser.fromVk()) {
                vkUnrecognizedMessageRepository.save(new VkUnrecognizedMessage(command, vkBotUser));
            } else {
                tgUnrecognizedMessageRepository.save(new TgUnrecognizedMessage(command, tgBotUser));
            }
        }

        botMessage.setBotUser(botUser);
        return botMessage;
    }
}
