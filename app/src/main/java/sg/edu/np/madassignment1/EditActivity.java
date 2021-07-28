package sg.edu.np.madassignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class EditActivity extends AppCompatActivity {

    public FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://mad-asg-6df37-default-rtdb.asia-southeast1.firebasedatabase.app/");
    private DatabaseReference mDatabase = firebaseDatabase.getReference();
    User theUser = new User();
    AuthCredential credential;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //firebase shit
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        TextView username = findViewById(R.id.editNameText);
        TextView password = findViewById(R.id.editPasswordText);

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

        Button saveBtn = findViewById(R.id.editSubmitBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!password.getText().toString().equals(theUser.getPassword())){
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
                else{
                    Log.d("NUUUUIIIIINNNNNNNNN", "NUUUUIIIIINN CHANGE BROZ");
                }

                if(!username.getText().toString().equals(theUser.getUsername())){
                    DatabaseReference mDatabase2 = firebaseDatabase.getReference().child("Users").child(user.getUid()).child("username");
                    mDatabase2.setValue(username.getText().toString());
                    finish();
                }
                else{
                    Log.d("NUUUUIIIIINNNNNNNNN", "NUUUUIIIIINN CHANGE MAANZZZZZ");
                }
            }
        });
    }
}