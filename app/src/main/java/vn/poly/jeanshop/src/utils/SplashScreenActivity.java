package vn.poly.jeanshop.src.utils;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import vn.poly.jeanshop.NavigationActivity;
import vn.poly.jeanshop.R;
import vn.poly.jeanshop.src.module.login.view.LoginActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

        setContentView(R.layout.activity_splash_screen);
//        APIVnFood.init(getApplicationContext());

        SharedPreferences pref = getSharedPreferences("User", MODE_PRIVATE);
        String token = pref.getString("token", null);

//        findViewById(R.id.btnLoginSplash).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                findViewById(R.id.btnLoginSplash).setClickable(false);
//                if (token != null) {
////                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
//                    startActivity(new Intent(SplashScreenActivity.this, NavigationActivity.class));
//                } else {
//                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
//                }
//                finish();
//            }
//        });

        Intent login = new Intent(this, LoginActivity.class);
        Intent main = new Intent(this, NavigationActivity.class);

//        if (token != null) {
            CountDownTimer Timer = new CountDownTimer(1000, 1000) {
                public void onTick(long millisUntilFinished) {
//                    tvSkip.setText("Bỏ qua trong " + millisUntilFinished/1000);
                }
                public void onFinish() {
                    if (token != null) {
                        startActivity(main);
                        Log.d("AAA", token);
                        finish();
                    }else {
                        startActivity(login);
                        finish();
                    }

                }
            }.start();
//        } else {
//            startActivity(login);
//            finish();
//        }
    }
}
