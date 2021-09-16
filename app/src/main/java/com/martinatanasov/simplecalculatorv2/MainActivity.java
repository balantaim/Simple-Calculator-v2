package com.martinatanasov.simplecalculatorv2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView txtPanel, txtLegacy;
    Button zero, one, two, three, four, five, six, seven, eight, nine, equal,
            comma, pi, sqrt, minus, plus, division, multiplication,
            percent, plus_minus, del, backspace;
    ImageButton x2, xy;
    String number1= "";
    String number2 = "";
    String operator = "+";
    long resultLong = 0;
    double result = 0;
    final double piNumber = 3.141592653589793;
    boolean newStr = true;
    boolean maxNum = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        idDeclaration();
    }

    private void idDeclaration() {
        txtPanel = findViewById(R.id.txtPanel);
        txtLegacy = findViewById(R.id.txtLegacy);
        zero = findViewById(R.id.zero);
        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        four = findViewById(R.id.four);
        five = findViewById(R.id.five);
        six = findViewById(R.id.six);
        seven = findViewById(R.id.seven);
        eight = findViewById(R.id.eight);
        nine = findViewById(R.id.nine);
        equal = findViewById(R.id.equal);
        comma = findViewById(R.id.comma);
        x2 = findViewById(R.id.x2);
        xy = findViewById(R.id.xy);
        minus = findViewById(R.id.minus);
        plus = findViewById(R.id.plus);
        division = findViewById(R.id.division);
        multiplication = findViewById(R.id.multiplication);
        percent = findViewById(R.id.percent);
        plus_minus = findViewById(R.id.plus_minus);
        del = findViewById(R.id.del);
        pi = findViewById(R.id.pi);
        sqrt = findViewById(R.id.sqrt);
        backspace = findViewById(R.id.backspace);
    }

    @SuppressLint("NonConstantResourceId")
    public void addNumbersToString(View view) {
        if (newStr) {
            txtPanel.setText("");
            newStr = false;
        }
        number1 = txtPanel.getText().toString();
        numberCount();
        switch (view.getId()) {
            case R.id.zero:
                if (maxNum==true){return;}
                number1 += "0";
                break;
            case R.id.one:
                if (maxNum==true){return;}
                number1 += "1";
                break;
            case R.id.two:
                if (maxNum==true){return;}
                number1 += "2";
                break;
            case R.id.three:
                if (maxNum==true){return;}
                number1 += "3";
                break;
            case R.id.four:
                if (maxNum==true){return;}
                number1 += "4";
                break;
            case R.id.five:
                if (maxNum==true){return;}
                number1 += "5";
                break;
            case R.id.six:
                if (maxNum==true){return;}
                number1 += "6";
                break;
            case R.id.seven:
                if (maxNum==true){return;}
                number1 += "7";
                break;
            case R.id.eight:
                if (maxNum==true){return;}
                number1 += "8";
                break;
            case R.id.nine:
                if (maxNum==true){return;}
                number1 += "9";
                break;
            case R.id.pi:
                number1 = piNumber + "";
                break;
            case R.id.comma:
                if(number1.contains(".")){
                    return;
                }
                number1 += ".";
                break;
            case R.id.plus_minus:
                if (number1 != "" && number1 != "0") {
                    if (number1.startsWith("-")) {
                        number1 = number1.substring(1);
                    } else {
                        number1 = "-" + number1;
                    }
                }
                break;
            default:
                break;
        }
        txtPanel.setText(number1);
    }

    @SuppressLint("NonConstantResourceId")
    public void operatorsEvent(View view) {
        if(parseNumber(number1)==true && parseNumber(number2)==true){
            if(operator=="%" || operator=="(exponent)"){return;
            } else {
                equalEvent(view);
                txtLegacy.setText(number1+" "+operator);
            }
        }
        if(number1==""){return;}
        number2 = number1;
        newStr = true;
        switch (view.getId()) {
            case R.id.plus:
                operator = "+";
                break;
            case R.id.minus:
                operator = "-";
                break;
            case R.id.multiplication:
                operator = "*";
                break;
            case R.id.division:
                operator = "/";
                break;
            case R.id.percent:
                operator = "%";
                break;
            case R.id.xy:
                operator = "(exponent)";
                break;
            default:
                break;
        }
        txtLegacy.setText(number1+" "+operator);
    }

    public void cleanAll(View view){
        txtPanel.setText("0");
        txtLegacy.setText("");
        number1="";
        number2="";
        operator="";
        newStr=true;
    }

    public void equalEvent(View view) {
        if(number2!="" && parseNumber(number2)==true && parseNumber(number1)==true){
            switch (operator) {
                case "+":
                    result = Double.parseDouble(number2) + Double.parseDouble(number1);
                    break;
                case "-":
                    result = Double.parseDouble(number2) - Double.parseDouble(number1);
                    break;
                case "*":
                    result = Double.parseDouble(number2) * Double.parseDouble(number1);
                    break;
                case "/":
                    result = Double.parseDouble(number2) / Double.parseDouble(number1);
                    break;
                case "%":
                    try{
                        float percentFunction = (float)(Float.parseFloat(number1)*(Double.parseDouble(number2)/100.0f));
                        result = percentFunction;
                    } catch (Exception e){
                        txtLegacy.setText("Error: "+e);
                    }
                    break;
                case "(exponent)":
                    result=Math.pow(Double.parseDouble(number2), Double.parseDouble(number1));
                    break;
                default:
                    break;
            }
            if(result%1==0){
                resultLong = (long) result;
                txtPanel.setText(resultLong + "");
                txtLegacy.setText(resultLong + "");
                number1=resultLong+"";
            } else {
                txtPanel.setText(result + "");
                txtLegacy.setText(result + "");
                number1=result+"";
            }
            newStr=true;
            number2="";
            resultLong =0;
        }
    }

    public void xPowered(View view){
        if (number1!="" && parseNumber(number1)==true) {
            double sum = Double.parseDouble(number1) * Double.parseDouble(number1);
            txtLegacy.setText(sum + "");
            newStr = true;
            number1 = sum + "";
            number2 = "";
        }
    }

    public void clearWord (View view) {
        if (number1!="" && 0<number1.length()){
            number1 = number1.substring(0, number1.length() - 1);
            txtPanel.setText(number1 + "");
        }
    }

    public void sqrt(View view){
        if (number1!="" && parseNumber(number1)==true) {
            if (Double.parseDouble(number1)<0){
                txtLegacy.setText("Invalid number");
                newStr = true;
                number1 = "";
                number2 = "";
                return;
            }
            double sum = Math.sqrt(Double.parseDouble(number1));
            txtLegacy.setText(sum + "");
            newStr = true;
            number1 = sum + "";
            number2 = "";
        }
    }

    public boolean numberCount(){
        maxNum= false;
        if (number1.length()<15){return maxNum;}
        int count =0;
        char[] c = new char[number1.length()];
        for (int i = 0; i < number1.length(); i++) {
            c[i] = number1.charAt(i);
        }
        for (int i =0; i<c.length; i++){
            if (Character.isDigit(c[i])){count++;}
        }
        if(count>=16){maxNum=true;}
        return maxNum;
    }

    public static boolean parseNumber(String s){
        try {
            Double.parseDouble(s);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}