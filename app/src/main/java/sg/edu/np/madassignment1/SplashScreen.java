package sg.edu.np.madassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {
    CountDownTimer cdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        // Hide the top title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startTimer(3);
    }

    void skipLoadingPage(){
        Intent in = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(in);
    }

    void startTimer(int duration){
        cdt = new CountDownTimer(duration*1000, 1000) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                Intent in = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(in);
            }
        };
        cdt.start();
    }
}