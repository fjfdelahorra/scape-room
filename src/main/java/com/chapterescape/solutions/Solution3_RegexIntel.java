package com.chapterescape.solutions;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solución Puzzle3: extraer REDIS correctamente del campo tag=[...].
 */
public class Solution3_RegexIntel {
    public static void main(String[] args) {
        var lines = List.of(
                "2025-08-21 INFO payload id=001 user=42 meta=[ignore] tag=[RE] extra=ok trail=[x]",
                "2025-08-21 INFO payload id=002 user=42 meta=[foo]    tag=[D]  extra=ok trail=[y]",
                "2025-08-21 INFO payload id=003 user=42 meta=[bar]    tag=[I]  extra=ok trail=[z]",
                "2025-08-21 INFO payload id=004 user=42 meta=[baz]    tag=[S]  extra=ok trail=[w]"
        );
        // Anclar exactamente al campo tag=[...]; capturamos letras mayúsculas dentro.
        Pattern p = Pattern.compile("tag=\\[([A-Z]{1,4})\\]");
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            Matcher m = p.matcher(line);
            if (m.find()) {
                String grp = m.group(1); // Ej: RE, D, I, S
                if ("RE".equals(grp)) {
                    sb.append("RE");
                } else {
                    sb.append(grp.charAt(0));
                }
            }
        }
        String token = sb.toString();
        if (!"REDIS".equals(token)) {
            throw new IllegalStateException("Token inesperado: " + token);
        }
        System.out.println(token); // REDIS
    }
}

