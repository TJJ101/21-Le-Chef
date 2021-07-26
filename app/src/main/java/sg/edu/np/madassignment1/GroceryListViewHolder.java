package sg.edu.np.madassignment1;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

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
