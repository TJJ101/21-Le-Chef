package sg.edu.np.madassignment1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class MyGroceryListAdapter extends RecyclerView.Adapter<MyGroceryListViewHolder> {
    ArrayList<Ingredient> myGroceryList;

    public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://mad-asg-6df37-default-rtdb.asia-southeast1.firebasedatabase.app/");
    private DatabaseReference mDatabase = firebaseDatabase.getReference();


    @NonNull
    @Override
    public MyGroceryListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.mygrocerylist_viewholder,
                parent,
                false);

        return new MyGroceryListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyGroceryListViewHolder holder, int position) {
        if(myGroceryList.get(position).getUnit().equals("none")){
            String text = myGroceryList.get(position).getQuantity() + " "
                    + myGroceryList.get(position).getName() + "\n";
            Log.d("Debug MGLA 1", text);
            Log.d("Debug MGLA 1", "123");
            holder.myGroceryItem.setText(text);
        }
        else{
            String text = myGroceryList.get(position).getQuantity() + " " + myGroceryList.get(position).getUnit()+ " of "
                    + myGroceryList.get(position).getName() + "\n";
            Log.d("Debug MGLA 2", text);
            Log.d("Debug MGLA 2", "456");
            holder.myGroceryItem.setText(text);
        }

        holder.acquiredCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myGroceryList.remove(position);
                //Add code to remove from database
                notifyDataSetChanged();
                holder.acquiredCheck.setChecked(false);
            }
        });
    }

    public MyGroceryListAdapter(ArrayList<Ingredient> iList){
        this.myGroceryList = iList;
    }

    @Override
    public int getItemCount() {
        return myGroceryList.size();
    }
}
