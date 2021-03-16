package com.example.authentication.Home;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.authentication.Course;
import com.example.authentication.DataHandler;
import com.example.authentication.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    File photoFile;

    private RecyclerView rvCourses;
    private RecyclerView rvCoursesBusiness;
    private CourseAdapter mCourseAdapter;
    private List<Course> mCourses;
    private List<Course> mCoursesBusiness;
    private ImageView imageview;

    private SharedPreferences sharedPreferences ;
    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        String ImageUri = sharedPreferences.getString("imagePreferance", "photo");

        super.onViewCreated(view, savedInstanceState);
        rvCourses = view.findViewById(R.id.rv_course_design);
        rvCoursesBusiness = view.findViewById(R.id.rv_course_business);
//        TextView someTextView =view.findViewById(R.id.before_sale_price);
//        someTextView.setPaintFlags(someTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        mCourses = new ArrayList<>();

        mCourses = new DataHandler(getContext(), null, null,1).loadDataHandler("Design");
        mCourseAdapter = new CourseAdapter(getContext(), mCourses);

        rvCourses.setAdapter(mCourseAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvCourses.setLayoutManager(linearLayoutManager);

        mCoursesBusiness = new ArrayList<>();
        mCoursesBusiness = new DataHandler(getContext(), null, null,1).loadDataHandler("Business");

        mCourseAdapter = new CourseAdapter(getContext(), mCoursesBusiness);

        rvCoursesBusiness.setAdapter(mCourseAdapter);

        LinearLayoutManager linearLayoutManagerBusiness = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvCoursesBusiness.setLayoutManager(linearLayoutManagerBusiness);

        imageview = getView().findViewById(R.id.avatar);
        imageview.setOnClickListener(v -> onLaunchPhoto());

        if (!ImageUri.equals("photo")) {
            Bitmap bitmap = decodeBase64(ImageUri);
            imageview.setImageBitmap(bitmap);
        }
    }

    public void onLaunchPhoto() {
        final CharSequence[] items = {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Pick a way to upload your image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("Camera")){
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_DENIED){
                        ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, 1);
                    } else {
                        Intent CameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        if(CameraIntent.resolveActivity(getActivity().getPackageManager()) != null){
                            startActivityForResult(CameraIntent, 1);
                        }
                    }

                }else if (items[which].equals("Gallery")){
                    Log.i("GalleryCode",""+ 0);
                    Intent GalleryIntent = null;
                    GalleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    GalleryIntent.setType("image/*");
                    GalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(GalleryIntent,0);
                }

            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        imageview = getView().findViewById(R.id.avatar);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
//                    getContext().grantUriPermission(getContext().getPackageName(), selectedImage, Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
//                    getContext().getContentResolver().takePersistableUriPermission(selectedImage, takeFlags);
//
//                    SharedPreferences preferences =
//                            PreferenceManager.getDefaultSharedPreferences(getContext());
//                    SharedPreferences.Editor editor = preferences.edit();
//                    editor.putString("image", String.valueOf(selectedImage));
//                    editor.apply();

                    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("imagePreferance", encodeTobase64(loadFromUri(selectedImage)));
                    editor.commit();
                    imageview.setImageBitmap(loadFromUri(selectedImage));

                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Bundle bundle = imageReturnedIntent.getExtras();
                    Bitmap bmp = (Bitmap) bundle.get("data");
                    Bitmap resized = Bitmap.createScaledBitmap(bmp, 40,40, true);

                    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("imagePreferance", encodeTobase64(resized));
                    editor.commit();

                    imageview.setImageBitmap(resized);
                }
                break;
        }
    }

    public Bitmap loadFromUri(Uri photoUri) {
        Bitmap image = null;
        try {
            // check version of Android on device
            if(Build.VERSION.SDK_INT > 27){
                // on newer versions of Android, use the new decodeBitmap method
                ImageDecoder.Source source = ImageDecoder.createSource(getContext().getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
            } else {
                // support older versions of Android by using getBitmap
                image = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), photoUri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    // method for base64 to bitmap
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
