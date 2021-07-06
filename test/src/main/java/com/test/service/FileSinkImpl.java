package com.test.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;



@Service
@PropertySource("classpath:messages.properties")
public class FileSinkImpl implements FileSink{

    //The directory in which the log files will be saved
    @Value( "${directoryPath}" )
    private String directoryPath;

    @Override
    public void write(String logMessage) {
        synchronized (this) {
            try {

                File file = new File(directoryPath);
                if (!file.exists()) {
                    file.createNewFile();
                }

                FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(logMessage);
                bw.newLine();
                bw.close();

            } catch (IOException e) {
            }
        }
    }
}
