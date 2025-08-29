package com.chapterescape;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.random.RandomGenerator;

/**
 * Puzzle 4 (Java 21 didáctico)
 * Objetivo: imprimir "JAVA21".
 * Demuestra varias características de Java 21:
 *  - Virtual Threads (JEP 444)
 *  - Records y Record Patterns (JEP 440)
 *  - Pattern Matching for switch (JEP 441)
 *  - Sequenced Collections (JEP 431) usando reversed()
 *
 * Narrativa:
 *  Se lanzan tareas virtuales que devuelven letras/dígitos codificados (un shift +1 sobre el caracter correcto).
 *  Luego se mezclan en orden inverso por error y se aplica un shift incorrecto (+1 de nuevo) en el switch.
 *
 * Bugs a corregir por el jugador:
 *  1. No invertir la secuencia (el uso de reversed() está mal aplicado).
 *  2. El shift en letras y dígitos debe ser -1, no +1.
 *  3. Validar que no haya nulos (comprobación defensiva opcional) — ahora mismo no rompe pero es buena práctica.
 *  4. (Opcional) Cerrar el executor con try-with-resources para asegurar liberación temprana.
 */
public class Puzzle4_CryptoChain {

    // Record base
    sealed interface Token permits LetterToken, DigitToken {}
    record LetterToken(int index, char value) implements Token {}
    record DigitToken(int index, char value) implements Token {}

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // Secuencia destino (para referencia mental del jugador): J A V A 2 1
        // La codificación produce cada caracter desplazado +1: K B W B 3 2
        List<Character> encodedInOrder = List.of('K','B','W','B','3','2');

        // Creamos un pool de virtual threads (Java 21 feature final)
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        try {
            List<Future<Token>> futures = new ArrayList<>();
            for (int i = 0; i < encodedInOrder.size(); i++) {
                final int idx = i;
                final char encoded = encodedInOrder.get(i);
                Callable<Token> task = () -> produceToken(idx, encoded);
                futures.add(executor.submit(task));
            }

            // Recogemos resultados en lista en orden de envío
            List<Token> collected = new ArrayList<>();
            for (Future<Token> f : futures) {
                collected.add(f.get());
            }

            // BUG 1: No deberíamos invertir. Esto altera el orden lógico.
            List<Token> processedOrder = collected.reversed(); // reversed() (SequencedCollection view) usado erróneamente

            StringBuilder sb = new StringBuilder();
            for (Token t : processedOrder) {
                if (t == null) continue; // Dejar pasar (se podría avisar)
                // Quitamos guards 'when' para exhaustividad (sólo hay dos subtipos permitidos)
                // BUG 2: shift aplicado está mal (usa +1 en vez de -1)
                String decoded = switch (t) {
                    case LetterToken(int pos, char c) -> {
                        char shifted = (char) ('A' + (c - 'A' + 1) % 26); // debería ser -1
                        yield String.valueOf(shifted);
                    }
                    case DigitToken(int pos, char c) -> {
                        char shifted = (char) ('0' + (c - '0' + 1) % 10); // debería ser -1
                        yield String.valueOf(shifted);
                    }
                };
                sb.append(decoded);
            }

            System.out.println(sb); // Debe imprimir: JAVA21 tras corregir bugs
        } finally {
            executor.close(); // Liberar recursos (Virtual threads lightweight pero buena práctica)
        }
    }

    private static Token produceToken(int index, char encoded) throws InterruptedException {
        // Simular latencia variable para desorden potencial (aunque mantenemos orden de futures)
        Thread.sleep(RandomGenerator.getDefault().nextInt(5, 25));
        if (Character.isDigit(encoded)) {
            return new DigitToken(index, encoded);
        } else {
            return new LetterToken(index, encoded);
        }
    }
}
