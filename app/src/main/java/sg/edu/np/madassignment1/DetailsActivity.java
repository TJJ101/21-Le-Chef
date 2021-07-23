package sg.edu.np.madassignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DetailsActivity extends AppCompatActivity {

    Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent in = getIntent();
        String name = in.getStringExtra("name");
        String cuisine = in.getStringExtra("cuisine");
        String rating = in.getStringExtra("rating");
        String description = in.getStringExtra("description");
        recipe = new Recipe(name, cuisine, rating, description);

        TextView nameTxt = findViewById(R.id.nameTxt);
        nameTxt.setText(recipe.getName());

        TextView desTxt = findViewById(R.id.descriptionTxt);
        desTxt.setText(recipe.getDescription());

        //button to go start cooking
        Button startBtn = findViewById(R.id.detailsStartBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(v.getContext(), StepsActivity.class);
                v.getContext().startActivity(in);
            }
        });

        //button to go back
        Button backBtn = findViewById(R.id.detailsBackBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //button to go home
        Button homeBtn = findViewById(R.id.detailsHomeBtn);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(v.getContext(), MainActivity.class);
                v.getContext().startActivity(in);
            }
        });
    }
}