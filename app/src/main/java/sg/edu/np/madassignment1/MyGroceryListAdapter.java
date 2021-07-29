package sg.edu.np.madassignment1;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class MyGroceryListAdapter extends RecyclerView.Adapter<MyGroceryListViewHolder> {
    ArrayList<Ingredient> myGroceryList = new ArrayList<>();

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
        Log.d("Debug MGLA", String.valueOf(myGroceryList.size()));
        if(myGroceryList.get(position).getMeasurement().equals("none")){
            String text = myGroceryList.get(position).getQuantity() + " "
                    + myGroceryList.get(position).getName() + "\n";
            Log.d("Debug MGLA 1", text);
            Log.d("Debug MGLA 1", "123");
            holder.myGroceryItem.setText(text);
        }
        else{
            String text = myGroceryList.get(position).getQuantity() + " " + myGroceryList.get(position).getMeasurement()+ " of "
                    + myGroceryList.get(position).getName() + "\n";
            Log.d("Debug MGLA 2", text);
            Log.d("Debug MGLA 2", "456");
            holder.myGroceryItem.setText(text);
        }

        holder.acquiredCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Add code to remove from database
                FirebaseUser user = mAuth.getCurrentUser();
                //mDatabase.child("Users").child(user.getUid()).child("groceryList").child()
                Log.d("Debug MGLA Delete", GetGroceryListDBNum(myGroceryList.get(position), String.valueOf(position)));
                mDatabase.child("Users").child(user.getUid()).child("groceyList").child(GetGroceryListDBNum(myGroceryList.get(position), String.valueOf(position))).removeValue();
                myGroceryList.remove(position);
                notifyDataSetChanged();
                notifyItemRemoved(position);
                //holder.acquiredCheck.setChecked(false);
            }
        });
    }

    public MyGroceryListAdapter(ArrayList<Ingredient> iList){
        this.myGroceryList = iList;
    }

    public String GetGroceryListDBNum(Ingredient i, String pos){
        String num = "null";
        FirebaseUser user = mAuth.getCurrentUser();
        num = mDatabase.child("Users").child(user.getUid()).child("groceryList").child(pos).getKey();
        return num;
    }

    @Override
    public int getItemCount() {
        return myGroceryList.size();
    }
}
