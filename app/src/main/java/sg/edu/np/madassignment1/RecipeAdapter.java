package sg.edu.np.madassignment1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.MyViewHolder> implements Filterable {

    private List<Recipe> recipeList;
    private List<Recipe> recipeListFull;


    @NonNull
    @Override
    public RecipeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recipe_viewholder,
                parent,
                false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.MyViewHolder holder, int position) {
        Recipe selectedRecipe = recipeList.get(position);
        holder.myRecipeName.setText(selectedRecipe.getName());
        holder.myRecipeCuisine.setText((selectedRecipe.getCuisine()));
        holder.myRecipeRating.setText((selectedRecipe.getRating()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = new Bundle();
                extras.putString("name", selectedRecipe.getName());
                extras.putString("cuisine", selectedRecipe.getCuisine());
                extras.putString("rating", selectedRecipe.getRating());
                Intent in = new Intent(holder.itemView.getContext(), DetailsActivity.class);
                in.putExtras(extras);
                holder.itemView.getContext().startActivity(in);

                /*fragment.setArguments(extras);
               FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                *//*FragmentTransaction transaction = myActivity.getSupportFragmentManager().beginTransaction();*//*
                fragmentManager.beginTransaction().replace(R.id.fragment_container, new DetailsFragment()).commit();*/
            }
        });
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView myRecipeName, myRecipeCuisine, myRecipeRating;
        ImageView myRecipeImg;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            myRecipeName = itemView.findViewById(R.id.myRecipeName);
            myRecipeCuisine = itemView.findViewById(R.id.myRecipeCuisine);
            myRecipeRating = itemView.findViewById(R.id.myRecipeRating);
            myRecipeImg = itemView.findViewById(R.id.myRecipeImg);
        }
    }


    RecipeAdapter(ArrayList<Recipe> recipeList){
        this.recipeList = recipeList;
        recipeListFull = new ArrayList<>(recipeList);
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    @Override
    public Filter getFilter() {
        return recipeFilter;
    }

    private Filter recipeFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Recipe> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(recipeListFull);
            }
            else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Recipe r : recipeListFull){
                    if (r.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(r);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            recipeList.clear();
            recipeList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
