package sg.edu.np.madassignment1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class ChecklistActivity extends AppCompatActivity {

    CheckListAdapter adapter;
    private RecyclerView recyclerView;
    ArrayList<Ingredient> ingredientList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);
        //      Hide the top title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        Intent in = getIntent();
        String name = in.getStringExtra("Name");
        String cuisine = in.getStringExtra("Cuisine");
        String rating = in.getStringExtra("Rating");
        String description = in.getStringExtra("Description");
        ingredientList = (ArrayList<Ingredient>) in.getSerializableExtra("IngredientList");
        Recipe recipe = new Recipe(name, cuisine, rating, description, ingredientList);

        RecyclerView recyclerView = findViewById(R.id.ingredientList);
        adapter = new CheckListAdapter(ingredientList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        Button checkListBtn = findViewById(R.id.checkListBtn);
        checkListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Ingredient> groceryList = adapter.getSelectedIngredient();
                Toast addedToGroceryList = Toast.makeText(getApplicationContext(), "Added to Grocery List", Toast.LENGTH_LONG);
                addedToGroceryList.show();
                Integer listSize = groceryList.size();
                Integer count = 0;
                while(count < listSize){
                    Log.d("Grocery List Data", groceryList.get(count).getName());
                    Log.d("Grocery List Data", String.valueOf(groceryList.get(count).getQuantity()));
                    count += 1;
                }
                finish();
            }
        });
    }
}