package sg.edu.np.madassignment1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class RecipesFragment extends Fragment {

    recipe mRecipe;
    ArrayList<recipe> recipeList = new ArrayList<>();
    private RecyclerView recyclerView;

    String[] name = {"Chicken Sandwich", "French Fries", "Chicken Soup", "Fish n Chips", "Eggs Benedict", "Ceasar Salad", "Beef Stew", "Salmon Shushi", "Ramen"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipes, container, false);

        for(String n : name){
            mRecipe = new recipe(
                    "Name: " + n,
                    "Cuisine",
                    "Rating"
            );
            recipeList.add(mRecipe);
        }

        recyclerView = view.findViewById(R.id.recipeRecyclerview);
        RecipeAdapter mAdapter = new RecipeAdapter(recipeList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        return view;
    }
}
