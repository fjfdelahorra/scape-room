package com.chapterescape;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Objetivo: de estas líneas sacar "REDIS" concatenando el contenido de [x] correcto en orden.
 * Bug: regex demasiado codiciosa / grupos mal definidos.
 */
public class Puzzle3_RegexIntel {

    public static void main(String[] args) {
        var lines = List.of(
                "2025-08-21 INFO payload id=001 user=42 meta=[ignore] tag=[RE] extra=ok trail=[x]",
                "2025-08-21 INFO payload id=002 user=42 meta=[foo]    tag=[D]  extra=ok trail=[y]",
                "2025-08-21 INFO payload id=003 user=42 meta=[bar]    tag=[I]  extra=ok trail=[z]",
                "2025-08-21 INFO payload id=004 user=42 meta=[baz]    tag=[S]  extra=ok trail=[w]"
        );

        // BUG: codiciosa y no anclada al campo 'tag'
        Pattern p = Pattern.compile("tag=(.*)");

        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            Matcher m = p.matcher(line);
            if (m.find()) {
                var chunk = m.group(1);
                // Lógica de ensamblado (correcta), pero falla si la captura es basura
                if (chunk.length() >= 2 && chunk.startsWith("RE")) {
                    sb.append("RE");
                } else if (!chunk.isEmpty()) {
                    sb.append(chunk.charAt(0));
                }
            }
        }
        System.out.println(sb.toString()); // Con el bug: basura. Arreglado: REDIS.
    }
}
