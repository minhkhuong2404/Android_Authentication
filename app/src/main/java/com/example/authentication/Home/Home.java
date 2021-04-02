package com.example.authentication.Home;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.authentication.CourseView;
import com.example.authentication.FixedSpeedScroller;
import com.example.authentication.Handler.CourseHandler;
import com.example.authentication.MyCourses.OnItemClickedListener;
import com.example.authentication.R;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;

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
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;
    private String mParam4;

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
    private TextView topCourse1;
    private TextView topCourse2;
    private SharedPreferences sharedPreferences ;
    private LottieAnimationView lottieAnimationView, lottieAnimationView2;

    static final int COLOR_INACTIVE = Color.WHITE;
    static final int COLOR_ACTIVE = Color.rgb(236,91,76);


    private ViewPager imageSlider;
    private int currentPage = 0;
    private Timer timer;
    private final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    private final long PERIOD_MS = 5000; // time in milliseconds between successive task executions.
    private String firstLogIn;
    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();

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
    public static Home newInstance(String param1, String param2, String param3, String param4) {
        Home fragment = new Home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
            mParam4 = getArguments().getString(ARG_PARAM4);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @SuppressLint({"SetTextI18n", "ResourceType"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        firstLogIn = mParam2;

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

        // get the username from the email
        if (!mParam1.split("@")[0].equals("")) {
            removeEmailDomain = mParam1.split("@")[0];
            String capitalizeName = removeEmailDomain.substring(0,1).toUpperCase() + removeEmailDomain.substring(1);
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            editor.putString("username", capitalizeName);
            editor.apply();
        }
        tvUsername.setText(time_of_day + ", " + removeEmailDomain.substring(0,1).toUpperCase() + removeEmailDomain.substring(1));


        topCourse1 = view.findViewById(R.id.top_course_1);
        mCourses = new ArrayList<>();

        topCourse1.setText("Top course in " + mParam3);

        mCourses = new CourseHandler(getContext(), null, null, 1).loadCourseHandler(mParam3);
        mCourseAdapter = new CourseAdapter(getContext());

        mCourseAdapter.setClickedListener(this);
        mCourseAdapter.notifyDataSetChanged();

        rvCourses.setAdapter(mCourseAdapter);
        rvCourses.invalidate();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvCourses.setLayoutManager(linearLayoutManager);

        /*
        *
        *
        *
        * */

        topCourse2 = view.findViewById(R.id.top_course_2);
        mCoursesBusiness = new ArrayList<>();

        topCourse2.setText("Top course in " + mParam4);
        mCoursesBusiness = new CourseHandler(getContext(), null, null, 1).loadCourseHandler(mParam4);

        mCourseAdapter = new CourseAdapter(getContext());
        mCourseAdapter.setClickedListener(this);
        mCourseAdapter.notifyDataSetChanged();

        rvCoursesBusiness.setAdapter(mCourseAdapter);
        rvCoursesBusiness.invalidate();

        LinearLayoutManager linearLayoutManagerBusiness = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvCoursesBusiness.setLayoutManager(linearLayoutManagerBusiness);

        // load avatar images
        imageview = getView().findViewById(R.id.avatar);
        imageview.setOnClickListener(v -> onLaunchPhoto());

        if (!ImageUri.equals("photo")) {
            Glide.with(getView()).load(Uri.parse(ImageUri)).into(imageview);
        }

        int[] urls = {
                (R.drawable.purple_background),
                (R.drawable.orange_background),
                (R.drawable.yellow_background),
                (R.drawable.blue_background),
                (R.drawable.green_background),
                (R.drawable.violet_background)
        };

        String[] title = {
                "Your course to success",
                "Grow your ability",
                "Save your time",
                "Get your dream job",
                "Sale up to 70%",
                "Learn with us now"
        };

        Integer[] colors_temp = {
                getResources().getColor(R.color.purple),
                getResources().getColor(R.color.orange),
                getResources().getColor(R.color.yellow),
                getResources().getColor(R.color.blue),
                getResources().getColor(R.color.green),
                getResources().getColor(R.color.violet)
        };

        imageSlider = view.findViewById(R.id.imageSlider);
        ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(getContext(), urls, title);
        imageSlider.setAdapter(imageSliderAdapter);
        imageSlider.setPageMargin(dpToPx(8));
        imageSlider.setBackgroundResource(R.drawable.background_slide_pager);

        Interpolator sInterpolator = new AccelerateInterpolator();

        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(imageSlider.getContext(), sInterpolator);
            if (currentPage == urls.length) {
                scroller.setmDuration(50);
            } else {
                scroller.setmDuration(1000);
            }
            mScroller.set(imageSlider, scroller);
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {

        }

        /*After setting the adapter use the timer */
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == urls.length) {
                    currentPage = 0;
                }
                imageSlider.setCurrentItem(currentPage, true);
                currentPage += 1;
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

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

                GradientDrawable draw = (GradientDrawable) imageSlider.getBackground();
                if (position < (imageSliderAdapter.getCount() -1) && position < (colors_temp.length - 1)) {

                    draw.setColor(
                            (Integer) argbEvaluator.evaluate(
                                    positionOffset,
                                    colors_temp[position],
                                    colors_temp[position + 1]
                    ));
                }

                else {
                    draw.setColor(
                            colors_temp[colors_temp.length - 1]);
                }
            }

            @Override
            public void onPageSelected(int position) {
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

    public void loading() {
        lottieAnimationView = getView().findViewById(R.id.loading1);
        lottieAnimationView.setVisibility(GONE);
        lottieAnimationView2 = getView().findViewById(R.id.loading2);
        lottieAnimationView2.setVisibility(GONE);
    }
    public void updateList(){
        mCourses = new CourseHandler(getContext(), null, null, 1).loadCourseHandler(mParam3);
        mCourseAdapter.setData(mCourses);
        rvCourses.setAdapter(mCourseAdapter);
        rvCourses.invalidate();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvCourses.setLayoutManager(linearLayoutManager);

        // *********
        mCoursesBusiness = new CourseHandler(getContext(), null, null, 1).loadCourseHandler(mParam4);
        mCourseAdapter.setData(mCoursesBusiness);
        rvCoursesBusiness.setAdapter(mCourseAdapter);
        rvCoursesBusiness.invalidate();

        LinearLayoutManager linearLayoutManagerBusiness = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvCoursesBusiness.setLayoutManager(linearLayoutManagerBusiness);
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
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

//                    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Glide.with(getView()).load(getImageUri(getContext(), resized)).into(imageview);
                    editor.putString("imagePreference", getImageUri(getContext(), resized).toString());
                    editor.apply();

//                    imageview.setImageBitmap(resized);
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

    private void switchToCourseView(Course course) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.zoom_in, R.anim.zoom_in, R.anim.zoom_out, R.anim.zoom_out);
        fragmentTransaction.add(R.id.myContainer, CourseView.newInstance(course.getCourseImage(), course.getCategory(), course.getBeforeSalePrice(), course.getAfterSalePrice(), course.getCourseName(), course.getRate()), "view");
        fragmentTransaction.addToBackStack("view");
        fragmentTransaction.commit();
    }

    @Override
    public void onViewClicked(Course course) {
        switchToCourseView(course);
    }

    @Override
    public void onAddClicked(TextView textView) {
        Animation a = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in);
        a.reset();
        textView.clearAnimation();
        textView.startAnimation(a);
    }

    @Override
    public void onCourseClicked(ImageView imageView) {
        Animation a = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in);
        a.reset();
        imageView.clearAnimation();
        imageView.startAnimation(a);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (firstLogIn.equals("0")) {
            currentPage = 0;
            firstLogIn = "1";
        } else {
            currentPage = sharedPreferences.getInt("PageHome", 0);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("PageHome", currentPage - 1);
        editor.apply();

    }

    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("PageHome", currentPage);
        editor.apply();

    }
}
