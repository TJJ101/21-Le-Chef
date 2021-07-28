package sg.edu.np.madassignment1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class StepsAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<Steps> list = new ArrayList<Steps>();
    private Context context;

    public StepsAdapter(ArrayList<Steps> list, Context context) {
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
            view = inflater.inflate(R.layout.steps_viewholder, null);
        }

        //Handle TextView and display string from your list
        TextView stepNum = (TextView)view.findViewById(R.id.stepNum);
        stepNum.setText(list.get(position).getStepNum());

        TextView stepsDesc = (TextView)view.findViewById(R.id.stepsTextView);
        stepsDesc.setText(list.get(position).getStepDescription());

        TextView stepTime = (TextView)view.findViewById(R.id.stepTime);
        stepTime.setText(list.get(position).getTime());

        //Handle buttons and add onClickListeners
        TextView deleteBtn = (TextView) view.findViewById(R.id.stepsDeleteBtn);

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
