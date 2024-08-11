package ru.geekbrains.jdk.parallel.philosophers;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Semaphore semaphore = new Semaphore(1, true);

        AtomicBoolean fork1 = new AtomicBoolean(false);
        AtomicBoolean fork2 = new AtomicBoolean(false);
        AtomicBoolean fork3 = new AtomicBoolean(false);
        AtomicBoolean fork4 = new AtomicBoolean(false);
        AtomicBoolean fork5 = new AtomicBoolean(false);

        Thread first = new Thread(new Eater(fork1, fork2, semaphore, "Платон"));
        first.start();
        Thread second = new Thread(new Eater(fork2, fork3, semaphore, "Сократ"));
        second.start();
        Thread third = new Thread(new Eater(fork3, fork4, semaphore, "Ницше"));
        third.start();
        Thread fourth = new Thread(new Eater(fork4, fork5, semaphore,  "Шопенгауэр"));
        fourth.start();
        Thread fifth = new Thread(new Eater(fork5, fork1,semaphore,  "Монтень"));
        fifth.start();


        if (first.isAlive()) first.join();
        if (second.isAlive()) second.join();
        if (third.isAlive()) third.join();
        if (fourth.isAlive()) fourth.join();
        if (first.isAlive()) fifth.join();

        System.out.println("Все философы накормлены!");


    }
}
