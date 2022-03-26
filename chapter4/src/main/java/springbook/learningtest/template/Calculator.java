package springbook.learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {

    public Integer calcSum(String filePath) throws IOException {
        LineCallback<Integer> callback = (line, value) -> Integer.parseInt(line) + value;
        return lineReadTemplate(filePath, callback, 0);
    }

    public Integer calcMultiple(String filePath) throws IOException {
        LineCallback<Integer> callback = (line, value) -> Integer.parseInt(line) * value;
        return lineReadTemplate(filePath, callback, 1);
    }

    public String concatenate(String filePath) throws IOException {
        LineCallback<String> callback = (line, value) -> value.concat(line);
        return lineReadTemplate(filePath, callback, "");
    }

    private <T> T lineReadTemplate(String filePath, LineCallback<T> callback, T initVal)
        throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = br.readLine()) != null) {
                initVal = callback.doSomethingWithLine(line, initVal);
            }
            return initVal;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private int fileReadTemplate(String filePath, BufferedReaderCallback callback)
        throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filePath));
            return callback.doSomethingWithReader(br);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
