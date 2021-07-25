package sg.edu.np.madassignment1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.text.InputType;

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
        holder.editQty.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        holder.editQty.setHint(String.valueOf(ingredient.getQuantity()));

        holder.editQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String temp = holder.editQty.getText().toString();
                if(!temp.isEmpty()){
                    ingredientList.get(position).setQuantity(Double.parseDouble(holder.editQty.getText().toString()));
                }
            }
        });

        holder.measurement.setText(ingredient.getMeasurement());
        holder.checkBox.setChecked(ingredient.getSelected());


        holder.checkBox.setTag(position);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer pos = (Integer) holder.checkBox.getTag();
                if(ingredientList.get(pos).getSelected()){
                    ingredientList.get(pos).setSelected(false);
                }
                else{
                    ingredientList.get(pos).setSelected(true);
                }
            }
        });
    }

    public CheckListAdapter (ArrayList<Ingredient> iList){
        this.ingredientList = iList;
    }

    public ArrayList<Ingredient> getSelectedIngredient(){
        ArrayList<Ingredient> selectedIngredientList = new ArrayList<>();
        for(Ingredient i : ingredientList){
            if(i.getSelected()){
                selectedIngredientList.add(i);
            }
        }
        return selectedIngredientList;
    }
    @Override
    public int getItemCount() {
        return ingredientList.size();
    }
}
