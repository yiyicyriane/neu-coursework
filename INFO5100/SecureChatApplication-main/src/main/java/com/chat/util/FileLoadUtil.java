package com.chat.util;

import java.io.IOException;
import java.util.Scanner;

public class FileLoadUtil {
    public static String getServerUrl() throws IOException {
        Scanner scanner = new Scanner(FileLoadUtil.class.getClassLoader().getResourceAsStream("serverUrl.csv"));
        String serverUrl = scanner.nextLine();
        scanner.close();
        return serverUrl;
    }
}
