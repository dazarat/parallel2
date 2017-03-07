package com.javarush.task.lab2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class OperatingSystem {

    private static final int MAX_VALUE = 10;
    private static final int MIN_VALUE = 0;
    private static final int NUMBER_OF_PROCESSES_TO_CREATE = 20;
    private static final int NUMBER_OF_QUEUE_THREADS = 1;
    private static volatile int MAX_QUEUE_SIZE = 0;
    private static volatile int createdProcesses = 0;

    private static Random rand = new Random();
    private CPUQueue queue;
    private CPUProcessor processor;
    private CPUProcess newProcess;
    private final Object monitor = new Object();
    private List<Thread> queueThreads;

    public OperatingSystem(String processorName, int queueSize) {
        processor = new CPUProcessor(processorName);
        queue = new CPUQueue(queueSize);

        start();
    }

    public static int getRandomProcessTime() {
        return MIN_VALUE + rand.nextInt(MAX_VALUE);
    }

    private void initializeQueueThreads(int amount) {

        queueThreads = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            Thread thread = new Thread(new TakeFromQueue());
            thread.setDaemon(true);
            queueThreads.add(thread);
        }
        for (Thread thread : queueThreads)
            thread.start();
    }

    public void sleep(long ms) {
        try {Thread.sleep(ms);}
        catch (InterruptedException e) {}
    }

    public void start() {
        processor.start();
        initializeQueueThreads(NUMBER_OF_QUEUE_THREADS);

        //while(true) {
        for (int k = 0; k < NUMBER_OF_PROCESSES_TO_CREATE; k++) {

            System.out.println(String.format("[process #%d] generated", ++createdProcesses));
            newProcess = new CPUProcess(getRandomProcessTime());

            if (!processor.isBusy()) {
                processor.loadProcess(newProcess);
            } else {
                System.out.println(String.format("[process #%d] pushed to queue, queue size: %d", createdProcesses, queue.size() + 1));
                synchronized (monitor) {
                    if (!queue.isFull()) {
                        queue.push(newProcess);
                    } else {
                        System.out.println("queue is full");
                    }
                }
            }
            sleep(2000);
        }
        sleep(3000);

        System.out.println(" All processes was generated.\n Max queue length: " + MAX_QUEUE_SIZE);
    }

    private class TakeFromQueue implements Runnable {
        @Override
        public void run() {
            while (true) {
                synchronized (monitor) {
                    if (!queue.isEmpty()) {
                        if (!processor.isBusy()) {
                            if (queue.size() > MAX_QUEUE_SIZE)
                                MAX_QUEUE_SIZE = queue.size();
                            processor.loadProcess(queue.pop());
                            System.out.println(String.format("[process #%d] deleted from queue",
                                    processor.getWorkingProcess().getId()));
                        }
                    }
                }
            }
        }
    }
}