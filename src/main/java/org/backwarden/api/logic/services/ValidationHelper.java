package org.backwarden.api.logic.services;

import java.util.regex.Pattern;

public class ValidationHelper {

    private static final String EMAIL_REGEX = "^((?!\\.)[\\w-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$";
    private static final Pattern PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final String PASSWORD_REGEX = "^(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[^\\w\\d\\s:])([^\\s]){12,30}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

    public static boolean isMailValid(String mail) {
        if (mail == null || mail.isEmpty()) {
            return false;
        }
        return PATTERN.matcher(mail).matches();
    }

    public static boolean isPasswordValid(String password, String mail) {
        if (password == null || password.isEmpty() || password.equals(mail)) {
            return false;
        }
        return PASSWORD_PATTERN.matcher(password).matches();
    }
}
