package sg.edu.np.madassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
//      Hide the top title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        Intent in = getIntent();
        String name = in.getStringExtra("name");
        String cuisine = in.getStringExtra("cuisine");
        String rating = in.getStringExtra("rating");
        String description = in.getStringExtra("description");
        String recipeId = in.getStringExtra("recipeId");
        ArrayList<Ingredient> ingredientList = (ArrayList<Ingredient>) in.getSerializableExtra("IngredientList");
        recipe = new Recipe(name, cuisine, rating, description, ingredientList);

        TextView nameTxt = findViewById(R.id.nameTxt);
        nameTxt.setText(recipe.getName());

        TextView desTxt = findViewById(R.id.descriptionTxt);
        desTxt.setText(recipe.getDescription());

        TextView ingredientTxt = findViewById(R.id.ingredientTxt);
        String ingredients = "";
        for(Ingredient i : recipe.getIngredientList()){
            if(i.getUnit().equals("none")){
                ingredients += i.getQuantity() + " " + i.getName() + "\n:";
            }
            else{
                ingredients += i.getQuantity() + " " +  i.getUnit() + " of " + i.getName() + "\n:";
            }
        };
        ingredientTxt.setText(ingredients);
        

        //button to go ingredient checklist
        Button checklistBtn = findViewById(R.id.detailsChecklistBtn);
        checklistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(DetailsActivity.this, GrocerylistActivity.class);
                intent.putExtra("IngredientList", (Serializable)recipe.getIngredientList());
                startActivity(intent);
            }
        });

        //button to go start cooking
        Button startBtn = findViewById(R.id.detailsStartBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("recipeId", recipeId);
                Intent in = new Intent(v.getContext(), StepsActivity.class);
                in.putExtras(bundle);
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