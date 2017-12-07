package com.newrelic;

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(6);
    private static final Random RNG = new Random();

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
        }
    }

    public static void main(String[] args) {

        final CountDownLatch latch = new CountDownLatch(6);

        // make some threads that only sleep
        for (int i = 0; i < 3; i++) {
            final int number = i + 1;
            EXECUTOR.execute(() -> {
                Thread.currentThread().setName("sleeper-" + number);
                System.out.println("Starting sleeping thread: " + Thread.currentThread().getName());
                latch.countDown();

                while (true) {
                    sleep(1000 + RNG.nextInt(2000));
                }
            });
        }

        // make some threads that generate load by busy waiting
        for (int i = 0; i < 3; i++) {
            final int number = i + 1;
            EXECUTOR.execute(() -> {
                Thread.currentThread().setName("spinner-" + number);
                System.out.println("Starting spinning thread: " + Thread.currentThread().getName());
                latch.countDown();

                while (true) {
                    long now = System.currentTimeMillis();
                    long spin = RNG.nextInt(2000);
                    while (System.currentTimeMillis() < now + spin) {
                    }
                    sleep(RNG.nextInt(2000));
                }
            });
        }

        // wait for all the threads to start
        try {
            latch.await();
        } catch (InterruptedException e) {
        }

        System.out.println("Press 'enter' to quit");
        Scanner console = new Scanner(System.in);
        console.nextLine();
        System.exit(0);
    }

}
