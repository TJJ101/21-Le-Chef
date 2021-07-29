package sg.edu.np.madassignment1;

import android.util.Log;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class FragmentAdapter extends FragmentStateAdapter {
    ArrayList<String> createdRecipes = new ArrayList<>();

    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, @NonNull ArrayList<String> recipes) {
        super(fragmentManager, lifecycle);
        createdRecipes = recipes;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Bundle bundle = new Bundle();
        switch (position){
            case 1:
                return new SavedFragment();
            case 2:
                return new MyGroceryListFragment();
            case 3:
                return new ProfileFragment();
        }
        Fragment myFrag = new MyRecipeFragment();
        bundle.putStringArrayList("createdRecipes", createdRecipes);
        myFrag.setArguments(bundle);
        return myFrag;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
