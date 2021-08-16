package com.scribassu.scribabot.services.bot;

import com.scribassu.scribabot.dto.BotMessage;
import com.scribassu.scribabot.dto.InnerBotUser;
import com.scribassu.scribabot.dto.rest.ExtramuralDto;
import com.scribassu.scribabot.entities.BotUser;
import com.scribassu.scribabot.services.CallRestService;
import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.util.BotMessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExtramuralEventService implements BotMessageService {

    private final CallRestService callRestService;

    @Autowired
    public ExtramuralEventService(CallRestService callRestService) {
        this.callRestService = callRestService;
    }

    @Override
    public BotMessage getBotMessage(String message, InnerBotUser botUser) {
        if (message.equalsIgnoreCase(CommandText.EXTRAMURAL_SCHEDULE)) {
            ExtramuralDto extramuralDto = callRestService.getExtramuralEventsByGroup(
                    botUser.getDepartment(),
                    botUser.getGroupNumber()
            );

            if (null == extramuralDto|| extramuralDto.getExtramuralEvents().isEmpty()) {
                return BotMessageUtils.getBotMessageForEmptyFullTimeExamPeriod(botUser);
            } else {
                return BotMessageUtils.getBotMessageForExtramuralEvent(extramuralDto, "", botUser);
            }
        } else {
            return BotMessageUtils.getBotMessageForEmptyExtramuralEvents(botUser);
        }
    }
}
