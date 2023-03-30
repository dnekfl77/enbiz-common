package com.enbiz.common.base;

import java.util.List;

import com.enbiz.common.base.exception.ValidationException;

public final class Validator {
    private Validator() {

    }

    public static final void throwIfNull(Object value, String message) {
        if (value == null) {
            throw new ValidationException(message);
        }
    }

    public static final void throwIfNotNull(Object value, String message) {
        if (value != null) {
            throw new ValidationException(message);
        }
    }

    public static final void throwIfEmpty(Object value, String message) {
        String strValue;
        if (value instanceof String) {
            strValue = (String) value;
        } else {
            strValue = String.valueOf(value);
        }

        if (value == null || strValue.trim().length() == 0) {
            throw new ValidationException(message);
        }
    }
    
    public static final void throwIfEmpty(List<String> value, String message) {
        if (value == null || value.size() == 0) {
            throw new ValidationException(message);
        }
    }

    private static final String objToString(Object value){
        String strValue;
        if (value instanceof String) {
            strValue = (String) value;
        } else {
            strValue = String.valueOf(value);
        }
        return strValue;
    }

    public static final void throwIfEqual(Object value1, Object value2, String message) {
        String strValue1 = objToString(value1);
        String strValue2 = objToString(value2);

        if (strValue1.equals(strValue2)) {
            throw new ValidationException(message);
        }
    }

    public static final void throwIfNotEqual(Object value1, Object value2, String message) {
        String strValue1 = objToString(value1);
        String strValue2 = objToString(value2);

        if (!strValue1.equals(strValue2)) {
            throw new ValidationException(message);
        }
    }

    public static final void throwIfNotBetween(int value, int from, int to, String message) {
        if (value < from || value > to) {
            throw new ValidationException(message);
        }
    }

    public static final void throwIfMatches(String value, String regex, String message) {
        if (value != null && regex != null && (value.matches(regex))) {
            throw new ValidationException(message);
        }
    }

    public static final void throwIfNotMatches(String value, String regex, String message) {
        if (value == null || regex == null || !(value.matches(regex))) {
            throw new ValidationException(message);
        }
    }

    public static final void throwIfShorterThan(String value, int length, String message) {
        if (value == null || value.length() < length) {
            throw new ValidationException(message);
        }
    }

    public static final void throwIfLongerThan(String value, int length, String message) {
        if (value == null || value.length() > length) {
            throw new ValidationException(message);
        }
    }

    public static void throwIfNotContains(String value, String contained, String message) {
        if (value == null || contained == null || !value.contains(contained)) {
            throw new ValidationException(message);
        }
    }

    public static void throwIfNotSameLength(String value, int length, String message) {
        if (value == null || value.length() != length) {
            throw new ValidationException(message);
        }
    }

    public static final void throwIfGreaterThan(long value, long max, String message) {
        if (value > max) {
            throw new ValidationException(message);
        }
    }

    public static final void throwIfLesserThan(long value, long min, String message) {
        if (value < min) {
            throw new ValidationException(message);
        }
    }

    public static final int throwIfNotInteger(Object value, String message) {
        try {
            return Integer.parseInt((String) value);
        } catch (Exception e) {
            throw new ValidationException(message, e);
        }
    }

    public static final void throwIfNotNumber(Object value, String message) {
        try {
            Double.parseDouble((String) value);
        } catch (Exception e) {
            throw new ValidationException(message, e);
        }
    }

    public static final void throwIfTrue(boolean condition, String message) {
        if (condition) {
            throw new ValidationException(message);
        }
    }

    public static final void throwIfFalse(boolean condition, String message) {
        throwIfTrue(!condition, message);
    }
}
