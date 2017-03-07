package com.javarush.task.lab2;

public class CPUProcess {
    private long timeInterval;
    private int id;
    private static int nextId = 1;


    CPUProcess(int timeInterval) {
        this.timeInterval = timeInterval;
        id = nextId++;
    }

    public long getTimeInterval() {
        return timeInterval;
    }

    public int getId() {
        return id;
    }

    public String getProcessName() {
        return String.format("process #%d", id);
    }
}
