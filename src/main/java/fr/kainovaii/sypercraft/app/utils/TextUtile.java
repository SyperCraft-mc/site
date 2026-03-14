package fr.kainovaii.sypercraft.app.utils;

import java.text.Normalizer;

public class TextUtile
{
    public static String slugify(String input)
    {
        if (input == null || input.isEmpty()) return "";
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .trim()
                .replaceAll("[\\s]+", "-")
                .replaceAll("-+", "-");
    }
}
