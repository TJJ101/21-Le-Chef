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
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class StepsFragment extends Fragment {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://mad-asg-6df37-default-rtdb.asia-southeast1.firebasedatabase.app/");
    private DatabaseReference mDatabase = firebaseDatabase.getReference();
    FirebaseStorage storage = FirebaseStorage.getInstance();

    Integer stepNum;
    String stepDes;
    String stepTime;
    String imgName;
    String recipeId;
    Recipe recipe;

    //Declare timer
    CountDownTimer cTimer = null;
    ArrayList<String> sixtyArray = new ArrayList<String>();
    ArrayList<String> twentyfourArray = new ArrayList<String>();
    int hours;
    int minutes;
    int seconds;
    long millis;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_steps, container, false);

        TextView stepNumTxt = view.findViewById(R.id.stepNumTxt);
        TextView stepDesTxt = view.findViewById(R.id.stepDesTxt);

        // get the current step information
        if (getArguments() != null){
            stepNum = getArguments().getInt("stepNum");
            stepDes = getArguments().getString("stepDes");
            stepTime = getArguments().getString("stepTime");
            imgName = getArguments().getString("imgName");
            recipeId = getArguments().getString("recipeId");
            stepNumTxt.setText("Step: " + (stepNum + 1));
            stepDesTxt.setText(stepDes);
        }
        else{Log.d("FUCCCCCCCCCCKKKKKK", "KIIIIIIIIIIILLLLLLLLLLLMMMMMMMMMMMMMMEEEEEEEEEEEEEEEEEEEEEE");}

        //getting current recipe object=
        mDatabase.child("Recipe").orderByChild("recipeId").equalTo(recipeId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("FUCCCCCCCCCCKKKKKK", dataSnapshot.exists() + "");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    recipe = snapshot.getValue(Recipe.class);
                }

                //put recipe image into image view
                String imgName = recipe.getRecipeId() + ".jpeg";
                StorageReference imageRef = storage.getReference().child("images").child(imgName);
                Glide.with(getContext()).load(imageRef).centerCrop().into((ImageView) view.findViewById(R.id.stepsImgView));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //convert time string into hr min sec
/*        Log.d("ZAWARUDOOOOOOOO", stepTime);
        String[] time = stepTime.split(":");
        Log.d("ZAWARUDOOOOOOOO", time[0] + time[1] + time[2]);*/

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
        //for setting the timer to whatever is the default
        String[] twentyfourArray2 = new String[twentyfourArray.size()];
        twentyfourArray2 = twentyfourArray.toArray(twentyfourArray2);
        hourPicker.setDisplayedValues(twentyfourArray2);
        hourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                millis = 0;
                hours = hourPicker.getValue() * 60 * 60 * 1000;
                Log.d("hours", hours + "");
            }
        });

        //for the minute spinner
        NumberPicker minutePicker = (NumberPicker) view.findViewById(R.id.minutePicker);
        minutePicker.setMaxValue(60);
        minutePicker.setMinValue(0);
        //for setting the timer to whatever is the default
        minutePicker.setValue(30);
        String[] sixtyArray2 = new String[sixtyArray.size()];
        sixtyArray2 = sixtyArray.toArray(sixtyArray2);
        minutePicker.setDisplayedValues(sixtyArray2);
        minutePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                millis = 0;
                minutes = minutePicker.getValue() * 60 * 1000;
                Log.d("minutes", minutes + "");
            }
        });

        //for the seconds spinner
        NumberPicker secondsPicker = (NumberPicker) view.findViewById(R.id.secondsPicker);
        secondsPicker.setMaxValue(60);
        secondsPicker.setMinValue(0);
        //for setting the timer to whatever is the default
        secondsPicker.setDisplayedValues(sixtyArray2);
        secondsPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                millis = 0;
                seconds = secondsPicker.getValue() * 1000;
                Log.d("seconds", seconds + "");
            }
        });

        // start and stop button listeners
        Button startBtn = view.findViewById(R.id.stepsStartBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startBtn.getText().equals("Start")){
                    if (millis != 0){
                        startTimer((int) millis);
                    }
                    else{
                        startTimer(hours + minutes + seconds);
                    }
                    startBtn.setText("Pause");
                }
                else if (startBtn.getText().equals("Pause")){
                    pauseTimer();
                    startBtn.setText("Start");
                }

            }
        });
        Button stopBtn = view.findViewById(R.id.stepsStopBtn);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTimer();
                startBtn.setText("Start");
            }
        });


        return view;
    }

    //start timer function
    public void startTimer(int duration) {
        TextView timer = getActivity().findViewById(R.id.timerTxt);
        cTimer = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                millis = millisUntilFinished;
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
    public void cancelTimer() {
        TextView timer = getActivity().findViewById(R.id.timerTxt);
        if(cTimer!=null){
            millis = 0;
            timer.setText("00:00:00");
            cTimer.cancel();}
    }

    public void pauseTimer(){
        cTimer.cancel();
    }
}
