package talkychefserver.config;

public class Constants {
    public static final String API_VERSION = "1";
    public static final String BASE_API_PATH = "/api/v" + API_VERSION;
    public static final int MAX_ITEMS_PER_PAGE = 10;
    public static final String RECOGNIZER_URL = "http://recognizer:8000/recognize";

    public static final String LOGIN_PATTERN = "^[\\w-]+$";
    public static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[^0-9\\s]).*$";
    public static final String EMAIL_PATTERN = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)" +
            "*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e" +
            "-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2" +
            "(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])" +
            "|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b" +
            "\\x0c\\x0e-\\x7f])+)\\])";
    public static final int LOGIN_MIN_SYMBOLS = 4;
    public static final int LOGIN_MAX_SYMBOLS = 32;
    public static final int PASSWORD_MIN_SYMBOLS = 4;
    public static final int PASSWORD_MAX_SYMBOLS = 32;

}
