package sg.edu.np.madassignment1;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MyGroceryListFragment extends Fragment {

    public FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://mad-asg-6df37-default-rtdb.asia-southeast1.firebasedatabase.app/");
    private DatabaseReference mDatabase = firebaseDatabase.getReference();
    private MyGroceryListAdapter adapter;

    ArrayList<Ingredient> myGroceryList = new ArrayList<>();

    User theUser = new User();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_grocery_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.myGroceryListRecycler);
        adapter = new MyGroceryListAdapter(myGroceryList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        //Text for if there is no item in Grocery List
        TextView noGroceryListText = view.findViewById(R.id.noGroceryListTxt);
        noGroceryListText.setText("No item(s) currently in your Grocery List");


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        mDatabase.child("Users").child(user.getUid()).child("groceryList").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    for (DataSnapshot data : task.getResult().getChildren()){
                        Ingredient i = data.getValue(Ingredient.class);
                        myGroceryList.add(i);
                        adapter.notifyDataSetChanged();
                    }
                }
                else {
                    Log.d("Debug 3", "Failed");
                }
            }
        });

        mDatabase.child("Users").child(user.getUid()).child("groceryList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    theUser = eventSnapshot.getValue(User.class);
                    for (Ingredient i : theUser.getGroceryList()){
                        myGroceryList.add(i);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return view;
    }
}