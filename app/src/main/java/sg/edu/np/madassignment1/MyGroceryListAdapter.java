package sg.edu.np.madassignment1;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

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

public class MyGroceryListAdapter extends RecyclerView.Adapter<MyGroceryListAdapter.MyGroceryListViewHolder> {
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
        holder.acquiredCheck.setChecked(false);
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
                FirebaseUser user = mAuth.getCurrentUser();
                Log.d("Debug MGLA Delete", GetGroceryListDBNum(myGroceryList.get(position), String.valueOf(position)));
                Log.d("Debug MGLA Delete", mDatabase.child("Users").child(user.getUid()).child("groceryList").child(GetGroceryListDBNum(myGroceryList.get(position), String.valueOf(position))).getKey());
                RemoveFromDB(myGroceryList.get(position));
                myGroceryList.remove(position);
                notifyItemRemoved(position);
            }
        });
    }

    public MyGroceryListAdapter(ArrayList<Ingredient> iList){
        this.myGroceryList = iList;
    }

    public String GetGroceryListDBNum(Ingredient i, String pos){
        FirebaseUser user = mAuth.getCurrentUser();
        String num = mDatabase.child("Users").child(user.getUid()).child("groceryList").child(pos).getKey();
        return num;
    }

    public void RemoveFromDB(Ingredient i){
        FirebaseUser user = mAuth.getCurrentUser();
        mDatabase.child("Users").child(user.getUid()).child("groceryList").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    Log.d("Debug Remove", "Success");
                    for (DataSnapshot data : task.getResult().getChildren()){
                        Ingredient test = data.getValue(Ingredient.class);
                        if(i.getName().equals(test.getName())){
                            Log.d("Debug Remove",data.getKey());
                            mDatabase.child("Users").child(user.getUid()).child("groceryList").child(data.getKey()).removeValue();
                            notifyDataSetChanged();
                        }
                    }
                }
                else {
                    Log.d("Debug Remove", "Unsuccessful");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return myGroceryList.size();
    }

    public class MyGroceryListViewHolder extends RecyclerView.ViewHolder {
        public TextView myGroceryItem;
        public CheckBox acquiredCheck;

        public MyGroceryListViewHolder(@NonNull View itemView) {
            super(itemView);
            myGroceryItem = itemView.findViewById(R.id.myGroceryListItem);
            acquiredCheck = itemView.findViewById(R.id.acquiredCheck);
        }
    }
}
