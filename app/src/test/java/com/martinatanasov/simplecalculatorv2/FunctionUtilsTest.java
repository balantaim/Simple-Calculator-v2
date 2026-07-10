package com.martinatanasov.simplecalculatorv2;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class FunctionUtilsTest {

    private FunctionUtils utils;

    @Before
    public void setUp() {
        utils = new FunctionUtils();
    }

    @Test
    public void testAddition() {
        double result = utils.getResultFromEqualKey("5", "10", "+");
        assertEquals(15.0, result, 0.0001);
    }

    @Test
    public void testSubtraction() {
        double result = utils.getResultFromEqualKey("5", "10", "-");
        assertEquals(5.0, result, 0.0001);
    }

    @Test
    public void testMultiplication() {
        double result = utils.getResultFromEqualKey("3", "4", "x");
        assertEquals(12.0, result, 0.0001);
    }

    @Test
    public void testDivision() {
        double result = utils.getResultFromEqualKey("2", "10", "/");
        assertEquals(5.0, result, 0.0001);
    }

    @Test
    public void testPercentage() {
        double result = utils.getResultFromEqualKey("10", "200", "%");
        assertEquals(20.0, result, 0.0001);
    }

    @Test
    public void testExponent() {
        double result = utils.getResultFromEqualKey("3", "2", "(exponent)");
        assertEquals(8.0, result, 0.0001);
    }

}
