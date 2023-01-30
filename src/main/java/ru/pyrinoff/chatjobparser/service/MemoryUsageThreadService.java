package ru.pyrinoff.chatjobparser.service;

import org.jetbrains.annotations.NotNull;
import ru.pyrinoff.chatjobparser.util.MemoryUsageUtil;

//@Slf4j
public class MemoryUsageThreadService extends Thread {

    @Override public void run() {
        long maxValue = 0;
        long currentValue;
        while (!Thread.interrupted()) {
            currentValue = MemoryUsageUtil.getMemoryUsageMb();
            if (maxValue < currentValue) maxValue = currentValue;
            //log.error("Max usage: " + maxValue + ", current usage: " + currentValue);
            System.out.println("Max usage: " + maxValue + ", current usage: " + currentValue);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("Max usage: "+maxValue);
                return;
            }
        }
    }

    public static void main(String[] args) {
        @NotNull final Thread memoryUsageThread = new Thread(new MemoryUsageThreadService());
        memoryUsageThread.start();
    }

}
