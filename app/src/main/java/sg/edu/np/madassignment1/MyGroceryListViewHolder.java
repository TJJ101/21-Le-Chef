package sg.edu.np.madassignment1;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class MyGroceryListViewHolder extends RecyclerView.ViewHolder {
    public TextView myGroceryItem;
    public CheckBox acquiredCheck;

    public MyGroceryListViewHolder(@NonNull View itemView) {
        super(itemView);
        myGroceryItem = itemView.findViewById(R.id.myGroceryListItem);
        acquiredCheck = itemView.findViewById(R.id.acquiredCheck);
    }
}
