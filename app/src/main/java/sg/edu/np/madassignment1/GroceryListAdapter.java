package sg.edu.np.madassignment1;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.text.InputType;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GroceryListAdapter extends RecyclerView.Adapter<GroceryListViewHolder> {
    Context context;
    ArrayList<Ingredient> ingredientList = new ArrayList<>();
    ArrayList<Ingredient> selectedIngredientList = new ArrayList<>();
    ArrayList<GroceryListViewHolder> viewList = new ArrayList<>();

    @NonNull
    @Override
    public GroceryListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.grocerylist_viewholder,
                parent,
                false);
        return new GroceryListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryListViewHolder holder, int position) {
        viewList.add(holder);
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
                    if(holder.checkBox.isChecked()){
                        if(getSelectedIngredientPos(selectedIngredientList, ingredientList.get(position).getName()).equals("")){
                            ingredientList.get(position).setQuantity(Double.parseDouble(temp));
                            selectedIngredientList.add(ingredientList.get(position));
                        }
                        else{
                            selectedIngredientList.get(Integer.parseInt(getSelectedIngredientPos(selectedIngredientList, ingredientList.get(position).getName()))).setQuantity(Double.parseDouble(temp));
                        }
                    }
                    else{
                        ingredientList.get(position).setQuantity(Double.parseDouble(temp));
                    }
                }
                else{
                    if(holder.checkBox.isChecked()){
                        if(getSelectedIngredientPos(selectedIngredientList, ingredientList.get(position).getName()).equals("")){
                            ingredientList.get(position).setQuantity(0);
                            selectedIngredientList.add(ingredientList.get(position));
                        }
                        else{
                            selectedIngredientList.get(Integer.parseInt(getSelectedIngredientPos(selectedIngredientList, ingredientList.get(position).getName()))).setQuantity(0);
                        }
                    }
                    else {
                        ingredientList.get(position).setQuantity(0);
                    }
                }
            }
        });

        holder.measurement.setText(ingredient.getMeasurement());

        holder.checkBox.setTag(position);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer pos = (Integer) holder.checkBox.getTag();
                if(holder.checkBox.isChecked()) {
                    String temp = holder.editQty.getText().toString();
                    if (!temp.isEmpty()) {
                        ingredientList.get(pos).setQuantity(Double.parseDouble(temp));
                        if(selectedIngredientList.isEmpty() || selectedIngredientList == null){
                            selectedIngredientList.add(ingredientList.get(pos));
                        }
                        else{
                            if(getSelectedIngredientPos(selectedIngredientList, ingredientList.get(pos).getName()).equals("")){
                                selectedIngredientList.add(ingredientList.get(pos));
                            }
                            else{
                                selectedIngredientList.get(Integer.parseInt(getSelectedIngredientPos(selectedIngredientList, ingredientList.get(pos).getName()))).setQuantity(Double.parseDouble(temp));

                            }
                        }
                    }
                    else{
                        if(getSelectedIngredientPos(selectedIngredientList, ingredientList.get(pos).getName()).equals("")){
                            ingredientList.get(pos).setQuantity(0);
                            selectedIngredientList.add(ingredientList.get(pos));
                        }
                        else{
                            selectedIngredientList.get(Integer.parseInt(getSelectedIngredientPos(selectedIngredientList, ingredientList.get(pos).getName()))).setQuantity(0);
                        }
                    }
                }
                else{
                    selectedIngredientList.remove(Integer.parseInt(getSelectedIngredientPos(selectedIngredientList, ingredientList.get(pos).getName())));
                }
            }
        });
    }

    public GroceryListAdapter(ArrayList<Ingredient> iList){
        this.ingredientList = iList;
    }

    public ArrayList<Ingredient> getSelectedIngredient(){
        return selectedIngredientList;
    }

    public String getSelectedIngredientPos(ArrayList<Ingredient> iList, String ingredientName){
        String pos = "";
        for(int i = 0; i < iList.size(); i++){
            if(iList.get(i).getName().equals(ingredientName)){
                pos = String.valueOf(i);
            }
        }
        return pos;

    }

    public void selectAll(){
        for(GroceryListViewHolder vh : viewList){
            vh.checkBox.setChecked(true);
        }
    }

    public void deselectAll(){
        for(GroceryListViewHolder vh : viewList){
            vh.checkBox.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }
}
