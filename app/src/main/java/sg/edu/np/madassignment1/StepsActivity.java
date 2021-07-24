package sg.edu.np.madassignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StepsActivity extends AppCompatActivity {

    int steps = 1;
    Bundle extra = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
//      Hide the top title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        BottomNavigationView bottomNav = findViewById(R.id.steps_bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.stepsFragment_container, new StepsFragment()).commit();
    }

    //to tell the nav to be selected when switching
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = new StepsFragment();

            switch (item.getItemId()){
                case R.id.nav_back:
                    steps --;
                    extra.putInt("stepNum", steps);
                    Log.d("debug", "back" + extra);
                    fragment.setArguments(extra);
                    break;
                case R.id.nav_home2:
                    finish();
                    break;
                case R.id.nav_next:
                    steps ++;
                    extra.putInt("stepNum", steps);
                    Log.d("debug", "next" + extra);
                    fragment.setArguments(extra);
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.stepsFragment_container, fragment).commit();
            return true;
        }
    };
}