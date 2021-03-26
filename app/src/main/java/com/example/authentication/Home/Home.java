package com.example.authentication.Home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.authentication.Handler.CourseHandler;
import com.example.authentication.MyCourses.OnItemClickedListener;
import com.example.authentication.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment implements OnItemClickedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private Uri selectedImage;
    private TextView tvUsername;
    private RecyclerView rvCourses;
    private RecyclerView rvCoursesBusiness;
    private CourseAdapter mCourseAdapter;
    private List<Course> mCourses;
    private List<Course> mCoursesBusiness;
    private ImageView imageview;
    private String removeEmailDomain = "";
    private String username = "";
    private SharedPreferences sharedPreferences ;
    static final int COLOR_INACTIVE = Color.WHITE;
    static final int COLOR_ACTIVE = Color.rgb(236,91,76);
    private TextView slidePagerText;
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        String ImageUri = sharedPreferences.getString("imagePreference", "photo");
        removeEmailDomain = sharedPreferences.getString("username", "No name");

        super.onViewCreated(view, savedInstanceState);
        rvCourses = view.findViewById(R.id.rv_course_design);
        rvCoursesBusiness = view.findViewById(R.id.rv_course_business);

        Date date = new Date();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        
        CharSequence time_of_day = "";
        if (6 <= hour && hour <= 12) {
            time_of_day = "Good Morning";
        } else if ( 12 < hour && hour <= 18) {
            time_of_day = "Good Afternoon";
        } else if (18 < hour && hour <= 24) {
            time_of_day = "Good Evening";
        }
        
        tvUsername = view.findViewById(R.id.username);

        if (!mParam1.split("@")[0].equals("")) {
            removeEmailDomain = mParam1.split("@")[0];
            String capitalizeName = removeEmailDomain.substring(0,1).toUpperCase() + removeEmailDomain.substring(1);
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", capitalizeName);
            editor.apply();
        }
        tvUsername.setText(time_of_day + ", " + removeEmailDomain.substring(0,1).toUpperCase() + removeEmailDomain.substring(1));



        mCourses = new ArrayList<>();

        mCourses = new CourseHandler(getContext(), null, null,1).loadDataHandler("Design");
        mCourseAdapter = new CourseAdapter(getContext(), mCourses);

        mCourseAdapter.setClickedListener(this);

        rvCourses.setAdapter(mCourseAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvCourses.setLayoutManager(linearLayoutManager);

        mCoursesBusiness = new ArrayList<>();
        mCoursesBusiness = new CourseHandler(getContext(), null, null,1).loadDataHandler("Business");

        mCourseAdapter = new CourseAdapter(getContext(), mCoursesBusiness);
        mCourseAdapter.setClickedListener(this);
        rvCoursesBusiness.setAdapter(mCourseAdapter);

        LinearLayoutManager linearLayoutManagerBusiness = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvCoursesBusiness.setLayoutManager(linearLayoutManagerBusiness);

        imageview = getView().findViewById(R.id.avatar);
        imageview.setOnClickListener(v -> onLaunchPhoto());

        if (!ImageUri.equals("photo")) {
            Glide.with(getView()).load(Uri.parse(ImageUri)).into(imageview);
        }

        int[] urls = {
                (R.drawable.blue_background),
                (R.drawable.orange_background),
                (R.drawable.yellow_background),
                (R.drawable.blue_background)
        };

        slidePagerText = view.findViewById(R.id.home_slide_pager_text);
        ViewPager imageSlider = view.findViewById(R.id.imageSlider);
        ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(urls);
        imageSlider.setAdapter(imageSliderAdapter);

        // Indicator:
        LinearLayout indicator = view.findViewById(R.id.indicator);
        for (int i = 0; i < urls.length; i++) {
            // COLOR_ACTIVE ứng với chấm ứng với vị trí hiện tại của ViewPager,
            // COLOR_INACTIVE ứng với các chấm còn lại
            // ViewPager có vị trí mặc định là 0, vì vậy color ở vị trí i == 0 sẽ là COLOR_ACTIVE
            View dot = createDot(indicator.getContext(), i == 0 ? COLOR_ACTIVE : COLOR_INACTIVE);
            indicator.addView(dot);
        }

        // Thay đổi màu các chấm khi ViewPager thay đổi vị trí:
        imageSlider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                for (int i = 0; i < urls.length; i++) {
                    // Duyệt qua từng "chấm" trong indicator
                    // Nếu i == position, tức i đang là vị trí hiện tại của ViewPager,
                    // ta sẽ đổi màu "chấm" thành COLOR_ACTIVE, nếu không
                    // thì sẽ đổi thành màu COLOR_INACTIVE
                    indicator.getChildAt(i).getBackground().mutate().setTint(i == position ? COLOR_ACTIVE : COLOR_INACTIVE);
                }
            }

            @Override
            public void onPageSelected(int position) {
                String title = "";
                switch (position) {
                    case 0:
                        title = "Your course to success";
                        break;
                    case 1:
                        title = "Grow your ability";
                        break;
                    case 2:
                        title = "Save your time";
                        break;
                    case 3:
                        title = "Get your dream job";
                        break;
                }
                slidePagerText.setText(title);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    View createDot(Context context, @ColorInt int color) {
        View dot = new View(context);
        LinearLayout.MarginLayoutParams dotParams = new LinearLayout.MarginLayoutParams(20, 20);
        dotParams.setMargins(20, 10, 20, 10);
        dot.setLayoutParams(dotParams);
        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.setTint(color);
        dot.setBackground(drawable);
        return dot;
    }

    public void onLaunchPhoto() {
        final CharSequence[] items = {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Pick a way to upload your image");
        builder.setItems(items, (dialog, which) -> {
            if (items[which].equals("Camera")){
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED){
                    ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, 1);
                } else {
                    Intent CameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if(CameraIntent.resolveActivity(getActivity().getPackageManager()) != null){
                        startActivityForResult(CameraIntent, 1);
                    }
                }

            }else if (items[which].equals("Gallery")){
                Log.i("GalleryCode",""+ 0);
                Intent GalleryIntent;
                GalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                GalleryIntent.setType("image/*");
                GalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(GalleryIntent,0);
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
                    selectedImage = imageReturnedIntent.getData();
                    Glide.with(getView()).load(selectedImage).into(imageview);
//                    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("imagePreference", selectedImage.toString());
                    editor.apply();
//                    imageview.setImageBitmap(loadFromUri(selectedImage));

                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Bundle bundle = imageReturnedIntent.getExtras();
                    Bitmap bmp = (Bitmap) bundle.get("data");
                    Bitmap resized = Bitmap.createScaledBitmap(bmp, 40,40, true);

                    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Glide.with(getView()).load(getImageUri(getContext(), resized)).into(imageview);
                    editor.putString("imagePreference", getImageUri(getContext(), resized).toString());
                    editor.apply();

                    imageview.setImageBitmap(resized);
                }
                break;
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        String path = "";
        if (checkPermissionWRITE_EXTERNAL_STORAGE(getContext())) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        }
        return Uri.parse(path);
    }

    public boolean checkPermissionWRITE_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                (dialog, which) -> ActivityCompat.requestPermissions((Activity) context,
                        new String[] { permission },
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE));
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }


    @Override
    public void onAddClicked(Course course) {
        Toast.makeText(getContext(), "Added "+course.getCourseName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onViewClicked(Course course) {
        Toast.makeText(getContext(), course.getCourseName(), Toast.LENGTH_SHORT).show();
    }
}
