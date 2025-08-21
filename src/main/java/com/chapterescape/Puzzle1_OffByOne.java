package com.chapterescape;

import java.util.List;

/**
 * Objetivo: imprimir VERTX
 * Pista: el mensaje está escondido tomando letras en posiciones correctas.
 * Bug: límites e incremento.
 */
public class Puzzle1_OffByOne {

    public static void main(String[] args) {
        String noisy = "VxEeQZrtYTXr—noep—XR!@#T$%^&*()";
        int start = 0;
        int step  = 2; // sospechoso
        StringBuilder sb = new StringBuilder();

        for (int i = start; i <= noisy.length(); i += step) { // BUG: <=
            char c = noisy.charAt(i); // puede petar al final
            if (Character.isAlphabetic(c)) {
                sb.append(c);
            }
        }
        String token = sb.toString();
        System.out.println(token); // Debe imprimir: VERTX tras arreglo
    }
}
