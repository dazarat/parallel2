package com.javarush.task.lab2;

import java.util.LinkedList;
import java.util.List;

public class CPUQueue {

    private List<CPUProcess> list;
    private int maxSize;

    CPUQueue(int maxSize) {
        this.maxSize = maxSize;
        list = new LinkedList<>();
    }


    boolean isFull() {
        return list.size() == maxSize;
    }

    boolean isEmpty() {
        return list.isEmpty();
    }

    int size() {
        return list.size();
    }


    synchronized void push(CPUProcess process) {
        list.add(process);
    }

    synchronized CPUProcess pop() {
        if (list.isEmpty())
            throw new RuntimeException("Empty queue.");

        CPUProcess process = list.get(0);
        list.remove(0);

        return process;
    }
}
