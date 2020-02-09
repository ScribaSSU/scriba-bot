package com.scribassu.scribabot.keyboard;

public enum KeyboardType {
    ButtonActions("ButtonActions.json"),
    ButtonAddActions("ButtonAddActions.json"),
    ButtonAddEditDelete("ButtonAddEditDelete.json"),
    ButtonAgreement("ButtonAgreement.json"),
    ButtonDepartment("ButtonDepartment.json"),
    ButtonGroupType("ButtonGroupType.json"),
    ButtonMailing("ButtonMailing.json"),
    ButtonSchedule("ButtonSchedule.json"),
    ButtonFullTimeSchedule("ButtonFullTimeSchedule.json"),
    ButtonSettings("ButtonSettings.json"),
    ButtonHours("ButtonHours.json");

    private String filename;

    public String getFilename() {
        return filename;
    }

    KeyboardType(String filename) {
        this.filename = filename;
    }
}
