package com.test.utils;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.*;
import java.util.Random;

public class Utils {

    private static long idCounter = 0;

    public static synchronized String createID() {
        return String.valueOf(idCounter++);
    }

    public static void deleteFile(String filePath) {
        Path path = FileSystems.getDefault().getPath(filePath);
        try {
            Files.delete(path);
        } catch (NoSuchFileException x) {
            System.err.format("%s: no such" + " file or directory%n", path);
        } catch (DirectoryNotEmptyException x) {
            System.err.format("%s not empty%n", path);
        } catch (IOException x) {
            // File permission problems are caught here.
            System.err.println(x);
        }
    }

    public static String readFile(String filePath) throws IOException {
        Path path = FileSystems.getDefault().getPath(filePath);
        String content = new String(Files.readAllBytes(path));
        return content;
    }



    public static String getRandString() {
        String charset = "abcdefghijklmnopqrstuvwxyz" +
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * charset.length());
            salt.append(charset.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }
}