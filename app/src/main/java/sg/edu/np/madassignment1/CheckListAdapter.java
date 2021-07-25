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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CheckListAdapter extends RecyclerView.Adapter<CheckListViewHolder> {
    Context context;
    ArrayList<Ingredient> ingredientList;

    @NonNull
    @Override
    public CheckListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.checklist_viewholder,
                parent,
                false);
        return new CheckListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckListViewHolder holder, int position) {
        Ingredient ingredient = ingredientList.get(position);
        holder.ingredientName.setText(ingredient.getName());
        holder.editQty.setText(""+ingredient.getQuantity());
        holder.measurement.setText(ingredient.getMeasurement());

    }

    public CheckListAdapter (ArrayList<Ingredient> iList){
        this.ingredientList = iList;
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }
}
