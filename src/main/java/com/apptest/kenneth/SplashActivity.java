package com.apptest.kenneth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

public class SplashActivity extends Activity {

    /** Splash screen timer. */
    private static final int SPLASH_TIME_OUT = 1500;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Lanzar MainActivity después del timeout
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("SplashActivity", "Iniciando MainActivity...");
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            ImageView IVTapLinxLogo = findViewById(R.id.imgTapLinx);

            // Ajustar tamaño del ImageView al tamaño del root view de forma segura
            IVTapLinxLogo.getLayoutParams().width = IVTapLinxLogo.getRootView().getWidth();
            IVTapLinxLogo.getLayoutParams().height = IVTapLinxLogo.getRootView().getHeight();
            IVTapLinxLogo.requestLayout();
        }
    }
}
