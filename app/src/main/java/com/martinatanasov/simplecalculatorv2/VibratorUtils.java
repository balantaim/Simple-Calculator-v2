package com.martinatanasov.simplecalculatorv2;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;

@SuppressWarnings(value = "deprecation")
public class VibratorUtils {

    private final Vibrator vibrator;
    private final static long DEFAULT_TIME_IN_MILLISECONDS = 20;

    public VibratorUtils(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            VibratorManager vibratorManager = (VibratorManager) context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
            vibrator = vibratorManager.getDefaultVibrator();
        } else {
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }
    }

    public void vibrate() {
        if (vibrator == null || !vibrator.hasVibrator()) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(DEFAULT_TIME_IN_MILLISECONDS, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(DEFAULT_TIME_IN_MILLISECONDS);
        }
    }

}
