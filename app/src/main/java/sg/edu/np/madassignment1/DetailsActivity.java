package sg.edu.np.madassignment1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    FloatingActionButton expandBtn, startBtn, saveBtn, backBtn, rateBtn;
    TextView nameTxt, desTxt, ingredientTxt, creatorTxt;
    RatingBar ratingBar;
    ListView stepsListView;
    StepsDetailsAdapter stepsDetailsAdapter;
    RadioGroup radioGroup;
    RadioButton radioBtn;
    ArrayList<String> savedList = new ArrayList<>();
    Boolean clicked = false;
    Recipe recipe;
    String recipeId;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://mad-asg-6df37-default-rtdb.asia-southeast1.firebasedatabase.app/");
    private DatabaseReference mDatabase = firebaseDatabase.getReference();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    User theUser = MainActivity.mUser;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
//      Hide the top title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        Intent in = getIntent();
        String imgName = in.getStringExtra("image");
        recipe = (Recipe) in.getSerializableExtra("Recipe");

        nameTxt = findViewById(R.id.nameTxt);
        desTxt = findViewById(R.id.descriptionTxt);
        ingredientTxt = findViewById(R.id.ingredientTxt);
        creatorTxt = findViewById(R.id.creatorTxt);
        expandBtn = findViewById(R.id.expandFloatBtn);
        startBtn = findViewById(R.id.startFloatBtn);
        saveBtn = findViewById(R.id.saveFloatButton);
        rateBtn = findViewById(R.id.rateFloatBtn);
        backBtn = findViewById(R.id.backFloatBtn);
        stepsListView = findViewById(R.id.detailsStepsList);

        //set image to image view
        ImageView imgView = findViewById(R.id.detailsImgView);
        StorageReference imageRef = storage.getReference().child("images").child(imgName);
        Glide.with(this).load(imageRef).centerCrop().into(imgView);

        stepsDetailsAdapter = new StepsDetailsAdapter((ArrayList<Steps>) recipe.getStepsList(), DetailsActivity.this);
        stepsListView.setAdapter(stepsDetailsAdapter);
        setListViewHeightBasedOnChildren(stepsListView);

        nameTxt.setText(recipe.getName());
        desTxt.setText(recipe.getDescription());
        creatorTxt.setText("by " + recipe.getCreatorId());
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

        //add all the current saved recipes to savedList
        for (String r : theUser.getSavedRecipes()){
            savedList.add(r);
        }
        if (checkSaved()){
            saveBtn.setImageResource(R.drawable.ic_baseline_bookmark_24);
            saveBtn.setTag("saved");
        }
        else{
            saveBtn.setImageResource(R.drawable.ic_baseline_bookmark_border_24);
            saveBtn.setTag("save");
        }

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
                bundle.putSerializable("stepsList", (Serializable) recipe.getStepsList());
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
                    savedList.add(recipe.getRecipeId());
                    DatabaseReference mDatabase2 = firebaseDatabase.getReference().child("Users").child(theUser.getId()).child("savedRecipes");
                    mDatabase2.setValue(savedList);
                    saveBtn.setImageResource(R.drawable.ic_baseline_bookmark_24);
                    saveBtn.setTag("saved");
                    toast.makeText(DetailsActivity.this, "Recipe saved", Toast.LENGTH_SHORT).show();
                }
                else{
                     savedList.remove(recipe.getRecipeId());
                    DatabaseReference mDatabase2 = firebaseDatabase.getReference().child("Users").child(theUser.getId()).child("savedRecipes");
                    mDatabase2.setValue(savedList);
                    saveBtn.setImageResource(R.drawable.ic_baseline_bookmark_border_24);
                    saveBtn.setTag("save");
                    toast.makeText(DetailsActivity.this, "Recipe unsaved", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //rate button

        //Open rating alert box
        rateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRatingDialog();
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

    public void openRatingDialog(){
        LayoutInflater l = getLayoutInflater();
        final View v = l.inflate(R.layout.select_ratings_dialog, null);

        radioGroup =  v.findViewById(R.id.radioGroup);

        final AlertDialog builder = new AlertDialog.Builder(this)
                .setPositiveButton("Submit", null)
                .setNegativeButton("Cancel", null)
                .setTitle("Select rating")
                .setView(v)
                .setCancelable(false)
                .create();
        builder.show();
        //Setting up OnClickListener on positive button of AlertDialog
        builder.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double rating;
                int radioId = radioGroup.getCheckedRadioButtonId();
                radioBtn = v.findViewById(radioId);

                if(radioBtn.getText().equals("1")){
                    rating = recipe.getRatings().getTwoStar() + 1;
                    recipe.getRatings().setOneStar(rating);
                    mDatabase.child("Recipe").child(recipe.getRecipeId()).child("ratings").setValue(recipe.getRatings());
                    toast.makeText(DetailsActivity.this, "Thanks for the feedback", Toast.LENGTH_SHORT).show();
                }
                else if(radioBtn.getText().equals("2")){
                    rating = recipe.getRatings().getOneStar() + 1;
                    recipe.getRatings().setTwoStar(rating);
                    mDatabase.child("Recipe").child(recipe.getRecipeId()).child("ratings").setValue(recipe.getRatings());
                    toast.makeText(DetailsActivity.this, "Thanks for the feedback :)", Toast.LENGTH_SHORT).show();
                }
                else if(radioBtn.getText().equals("3")){
                    rating = recipe.getRatings().getThreeStar() + 1;
                    recipe.getRatings().setThreeStar(rating);
                    mDatabase.child("Recipe").child(recipe.getRecipeId()).child("ratings").setValue(recipe.getRatings());
                    toast.makeText(DetailsActivity.this, "Thanks for the rating 😄", Toast.LENGTH_SHORT).show();
                }
                else if(radioBtn.getText().equals("4")){
                    rating = recipe.getRatings().getFourStar() + 1;
                    recipe.getRatings().setFourStar(rating);
                    mDatabase.child("Recipe").child(recipe.getRecipeId()).child("ratings").setValue(recipe.getRatings());
                    toast.makeText(DetailsActivity.this, "Thanks for the good rating 😁", Toast.LENGTH_SHORT).show();
                }
                else if(radioBtn.getText().equals("5")){
                    rating = recipe.getRatings().getFiveStar() + 1;
                    recipe.getRatings().setFiveStar(rating);
                    mDatabase.child("Recipe").child(recipe.getRecipeId()).child("ratings").setValue(recipe.getRatings());
                    toast.makeText(DetailsActivity.this, "Thanks for the perfect rating 🤩", Toast.LENGTH_SHORT).show();
                }
                else {
                    toast.makeText(DetailsActivity.this, "No ratings provided", Toast.LENGTH_SHORT).show();
                }

                builder.dismiss();
            }
        });
    }

    //checking if recipe is saved
    public Boolean checkSaved(){
        if (!savedList.isEmpty()){
            for (String s : savedList){
                if (s.equals(recipe.getRecipeId())){
                    Log.d("YESYESYESYEYSYES", recipe.getRecipeId());
                    return true;
                }
            }
        }
        return false;
    }

    //setting the list view to show all rows
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
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
            rateBtn.setVisibility(View.VISIBLE);
            backBtn.setVisibility(View.VISIBLE);
        }
        else {
            startBtn.setVisibility(View.INVISIBLE);
            saveBtn.setVisibility(View.INVISIBLE);
            rateBtn.setVisibility(View.INVISIBLE);
            backBtn.setVisibility(View.INVISIBLE);
        }
    }

    private void setAnimation(Boolean clicked){
        if(!clicked){
            startBtn.startAnimation(fromBottom(DetailsActivity.this));
            saveBtn.startAnimation(fromBottom(DetailsActivity.this));
            backBtn.startAnimation(fromBottom(DetailsActivity.this));
            rateBtn.startAnimation(fromBottom(DetailsActivity.this));
            expandBtn.startAnimation(rotateOpen(DetailsActivity.this));
        }
        else{
            startBtn.startAnimation(toBottom(DetailsActivity.this));
            saveBtn.startAnimation(toBottom(DetailsActivity.this));
            backBtn.startAnimation(toBottom(DetailsActivity.this));
            rateBtn.startAnimation(toBottom(DetailsActivity.this));
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