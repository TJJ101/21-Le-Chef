package sg.edu.np.madassignment1;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class StepsFragment extends Fragment {

    //Declare timer
    CountDownTimer cTimer = null;
    ArrayList<String> sixtyArray = new ArrayList<String>();
    ArrayList<String> twentyfourArray = new ArrayList<String>();
    int hours;
    int minutes;
    int seconds;

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
        while(i < 25){
            twentyfourArray.add(String.valueOf(i));
            i++;
        }
        i = 0;
        while(i < 61){
            sixtyArray.add(String.valueOf(i));
            i++;
        }

        //for the hour spinner
        NumberPicker hourPicker = (NumberPicker) view.findViewById(R.id.hoursPicker);
        hourPicker.setMaxValue(24);
        hourPicker.setMinValue(0);
        String[] twentyfourArray2 = new String[twentyfourArray.size()];
        twentyfourArray2 = twentyfourArray.toArray(twentyfourArray2);
        hourPicker.setDisplayedValues(twentyfourArray2);
        hourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                hours = hourPicker.getValue() * 60 * 60 * 1000;
                Log.d("hours", hours + "");
            }
        });

        //for the minute spinner
        NumberPicker minutePicker = (NumberPicker) view.findViewById(R.id.minutePicker);
        minutePicker.setMaxValue(60);
        minutePicker.setMinValue(0);
        String[] sixtyArray2 = new String[sixtyArray.size()];
        sixtyArray2 = sixtyArray.toArray(sixtyArray2);
        minutePicker.setDisplayedValues(sixtyArray2);
        minutePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                minutes = minutePicker.getValue() * 60 * 1000;
                Log.d("minutes", minutes + "");
            }
        });

        //for the seconds spinner
        NumberPicker secondsPicker = (NumberPicker) view.findViewById(R.id.secondsPicker);
        secondsPicker.setMaxValue(60);
        secondsPicker.setMinValue(0);
        secondsPicker.setDisplayedValues(sixtyArray2);
        secondsPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                seconds = secondsPicker.getValue() * 1000;
                Log.d("seconds", seconds + "");
            }
        });

        // start and stop button listeners
        Button startBtn = view.findViewById(R.id.stepsStartBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer(hours + minutes + seconds);
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
            @Override
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                String hms = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                (TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))),
                (TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))));
                timer.setText(hms);//set text
            }
            @Override
            public void onFinish() {
                timer.setText("00:00:00");
            }
        };
        cTimer.start();
    }
    //cancel timer
    void cancelTimer() {
        TextView timer = getView().findViewById(R.id.timerTxt);
        if(cTimer!=null){
            timer.setText("00:00:00");
            cTimer.cancel();}
    }
}
