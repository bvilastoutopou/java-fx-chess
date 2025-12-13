package com.example.chess;

import java.util.Locale;
import java.util.ResourceBundle;

public class Language {
    private static Locale locale = Locale.ENGLISH;

    public static void setLocale(String code){
        switch (code) {
            case "el": locale = new Locale("el"); break;
            case "de": locale = Locale.GERMAN; break;
            case "fr": locale = Locale.FRENCH; break;
            case "es": locale = new Locale("es"); break;
            case "it": locale = new Locale("it"); break;
            default: locale = Locale.ENGLISH;
        }
    }

    public static ResourceBundle getBundle() {
        return ResourceBundle.getBundle("com.example.chess.lang.lang", locale);
    }
}
