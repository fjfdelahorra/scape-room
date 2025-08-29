package com.chapterescape.solutions;

/**
 * Solución Puzzle1: construir VERTX desde la cadena ruidosa.
 * Estrategia: buscar secuencialmente las letras de TARGET en orden (case-insensitive).
 */
public class Solution1_OffByOne {
    private static final String NOISY = "VxEeQZrtYTXr—noep—XR!@#T$%^&*()";
    private static final String TARGET = "VERTX";

    public static void main(String[] args) {
        StringBuilder out = new StringBuilder();
        int ti = 0;
        for (int i = 0; i < NOISY.length() && ti < TARGET.length(); i++) {
            char c = NOISY.charAt(i);
            if (Character.toUpperCase(c) == TARGET.charAt(ti)) {
                out.append(TARGET.charAt(ti));
                ti++;
            }
        }
        String token = out.toString();
        if (!TARGET.equals(token)) {
            throw new IllegalStateException("No se pudo reconstruir token: " + token);
        }
        System.out.println(token); // VERTX
    }
}
