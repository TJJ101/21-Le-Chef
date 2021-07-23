package sg.edu.np.madassignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://mad-asg-6df37-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference mDatabase = firebaseDatabase.getReference();
    Account newAccount = new Account();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        TextView usernameTxt = findViewById(R.id.usernameTxt);
        TextView emailTxt = findViewById(R.id.emailTxt);
        TextView passwordTxt = findViewById(R.id.passwordTxt);
        TextView cfmPasswordTxt = findViewById(R.id.cfmPasswordTxt);

        Button signUpBtn = findViewById(R.id.signUpBtn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(usernameTxt.getText().toString(), emailTxt.getText().toString(), passwordTxt.getText().toString());

//                Intent i = new Intent(SignUpActivity.this, MainActivity.class);
//                SignUpActivity.this.startActivity(i);
            }
        });
    }
    private void createAccount(String username, String email, String password) {
    mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
//                      Sign up success, bring user to log in page
                        Log.d("create", "createUserWithEmail:success");
//                      Get current user
                        FirebaseUser user = mAuth.getCurrentUser();
//                      loadingBar.setVisibility(View.GONE);
                        newAccount.setUsername(username);
                        newAccount.setEmail(email);
                        newAccount.setPassword(password);

                        //set username and save it to firebase
                        mDatabase.child("Users").child(user.getUid()).setValue(newAccount);

                    } else {
                    // If sign up fails, display a message to the user.
//                    Toast.makeText(SignUp.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
//                    loadingBar.setVisibility(View.GONE);
                    }
                }
            });
    }
}