package sg.edu.np.madassignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StepsActivity extends AppCompatActivity {

    int steps = 0;
    Bundle extra = new Bundle();

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://mad-asg-6df37-default-rtdb.asia-southeast1.firebasedatabase.app/");
    private DatabaseReference mDatabase = firebaseDatabase.getReference();
    Recipe recipe = new Recipe();
    ArrayList<Steps> stepsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
//      Hide the top title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        //get the recipe id
        Intent intent = getIntent();
        String recipeId = intent.getStringExtra("recipeId");

        //get steps list from the correct recipe
        mDatabase.child("Recipe").orderByChild("recipeId").equalTo(recipeId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    recipe = snapshot.getValue(Recipe.class);
                }
                for (Steps s : recipe.getStepsList()){
                    stepsList.add(s);
                }

                Fragment fragment = new StepsFragment();
                extra.putInt("stepNum", steps);
                extra.putString("stepDes", stepsList.get(steps).getStepDescription());
                Log.d("debug", "back" + extra);
                fragment.setArguments(extra);
                getSupportFragmentManager().beginTransaction().replace(R.id.stepsFragment_container, fragment).commit();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        BottomNavigationView bottomNav = findViewById(R.id.steps_bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

    //to tell the nav to be selected when switching
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = new StepsFragment();
            switch (item.getItemId()){
                case R.id.nav_back:
                    if (steps > 0){
                        steps --;
                        extra.putInt("stepNum", steps);
                        extra.putString("stepDes", stepsList.get(steps).getStepDescription());
                        Log.d("debug", "back" + extra);
                        fragment.setArguments(extra);
                        getSupportFragmentManager().beginTransaction().replace(R.id.stepsFragment_container, fragment).commit();
                    }
                    break;
                case R.id.nav_home2:
                    finish();
                    break;
                case R.id.nav_next:
                    if (steps < stepsList.size() - 1){
                        steps ++;
                        extra.putInt("stepNum", steps);
                        extra.putString("stepDes", stepsList.get(steps).getStepDescription());
                        Log.d("debug", "next" + extra);
                        fragment.setArguments(extra);
                        getSupportFragmentManager().beginTransaction().replace(R.id.stepsFragment_container, fragment).commit();
                    }
                    break;
            }
            return true;
        }
    };
}