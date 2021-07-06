package com.test;

import com.test.service.FileSinkImpl;
import com.test.utils.Utils;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/*Test your answer by writing tests (through JUnit or a main method) which validates the following scenarios:
        Writing one line
        Writing multiple lines
        Writing multiple lines from multiple threads
        Make sure your test verifies that all the messages written to the FileSink are actually there
        Each thread should write 10,000 unique messages
        Each test should validate that the scenario actually worked (not manually by inspecting the output)
*/
@SpringBootTest

public class FileSinkTests {

    @Autowired
    private FileSinkImpl fileSinkImpl;

    @Value( "${directoryPath}" )
    private String directoryPath;

    @Before
    public void init() {
        Utils.deleteFile(directoryPath);
    }
    @Test
    void testWritingOneLine() throws IOException {
        Utils.deleteFile(directoryPath);
        String expectedMessage = Utils.getRandString();
        fileSinkImpl.write(expectedMessage);

        String data = Utils.readFile(directoryPath);


        assertEquals(expectedMessage, data.trim());

//        String fileContrent = Utils.readFile(directoryPath);
//        if(!fileContrent.equals(message)) {
//            fail("Failed to Write One Line");
//        }
    }

    @Test
    void testWritingMultipleLines() throws IOException {
        Utils.deleteFile(directoryPath);
        StringBuilder expectedMessages = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            String message = Utils.getRandString();
            fileSinkImpl.write(message);
            expectedMessages.append(message);
        }
        String data = Utils.readFile(directoryPath);
        data = data.replaceAll("(\\r|\\n)", "");
        String res = expectedMessages.toString();

        assertEquals(res, data.trim());
    }


}
