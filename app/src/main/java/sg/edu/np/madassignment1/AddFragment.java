package sg.edu.np.madassignment1;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;

import java.io.IOException;
import java.util.ArrayList;

public class AddFragment extends Fragment {
    TextInputLayout nameTxt, descTxt, cuisineTxt;
    TextView addBtn, ingredientNameTxt, ingredientQtyTxt, ingredientUnitTxt;
    Button uploadBtn;
    ImageView recipeImg;
    LinearLayout titleSection;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    Uri filePath;
    String[] cuisine = {"Turkish", "Thai", "Japanese", "Indian", "French", "Chinese", "Western"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayList<Ingredient> ingredientList;
    ListView listView;
    Boolean imgIsHidden, titleIsHidden, ingredientIsHidden, stepsIsHidden;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        //Initialise data
        imgIsHidden = titleIsHidden = ingredientIsHidden = stepsIsHidden = false;

        //Set autoCompleteTextView input type to text
        autoCompleteTextView = view.findViewById(R.id.cuisineDropdown);
        autoCompleteTextView.setInputType(InputType.TYPE_CLASS_TEXT);

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
                Log.d("LONGGG", "DICKKKKKKKKK");
                openDialog();
                return true;
            }
        });

        titleSection = view.findViewById(R.id.titleSection);
        recipeImg = view.findViewById(R.id.recipeImg);
        nameTxt = view.findViewById(R.id.ingredientTextView);
        descTxt = view.findViewById(R.id.textInputDescriptionLayout);
        cuisineTxt = view.findViewById(R.id.textInputDropdownLayout);

        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    titleSection.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    Log.d("ONLINE PIXELS TO DP", "" + convertPixelsToDp(titleSection.getHeight()));
                }
            });
        }

        // Click on section title to show & hide section
        TextView imgLabel = view.findViewById(R.id.imgLabel);
        imgLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideImgSection();
            }
        });

        //setup the cuisine dropdown text
        ArrayAdapter<String> cuisineAdapter = new ArrayAdapter<String>(getActivity(), R.layout.dropdown, cuisine);
        AutoCompleteTextView cuisineTxt = view.findViewById(R.id.cuisineDropdown);
        cuisineTxt.setThreshold(1);//start when u type first char
        cuisineTxt.setAdapter(cuisineAdapter);



        //for adding new steps to steps array and list view
        listView = (ListView) view.findViewById(R.id.stepListView);
        addBtn = (TextView) view.findViewById(R.id.addIngredientBtn);
        ingredientNameTxt = (TextView) view.findViewById(R.id.addIngredientTxt);
        ingredientQtyTxt = (TextView) view.findViewById(R.id.addQtyTxt);
        ingredientUnitTxt = (TextView) view.findViewById(R.id.addUnitTxt);

        ingredientList = new ArrayList<Ingredient>();
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) listView.getLayoutParams();
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String steps = ingredientNameTxt.getText().toString();
                if (!steps.isEmpty() && !(steps.trim().length() == 0)){
                    steps = ingredientNameTxt.getText().toString();
                    ingredientList.add(new Ingredient(
                            ingredientNameTxt.getText().toString(),
                            Double.parseDouble(ingredientQtyTxt.getText().toString()),
                            ingredientUnitTxt.getText().toString()));
                    lp.height += DipToPixels(60);
                    listView.setLayoutParams(lp);
                    listView.setAdapter(new IngredientAdapter(ingredientList, getContext()));
                    Log.d("SIZEESE", "" + ingredientList.size());
                }

                //hide the keyboard
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                ingredientNameTxt.setText("");
            }
        });
        return view;
    }

    public float convertPixelsToDp(float px){
        return px / ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public float DipToPixels(float dp){
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }
//  Hide image section
    public void HideImgSection(){
        if(recipeImg.getVisibility() == View.VISIBLE){
            if(imgIsHidden){
                titleSection.animate().translationY(DipToPixels(0));
            }
            else{
                titleSection.animate().translationY(DipToPixels(-300));
            }
            imgIsHidden = !imgIsHidden;
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
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
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
        //build an AlertDialog
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
