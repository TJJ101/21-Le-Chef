package sg.edu.np.madassignment1;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class StepsFragment extends Fragment {

    //Declare timer
    CountDownTimer cTimer = null;
    ArrayList<Integer> sixtyArray = new ArrayList<Integer>();
    ArrayList<Integer> twentyfourArray = new ArrayList<Integer>();
    int totalMs = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_steps, container, false);

        TextView stepNumTxt = view.findViewById(R.id.stepNumTxt);
        // get the current step information
        if (getArguments() != null){
            Integer stepNum = getArguments().getInt("stepNum");
            stepNumTxt.setText("Step: " + stepNum);
        }
        else{
            stepNumTxt.setText("Step: 1");
        }

        //populate arrays
        int i = 0;
        while(i < 24){
            twentyfourArray.add(i);
            i++;
        }
        i = 0;
        while(i < 61){
            sixtyArray.add(i);
            i++;
        }

        //for the hour spinner
        Spinner hourSpinner = (Spinner) view.findViewById(R.id.hourSpinner);
        ArrayAdapter<Integer> spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, twentyfourArray);
        hourSpinner.setAdapter(spinnerAdapter);
        //Setting up for what the cuisine filter does
        hourSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                /*Log.v("item", (String) parent.getItemAtPosition(position));*/
                totalMs = (totalMs + (int) parent.getItemAtPosition(position)) * 60 * 60 * 1000;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // I have no idea what to put here, probably noting
            }
        });

        //for the minute spinner
        Spinner minuteSpinner = (Spinner) view.findViewById(R.id.minuteSpinner);
        ArrayAdapter<Integer> spinnerAdapter2 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, sixtyArray);
        minuteSpinner.setAdapter(spinnerAdapter2);
        //Setting up for what the cuisine filter does
        minuteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                /*Log.v("item", (String) parent.getItemAtPosition(position));*/
                totalMs = (totalMs + (int) parent.getItemAtPosition(position)) * 60 * 1000;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // I have no idea what to put here, probably noting
            }
        });

        //for the seconds spinner
        Spinner secondsSpinner = (Spinner) view.findViewById(R.id.secondsSpinner);
        secondsSpinner.setAdapter(spinnerAdapter2);
        //Setting up for what the cuisine filter does
        secondsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                /*Log.v("item", (String) parent.getItemAtPosition(position));*/
                totalMs = (totalMs + (int) parent.getItemAtPosition(position)) * 1000;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // I have no idea what to put here, probably noting
            }
        });

        Button startBtn = view.findViewById(R.id.stepsStartBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer(totalMs);
            }
        });
        Button stopBtn = view.findViewById(R.id.stepsStopBtn);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTimer();
            }
        });


        return view;
    }

    //start timer function
    void startTimer(int duration) {
        TextView timer = getView().findViewById(R.id.timerTxt);
        cTimer = new CountDownTimer(duration, 1000) {
            public void onTick(long millisUntilFinished) {
                timer.setText("Remaining: " + millisUntilFinished / 1000 + "s");
            }
            public void onFinish() {
                totalMs = 0;
                timer.setText("Done!");
            }
        };
        cTimer.start();
    }
    //cancel timer
    void cancelTimer() {
        TextView timer = getView().findViewById(R.id.timerTxt);
        if(cTimer!=null){
            timer.setText("Done");
            totalMs = 0;
            cTimer.cancel();}
    }
}
