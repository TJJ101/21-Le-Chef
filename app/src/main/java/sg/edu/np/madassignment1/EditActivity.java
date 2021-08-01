 package sg.edu.np.madassignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

 public class EditActivity extends AppCompatActivity {

    public FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://mad-asg-6df37-default-rtdb.asia-southeast1.firebasedatabase.app/");
    private DatabaseReference mDatabase = firebaseDatabase.getReference();

    List<User> userList = new ArrayList<User>();
    User theUser = MainActivity.mUser;
    AuthCredential credential;

    EditText username, password, password2;
    TextView unameErr, passErr, pass2Err;
    Boolean isValid = true;
    private String cfmPass = "";
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
//      Hide the top title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        //firebase
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        username = findViewById(R.id.editNameText);
        password = findViewById(R.id.editPasswordText);
        password2 = findViewById(R.id.editPassword2Text);
        unameErr = findViewById(R.id.nameWrongTxt);
        passErr = findViewById(R.id.passwordWrongTxt);
        pass2Err = findViewById(R.id.rePasswordWrongTxt);

        //get all users
        mDatabase.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    userList.add(eventSnapshot.getValue(User.class));
                }
                //set some data to be current user
                for (User u : userList){
                    if (u.getId().equals(theUser.getId())){
                        username.setText(u.getUsername());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        Button saveBtn = findViewById(R.id.editSubmitBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
                if (checks() && isValid){
                    if (!password.getText().toString().equals("")){
                        credential = EmailAuthProvider.getCredential(theUser.getEmail(), password.getText().toString());
                        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    if (!password2.getText().toString().equals("")){
                                        checkPass(user);
                                    }
                                    else if (!username.getText().toString().equals("")){
                                        updateUsername(user);
                                    }
                                }
                                else {
                                    passErr.setText("Wrong Password");
                                }
                            }
                        });
                    }
                    else if (username.getText().toString().equals(theUser.getUsername()) && password.getText().toString().equals("") && password2.getText().toString().equals("")){
                        unameErr.setText("");
                        passErr.setText("");
                        pass2Err.setText("");
                        Toast.makeText(context, "No changes made",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        passErr.setText("Please enter current password");
                    }
                }

            }
        });
    }

     public void checkPass(FirebaseUser user){
         //alert dialog to check for password again
         AlertDialog.Builder builder = new AlertDialog.Builder(context);
         View viewInflated = getLayoutInflater().inflate(R.layout.alert_input_view, null, false);
         builder.setTitle("Detected password change").setCancelable(true).setMessage("Please re-enter your new password");
         // Set up the input
         final EditText input = (EditText) viewInflated.findViewById(R.id.alertInput);
         builder.setView(viewInflated);
         // Set up the buttons
         builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 cfmPass = input.getText().toString();
                 if (!cfmPass.equals(password2.getText().toString())) {
                     Toast.makeText(context, "Passwords do not match", Toast.LENGTH_LONG).show();
                 }
                 else{
                     //Updating of password if password matches new pass
                     user.updatePassword(password2.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {
                             if (task.isSuccessful()) {
                                 Log.d("Success", "Password updated");
                             }
                             else {
                                 Log.d("Failed", "Error password not updated");
                             }
                         }
                     });
                     if(!username.getText().toString().equals("")){
                         updateUsername(user);
                     }
                     finish();
                 }
             }
         });
         builder.show();
     }

    public Boolean checks(){
        if (password2.getText().toString().length() < 6 && !password2.getText().toString().equals("")){
            pass2Err.setText("Password must have at least 6 characters");
            return false;
        }
        return true;
    }

    public void updateUsername(FirebaseUser user){
        DatabaseReference mDatabase3 = firebaseDatabase.getReference().child("Users").child(user.getUid()).child("username");
        mDatabase3.setValue(username.getText().toString());
        finish();
     }

    public void validate(){
        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    for(User user : userList){
                        if (user.getUsername().toLowerCase().equals(theUser.getUsername().toLowerCase())){
                            continue;
                        }
                        else if(user.getUsername().toLowerCase().equals(username.getText().toString().toLowerCase())){
                            unameErr.setText("This username has been taken");
                            isValid = false;
                        }
                    }
                    if(username.getText().toString().equals("")){
                        unameErr.setText("Username cannot be empty");
                        isValid = false;
                    }
                    else {
                        unameErr.setText("");
                        isValid = true;
                    }
                }
                else{unameErr.setText("");}
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                passErr.setText("");
            }
        });

        password2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(password2.getText().toString().equals("") || password2.getText().toString().length() < 6)
                    {
                        pass2Err.setText("Password must have at least 6 characters");
                        isValid = false;
                    }
                    else {
                        pass2Err.setText("");
                        isValid = true;
                    }
                }
                else{pass2Err.setText("");isValid=true;}
            }
        });
    }
}