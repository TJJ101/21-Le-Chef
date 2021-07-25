package sg.edu.np.madassignment1;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class SavedFragment extends Fragment {

    Recipe mRecipe;
    ArrayList<Recipe> recipeList = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved, container, false);

        ArrayList<Ingredient> iList = new ArrayList<>();
        iList.add(new Ingredient("Olive oil", 1, "Teaspoon"));
        iList.add(new Ingredient("Butter", 2, "Teaspoon"));
        for (int i = 0 ; i < 10; i++){
            mRecipe = new Recipe(
                    "Name",
                    "Cuisine",
                    "Rating",
                    "This is a description",
                    iList
            );
            recipeList.add(mRecipe);
        }

        recyclerView = view.findViewById(R.id.savedRecipeRecycler);
        RecipeAdapter mAdapter = new RecipeAdapter(recipeList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        return view;
    }
}