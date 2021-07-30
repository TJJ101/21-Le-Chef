package sg.edu.np.madassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    Recipe recipe;
    ArrayList<Steps> stepsList = new ArrayList<>();
    String step1Time;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://mad-asg-6df37-default-rtdb.asia-southeast1.firebasedatabase.app/");
    private DatabaseReference mDatabase = firebaseDatabase.getReference();
    FirebaseStorage storage = FirebaseStorage.getInstance();

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
        String imgName = in.getStringExtra("image");
        ArrayList<Ingredient> ingredientList = (ArrayList<Ingredient>) in.getSerializableExtra("IngredientList");
        ArrayList<Steps> stepsList = (ArrayList<Steps>) in.getSerializableExtra("StepsList");
        recipe = new Recipe(name, cuisine, description, ingredientList, stepsList);

        //get image data
        ImageView imgView = findViewById(R.id.detailsImgView);
        StorageReference imageRef = storage.getReference().child("images").child(imgName);
        Glide.with(this).load(imageRef).centerCrop().into(imgView);

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
                step1Time = stepsList.get(0).getTime();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        TextView nameTxt = findViewById(R.id.nameTxt);
        nameTxt.setText(recipe.getName());

        TextView desTxt = findViewById(R.id.descriptionTxt);
        desTxt.setText(recipe.getDescription());

        TextView ingredientTxt = findViewById(R.id.ingredientTxt);
        String ingredients = "";
        for(Ingredient i : recipe.getIngredientList()){
            if(i.getMeasurement().equals("n/a")){
                ingredients += i.getQuantity() + " " + i.getName() + "\n";
            }
            else{
                ingredients += i.getQuantity() + i.getMeasurement() + " of " + i.getName() + "\n";
            }
        };
        ingredientTxt.setText(ingredients);
        

        //button to go add ingredient to Grocery List
        Button checklistBtn = findViewById(R.id.detailsGroceryListBtn);
        checklistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(DetailsActivity.this, GrocerylistActivity.class);
                intent.putExtra("IngredientList", (Serializable)recipe.getIngredientList());
                intent.putExtra("recipeName", recipe.getName());
                intent.putExtra("recipeImg", imgName);
                startActivity(intent);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    //Slide right to Left Transition
                    overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
                }
            }
        });

        //button to go start cooking
        Button startBtn = findViewById(R.id.detailsStartBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("recipeId", recipeId);
                bundle.putString("step1Time", step1Time);
                Intent in = new Intent(v.getContext(), StepsActivity.class);
                in.putExtras(bundle);
                v.getContext().startActivity(in);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //Slide right to Left Transition
                    overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
                }

            }
        });

        //button to go back
        Button backBtn = findViewById(R.id.detailsBackBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //Slide Left to Right Transition
                    overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_right);
                }
            }
        });

        //button to go home
        Button homeBtn = findViewById(R.id.detailsHomeBtn);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(v.getContext(), MainActivity.class);
                v.getContext().startActivity(in);
                //Slide right to Left Transition
                //overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
            }
        });
    }

    //Method for Android's back button animation
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        DetailsActivity.this.overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_right);
    }
}