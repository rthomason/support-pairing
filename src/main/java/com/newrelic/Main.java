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

        for (int i = 0; i < 4; i++) {
            EXECUTOR.execute(() -> {
                latch.countDown();

                System.out.println("Starting sleeping thread: " + Thread.currentThread().getName());

                while (true) {
                    sleep(1000 + RNG.nextInt(3000));
                }
            });
        }

        for (int i = 0; i < 2; i++) {
            EXECUTOR.execute(() -> {
                latch.countDown();

                System.out.println("Starting spinning thread: " + Thread.currentThread().getName());

                while (true) {
                    long now = System.currentTimeMillis();
                    long spin = RNG.nextInt(1000);
                    while (System.currentTimeMillis() < now + spin) {
                    }
                    sleep(RNG.nextInt(2000));
                }
            });
        }

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
