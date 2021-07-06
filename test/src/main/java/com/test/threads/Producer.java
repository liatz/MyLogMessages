package com.test.threads;


import java.util.concurrent.ArrayBlockingQueue;

public class Producer implements Runnable {

    private ArrayBlockingQueue<String> buffer;
    private ArrayBlockingQueue<String> messages;
    private String color;

    public Producer(ArrayBlockingQueue<String> messages, ArrayBlockingQueue<String> buffer, String color) {
        this.messages = messages;
        this.buffer = buffer;
        this.color = color;
    }

    public void run() {

        while(true) {
            synchronized (messages) {
                try {
                    if (messages.isEmpty()) {
                        continue;
                    }
                    String message = messages.take();
                    System.out.println(color + "Adding..." + message.toString());
                    buffer.put(message);
                    //Thread.sleep(random.nextInt(1000));
                } catch (InterruptedException e) {

                }
            }
        }
    }
}