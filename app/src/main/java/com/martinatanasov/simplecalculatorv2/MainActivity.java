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

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    private boolean isNewStr = true, isMaxNum = false;
    private FunctionUtils utils;
    private static final int ALLOWED_MAX_CHARACTERS = 15;
    private static final int EXCEEDED_MAX_CHARACTERS = 16;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Apply dynamic colors if available
        //DynamicColors.applyToActivityIfAvailable(this);

        setContentView(R.layout.activity_main);
        initScreenManager();
        idDeclaration();
        //Init onClickListener for txtLegacy TextView
        txtLegacy.setOnClickListener(this::showPopupOptions);
    }

    private void initScreenManager() {
        new ScreenManager(findViewById(R.id.root_layout),
                getWindow(),
                false);
    }

    private void showAnimationEqual() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.jump_to_top_anim);
        animation.reset();
        txtLegacy.clearAnimation();
        txtLegacy.startAnimation(animation);
    }

    public void showPopupOptions(View view) {
        if (txtLegacy.getText().toString().length() > 1 && txtLegacy.getText().toString().contains("=")) {
            PopupMenu option = new PopupMenu(this, view);
            option.setOnMenuItemClickListener(this);
            option.inflate(R.menu.options);
            option.show();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.copy_btn) {
            copyResult();
            Toast.makeText(this, R.string.clipboard_copy, Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == R.id.share_btn) {
            shareResult();
            return true;
        }
        return false;
    }

    private void copyResult() {
        // Gets a handle to the clipboard service.
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        String shareValue = txtLegacy.getText().toString().replace("=", "");
        // Creates a new text clip to put on the clipboard.
        ClipData clip = ClipData.newPlainText("Final result", shareValue);
        // Set the clipboard's primary clip.
        clipboard.setPrimaryClip(clip);
    }

    private void shareResult() {
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
            Log.e(TAG, "onPrepareOptionsMenu: ", e);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    //    @SuppressWarnings("deprecation")
    private void initVibratorForOlderDevices() {
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    public void useVibrationEffect() {
        //Check if the device has vibrator hardware
        if (vibrator.hasVibrator()) {
            //Check if the Android version is 8 or newer
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE));
            }
        }
    }

    public void addNumbersToString(View view) {
        if (isNewStr) {
            txtPanel.setText("");
            isNewStr = false;
        }
        number1 = txtPanel.getText().toString();
        numberCount();
        //Update number1's value
        number1 = utils.applyNumberOperationById(view.getId(), number1, isMaxNum);
        txtPanel.setText(number1);
        useVibrationEffect();
    }

    @SuppressLint("SetTextI18n")
    public void operatorsEvent(View view) {
        if (parseNumber(number1) && parseNumber(number2)) {
            if (operator.equals("%") || operator.equals("(exponent)")) {
                return;
            } else {
                equalEvent(view);
                txtLegacy.setText(number1 + " " + operator);
            }
        }
        if (number1.equals("")) {
            return;
        }
        number2 = number1;
        isNewStr = true;
        //Update operator's value
        operator = utils.getOperatorById(view.getId());
        txtLegacy.setText(number1 + " " + operator);
        useVibrationEffect();
    }

    public void cleanAll(View view) {
        if (!number1.equals("") || !number2.equals("")) {
            useVibrationEffect();
        }
        txtPanel.setText("0");
        txtLegacy.setText("");
        number1 = "";
        number2 = "";
        operator = "";
        isNewStr = true;
    }

    @SuppressLint("SetTextI18n")
    public void equalEvent(View view) {
        if (!number2.equals("") && parseNumber(number2) && parseNumber(number1)) {
            //Calculate result based on equal button pressed
            result = utils.getResultFromEqualKey(number1, number2, operator);

            boolean negativeNum = false;
            if (number1.contains("-")) {
                negativeNum = true;
            }
            if (result % 1 == 0) {
                resultLong = (long) result;
                if (negativeNum && (operator.contains("-") || operator.contains("+"))) {
                    txtPanel.setText(number2 + operator + "(" + number1 + ")");
                } else {
                    txtPanel.setText(number2 + operator + number1 + "");
                }
                txtLegacy.setText("=" + resultLong);
                number1 = resultLong + "";
            } else {
                if (negativeNum && (operator.contains("-") || operator.contains("+"))) {
                    txtPanel.setText(number2 + operator + "(" + number1 + ")");
                } else {
                    txtPanel.setText(number2 + operator + number1 + "");
                }
                txtLegacy.setText("=" + result);
                number1 = result + "";
            }
        }
        isNewStr = true;
        number2 = "";
        resultLong = 0;
        result = 0;
        showAnimationEqual();
        useVibrationEffect();
    }

    @SuppressLint("SetTextI18n")
    public void xPowered(View view) {
        if (!number1.equals("") && parseNumber(number1)) {
            double sum = parseDouble(number1) * parseDouble(number1);
            if (!number1.contains(getString(R.string.infinity))) {
                txtPanel.setText(number1 + " exp(2)");
            }
            txtLegacy.setText("=" + sum);
            isNewStr = true;
            number1 = sum + "";
            number2 = "";
            showAnimationEqual();
            useVibrationEffect();
        }
    }

    @SuppressLint("SetTextI18n")
    public void clearWord(View view) {
        if (number1.contains(getString(R.string.infinity))) {
            cleanAll(view);
        }
        if (isNewStr) {
            return;
        } else if (!number1.contains("E") || number1.contains("e")) {
            if (!number1.equals("") && 0 < number1.length()) {
                number1 = number1.substring(0, number1.length() - 1);
                txtPanel.setText(number1 + "");
                useVibrationEffect();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void sqrt(View view) {
        if (!number1.equals("") && parseNumber(number1)) {
            if (parseDouble(number1) < 0) {
                txtLegacy.setText(R.string.invalid_number); //"Invalid number"
                isNewStr = true;
                number1 = "";
                number2 = "";
                useVibrationEffect();
                return;
            }
            double sum = Math.sqrt(parseDouble(number1));
            txtPanel.setText("√" + number1);
            txtLegacy.setText("=" + sum);
            isNewStr = true;
            number1 = sum + "";
            number2 = "";
            useVibrationEffect();
        }
    }

    private void numberCount() {
        isMaxNum = false;
        if (number1.length() < ALLOWED_MAX_CHARACTERS) {
            return;
        }
        int count = 0;
        char[] c = new char[number1.length()];
        for (int i = 0; i < number1.length(); i++) {
            c[i] = number1.charAt(i);
        }
        for (int i = 0; i < c.length; i++) {
            if (Character.isDigit(c[i])) {
                count++;
            }
        }
        if (count >= EXCEEDED_MAX_CHARACTERS) {
            isMaxNum = true;
        }
    }

    private static boolean parseNumber(String s) {
        try {
            parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
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
        // Init Utils
        utils = new FunctionUtils();
        // Vibrator init
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            VibratorManager vibratorManager = (VibratorManager) getSystemService(VIBRATOR_MANAGER_SERVICE);
            vibrator = vibratorManager.getDefaultVibrator();
        } else {
            initVibratorForOlderDevices();
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
        outState.putBoolean("key_new_str", isNewStr);
        outState.putBoolean("key_max_num", isMaxNum);
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
        isNewStr = savedInstanceState.getBoolean("key_new_str", true);
        isMaxNum = savedInstanceState.getBoolean("key_max_num", false);
        super.onRestoreInstanceState(savedInstanceState);
    }

}