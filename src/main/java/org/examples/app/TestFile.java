package org.examples.app;

import java.io.*;

public class TestFile {
    public static void main(String[] args) throws IOException, InterruptedException {
        String filePath = "src/main/resources/ExportCSV.csv";

        FileInputStream fis = new FileInputStream(filePath);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader buffReader = new BufferedReader(isr);

        String strTmp = "";
        while((strTmp = buffReader.readLine()) != null) {
            System.out.println(strTmp);
            Thread.sleep(1000);
        }

        buffReader.close();


    }
}
