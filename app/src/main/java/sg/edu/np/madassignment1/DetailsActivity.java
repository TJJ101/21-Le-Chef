package sg.edu.np.madassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

    public Animation rotateOpen (Context context) {return AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim);}
    public Animation rotateClose (Context context) {return AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim);}
    public Animation fromBottom (Context context) {return AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim);}
    public Animation toBottom (Context context) {return AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim);}
    FloatingActionButton expandBtn, startBtn, saveBtn, backBtn;
    TextView nameTxt, desTxt, ingredientTxt;
    ListView stepsListView;
    StepsDetailsAdapter stepsDetailsAdapter;

    Boolean clicked = false;
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

        nameTxt = findViewById(R.id.nameTxt);
        desTxt = findViewById(R.id.descriptionTxt);
        ingredientTxt = findViewById(R.id.ingredientTxt);
        expandBtn = findViewById(R.id.expandFloatBtn);
        startBtn = findViewById(R.id.startFloatBtn);
        saveBtn = findViewById(R.id.saveFloatButton);
        backBtn = findViewById(R.id.backFloatBtn);
        stepsListView = findViewById(R.id.detailsStepsList);

        step1Time = stepsList.get(0).getTime();
        stepsDetailsAdapter = new StepsDetailsAdapter(stepsList, DetailsActivity.this);
        stepsListView.setAdapter(stepsDetailsAdapter);

        nameTxt.setText(recipe.getName());
        desTxt.setText(recipe.getDescription());
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

        //listeners for floating buttons
        //expand button
        expandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onExpandClicked();
            }
        });
        //start button
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
/*                bundle.putString("recipeId", recipeId);
                bundle.putString("step1Time", step1Time);*/
                bundle.putString("imgName", imgName);
                bundle.putSerializable("stepsList", stepsList);
                Intent in = new Intent(v.getContext(), StepsActivity.class);
                in.putExtras(bundle);
                v.getContext().startActivity(in);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //Slide right to Left Transition
                    overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
                }
                onExpandClicked();
            }
        });
        //save btn
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveBtn.getTag().equals("save")){
                    //saveBtn.setImageResource(R.drawable.ic_baseline_bookmark_24);
                    saveBtn.setTag("saved");
                }
                else{
                    //saveBtn.setImageResource(R.drawable.ic_baseline_bookmark_border_24);
                    saveBtn.setTag("save");
                }
            }
        });
        //backButton
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //Slide Left to Right Transition
                    overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_right);
                    onExpandClicked();
                }
            }
        });

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
    }

    public void onExpandClicked(){
        setVisibility(clicked);
        setAnimation(clicked);
        setClickable(clicked);
        clicked = !clicked;
    }

    private void setVisibility(Boolean clicked){
        if (!clicked){
            startBtn.setVisibility(View.VISIBLE);
            saveBtn.setVisibility(View.VISIBLE);
            backBtn.setVisibility(View.VISIBLE);
        }
        else {
            startBtn.setVisibility(View.INVISIBLE);
            saveBtn.setVisibility(View.INVISIBLE);
            backBtn.setVisibility(View.INVISIBLE);
        }
    }

    private void setAnimation(Boolean clicked){
        if(!clicked){
            startBtn.startAnimation(fromBottom(DetailsActivity.this));
            saveBtn.startAnimation(fromBottom(DetailsActivity.this));
            backBtn.startAnimation(fromBottom(DetailsActivity.this));
            expandBtn.startAnimation(rotateOpen(DetailsActivity.this));
        }
        else{
            startBtn.startAnimation(toBottom(DetailsActivity.this));
            saveBtn.startAnimation(toBottom(DetailsActivity.this));
            backBtn.startAnimation(toBottom(DetailsActivity.this));
            expandBtn.startAnimation(rotateClose(DetailsActivity.this));
        }
    }

    private void setClickable(Boolean clicked){
        if(clicked){
            startBtn.setClickable(false);
            saveBtn.setClickable(false);
            backBtn.setClickable(false);
        }
        else{
            startBtn.setClickable(true);
            saveBtn.setClickable(true);
            backBtn.setClickable(true);
        }
    }

    //Method for Android's back button animation
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        DetailsActivity.this.overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_right);
    }
}