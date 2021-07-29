package sg.edu.np.madassignment1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class IngredientAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<Ingredient> list = new ArrayList<Ingredient>();
    private Context context;

    public IngredientAdapter(ArrayList<Ingredient> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.ingredient_viewholder, null);
        }

        //Handle TextView and display string from your list
        TextView ingredientTxt = (TextView)view.findViewById(R.id.ingredientTextView);
        ingredientTxt.setText(list.get(position).getName());

        TextView qtyTxt = (TextView)view.findViewById(R.id.qtyTextView);
        qtyTxt.setText(""+list.get(position).getQuantity());

        TextView unitTxt = (TextView)view.findViewById(R.id.unitTextView);
        unitTxt.setText(list.get(position).getMeasurement());

        //Handle buttons and add onClickListeners
        TextView deleteBtn = (TextView) view.findViewById(R.id.ingredientDeleteBtn);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyDataSetChanged();
            }
        });

        return view;
    }
}
