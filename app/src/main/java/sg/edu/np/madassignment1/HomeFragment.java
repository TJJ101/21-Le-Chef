package sg.edu.np.madassignment1;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public class HomeFragment extends Fragment {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://mad-asg-6df37-default-rtdb.asia-southeast1.firebasedatabase.app/");
    private DatabaseReference mDatabase = firebaseDatabase.getReference();
    private Recipe mRecipe;
    private RecipeAdapter adapter;
    private RecyclerView recyclerView;
    public ArrayList<Recipe> recipeList = new ArrayList<>();
/*    String[] name = {"Chicken Sandwich", "French Fries", "Chicken Soup", "Fish n Chips", "Eggs Benedict", "Ceasar Salad", "Beef Stew", "Salmon Shushi", "Ramen"};*/
    String[] cuisine = {"-None-", "Turkish", "Thai", "Japanese", "Indian", "French", "Chinese", "Western"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //everything for recycler view
        // putting data in to recycler view / recipe object
        adapter = new RecipeAdapter(getContext(), recipeList);
        mDatabase.child("Recipe").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                recipeList = new ArrayList<>();
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    recipeList.add(eventSnapshot.getValue(Recipe.class));
                }

                recyclerView = view.findViewById(R.id.recipeRecycler);
                adapter = new RecipeAdapter(getContext(),recipeList);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 1));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                adapter.notifyDataSetChanged();
                Log.d("SIZE", "" + recipeList.size());

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        Spinner spinner = (Spinner) view.findViewById(R.id.cuisineSpinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, cuisine);
        spinner.setAdapter(spinnerAdapter);
        //Setting up for what the cuisine filter does
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
                switch (position) {
                    case 0:
                        adapter.getCuisineFilter().filter("");
                        break;
                    case 1:
                        adapter.getCuisineFilter().filter("Turkish");
                        break;
                    case 2:
                        adapter.getCuisineFilter().filter("Thai");
                        break;
                    case 3:
                        adapter.getCuisineFilter().filter("Japanese");
                        break;
                    case 4:
                        adapter.getCuisineFilter().filter("Indian");
                        break;
                    case 5:
                        adapter.getCuisineFilter().filter("French");
                        break;
                    case 6:
                        adapter.getCuisineFilter().filter("Chinese");
                        break;
                    case 7:
                        adapter.getCuisineFilter().filter("Western");
                        break;

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // I have no idea what to put here, probably noting
            }
        });

        //for enabling the search bar
        SearchView searchView = view.findViewById(R.id.searchTxt);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        /*setHasOptionsMenu(true);*/

        //gird layout switcher
        Switch gridSwitch = (Switch) view.findViewById(R.id.gridSwitch);
        gridSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    adapter = new RecipeAdapter(getContext(),recipeList);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
                    gridSwitch.setText("Grid");
                }
                else{
                    adapter = new RecipeAdapter(getContext(),recipeList);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 1));
                    gridSwitch.setText("Single");
                }
            }
        });

        //everything for recycler
/*        recyclerView = view.findViewById(R.id.myRecipeRecycler);
        adapter = new RecipeAdapter(recipeList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);*/

        return view;
    }
/*    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu,inflater);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }*/
}