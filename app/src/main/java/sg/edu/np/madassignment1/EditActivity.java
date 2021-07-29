 package sg.edu.np.madassignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
    User theUser = new User();
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

        //firebase shit
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        username = findViewById(R.id.editNameText);
        password = findViewById(R.id.editPasswordText);
        password2 = findViewById(R.id.editPassword2Text);
        unameErr = findViewById(R.id.nameWrongTxt);
        passErr = findViewById(R.id.passwordWrongTxt);
        pass2Err = findViewById(R.id.rePasswordWrongTxt);

        validate();

        //get current user
        mDatabase.child("Users").orderByChild("id").equalTo(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    theUser = eventSnapshot.getValue(User.class);
                }
                username.setText(theUser.getUsername());
                password.setText(theUser.getPassword());
                credential = EmailAuthProvider.getCredential(theUser.getEmail(), theUser.getPassword());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //get all users
        mDatabase.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    userList.add(eventSnapshot.getValue(User.class));
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
                if (!password.getText().toString().equals(theUser.getPassword())){
                    if (isValid && !password2.getText().toString().equals("")){
                        checkPass(user);
                }
                else{pass2Err.setText("Passwords does not match");}
                }

                if(!username.getText().toString().equals(theUser.getUsername())){
                    DatabaseReference mDatabase2 = firebaseDatabase.getReference().child("Users").child(user.getUid()).child("username");
                    mDatabase2.setValue(username.getText().toString());
                    finish();
                }
            }
        });
    }

    public void checkPass(FirebaseUser user){
        //alert dialog to check for password again
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Detected password change").setCancelable(true).setMessage("Please re-enter your current password");

        // Set up the input
        final EditText input = new EditText(context);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cfmPass = input.getText().toString();
                if (!cfmPass.equals(theUser.getPassword())){
                    Toast toast = Toast.makeText(context, "Wrong Password", Toast.LENGTH_LONG);
                    toast.show();
                }
                else{
                    Log.d("yaaaaaaaaaaaaaay", "MATAFAKAAAAAAAAAAAA");
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user.updatePassword(password.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            DatabaseReference mDatabase2 = firebaseDatabase.getReference().child("Users").child(user.getUid()).child("password");
                                            mDatabase2.setValue(password.getText().toString());
                                            Log.d("YESSSSSSSSSSSSSSSSSS", "Password updated");

                                            if(!username.getText().toString().equals(theUser.getUsername())){
                                                DatabaseReference mDatabase3 = firebaseDatabase.getReference().child("Users").child(user.getUid()).child("username");
                                                mDatabase3.setValue(username.getText().toString());
                                                finish();
                                            }

                                            finish();
                                        } else {
                                            Log.d("NOOOOOOOOOOOOOOOOOOOO", "Error password not updated");
                                        }
                                    }
                                });
                            }
                            else {
                                Log.d("NOOOOOOOOOOOOOOOOOOOO", "Error auth failed");
                            }
                        }
                    });
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void validate(){
        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    for(User user : userList){
                        if (username.getText().toString().equals(theUser.getUsername())){
                            continue;
                        }
                        else if(user.getUsername().toLowerCase().equals(username.getText().toString().toLowerCase())){
                            unameErr.setText("This username has been taken");
                            isValid = false;
                        }
                        else if(username.getText().toString().equals("")){
                            unameErr.setText("Username cannot be empty");
                            isValid = false;
                        }
                        else {
                            unameErr.setText("");
                            isValid = true;
                        }
                    }
                }
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(password.getText().toString().equals("") || password.getText().toString().length() < 6)
                    {
                        passErr.setText("Password must have at least 6 characters");
                        isValid = false;
                    }
                    else {
                        passErr.setText("");
                        isValid = true;
                    }
                }
            }
        });

        password2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(!password.getText().toString().equals(theUser.getPassword()) && (password2.getText().toString().equals("") || !password.getText().toString().equals(password2.getText().toString()))){
                        pass2Err.setText("Passwords does not match");
                        isValid = false;
                    }
                    else {
                        password2.setText("");
                        isValid = true;
                    }
                }
            }
        });
    }

}