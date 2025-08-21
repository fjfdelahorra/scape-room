package com.chapterescape;

import java.nio.charset.Charset;
import java.util.Base64;

/**
 * Objetivo: imprimir "JAVA21".
 * Proceso correcto: Base64 decode, luego shift -1 sobre letras/dígitos que corresponda.
 * Bugs: charset y dirección del shift.
 */
public class Puzzle4_CryptoChain {

    public static void main(String[] args) {
        // Base64 de "KBWB32" => "S0JXQjMy"
        String base64 = "S0JXQjMy";
        byte[] decoded = Base64.getDecoder().decode(base64);

        // BUG: charset y shifts
        String s = new String(decoded, Charset.forName("ISO-8859-1"));
        StringBuilder out = new StringBuilder();

        for (char c : s.toCharArray()) {
            if (Character.isUpperCase(c)) {
                // incorrecto: +1
                char shifted = (char) ('A' + (c - 'A' + 1) % 26);
                out.append(shifted);
            } else if (Character.isDigit(c)) {
                // incorrecto: +1
                char shifted = (char) ('0' + (c - '0' + 1) % 10);
                out.append(shifted);
            } else {
                out.append(c);
            }
        }
        System.out.println(out.toString()); // Debe imprimir: JAVA21 tras arreglo
    }
}
