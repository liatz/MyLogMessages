package com.test;

import com.test.service.FileSinkImpl;
import com.test.threads.Consumer;
import com.test.threads.Producer;
import com.test.utils.Utils;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import com.test.utils.ThreadColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.concurrent.*;

import static org.junit.Assert.assertEquals;

@SpringBootTest
public class ConcurrentFileSinkTests {

    @Autowired
    private FileSinkImpl fileSinkImpl;

    @Value( "${directoryPath}" )
    private String directoryPath;

    @Before
    public void init() {
        Utils.deleteFile(directoryPath);
    }

    @Test
    void testNewTreeConcurrent() throws InterruptedException, IOException {

        Utils.deleteFile(directoryPath);

        // Generate messages
        int N = 10;
        StringBuilder expectedMessages = new StringBuilder();
        ArrayBlockingQueue<String> messages = new ArrayBlockingQueue<String>(N);
        for(int i = 1; i < N; i++) {
            String message = Utils.getRandString();
            messages.put(message);
            expectedMessages.append(message);
        }


        //BlockingQueue implementations are thread-safe
        //A bounded blocking queue backed by an array. This queue orders elements FIFO (first-in-first-out).
        ArrayBlockingQueue<String> buffer = new ArrayBlockingQueue<String>(N);

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        Producer producer1 = new Producer(messages, buffer, ThreadColor.ANSI_YELLOW);
        Producer producer2 = new Producer(messages, buffer, ThreadColor.ANSI_PURPLE);
        Consumer consumer = new Consumer(fileSinkImpl, buffer, ThreadColor.ANSI_CYAN);

        executorService.execute(producer1);
        executorService.execute(producer2);

        executorService.execute(consumer);


        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }

        String data = Utils.readFile(directoryPath);
        data = data.replaceAll("(\\r|\\n)", "");
        String res = expectedMessages.toString();

        assertEquals(res, data.trim());

    }
}
