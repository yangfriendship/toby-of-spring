package springbook.learningtest.template;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class CalcSumTest {

    Calculator calculator;
    String filePath;

    @Before
    public void setup() {
        calculator = new Calculator();
        filePath = this.getClass().getResource("/numbers.txt").getPath();
    }

    @Test
    public void sumOfNumbers() throws Exception {
        Integer result = calculator.calcSum(filePath);
        assertEquals(15, result);
    }

    @Test
    public void multipleOfNumbers() throws Exception {
        Integer result = calculator.calcMultiple(filePath);
        System.out.println(result);
        assertEquals(120, result);
    }

    @Test
    public void concatenate() throws Exception {
        String concatenate = calculator.concatenate(filePath);
        assertEquals("12345", concatenate);

    }

}
