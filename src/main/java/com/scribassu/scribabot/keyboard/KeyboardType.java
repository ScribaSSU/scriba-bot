package com.scribassu.scribabot.keyboard;

public enum KeyboardType {
    ButtonActions("ButtonActions.json"),
    ButtonDepartment("ButtonDepartment.json"),
    ButtonGroupType("ButtonGroupType.json"),
    ButtonFullTimeSchedule("ButtonFullTimeSchedule.json"),
    ButtonExtramuralSchedule("ButtonExtramuralSchedule.json"),
    ButtonSettings("ButtonSettings.json"),
    ButtonHours("ButtonHours.json"),
    ButtonCourse("ButtonCourse.json"),
    ButtonSettingsScheduleNotifications("ButtonSettingsScheduleNotifications.json"),
    ButtonSettingsExamPeriodNotifications("ButtonSettingsExamPeriodNotifications.json"),
    PartButtonEnableWeekFilter("PartButtonEnableWeekFilter.json"),
    PartButtonDisableWeekFilter("PartButtonDisableWeekFilter.json"),
    PartButtonDisableExamNotificationAfterTomorrow("PartButtonDisableExamNotificationAfterTomorrow.json"),
    PartButtonDisableExamNotificationDaily("PartButtonDisableExamNotificationDaily.json"),
    PartButtonDisableExamNotificationTomorrow("PartButtonDisableExamNotificationTomorrow.json"),
    PartButtonDisableScheduleNotificationDaily("PartButtonDisableScheduleNotificationDaily.json"),
    PartButtonDisableScheduleNotificationTomorrow("PartButtonDisableScheduleNotificationTomorrow.json"),
    PartButtonEnableExamNotificationAfterTomorrow("PartButtonEnableExamNotificationAfterTomorrow.json"),
    PartButtonEnableExamNotificationDaily("PartButtonEnableExamNotificationDaily.json"),
    PartButtonEnableExamNotificationTomorrow("PartButtonEnableExamNotificationTomorrow.json"),
    PartButtonEnableScheduleNotificationDaily("PartButtonEnableScheduleNotificationDaily.json"),
    PartButtonEnableScheduleNotificationTomorrow("PartButtonEnableScheduleNotificationTomorrow.json");

    private final String filename;

    public String getFilename() {
        return filename;
    }

    KeyboardType(String filename) {
        this.filename = filename;
    }
}
