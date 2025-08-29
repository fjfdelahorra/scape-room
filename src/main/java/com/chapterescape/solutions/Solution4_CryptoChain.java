package com.chapterescape.solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.random.RandomGenerator;

/**
 * Solución Puzzle4: decodificar "JAVA21" corrigiendo orden y desplazamiento.
 * - Se mantiene demostración de virtual threads.
 * - Se elimina el reversed erróneo.
 * - Shift correcto: -1 (con wrap) sobre letras y dígitos.
 */
public class Solution4_CryptoChain {

    sealed interface Token permits LetterToken, DigitToken {}
    record LetterToken(int index, char value) implements Token {}
    record DigitToken(int index, char value) implements Token {}

    public static void main(String[] args) throws Exception {
        List<Character> encoded = List.of('K','B','W','B','3','2'); // +1 sobre destino
        StringBuilder sb = new StringBuilder();

        try (ExecutorService exec = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Callable<Token>> tasks = new ArrayList<>();
            for (int i = 0; i < encoded.size(); i++) {
                final int idx = i; final char c = encoded.get(i);
                tasks.add(() -> produce(idx, c));
            }
            var futures = exec.invokeAll(tasks);
            List<Token> collected = new ArrayList<>(futures.size());
            for (var f : futures) collected.add(f.get());

            // Usamos directamente collected (orden correcto)
            for (Token t : collected) {
                switch (t) {
                    case LetterToken lt -> sb.append(shiftBackLetter(lt.value()));
                    case DigitToken dt -> sb.append(shiftBackDigit(dt.value()));
                }
            }
        }
        String token = sb.toString();
        if (!"JAVA21".equals(token)) throw new IllegalStateException("Token inesperado: " + token);
        System.out.println(token);
    }

    private static char shiftBackLetter(char c) {
        return c == 'A' ? 'Z' : (char)(c - 1);
    }
    private static char shiftBackDigit(char c) {
        return c == '0' ? '9' : (char)(c - 1);
    }

    private static Token produce(int index, char c) throws InterruptedException {
        Thread.sleep(RandomGenerator.getDefault().nextInt(5,25));
        return Character.isDigit(c) ? new DigitToken(index, c) : new LetterToken(index, c);
    }
}
