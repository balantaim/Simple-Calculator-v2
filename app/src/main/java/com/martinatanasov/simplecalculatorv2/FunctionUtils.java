package com.martinatanasov.simplecalculatorv2;

import static java.lang.Double.parseDouble;

import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FunctionUtils {

    private static final String TAG = "FunctionUtils";
    private static final String PI_NUMBER = "3.141592653589793";

    public double getResultFromEqualKey(String number1, String number2, String operator) {
        BigDecimal resultBigDecimal;
        switch (operator) {
            case "+" -> {
                resultBigDecimal = new BigDecimal(number2).add(new BigDecimal(number1));
                return resultBigDecimal.doubleValue();
            }
            case "-" -> {
                resultBigDecimal = new BigDecimal(number2).subtract(new BigDecimal(number1));
                return resultBigDecimal.doubleValue();
            }
            case "x" -> {
                resultBigDecimal = new BigDecimal(number2).multiply(new BigDecimal(number1));
                return resultBigDecimal.doubleValue();
            }
            case "/" -> {
                resultBigDecimal = new BigDecimal(number2).divide(new BigDecimal(number1), 10, RoundingMode.HALF_UP);
                return resultBigDecimal.doubleValue();
            }
            case "%" -> {
                resultBigDecimal = new BigDecimal(number2).multiply(new BigDecimal(number1)).divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP);
                return resultBigDecimal.doubleValue();
            }
            case "(exponent)" -> {
                return Math.pow(parseDouble(number2), parseDouble(number1));
            }
        }
        Log.e(TAG, "getResultFromEqualKey: Current operator not implemented " + operator);
        return 0.0;
    }

    public String getOperatorById(final int id) {
        if (id == R.id.plus) {
            return "+";
        } else if (id == R.id.minus) {
            return "-";
        } else if (id == R.id.multiplication) {
            return "x";
        } else if (id == R.id.division) {
            return "/";
        } else if (id == R.id.percent) {
            return "%";
        } else if (id == R.id.xy) {
            return "(exponent)";
        }
        Log.e(TAG, "getOperatorById: Button not found " + id);
        return "";
    }

    public String applyNumberOperationById(final int id, String number1, boolean isMaxNum) {
        if (id == R.id.zero) {
            if (isMaxNum) {
                return number1;
            }
            number1 += "0";
            return number1;
        } else if (id == R.id.one) {
            if (isMaxNum) {
                return number1;
            }
            number1 += "1";
            return number1;
        } else if (id == R.id.two) {
            if (isMaxNum) {
                return number1;
            }
            number1 += "2";
            return number1;
        } else if (id == R.id.three) {
            if (isMaxNum) {
                return number1;
            }
            number1 += "3";
            return number1;
        } else if (id == R.id.four) {
            if (isMaxNum) {
                return number1;
            }
            number1 += "4";
            return number1;
        } else if (id == R.id.five) {
            if (isMaxNum) {
                return number1;
            }
            number1 += "5";
            return number1;
        } else if (id == R.id.six) {
            if (isMaxNum) {
                return number1;
            }
            number1 += "6";
            return number1;
        } else if (id == R.id.seven) {
            if (isMaxNum) {
                return number1;
            }
            number1 += "7";
            return number1;
        } else if (id == R.id.eight) {
            if (isMaxNum) {
                return number1;
            }
            number1 += "8";
            return number1;
        } else if (id == R.id.nine) {
            if (isMaxNum) {
                return number1;
            }
            number1 += "9";
            return number1;
        } else if (id == R.id.pi) {
            return PI_NUMBER;
        } else if (id == R.id.comma) {
            if (number1.contains(".")) {
                return number1;
            }
            number1 += ".";
            return number1;
        } else if (id == R.id.plus_minus) {
            if (!number1.isEmpty() && !number1.equals("0")) {
                if (number1.startsWith("-")) {
                    number1 = number1.substring(1);
                    return number1;
                } else {
                    return "-" + number1;
                }
            }
        }
        Log.e(TAG, "applyNumberOperationById: Button not found " + id);
        return number1;
    }

}
