package com.example.capstone_2.util;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class StylesHandler {
    private static final List<String> GOOD_COLORS = Arrays.asList(
            "lightgray", "lightblue", "lightcoral", "lightgreen", "lightpink",
            "lightseagreen", "lightskyblue", "lightsteelblue", "lightyellow",
            "aliceblue", "antiquewhite", "aqua", "aquamarine", "azure",
            "beige", "bisque", "blanchedalmond", "burlywood",
            "cadetblue", "chartreuse", "cornflowerblue", "cornsilk", "cyan",
            "darkgray", "darkkhaki", "darkseagreen", "darkturquoise", "darkviolet",
            "deepskyblue", "floralwhite", "gainsboro", "ghostwhite", "gold",
            "goldenrod", "honeydew", "ivory", "khaki", "lavender",
            "lavenderblush", "lemonchiffon", "lightcyan", "lightgoldenrodyellow", "linen",
            "mintcream", "mistyrose", "moccasin", "navajowhite", "oldlace",
            "palegoldenrod", "palegreen", "paleturquoise", "papayawhip", "peachpuff",
            "powderblue", "rosybrown", "seashell", "silver", "thistle",
            "tomato", "turquoise", "wheat", "whitesmoke", "yellowgreen"
    );

    public static String getColor()
    {
        int size = GOOD_COLORS.size() -1;
        Random rand = new Random();
        int idx = rand.nextInt(size);

        return GOOD_COLORS.get(idx);
    }

    public static String getLinearGradient() {
        if (GOOD_COLORS.isEmpty()) {
            // Handle the case where the list is empty
            return "linear-gradient(to right, #cccccc, #999999)"; // Default gradient or handle differently
        }

        int size = GOOD_COLORS.size();
        Random rand = new Random();
        int idx1 = rand.nextInt(size);
        int idx2 = rand.nextInt(size);

        // Create a linear gradient from two randomly selected colors
        String color1 = GOOD_COLORS.get(idx1);
        String color2 = GOOD_COLORS.get(idx2);

        return "linear-gradient(to right, " + color1 + ", " + color2 + ")";
    }


}
