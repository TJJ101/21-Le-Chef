package sg.edu.np.madassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class StepsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        getSupportFragmentManager().beginTransaction().replace(R.id.stepsFragment_container, new StepsFragment()).commit();
    }
}