package com.scribassu.scribabot.util;

public class Constants {

    public static final String OK = "ok";

    public static final String KEY_MESSAGE = "message";
    public static final String KEY_KEYBOARD = "keyboard";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_OBJECT = "object";
    public static final String KEY_FROM_ID = "from_id";
    public static final String KEY_PEER_ID = "peer_id";
    public static final String KEY_TEXT = "text";
    public static final String KEY_PAYLOAD = "payload";

    public static final String TYPE_CONFIRMATION = "confirmation";
    public static final String TYPE_MESSAGE_NEW = "message_new";

    public static final String PAYLOAD = "{\"button\": \"%s\"}";
    public static final String PAYLOAD_START = "{\"button\": \"";
    public static final String PAYLOAD_END = "\"}";
    public static final String DEFAULT_PAYLOAD = "{\"button\": \"1\"}";

    public static final String TEACHER_ID = "TEACHER_ID ";

    public static final int MAX_KEYBOARD_TEXT_LENGTH = 40;
    public static final int MAX_VK_KEYBOARD_SIZE_FOR_LISTS = 38; //render lists without counting menu buttons
    public static final long PEER_ID_SHIFT = 2000000000;
}
