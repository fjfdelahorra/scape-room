package com.chapterescape;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Objetivo: evitar deadlock y conseguir que ambos hilos impriman y formen "KAFKA".
 * Bug: orden inconsistente de adquisición de locks.
 */
public class Puzzle2_Deadlock {
    private static final Lock A = new ReentrantLock();
    private static final Lock B = new ReentrantLock();

    private static StringBuilder out = new StringBuilder();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            A.lock();
            try {
                sleep(50);
                B.lock();
                try {
                    out.append("KA");
                } finally {
                    B.unlock();
                }
            } finally {
                A.unlock();
            }
        }, "T1");

        Thread t2 = new Thread(() -> {
            // BUG: orden inverso
            B.lock();
            try {
                sleep(50);
                A.lock();
                try {
                    out.append("FKA");
                } finally {
                    A.unlock();
                }
            } finally {
                B.unlock();
            }
        }, "T2");

        t1.start(); t2.start();
        t1.join(); t2.join();

        var token = out.toString(); // debería ser exactamente "KAFKA"
        System.out.println(token);
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}
