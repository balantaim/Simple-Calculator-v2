/*
 * Copyright (c) 2022 Martin Atanasov. All rights reserved.
 *
 * IMPORTANT!
 * Use of .xml vector path, .svg, .png and .bmp files, as well as all brand logos,
 * is excluded from this license. Any use of these file types or logos requires
 * prior permission from the respective owner or copyright holder.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.martinatanasov.simplecalculatorv2;

import static java.lang.Double.parseDouble;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.reflect.Field;


public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    TextView txtPanel, txtLegacy;
    Button btnZero, one, two, three, four, five, six, seven, eight, nine, equal,
            comma, pi, sqrt, minus, plus, division, multiplication,
            percent, plus_minus, del, backspace;
    ImageButton x2, xy;
    private Vibrator vibrator;
    private String number1 = "", number2 = "", operator = "";
    private long resultLong = 0;
    private double result = 0;
    private boolean newStr = true, maxNum = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Apply dynamic colors if available
        //DynamicColors.applyToActivityIfAvailable(this);

        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        idDeclaration();

        txtLegacy.setOnClickListener(view -> showPopupOptions(view) );
    }

    private void showAnimationEqual(){
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.jump_to_top_anim);
        animation.reset();
        txtLegacy.clearAnimation();
        txtLegacy.startAnimation(animation);
    }
    public void showPopupOptions(View view){
        if(txtLegacy.getText().toString().length() > 1 && txtLegacy.getText().toString().contains("=")){
            PopupMenu option = new PopupMenu(this, view);
            option.setOnMenuItemClickListener(this);
            option.inflate(R.menu.options);
            option.show();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if(item.getItemId() == R.id.copy_btn){
            copyResult();
            Toast.makeText(this, R.string.clipboard_copy, Toast.LENGTH_SHORT).show();
            return true;
        }else if(item.getItemId() == R.id.share_btn){
            shareResult();
            return true;
        }
        return false;
    }
    private void copyResult(){
        // Gets a handle to the clipboard service.
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        String shareValue = txtLegacy.getText().toString().replace("=", "");
        // Creates a new text clip to put on the clipboard.
        ClipData clip = ClipData.newPlainText("Final result", shareValue);
        // Set the clipboard's primary clip.
        clipboard.setPrimaryClip(clip);
    }
    private void shareResult(){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareValue = txtLegacy.getText().toString().replace("=", "");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Final result");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareValue);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
    //Try to add popup icons
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        try {
            Field field = menu.getClass().getDeclaredField("options");
            field.setAccessible(true);
            field.setBoolean(menu, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void idDeclaration() {
        txtPanel = findViewById(R.id.txtPanel);
        txtLegacy = findViewById(R.id.txtLegacy);
        btnZero = findViewById(R.id.zero);
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
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    public void vibroClick(){
        //Check if the device has vibrator hardware
        if(vibrator.hasVibrator()) {
            //Check if the Android version is 8 or newer
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE));
            }
        }
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
                if (maxNum){return;}
                number1 += "0";
                break;
            case R.id.one:
                if (maxNum){return;}
                number1 += "1";
                break;
            case R.id.two:
                if (maxNum){return;}
                number1 += "2";
                break;
            case R.id.three:
                if (maxNum){return;}
                number1 += "3";
                break;
            case R.id.four:
                if (maxNum){return;}
                number1 += "4";
                break;
            case R.id.five:
                if (maxNum){return;}
                number1 += "5";
                break;
            case R.id.six:
                if (maxNum){return;}
                number1 += "6";
                break;
            case R.id.seven:
                if (maxNum){return;}
                number1 += "7";
                break;
            case R.id.eight:
                if (maxNum){return;}
                number1 += "8";
                break;
            case R.id.nine:
                if (maxNum){return;}
                number1 += "9";
                break;
            case R.id.pi:
                number1 = "3.141592653589793";
                break;
            case R.id.comma:
                if(number1.contains(".")){
                    return;
                }
                number1 += ".";
                break;
            case R.id.plus_minus:
                if (!number1.equals("") && !number1.equals("0")) {
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
        vibroClick();
    }

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    public void operatorsEvent(View view) {
        if(parseNumber(number1) && parseNumber(number2)){
            if(operator.equals("%") || operator.equals("(exponent)")){return;
            } else {
                equalEvent(view);
                txtLegacy.setText(number1+" "+operator);
            }
        }
        if(number1.equals("")){return;}
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
                operator = "x";
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
        vibroClick();
    }

    public void cleanAll(View view){
        if(!number1.equals("") || !number2.equals("")){
            vibroClick();
        }
        txtPanel.setText("0");
        txtLegacy.setText("");
        number1="";
        number2="";
        operator="";
        newStr=true;
    }

    @SuppressLint("SetTextI18n")
    public void equalEvent(View view) {
        if(!number2.equals("") && parseNumber(number2) && parseNumber(number1)){
            switch (operator) {
                case "+":
                    result = parseDouble(number2) + parseDouble(number1);
                    break;
                case "-":
                    result = parseDouble(number2) - parseDouble(number1);
                    break;
                case "x":
                    result = parseDouble(number2) * parseDouble(number1);
                    break;
                case "/":
                    result = parseDouble(number2) / parseDouble(number1);
                    break;
                case "%":
                    try{
                        result = (float)(Float.parseFloat(number1)*(parseDouble(number2)/100.0f));
                    } catch (Exception e){
                        txtLegacy.setText(new StringBuilder().append("Error: ").append(e).toString());
                    }
                    break;
                case "(exponent)":
                    result=Math.pow(parseDouble(number2), parseDouble(number1));
                    break;
                default:
                    break;
            }

            boolean negativeNum = false;
            if(number1.contains("-")) {
                negativeNum = true;
            }
            if(result%1==0){
                resultLong = (long) result;
                if(negativeNum && (operator.contains("-") || operator.contains("+"))){
                    txtPanel.setText(number2  + operator + "(" +number1+ ")");
                } else {
                    txtPanel.setText(number2 + operator + number1+ "");
                }
//                if(!txtLegacy.getText().toString().replaceAll("^[x/+-=]", "").equals(String.valueOf(resultLong))){
//                    showAnimationEqual();
//                }
                txtLegacy.setText("=" + resultLong);
                number1=resultLong+"";
            } else {
                if(negativeNum && (operator.contains("-") || operator.contains("+"))){
                    txtPanel.setText(number2 + operator + "(" + number1+ ")");
                } else {
                    txtPanel.setText(number2 + operator + number1+ "");
                }
//                if(!txtLegacy.getText().toString().replaceAll("^[x/+-=]", "").equals(String.valueOf(result))){
//                    showAnimationEqual();
//                }
                txtLegacy.setText("=" + result);
                number1=result+"";
            }
        }
        newStr=true;
        number2="";
        resultLong=0;
        result=0;
        showAnimationEqual();
        vibroClick();
    }

    @SuppressLint("SetTextI18n")
    public void xPowered(View view){
        if (!number1.equals("") && parseNumber(number1)) {
            double sum = parseDouble(number1) * parseDouble(number1);
            if(!number1.contains(getString(R.string.infinity))){
                txtPanel.setText(number1 + " exp(2)");
            }
            txtLegacy.setText("=" + sum);
            newStr = true;
            number1 = sum + "";
            number2 = "";
            showAnimationEqual();
            vibroClick();
        }
    }

    @SuppressLint("SetTextI18n")
    public void clearWord (View view) {
        if(number1.contains(getString(R.string.infinity))){
            cleanAll(view);
        }
        if (newStr){
            return;
        }
        else if(!number1.contains("E") || number1.contains("e")){
            if (!number1.equals("") && 0<number1.length()){
                number1 = number1.substring(0, number1.length() - 1);
                txtPanel.setText(number1 + "");
                vibroClick();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void sqrt(View view){
        if (!number1.equals("") && parseNumber(number1)) {
            if (parseDouble(number1) < 0){
                txtLegacy.setText(R.string.invalid_number); //"Invalid number"
                newStr = true;
                number1 = "";
                number2 = "";
                vibroClick();
                return;
            }
            double sum = Math.sqrt(parseDouble(number1));
            txtPanel.setText("√" + number1);
            txtLegacy.setText("=" + sum);
            newStr = true;
            number1 = sum + "";
            number2 = "";
            vibroClick();
        }
    }

    private boolean numberCount(){
        maxNum = false;
        if (number1.length()<15){return maxNum;}
        int count = 0;
        char[] c = new char[number1.length()];
        for (int i = 0; i < number1.length(); i++) {
            c[i] = number1.charAt(i);
        }
        for (int i = 0; i<c.length; i++){
            if (Character.isDigit(c[i])){count++;}
        }
        if(count >= 16){maxNum = true;}
        return maxNum;
    }

    private static boolean parseNumber(String s){
        try {
            parseDouble(s);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    //Save Instance when you rotate the device or use recreate
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("key_txtPanel", txtPanel.getText().toString());
        outState.putString("key_txtLegacy", txtLegacy.getText().toString());
        outState.putString("key_number1", number1);
        outState.putString("key_number2", number2);
        outState.putString("key_operator", operator);
        outState.putLong("key_result_long", resultLong);
        outState.putDouble("key_result", result);
        outState.putBoolean("key_new_str", newStr);
        outState.putBoolean("key_max_num", maxNum);

        super.onSaveInstanceState(outState);
    }

    //Restore the instance settings
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        txtPanel.setText(savedInstanceState.getString("key_txtPanel", "0"));
        txtLegacy.setText(savedInstanceState.getString("key_txtLegacy", ""));
        number1 = savedInstanceState.getString("key_number1", "");
        number2 = savedInstanceState.getString("key_number2", "");
        operator = savedInstanceState.getString("key_operator", "");
        resultLong = savedInstanceState.getLong("key_result_long", 0);
        result = savedInstanceState.getDouble("key_result", 0);
        newStr = savedInstanceState.getBoolean("key_new_str", true);
        maxNum = savedInstanceState.getBoolean("key_max_num", false);
        super.onRestoreInstanceState(savedInstanceState);
    }
}
