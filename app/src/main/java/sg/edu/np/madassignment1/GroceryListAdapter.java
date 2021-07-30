package sg.edu.np.madassignment1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.text.InputType;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class GroceryListAdapter extends RecyclerView.Adapter<GroceryListAdapter.GroceryListViewHolder> {
    ArrayList<Ingredient> ingredientList = new ArrayList<>();
    ArrayList<Ingredient> selectedIngredientList = new ArrayList<>();
    ArrayList<GroceryListViewHolder> viewList = new ArrayList<>();
    Context context;

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
        holder.editQty.setHint(String.valueOf(ingredient.getQuantity()));

        holder.editQty.setText(String.valueOf(ingredientList.get(position).getQuantity()));

        holder.editQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(holder, position);
            }
        });

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
                        if(selectedIngredientList.isEmpty() || selectedIngredientList == null){
                            ingredientList.get(position).setQuantity(Double.parseDouble(temp));
                            selectedIngredientList.add(ingredientList.get(position));
                        }
                        else {
                            if (getSelectedIngredientPos(selectedIngredientList, ingredientList.get(position).getName()).equals("")) {
                                ingredientList.get(position).setQuantity(Double.parseDouble(temp));
                                selectedIngredientList.add(ingredientList.get(position));
                            } else {
                                selectedIngredientList.get(Integer.parseInt(getSelectedIngredientPos(selectedIngredientList, ingredientList.get(position).getName()))).setQuantity(Double.parseDouble(temp));
                            }
                        }
                    }
                }
                else{
                    if(holder.checkBox.isChecked()){
                        if(selectedIngredientList.isEmpty() || selectedIngredientList == null) {
                            ingredientList.get(position).setQuantity(0);
                            selectedIngredientList.add(ingredientList.get(position));
                        }
                        else {
                            if (getSelectedIngredientPos(selectedIngredientList, ingredientList.get(position).getName()).equals("")) {
                                ingredientList.get(position).setQuantity(0);
                                selectedIngredientList.add(ingredientList.get(position));
                            } else {
                                selectedIngredientList.get(Integer.parseInt(getSelectedIngredientPos(selectedIngredientList, ingredientList.get(position).getName()))).setQuantity(0);
                            }
                        }
                    }
                }
            }
        });

        //If the measurement of the ingredient is n/a, dont display anything in the recylerView
        if(ingredientList.get(position).getMeasurement().equals("n/a")){
            holder.measurement.setVisibility(View.INVISIBLE);
        }
        else{
            holder.measurement.setVisibility(View.VISIBLE);
            holder.measurement.setText(ingredient.getMeasurement());
        }

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

    public GroceryListAdapter(ArrayList<Ingredient> iList, Context context){
        this.ingredientList = iList;
        this.context = context;
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
            String temp = vh.editQty.getText().toString();
            if(vh.checkBox.isChecked() == false){
                if(getSelectedIngredientPos(selectedIngredientList, String.valueOf(vh.ingredientName.getText())).equals("")){
                    if(!temp.isEmpty()){
                        Ingredient i = new Ingredient(String.valueOf(vh.ingredientName.getText()), Double.parseDouble(temp), String.valueOf(vh.measurement.getText()));
                        selectedIngredientList.add(i);
                    }
                    else{
                        Ingredient i = new Ingredient(String.valueOf(vh.ingredientName.getText()), 0, String.valueOf(vh.measurement.getText()));
                        selectedIngredientList.add(i);
                    }
                }
                else{
                    selectedIngredientList.get(Integer.parseInt(getSelectedIngredientPos(selectedIngredientList, String.valueOf(vh.ingredientName.getText())))).setQuantity(Double.parseDouble(String.valueOf(vh.editQty.getText())));
                }
                vh.checkBox.setChecked(true);
                Log.d("Debug 1", String.valueOf(selectedIngredientList.size()));
            }
        }
    }

    public void deselectAll(){
        selectedIngredientList.clear();
        for(GroceryListViewHolder vh : viewList){
            vh.checkBox.setChecked(false);
        }
    }

    private void openDialog(GroceryListViewHolder holder, int position){
        final LinearLayout linearLayout = (LinearLayout) ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.select_num_dialog, null);
        NumberPicker numberPicker = (NumberPicker) linearLayout.findViewById(R.id.numberPicker);
        NumberPicker numberPickerDec = (NumberPicker) linearLayout.findViewById(R.id.numberPickerDec);

        String decNum = String.valueOf(ingredientList.get(position).getQuantity());
        int decIndex = decNum.indexOf(".");
        Log.d("Debug Decimal", decNum);
        Log.d("Debug Decimal", String.valueOf(decIndex));
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(9999);
        numberPicker.setValue(Integer.parseInt(decNum.substring(0, decIndex)));
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        numberPickerDec.setMinValue(0);
        numberPickerDec.setMaxValue(99);
        numberPickerDec.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        final AlertDialog builder = new AlertDialog.Builder(context)
                .setPositiveButton("Submit", null)
                .setNegativeButton("Cancel", null)
                .setView(linearLayout)
                .setCancelable(false)
                .create();
        builder.show();
        //Setting up OnClickListener on positive button of AlertDialog
        builder.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.editQty.setText(String.valueOf(numberPicker.getValue()) + "." + String.valueOf(numberPickerDec.getValue()));
                builder.dismiss();
            }
        });
    }

    public boolean validationCheck(){
        int num = 0;
        for (Ingredient i : selectedIngredientList){
            if(i.getQuantity() > 0){
                num += 1;
            }
        }
        if(selectedIngredientList.size() > 0 && num == selectedIngredientList.size()){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public class GroceryListViewHolder extends RecyclerView.ViewHolder {
        public TextView ingredientName;
        public EditText editQty;
        public CheckBox checkBox;
        public TextView measurement;
        public View view;

        public GroceryListViewHolder(View itemView){
            super(itemView);
            ingredientName = itemView.findViewById(R.id.ingredientName);
            editQty = itemView.findViewById(R.id.editQty);
            checkBox = itemView.findViewById(R.id.checkBox);
            measurement = itemView.findViewById(R.id.measurement);
            view = itemView;
        }
    }
}
