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
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    Recipe mRecipe;
    RecipeAdapter adapter;
    private RecyclerView recyclerView;
    public ArrayList<Recipe> recipeList = new ArrayList<>();
    String[] name = {"Chicken Sandwich", "French Fries", "Chicken Soup", "Fish n Chips", "Eggs Benedict", "Ceasar Salad", "Beef Stew", "Salmon Shushi", "Ramen"};
    String[] cuisine = {"- None -", "Turkish", "Thai", "Japanese", "Indian", "French"};



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // putting data in to recycler view / recipe object
        int i = 1;
        for (String n : name){
            if (i > 5){i = 1;}

            mRecipe = new Recipe(
                    n,
                    "Rating",
                    cuisine[i],
                    "This is a description"
            );
            recipeList.add(mRecipe);
            i ++;
        }

        //for the spinner activity
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

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // I have no idea what to put here, probably noting
            }
        });

        //for showing the search bar
        setHasOptionsMenu(true);

        //everything for recycler
        recyclerView = view.findViewById(R.id.myRecipeRecycler);
        adapter = new RecipeAdapter(recipeList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        return view;
    }



    @Override
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
    }
}