package sg.edu.np.madassignment1;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class AddFragment extends Fragment {

    ListView listView;
    ArrayList<String> stepsList;
    Button addBtn;
    EditText addTxt;
    ArrayAdapter<String> arrayAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);


        //for adding new steps to steps array and list view
        listView = (ListView) view.findViewById(R.id.stepListView);
        addBtn = (Button) view.findViewById(R.id.addStepBtn);
        addTxt = (EditText) view.findViewById(R.id.addStepTxt);

        stepsList = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, stepsList);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String steps = addTxt.getText().toString();
                if (!steps.isEmpty() && !(steps.trim().length() == 0)){
                    steps = addTxt.getText().toString();
                    stepsList.add(steps);
                    listView.setAdapter(arrayAdapter);
                    arrayAdapter.notifyDataSetChanged();
                }
                //hide the keyboard
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                addTxt.setText("");
            }
        });

        return view;
    }
}
