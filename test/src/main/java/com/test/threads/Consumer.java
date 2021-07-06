package com.test.threads;

import com.test.service.FileSink;

import java.util.concurrent.ArrayBlockingQueue;




public class Consumer implements Runnable {

    private FileSink fileSink;

    private ArrayBlockingQueue<String> buffer;
    private String color;

    public Consumer(FileSink fileSink, ArrayBlockingQueue<String> buffer, String color) {
        this.fileSink = fileSink;
        this.buffer = buffer;
        this.color = color;
    }

    public void run() {

        while(true) {
            synchronized (buffer) {
                try {
                    if (buffer.isEmpty()) {
                        continue;
                    }

                    String message = buffer.take();
                    System.out.println(color + "Removed " + message);
                    //while (!LimitBox.isWritePossibleNow(message.length()))
                        fileSink.write(message);
                    //}
                } catch (InterruptedException e) {

                }
            }
        }
    }
}

