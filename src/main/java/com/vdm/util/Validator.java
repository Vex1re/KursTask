package com.vdm.util;

import java.util.regex.Pattern;

public class Validator {
    private static final String PASSWORD_PATTERN = 
        "^(?=.*[0-9])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$";

    private static final String NUMBER_PATTERN =
            "^\\+7\\(\\d{3}\\)\\d{3}-\\d{2}-\\d{2}$";

    private static final String NAME_PATTERN =
            "^[А-ЯЁ][а-яё]+(-[А-ЯЁ][а-яё]+)? [А-ЯЁ][а-яё]+ [А-ЯЁ][а-яё]+$";

    private static final Pattern passwordPattern = Pattern.compile(PASSWORD_PATTERN);
    private static final Pattern numberPattern = Pattern.compile(NUMBER_PATTERN);
    private static final Pattern namePattern = Pattern.compile(NAME_PATTERN);


    public static boolean isValidPassword(String password) {
        if (password == null) return false;
        return (passwordPattern.matcher(password).matches());
    }

    public static boolean isValidNumber(String number) {
        if (number == null) return false;
        return (numberPattern.matcher(number).matches());
    }

    public static boolean isValidName(String name) {
        if (name == null) return false;
        return (namePattern.matcher(name).matches());
    }
}
