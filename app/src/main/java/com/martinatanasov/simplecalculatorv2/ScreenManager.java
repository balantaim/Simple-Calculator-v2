package com.martinatanasov.simplecalculatorv2;

import android.view.View;
import android.view.Window;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public class ScreenManager {

    public ScreenManager(View content, Window window, final boolean decorFitsSystemWindows) {
        // Show system bars to allow easy navigation and minimizing the app
        setSystemBarsVisibility(window, decorFitsSystemWindows);
        // Apply padding to avoid navigation bar overlap
        setPaddingToRootLayout(content);
    }

    private void setPaddingToRootLayout(View content) {
        ViewCompat.setOnApplyWindowInsetsListener(content, (v, insets) -> {
            Insets systemInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(
                    systemInsets.left,
                    systemInsets.top,
                    systemInsets.right,
                    systemInsets.bottom
            );
            return insets;
        });
    }

    private void setSystemBarsVisibility(Window window, final boolean decorFitsSystemWindows) {
        WindowCompat.setDecorFitsSystemWindows(window, decorFitsSystemWindows);
        WindowInsetsControllerCompat windowInsetsControllerCompat = WindowCompat.getInsetsController(window, window.getDecorView());
        if (windowInsetsControllerCompat != null) {
            // Hide the status bar
            windowInsetsControllerCompat.hide(WindowInsetsCompat.Type.statusBars());
            // Ensure the navigation bar is visible
            windowInsetsControllerCompat.show(WindowInsetsCompat.Type.navigationBars());
            // Set behavior to allow showing hidden bars by swipe
            windowInsetsControllerCompat.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        }
    }

}
