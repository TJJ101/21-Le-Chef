 package sg.edu.np.madassignment1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
/*import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;*/

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

 public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://mad-asg-6df37-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference mDatabase = firebaseDatabase.getReference();

     ArrayList<String> myRecipeList = new ArrayList<>();
     User theUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Hide the top title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        try{
            //get the list of recipe id that the user has created
            mDatabase.child("Users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    theUser = dataSnapshot.getValue(User.class);
                    for (String r : theUser.getCreatedRecipes()){
                        myRecipeList.add(r);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        catch (Exception e){
            Log.d("fuck", e + "");
        }


        // init bottom nav
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        //this is to open the home fragment when creating
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment_container, new HomeFragment()).commit();
    }

    //to tell the nav to be selected when switching
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            Bundle bundle = new Bundle();
            switch (item.getItemId()){
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_account:
                    selectedFragment = new AccountFragment();
                    bundle.putStringArrayList("createdRecipes", myRecipeList);
                    selectedFragment.setArguments(bundle);
                    break;
                case R.id.nav_add:
                    selectedFragment = new AddFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment_container, selectedFragment).commit();
            return true;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
        }
    }

    public void hideKeyboard(View view){
        final InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }  //function to hide keyboard (to be called in the fragments)

    //get the recipes list
/*    public ArrayList<Recipe> getAllRecipes(ArrayList<Recipe> recipeList){
        mDatabase.child("Recipe").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Recipe recipe = dataSnapshot.getValue(Recipe.class);
                    recipeList.add(recipe);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());
            }
        });
        return recipeList;
    }*/
}