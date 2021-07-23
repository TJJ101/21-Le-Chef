package sg.edu.np.madassignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://mad-asg-6df37-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference mDatabase = firebaseDatabase.getReference();
    User newUser = new User();
    List<User> userList = new ArrayList<User>();
    boolean validInput = true;
    TextView usernameTxt, emailTxt, passwordTxt, cfmPasswordTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();

        //Load all users for Username validation later
        mDatabase.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    userList.add(user);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        usernameTxt = findViewById(R.id.newUsernameTxt);
        emailTxt = findViewById(R.id.newEmailTxt);
        passwordTxt = findViewById(R.id.newPasswordTxt);
        cfmPasswordTxt = findViewById(R.id.cfmPasswordTxt);

        Button signUpBtn = findViewById(R.id.signUpBtn);
        Validation(usernameTxt, emailTxt, passwordTxt, cfmPasswordTxt);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(SignUpActivity.this);
                clearAllFocus();
                if(validInput){
                    Log.d("THANK GOD", "GO SLEEEP");
                    SignUp(usernameTxt.getText().toString(), emailTxt.getText().toString(), passwordTxt.getText().toString());
                    Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                    SignUpActivity.this.startActivity(i);
                }
            }
        });
    }

//  Clear all EditText focus
    public void clearAllFocus(){
        usernameTxt.clearFocus();
        emailTxt.clearFocus();
        passwordTxt.clearFocus();
        cfmPasswordTxt.clearFocus();
    }

//  Hide keyboard
    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

//  Validation for all fields after EditText lose focus (User change to different text box)
    private void Validation(TextView usernameTxt, TextView emailTxt, TextView passwordTxt, TextView cfmPasswordTxt){
        usernameTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                TextView usernameError = findViewById(R.id.uError);
                if(!hasFocus){
                    for(User user : userList){
                        if(user.getUsername().toLowerCase().equals(usernameTxt.getText().toString().toLowerCase())){
                            usernameError.setText("This username has been taken");
                            validInput = false;
                        }
                    }
                }
                else {
                    usernameError.setText("");
                    validInput = true;
                }
            }
        });

        emailTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                TextView emailError = findViewById(R.id.eError);
                if(!hasFocus){
                    if(!isValidEmail(emailTxt.getText()))
                    {
                        emailError.setText("Please enter a valid email address");
                        validInput = false;
                    }
                }
                else {
                    emailError.setText("");
                    validInput = true;
                }
            }
        });

        passwordTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                TextView passwordError = findViewById(R.id.pError);
                if(!hasFocus){
                    if(passwordTxt.getText().toString().length() < 6)
                    {
                        passwordError.setText("Password must have at least 6 characters");
                        validInput = false;
                    }
                }
                else {
                    passwordError.setText("");
                    validInput = true;
                }
            }
        });

        cfmPasswordTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                TextView usernameError = findViewById(R.id.cError);
                ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) usernameError.getLayoutParams();
                if(!hasFocus){
                    if(!passwordTxt.getText().toString().equals(cfmPasswordTxt.getText().toString())){
                        usernameError.setText("Password does not match");
                        validInput = false;
                    }
                    params.height = 50;
                }
                else {
                    usernameError.setText("");
                    validInput = true;
                    params.height = 0;
                }
                usernameError.setLayoutParams(params);
            }
        });
    }
//  Sign Up using firebase authentication
    private void SignUp(String username, String email, String password) {
    mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
//                      Sign up success, bring user to log in page
                        Log.d("Create", "createUserWithEmail:success");
//                      Get current user
                        FirebaseUser user = mAuth.getCurrentUser();
//                      loadingBar.setVisibility(View.GONE);
                        newUser.setUsername(username);
                        newUser.setEmail(email);
                        newUser.setPassword(password);
                        newUser.setId(user.getUid());

                        //set username and save it to firebase
                        mDatabase.child("Users").child(user.getUid()).setValue(newUser);
                    }
                    else {
                        // If sign up fails, display a message to the user.
                        Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                      loadingBar.setVisibility(View.GONE);
                    }
                }
            });
    }

//  Check email format
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}