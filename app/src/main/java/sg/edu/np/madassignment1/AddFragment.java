package sg.edu.np.madassignment1;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddFragment extends Fragment {
    TextView titleTxt;
    Button uploadBtn;
    ImageView recipeImg;
    LinearLayout titleSection;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private Uri filePath;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        uploadBtn = view.findViewById(R.id.uploadBtn);
        recipeImg = view.findViewById(R.id.recipeImg);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        // Long click on Image to change image
        recipeImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d("LONGGG", "CLICKKKKKKKKK");
                openDialog();
                return true;
            }
        });

        // Click on section title to show
        titleSection = view.findViewById(R.id.titleSection);
        TextView titleLabel = view.findViewById(R.id.recipeTitle);
        titleLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recipeImg.getVisibility() == View.VISIBLE){
                    titleSection.animate().translationY(DipToPixels(-300));
                    titleTxt = view.findViewById(R.id.recipeTitleTxt);
                    titleTxt.setVisibility(View.VISIBLE);
                }
            }
        });

        TextView imgLabel = view.findViewById(R.id.imgLabel);
        imgLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowImgSection();
            }
        });

        return view;
    }

    public float DipToPixels(float dp){
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }

    public void ShowImgSection(){
        if(recipeImg.getVisibility() == View.VISIBLE){
            titleSection.animate().translationY(DipToPixels(0));
            titleTxt.setVisibility(View.GONE);
        }
    }

    private void SelectImage()
    {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(intent);
    }
    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
//                      There are no request codes
                        Intent data = result.getData();
//                      Get the Uri of data
                        filePath = data.getData();
                        try {
                            // Setting image on image view using Bitmap
                            Bitmap bitmap = MediaStore
                                    .Images
                                    .Media
                                    .getBitmap(
                                            getActivity().getContentResolver(),
                                            filePath);
                            recipeImg.setImageBitmap(bitmap);
                            recipeImg.setVisibility(View.VISIBLE);
                            uploadBtn.setVisibility(View.GONE);
                        }

                        catch (IOException e) {
                            // Log the exception
                            Log.d("Exception: ", e.getMessage());
                        }
                    }
                }
            });


    private void openDialog() {
        //Finally building an AlertDialog
        final AlertDialog builder = new AlertDialog.Builder(getActivity())
                .setMessage("Upload new image")
                .setPositiveButton("Upload", null)
                .setNegativeButton("Cancel", null)
                .setCancelable(false)
                .create();
        builder.show();
        //Setting up OnClickListener on positive button of AlertDialog
        builder.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
                builder.dismiss();
            }
        });
    }
}
