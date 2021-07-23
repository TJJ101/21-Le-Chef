package sg.edu.np.madassignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
        recipe = new Recipe(name, cuisine, rating);

        TextView nameTxt = findViewById(R.id.nameTxt);
        nameTxt.setText(recipe.getName());

        Button backButton = findViewById(R.id.detailsBackBtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(v.getContext(), MainActivity.class);
                v.getContext().startActivity(in);
            }
        });
    }
}