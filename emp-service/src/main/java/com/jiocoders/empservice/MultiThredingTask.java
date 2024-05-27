package com.jiocoders.empservice;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class MultithreadingTask extends Thread {
    private int count = 0;
    private Object oLock = new Object();

    public synchronized void increment1() {
        count++;
    }

    public int getCount() {
        int c = count;
        synchronized (oLock) {
            log.info("Value inside getcount---> " + c);
            return c;
        }
    }

    private final Lock lock = new ReentrantLock();

    public void increment2() {
        lock.lock();
        count = count + 2;
        lock.unlock();
    }

    private final Semaphore semaphore = new Semaphore(1);

    public void increment3() {
        try {
            semaphore.acquire();
            count = count + 3;
        } catch (InterruptedException e) {
            System.out.println("Semaphore Exception: " + e.getMessage());
        } finally {
            semaphore.release();
        }
    }

    private final CountDownLatch latch = new CountDownLatch(3);

    public void increment4() {
        try {
            latch.await();
            count = count + 4;
        } catch (InterruptedException e) {

            System.out.println("CountDownLatch Exception: " + e.getMessage());
        }
    }

    private final CyclicBarrier barrier = new CyclicBarrier(3, () -> {
        System.out.println("parties have reached the barrier.");
    });

    public void increment5() {
        try {
            barrier.await();
            count = count + 5;
        } catch (InterruptedException e) {
            System.out.println("CyclicBarrier IException: " + e.getMessage());
        } catch (BrokenBarrierException e) {
            System.out.println("CyclicBarrier Exception: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            log.info("Thread started ID=> " + Thread.currentThread().getId());
            for (int i = 0; i < 10; i++) {
                sleep(2000);
                increment1();
                increment2();
                increment3();
                // latch.countDown();
                // increment5();
                log.info("Thread---> " + Thread.currentThread().getId()
                        + ", Counter---> " + getCount());
                sleep(5000);
                if (i == 3) {
                    interrupt();
                }
            }
        } catch (Exception e) {
            System.out.println("Thread Exception: " + e.getMessage());
        }
    }
}