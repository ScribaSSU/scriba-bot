package com.scribassu.scribabot.keyboard;

public enum KeyboardType {
    ButtonActions("ButtonActions.json"),
    ButtonDepartment("ButtonDepartment.json"),
    ButtonGroupType("ButtonGroupType.json"),
    ButtonFullTimeSchedule("ButtonFullTimeSchedule.json"),
    ButtonSettings("ButtonSettings.json"),
    ButtonHours("ButtonHours.json"),
    ButtonCourse("ButtonCourse.json");

    private String filename;

    public String getFilename() {
        return filename;
    }

    KeyboardType(String filename) {
        this.filename = filename;
    }
}
