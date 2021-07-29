package sg.edu.np.madassignment1;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayout;

public class FragmentAdapter extends FragmentStateAdapter {
    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }
    
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment selectedFragment = null;
        switch (position){
            case 1:
                return new SavedFragment();
            case 2:
                return new MyGroceryListFragment();
            case 3:
                return new ProfileFragment();
        }
        return new MyRecipeFragment();
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
