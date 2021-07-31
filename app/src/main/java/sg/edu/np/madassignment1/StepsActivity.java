package sg.edu.np.madassignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.*;

public class StepsActivity extends AppCompatActivity {

    NumberPicker hourPicker;
    NumberPicker minutePicker;
    NumberPicker secondsPicker;
    Button stopBtn;
    Button startBtn;

    TextView stepNumTxt, stepDesTxt, timerTxt, reccoTimeTxt;
    ImageView recipeImg;

    int steps = 0;
    Bundle extra = new Bundle();
    String imgName;
    String recipeId;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://mad-asg-6df37-default-rtdb.asia-southeast1.firebasedatabase.app/");
    private DatabaseReference mDatabase = firebaseDatabase.getReference();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    Recipe recipe = new Recipe();
    ArrayList<Steps> stepsList = new ArrayList<>();
    AlertDialog.Builder builder;
    Context context;

    //Declare timer
    String[] time;
    CountDownTimer cTimer = null;
    ArrayList<String> sixtyArray = new ArrayList<String>();
    ArrayList<String> twentyfourArray = new ArrayList<String>();
    int hours;
    int minutes;
    int seconds;
    long millis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
//      Hide the top title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        //setup alert dialog
        builder = new AlertDialog.Builder(this);

        //set views
        stepNumTxt = findViewById(R.id.stepNumTxt);
        stepDesTxt = findViewById(R.id.stepDesTxt);
        reccoTimeTxt = findViewById(R.id.reccomendedTime);

        //get the recipe id
        Intent intent = getIntent();
        String imgName = intent.getStringExtra("imgName");
        stepsList = (ArrayList<Steps>) intent.getSerializableExtra("stepsList");

        //convert time into hr min sec
        time = stepsList.get(0).getTime().split(":");
        hours = Integer.parseInt(time[0]);
        minutes = Integer.parseInt(time[1]);
        seconds = Integer.parseInt(time[2]);

        //for getting image
        StorageReference imageRef = storage.getReference().child("images").child(imgName);
        Glide.with(StepsActivity.this).load(imageRef).centerCrop().into((ImageView) findViewById(R.id.stepsImgView));

        stepNumTxt.setText("Step: " + (steps + 1));
        stepDesTxt.setText(stepsList.get(steps).getStepDescription());
        reccoTimeTxt.setText("Recommended time: " + stepsList.get(steps).getTime());

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
        //spinner listeners
        //for the hour spinner
        hourPicker = (NumberPicker) findViewById(R.id.hoursPicker);
        hourPicker.setMaxValue(24);
        hourPicker.setMinValue(0);
        //for setting the timer to whatever is the default, set it to 30 for now
        hourPicker.setValue(hours);
        String[] twentyfourArray2 = new String[twentyfourArray.size()];
        twentyfourArray2 = twentyfourArray.toArray(twentyfourArray2);
        hourPicker.setDisplayedValues(twentyfourArray2);
        hourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                millis = 0;
                hours = hourPicker.getValue();
                Log.d("hours", hours + "");
            }
        });

        //for the minute spinner
        minutePicker = (NumberPicker) findViewById(R.id.minutePicker);
        minutePicker.setMaxValue(60);
        minutePicker.setMinValue(0);
        //for setting the timer to whatever is the default, set it to 30 for now
        minutePicker.setValue(minutes);
        String[] sixtyArray2 = new String[sixtyArray.size()];
        sixtyArray2 = sixtyArray.toArray(sixtyArray2);
        minutePicker.setDisplayedValues(sixtyArray2);
        minutePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                millis = 0;
                minutes = minutePicker.getValue();
                Log.d("minutes", minutes + "");
            }
        });

        //for the seconds spinner
        secondsPicker = (NumberPicker) findViewById(R.id.secondsPicker);
        secondsPicker.setMaxValue(60);
        secondsPicker.setMinValue(0);
        //for setting the timer to whatever is the default, set it to 30 for now
        secondsPicker.setValue(seconds);
        secondsPicker.setDisplayedValues(sixtyArray2);
        secondsPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                millis = 0;
                seconds = secondsPicker.getValue();
                Log.d("seconds", seconds + "");
            }
        });

        // start and stop button listeners
        startBtn = findViewById(R.id.stepsStartBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hours = hours * 60 * 60 * 1000;
                minutes = minutes * 60 * 1000;
                seconds = seconds * 1000;
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
        stopBtn = findViewById(R.id.stepsStopBtn);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTimer();
                startBtn.setText("Start");
            }
        });

        BottomNavigationView bottomNav = findViewById(R.id.steps_bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

    //to tell the nav to be selected when switching
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_back:
                    if (steps > 0) {
                        cancelTimer();
                        steps--;
                        setTimes();
                    }
                    else {
                        alertDialog("This is the first step", "Notice");
                    }
                    break;
                case R.id.nav_home2:
                    finish();
                    break;
                case R.id.nav_next:
                    if (steps < stepsList.size() - 1) {
                        cancelTimer();
                        steps++;
                        setTimes();
                    }
                    else {
                        alertDialog("This is the last step", "Notice");
                    }
                    break;
            }
            return true;
        }
    };

    //set the timer and time when switching steps
    public void setTimes(){
        stepNumTxt.setText("Step: " + (steps + 1));
        stepDesTxt.setText(stepsList.get(steps).getStepDescription());
        reccoTimeTxt.setText("Recommended time: " + stepsList.get(steps).getTime());
        time = stepsList.get(steps).getTime().split(":");
        hours = Integer.parseInt(time[0]);
        minutes = Integer.parseInt(time[1]);
        seconds = Integer.parseInt(time[2]);
        hourPicker.setValue(hours);
        minutePicker.setValue(minutes);
        secondsPicker.setValue(seconds);
    }

    //start timer function
    public void startTimer(int duration) {
        timerTxt = this.findViewById(R.id.timerTxt);
        cTimer = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                millis = millisUntilFinished;
                String hms = String.format("%02d:%02d:%02d",
                        MILLISECONDS.toHours(millis),
                        (MILLISECONDS.toMinutes(millis) - HOURS.toMinutes(MILLISECONDS.toHours(millis))),
                        (MILLISECONDS.toSeconds(millis) - MINUTES.toSeconds(MILLISECONDS.toMinutes(millis))));
                timerTxt.setText(hms);//set text
            }
            @Override
            public void onFinish() {
                timerTxt.setText("00:00:00");
            }
        };
        cTimer.start();
    }
    //cancel timer
    public void cancelTimer() {
        TextView timer = this.findViewById(R.id.timerTxt);
        if(cTimer!=null){
            hours = 0;
            minutes = 0;
            seconds = 0;
            millis = 0;
            hourPicker.setValue(0);
            minutePicker.setValue(0);
            secondsPicker.setValue(0);
            timer.setText("00:00:00");
            startBtn.setText("Start");
            cTimer.cancel();}
    }
    //pause timer
    public void pauseTimer(){
        cTimer.cancel();
    }

    public void alertDialog(String message, String title) {
        //Setting message manually and performing action on button click
        builder.setMessage(message)
                .setCancelable(true)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle(title);
        alert.show();
    }
}