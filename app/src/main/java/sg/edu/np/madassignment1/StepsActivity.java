package sg.edu.np.madassignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class StepsActivity extends AppCompatActivity {

    int steps = 0;
    Bundle extra = new Bundle();
    String imgName;
    String recipeId;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://mad-asg-6df37-default-rtdb.asia-southeast1.firebasedatabase.app/");
    private DatabaseReference mDatabase = firebaseDatabase.getReference();
    Recipe recipe = new Recipe();
    ArrayList<Steps> stepsList = new ArrayList<>();
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
//      Hide the top title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        //setup alert dialog
        builder = new AlertDialog.Builder(this);

        //get the recipe id
        Intent intent = getIntent();
        recipeId = intent.getStringExtra("recipeId");

        //get steps list from the correct recipe
        mDatabase.child("Recipe").orderByChild("recipeId").equalTo(recipeId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    recipe = snapshot.getValue(Recipe.class);
                }
                for (Steps s : recipe.getStepsList()) {
                    stepsList.add(s);
                }
                Fragment fragment = new StepsFragment();
                //get the recipe's image
                imgName = recipe.getRecipeId() + ".jpeg";
                //send argument
                extra.putString("recipeId",  recipeId);
                extra.putInt("stepNum", steps);
                extra.putString("stepDes", stepsList.get(steps).getStepDescription());
                extra.putString("stepTime",  stepsList.get(steps).getTime());
                extra.putString("imgName",  imgName);
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
            switch (item.getItemId()) {
                case R.id.nav_back:
                    if (steps > 0) {
                        steps--;
                        extra.putString("recipeId",  recipe.getRecipeId());
                        extra.putInt("stepNum", steps);
                        extra.putString("stepDes", stepsList.get(steps).getStepDescription());
                        extra.putString("stepTime", stepsList.get(steps).getTime());
                        extra.putString("imgName",  imgName);
                        Log.d("debug", "back" + extra);
                        fragment.setArguments(extra);
                        getSupportFragmentManager().beginTransaction().replace(R.id.stepsFragment_container, fragment).commit();
                    }
                    else {
                        alertDialog("This is already the first step", "Notice");
                    }
                    break;
                case R.id.nav_home2:
                    finish();
                    break;
                case R.id.nav_next:
                    if (steps < stepsList.size() - 1) {
                        steps++;
                        extra.putString("recipeId",  recipe.getRecipeId());
                        extra.putInt("stepNum", steps);
                        extra.putString("stepDes", stepsList.get(steps).getStepDescription());
                        extra.putString("stepTime", stepsList.get(steps).getTime());
                        extra.putString("imgName",  imgName);
                        Log.d("debug", "next" + extra);
                        fragment.setArguments(extra);
                        getSupportFragmentManager().beginTransaction().replace(R.id.stepsFragment_container, fragment).commit();
                    }
                    else {
                        alertDialog("This is already the last step", "Notice");
                    }
                    break;
            }
            return true;
        }
    };

    public void alertDialog(String message, String title) {
        //Setting message manually and performing action on button click
        builder.setMessage(message)
                .setCancelable(true)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle(title);
        alert.show();
    }
}