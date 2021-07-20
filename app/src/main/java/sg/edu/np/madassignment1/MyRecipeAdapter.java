package sg.edu.np.madassignment1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyRecipeAdapter extends RecyclerView.Adapter<MyRecipeAdapter.MyViewHolder>{

    ArrayList<myRecipe> data;
    public MyRecipeAdapter(ArrayList<myRecipe> input) { data = input; }

    @NonNull
    @Override
    public MyRecipeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.myrecipe_viewholder,
                parent,
                false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecipeAdapter.MyViewHolder holder, int position) {
        myRecipe myRecipesList = data.get(position);
        holder.myRecipeName.setText(myRecipesList.getName());
        holder.myRecipeCuisine.setText((myRecipesList.getCuisine()));
        holder.myRecipeRating.setText((myRecipesList.getRating()));
    }

    @Override
    public int getItemCount() {
        return data.size();
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
}
