package com.martinatanasov.simplecalculatorv2;

import android.content.Context;
import android.os.Build;
import android.os.VibrationAttributes;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;

/**
 * Utility class for handling haptic feedback across different Android API levels.
 * Updated for Android 17 (API 37) compatibility.
 * Vibration is disabled for API 26 (Android 8.0) and lower.
 */
public class VibratorUtils {

    private final Vibrator vibrator;
    private final static long DEFAULT_TIME_IN_MILLISECONDS = 20;

    public VibratorUtils(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Use VibratorManager for API 31+
            VibratorManager vibratorManager = context.getSystemService(VibratorManager.class);
            if (vibratorManager != null) {
                vibrator = vibratorManager.getDefaultVibrator();
            } else {
                vibrator = context.getSystemService(Vibrator.class);
            }
        } else {
            // Use Vibrator for older versions
            vibrator = context.getSystemService(Vibrator.class);
        }
    }

    public void vibrate() {
        if (vibrator == null || !vibrator.hasVibrator()) {
            return;
        }

        // Only vibrate for API levels higher than 26
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            VibrationEffect effect;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                // Use rich haptic composition (API 31+)
                effect = VibrationEffect.startComposition()
                        .addPrimitive(VibrationEffect.Composition.PRIMITIVE_CLICK)
                        .compose();
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Use predefined click effect (API 29+)
                effect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK);
            } else {
                // Use one-shot effect for API 27-28
                effect = VibrationEffect.createOneShot(DEFAULT_TIME_IN_MILLISECONDS, VibrationEffect.DEFAULT_AMPLITUDE);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // Use VibrationAttributes for API 33+
                VibrationAttributes attributes = new VibrationAttributes.Builder()
                        .setUsage(VibrationAttributes.USAGE_TOUCH)
                        .build();
                vibrator.vibrate(effect, attributes);
            } else {
                vibrator.vibrate(effect);
            }
        }
    }

}
