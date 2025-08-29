package com.chapterescape.solutions;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Solución Puzzle2: evitar el deadlock asegurando orden consistente de locks.
 */
public class Solution2_Deadlock {
    private static final Lock A = new ReentrantLock();
    private static final Lock B = new ReentrantLock();
    private static final StringBuilder out = new StringBuilder();

    public static void main(String[] args) throws InterruptedException {
        Runnable r1 = () -> safeAppend("KA");
        Runnable r2 = () -> safeAppend("FKA");
        Thread t1 = new Thread(r1, "T1");
        Thread t2 = new Thread(r2, "T2");
        t1.start(); t2.start();
        t1.join(); t2.join();
        String token = out.toString();
        if (!"KAFKA".equals(token)) {
            throw new IllegalStateException("Resultado inesperado: " + token);
        }
        System.out.println(token);
    }

    private static void safeAppend(String fragment) {
        // Orden global: siempre A luego B
        A.lock();
        try {
            B.lock();
            try {
                out.append(fragment);
            } finally {
                B.unlock();
            }
        } finally {
            A.unlock();
        }
    }
}

