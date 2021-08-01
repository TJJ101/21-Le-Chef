package sg.edu.np.madassignment1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
        if(myGroceryList.get(position).getMeasurement().equals("n/a")){
            String text = myGroceryList.get(position).getQuantity() + " "
                    + myGroceryList.get(position).getName() + "\n";
            holder.myGroceryItem.setText(text);
        }
        else{
            String text = myGroceryList.get(position).getQuantity() + " " + myGroceryList.get(position).getMeasurement()+ " of "
                    + myGroceryList.get(position).getName() + "\n";
            holder.myGroceryItem.setText(text);
        }

        holder.acquiredCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Delete(position);
            }
        });
    }

    public MyGroceryListAdapter(ArrayList<Ingredient> iList){ this.myGroceryList = iList; }

    public void Delete(int position){
        myGroceryList.remove(position);
        DatabaseReference mDatabase2 = firebaseDatabase.getReference().child("Users").child(MainActivity.mUser.getId()).child("groceryList");
        mDatabase2.setValue(myGroceryList);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, myGroceryList.size());
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
