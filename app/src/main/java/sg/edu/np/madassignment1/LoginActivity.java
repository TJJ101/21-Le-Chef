package sg.edu.np.madassignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Html;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://mad-asg-6df37-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference mDatabase = firebaseDatabase.getReference();
    User newUser = new User();
    boolean validInput = true;
    TextView emailTxt, passwordTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//      Hide the top title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();

        emailTxt = findViewById(R.id.emailTxt);
        passwordTxt = findViewById(R.id.passwordTxt);
//      Validate input fields
        Validation(emailTxt, passwordTxt);
        Button loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(LoginActivity.this);
                clearAllFocus();
                EmptyFieldValidation(emailTxt, passwordTxt);
                if(validInput) {
                    Login(emailTxt.getText().toString(), passwordTxt.getText().toString());
                }
            }
        });
        TextView toSignUpBtn = findViewById(R.id.toSignUp);
//      Make specific text bold
        String sourceString = "Don't have an account? <b>Sign Up</b>" ;
        toSignUpBtn.setText(Html.fromHtml(sourceString));
//      Add onclick listener to navigate to Sign Up page
        toSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }
//  Login using Firebase authentication
    public void Login(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Sign In", "signInWithEmail:success");
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            LoginActivity.this.startActivity(i);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Sign In", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            TextView passwordError = findViewById(R.id.pError2);
                            ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) passwordError.getLayoutParams();
                            passwordError.setText("Invalid email or password");
                            params.height = 50;
                            passwordError.setLayoutParams(params);
                        }
                    }
                });
    }

    private void EmptyFieldValidation(TextView emailTxt, TextView passwordTxt){
        if(emailTxt.getText().toString().equals("")){
            TextView emailError = findViewById(R.id.eError2);
            emailError.setText("Please enter your    email address");
            validInput = false;
        }
        if(passwordTxt.getText().toString().equals("")){
            TextView passwordError = findViewById(R.id.pError2);
            ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) passwordError.getLayoutParams();
            passwordError.setText("Please enter your password");
            params.height = 50;
            passwordError.setLayoutParams(params);
            validInput = false;
        }
    }

    //  Validation for all fields after EditText lose focus (User change to different text box)
    private void Validation(TextView emailTxt, TextView passwordTxt){
        emailTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                TextView emailError = findViewById(R.id.eError2);
                if(!hasFocus){
                    if(!emailTxt.getText().toString().equals("") && !isValidEmail(emailTxt.getText()))
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
                TextView passwordError = findViewById(R.id.pError2);
                ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) passwordError.getLayoutParams();
                if(hasFocus){
                    passwordError.setText("");
                    validInput = true;
                    params.height = 0;
                    passwordError.setLayoutParams(params);
                }
            }
        });
    }

    //  Check email format
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    //  Clear all EditText focus
    private void clearAllFocus(){
        emailTxt.clearFocus();
        passwordTxt.clearFocus();
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
}