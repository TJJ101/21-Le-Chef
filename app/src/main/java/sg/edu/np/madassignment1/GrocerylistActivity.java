package sg.edu.np.madassignment1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class GrocerylistActivity extends AppCompatActivity {

    GroceryListAdapter adapter;
    private RecyclerView recyclerView;
    ArrayList<Ingredient> ingredientList = new ArrayList();

    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://mad-asg-6df37-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference mDatabase = firebaseDatabase.getReference();
    FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocerylist);
        //      Hide the top title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();

        Intent in = getIntent();
        ingredientList = (ArrayList<Ingredient>) in.getSerializableExtra("IngredientList");
        TextView header = findViewById(R.id.heading);
        header.setText("Ingredients for " + in.getStringExtra("recipeName"));

        //To display header background
        ImageView headerImg = findViewById(R.id.headerImg);
        String imgName = in.getStringExtra("recipeImg");
        StorageReference imageRef = storage.getReference().child("images").child(imgName);
        Glide.with(this).load(imageRef).into(headerImg);

        //This is for recyclerView
        RecyclerView recyclerView = findViewById(R.id.ingredientList);
        adapter = new GroceryListAdapter(ingredientList, this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        Button checkListBtn = findViewById(R.id.groceryListBtn);
        checkListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter.validationCheck()){
                    ArrayList<Ingredient> groceryList = adapter.getSelectedIngredient();
                    Toast addedToGroceryList = Toast.makeText(getApplicationContext(), "Added to Grocery List", Toast.LENGTH_LONG);
                    addedToGroceryList.show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    mDatabase.child("Users").child(user.getUid()).child("groceryList").setValue(groceryList);
                    Integer listSize = groceryList.size();
                    for(int i = 0; i < groceryList.size(); i++){
                        Log.d("Grocery Test Data", groceryList.get(i).getName());
                        Log.d("Grocery Test Data", String.valueOf(groceryList.get(i).getQuantity()));
                    }
                    finish();
                    //Fade animation for transition
                    overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
                }
                else{
                    Toast fail = Toast.makeText(getApplicationContext(), "No item selected/No quantity entered", Toast.LENGTH_LONG);
                    fail.show();
                }
            }
        });

        Switch selectAll = findViewById(R.id.selectAllSwitch);
        selectAll.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (selectAll.isChecked()){
                    adapter.selectAll();
                    selectAll.setText("Deselect All");
                } else {
                    adapter.deselectAll();
                    selectAll.setText("Select All");
                }
            }
        });

        Button backToHomeBtn = findViewById(R.id.groceryListHomeBtn);
        backToHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
                //Fade animation for transition
                overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
            }
        });
    }
}