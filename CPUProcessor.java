package com.javarush.task.lab2;


public class CPUProcessor extends Thread{


    private boolean busy;
    private CPUProcess workingProcess;

    private String processorName;

    CPUProcessor(String name) {
        processorName = name;
        setDaemon(true);
    }

    public boolean isBusy() {
        return busy;
    }

    public synchronized void loadProcess(CPUProcess process) {
        workingProcess = process;

    }

    public CPUProcess getWorkingProcess() {
        return workingProcess;
    }

    @Override
    public void run() {
        while(true) {

            if (workingProcess != null) {
                System.out.println( String.format("[%s] started working with [%s]", processorName, workingProcess.getProcessName()));
                busy = true;
                try {
                    sleep(1000 * Math.abs(workingProcess.getTimeInterval()) + 1300);
                } catch ( InterruptedException e ) {}
                System.out.println(String.format("[%s] finished working with [%s]", processorName, workingProcess.getProcessName()));
                busy = false;
                workingProcess = null;
            }
            try {
                sleep(2000);
            } catch ( InterruptedException e ) {}
        }
    }
}
