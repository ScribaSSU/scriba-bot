package com.scribassu.scribabot.keyboard;

public enum KeyboardType {
    ButtonActions("ButtonActions.json"),
    ButtonConfirmDeletion("ButtonConfirmDeletion.json"),
    ButtonDepartment("ButtonDepartment.json"),
    ButtonGroupType("ButtonGroupType.json"),
    ButtonFullTimeSchedule("ButtonFullTimeSchedule.json"),
    ButtonExtramuralSchedule("ButtonExtramuralSchedule.json"),
    ButtonHours("ButtonHours.json"),
    ButtonCourse("ButtonCourse.json");

    private final String filename;

    public String getFilename() {
        return filename;
    }

    KeyboardType(String filename) {
        this.filename = filename;
    }
}
