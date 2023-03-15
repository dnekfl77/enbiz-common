package com.x2bee.common.base.exception;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MessageResolver {
    private static MessageSource messageSource;
    
    @Autowired
    public void init(MessageSource messageSource) {
    	MessageResolver.messageSource = messageSource;
    }

    public static String getMessage(String messageKey) {
    	return messageSource.getMessage(messageKey, new String[] {}, LocaleContextHolder.getLocale());
    }

    public static String getMessage(String messageKey, Object[] args) {
        if (args == null || args.length == 0) {
            return getMessage(messageKey);
        }
    	return messageSource.getMessage(messageKey, args, LocaleContextHolder.getLocale());
    }

    public static String getMessage(String messageKey, Locale locale) {
    	return messageSource.getMessage(messageKey, new String[] {}, locale);
    }

    public static String getMessage(String messageKey, Object[] args, Locale locale) {
        if (args == null || args.length == 0) {
            return getMessage(messageKey, locale);
        }

        return getMessage(messageKey, args, locale);
    }

    public static String getMessage(AppError appError) {
    	return messageSource.getMessage(appError.getMessageKey(), new String[] {}, LocaleContextHolder.getLocale());
    }

    public static String getMessage(AppError appError, Object[] args) {
        if (args == null || args.length == 0) {
            return getMessage(appError);
        }
    	return messageSource.getMessage(appError.getMessageKey(), args, LocaleContextHolder.getLocale());
    }

    public static String getMessage(AppError appError, Locale locale) {
    	return messageSource.getMessage(appError.getMessageKey(), new String[] {}, locale);
    }

    public static String getMessage(AppError appError, Object[] args, Locale locale) {
        if (args == null || args.length == 0) {
            return getMessage(appError, locale);
        }

        return getMessage(appError, args, locale);
    }

    public static String getMessage(AppError appError, Object[] args, String defaultMessage) {
        return getMessage(appError, args, defaultMessage, LocaleContextHolder.getLocale());
    }

    public static String getMessage(AppError appError, Object[] args, String defaultMessage, Locale locale) {
        return messageSource.getMessage(appError.getMessageKey(), args, defaultMessage, locale);
    }
}
