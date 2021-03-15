package com.example.authentication.Home;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.authentication.Course;
import com.example.authentication.R;

import java.io.File;
import java.io.IOException;
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
        super.onViewCreated(view, savedInstanceState);
        rvCourses = view.findViewById(R.id.rv_course_design);
        rvCoursesBusiness = view.findViewById(R.id.rv_course_business);
//        TextView someTextView =view.findViewById(R.id.before_sale_price);
//        someTextView.setPaintFlags(someTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        mCourses = new ArrayList<>();
        mCourses.add(new Course("Sketch App Masterclass","$ 340", "$ 199", "3.0", getContext().getResources().getDrawable(R.drawable.course_image)));
        mCourses.add(new Course("Figma App Materclass","$ 350", "$ 199", "5.0", getContext().getResources().getDrawable(R.drawable.course_image)));
        mCourses.add(new Course("Business Master Class","$ 440", "$ 299", "4.8", getContext().getResources().getDrawable(R.drawable.course_image)));
        mCourses.add(new Course("Adobe XD Masterclass","$ 140", "$ 99", "4.8", getContext().getResources().getDrawable(R.drawable.course_image)));
        mCourses.add(new Course("Photoshop CC Masterclass","$ 540", "$ 399", "4.5", getContext().getResources().getDrawable(R.drawable.course_image)));
        mCourses.add(new Course("Illustrator CC Masterclass","$ 640", "$ 499", "4.8", getContext().getResources().getDrawable(R.drawable.course_image)));
        mCourses.add(new Course("Premiere Pro CC Masterclass","$ 849", "$ 599", "4.7", getContext().getResources().getDrawable(R.drawable.course_image)));

        mCourseAdapter = new CourseAdapter(getContext(), mCourses);

        rvCourses.setAdapter(mCourseAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvCourses.setLayoutManager(linearLayoutManager);

        mCoursesBusiness = new ArrayList<>();
        mCoursesBusiness.add(new Course("Business Masterclass", "$ 450", "$ 299", "5.0", getContext().getResources().getDrawable(R.drawable.course_image)));
        mCoursesBusiness.add(new Course("Business Introduction", "$ 350", "$ 199", "4.6", getContext().getResources().getDrawable(R.drawable.course_image)));
        mCoursesBusiness.add(new Course("Business Management", "$ 250", "$ 99", "3.8", getContext().getResources().getDrawable(R.drawable.course_image)));

        mCourseAdapter = new CourseAdapter(getContext(), mCoursesBusiness);

        rvCoursesBusiness.setAdapter(mCourseAdapter);

        LinearLayoutManager linearLayoutManagerBusiness = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvCoursesBusiness.setLayoutManager(linearLayoutManagerBusiness);

        ImageView imageView = getView().findViewById(R.id.avatar);

        imageView.setOnClickListener(v -> onLaunchPhoto());
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

//        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(pickPhoto , 0);
//
//        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
//                == PackageManager.PERMISSION_DENIED){
//            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, 1);
//        } else {
//            dispatchTakePictureIntent();
//        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, 1);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        ImageView imageview = getView().findViewById(R.id.avatar);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    imageview.setImageURI(selectedImage);
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Bundle bundle = imageReturnedIntent.getExtras();
                    Bitmap bmp = (Bitmap) bundle.get("data");
                    Bitmap resized = Bitmap.createScaledBitmap(bmp, 40,40, true);
                    imageview.setImageBitmap(resized);
                }
                break;
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        File storageDir = Environment.getExternalStorageDirectory();
        File image = File.createTempFile(
                "example",  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
