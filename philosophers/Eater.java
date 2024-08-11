package ru.geekbrains.jdk.parallel.philosophers;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class Eater implements Runnable{
    Semaphore semaphore;
    private AtomicBoolean left;
    private AtomicBoolean right;
    private String name;
    private int count;
    private int speed;

    public Eater(AtomicBoolean left, AtomicBoolean right, Semaphore semaphore, String name) {
        this.left = left;
        this.right = right;
        this.name = name;
        this.semaphore = semaphore;
        speed = new Random().nextInt(1, 5) * 1000;
        count = 1;
    }

    @Override
    public void run() {
        while (count <= 3) {
            takeForks();
            feed();
            returnForks();
            System.out.println(name + ": \"Ну вот поели, можно и поспа... подумать " + speed + "мс\"");
            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            count++;
        }

    }

    /**
     * Кормит философа
     */
    private void feed() {
        System.out.println(name + " приём пищи №" + count + "  left:" + left.get() + "  right: " + right.get());
    }

    /**
     * Берёт вилки
     */
    private void takeForks() {
        boolean status = false;
        while (!status) {
            try {
                semaphore.acquire();
                status = upForks();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        }
    }

    /**
     * Бронирует вилки, если обе свободны
     * @return результат
     */
    private boolean upForks() {
        if (!left.get() && !right.get()) {
            left.set(true);
            right.set(true);
            return true;
        }
        return false;
    }

    /**
     * Возвращает вилки
     */
    private void returnForks() {
        try {
            semaphore.acquire();
            downForks();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

    /**
     * Освобождает вилки
     */
    private void downForks() {
        left.set(false);
        right.set(false);
    }
}
