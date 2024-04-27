package com.company.server;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mike Kostenko on 31/03/2024
 */
public class UserNamePainter {
    @Getter
    private static final String POSTFIX = "\033[0m";
    private static final Map<String, String> COLORS = new HashMap<>() {{
        put("Black", "\033[0;30m");
        put("Red", "\033[0;31m");
        put("Green", "\033[0;32m");
        put("Yellow", "\033[0;33m");
        put("Blue", "\033[0;34m");
        put("Purple", "\033[0;35m");
        put("Cyan", "\033[0;36m");
        put("Light Gray", "\033[0;37m");
        put("Dark Gray", "\033[1;30m");
        put("Light Red", "\033[1;31m");
        put("Light Green", "\033[1;32m");
        put("Light Yellow", "\033[1;33m");
        put("Light Blue", "\033[1;34m");
        put("Light Purple", "\033[1;35m");
        put("Light Cyan", "\033[1;36m");
        put("White", "\033[1;37m");
        put("BlackItalic", "\033[3;30m");
        put("RedItalic", "\033[3;31m");
        put("GreenItalic", "\033[3;32m");
        put("YellowItalic", "\033[3;33m");
        put("BlueItalic", "\033[3;34m");
        put("PurpleItalic", "\033[3;35m");
        put("CyanItalic", "\033[3;36m");
        put("LightGrayItalic", "\033[3;37m");
    }};
    private static List<String> colorValues;

    public static String getUserColor(String userName) {
        if (colorValues == null) {
            colorValues = new ArrayList<>(COLORS.values());
        }
        int index = Math.abs(userName.hashCode() % colorValues.size());
        return colorValues.get(index);

    }
}
