package com.martinatanasov.simplecalculatorv2;

import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;

public class ScreenManager {

    public ScreenManager(View content, Window window, final boolean decorFitsSystemWindows) {
        // Make application to be on full screen
        setApplicationFullscreenMode(window, decorFitsSystemWindows);
        // Apply padding to avoid navigation bar overlap
        setPaddingToRootLayout(content);
    }

    private void setPaddingToRootLayout(View content) {
        ViewCompat.setOnApplyWindowInsetsListener(content, (v, insets) -> {
            Insets systemInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(
                    // Add padding at the left for navigation bar
                    systemInsets.left,
                    v.getPaddingTop(),
                    // Add padding at the right for navigation bar
                    systemInsets.right,
                    // Add padding at the bottom for navigation bar
                    systemInsets.bottom
            );
            return insets;
        });
    }

    private void setApplicationFullscreenMode(Window window, final boolean decorFitsSystemWindows) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowCompat.setDecorFitsSystemWindows(window, decorFitsSystemWindows);
            Objects.requireNonNull(window.getInsetsController()).hide(WindowInsets.Type.statusBars());
        } else {
            setApplicationFullscreenModeOnOlderSDK(window);
        }
    }

    @SuppressWarnings("deprecation")
    private void setApplicationFullscreenModeOnOlderSDK(Window window) {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

}
