package fr.pinguet62.jxb.swagger;

import static java.util.regex.Pattern.compile;

import java.util.regex.Matcher;

public class RegexUtils {

    static String get(String source, String regex) {
        Matcher matcher = compile(regex).matcher(source);
        if (!matcher.find())
            return null;
        return matcher.group();
    }

}