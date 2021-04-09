package com.example.authentication.Fragment.Home;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.authentication.Adapter.RecyclerView.CourseRecyclerViewAdapter;
import com.example.authentication.Adapter.SlidePager.ImageSliderAdapter;
import com.example.authentication.Fragment.AbstractFragment;
import com.example.authentication.Language.LocaleHelper;
import com.example.authentication.Model.Handler.CourseHandler;
import com.example.authentication.Object.Course.Course;
import com.example.authentication.Object.Course.CourseView;
import com.example.authentication.Object.Course.OnItemClickedListener;
import com.example.authentication.R;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends AbstractFragment implements OnItemClickedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";

    // TODO: Rename and change types of parameters
    private String mParam1, mParam2, mParam3, mParam4;

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private TextView tvUsername, topCourse1, topCourse2, seeAllCourse1, seeAllCourse2, designIcon, codeIcon, businessIcon, photographIcon;
    private RecyclerView rvCourses, rvCoursesBusiness;
    private CourseRecyclerViewAdapter mCourseAdapter;
    private ImageView imageview;
    private String removeEmailDomain = "", ImageUri = "", firstLogIn, topCourseString1, topCourseString2;
    private LottieAnimationView lottieAnimationView, lottieAnimationView2;

    private CharSequence time_of_day = "";
    static final int COLOR_INACTIVE = Color.WHITE;
    static final int COLOR_ACTIVE = Color.rgb(236,91,76);

    private ViewPager imageSlider;
    private int currentPage = 0;
    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    public Home() {
        // Required empty public constructor
    }

    @Override
    protected Home newInstance(String mParam1, String mParam2) {
        return null;
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
    public Home newInstance(String param1, String param2, String param3, String param4) {
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
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void UpdateLanguage(String language) {
        Context context = LocaleHelper.setLocale(getContext(), language);
        Resources resources = context.getResources();

        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(new Date());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        if (6 <= hour && hour <= 12) {
            time_of_day = resources.getString(R.string.good_morning);
        } else if ( 12 < hour && hour <= 18) {
            time_of_day = resources.getString(R.string.good_afternoon);
        } else if (18 < hour) {
            time_of_day = resources.getString(R.string.good_evening);
        }
        ((TextView)findViewById(R.id.username)).setText(String.format("%s, %s%s", time_of_day, removeEmailDomain.substring(0, 1).toUpperCase(), removeEmailDomain.substring(1)));
        ((TextView)findViewById(R.id.top_course_1)).setText(String.format("%s %s", resources.getString(R.string.top_courses_in), mParam3));
        ((TextView)findViewById(R.id.top_course_2)).setText(String.format("%s %s", resources.getString(R.string.top_courses_in), mParam4));
        ((TextView)findViewById(R.id.see_all_design_courses)).setText(resources.getString(R.string.see_all));
        ((TextView)findViewById(R.id.see_all_business_courses)).setText(resources.getString(R.string.see_all));
        ((TextView)findViewById(R.id.design_icon)).setText(resources.getString(R.string.design_icon));
        ((TextView)findViewById(R.id.code_icon)).setText(resources.getString(R.string.code_icon));
        ((TextView)findViewById(R.id.business_icon)).setText(resources.getString(R.string.business_icon));
        ((TextView)findViewById(R.id.photograph_icon)).setText(resources.getString(R.string.photograph_icon));

    }

    @SuppressLint({"SetTextI18n", "ResourceType"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firstLogIn = mParam2;
        topCourseString1 = mParam3;
        topCourseString2 = mParam4;

        ImageUri = getStringPref("imagePreference", "photo");
        removeEmailDomain = getStringPref("username", "No name");

        rvCourses = findViewById(R.id.rv_course_design);
        rvCoursesBusiness = findViewById(R.id.rv_course_business);

        // get the username from the email
        if (!mParam1.split("@")[0].equals("")) {
            removeEmailDomain = mParam1.split("@")[0];
            putStringPref("username", removeEmailDomain.substring(0,1).toUpperCase() + removeEmailDomain.substring(1));
        }

        setRvCourses(rvCourses, topCourseString1);
        setRvCourses(rvCoursesBusiness, topCourseString2);

        // load avatar images
        imageview = findViewById(R.id.avatar);
        imageview.setOnClickListener(v -> onLaunchPhoto());

        if (!ImageUri.equals("photo")) {
            Glide.with(getView()).load(Uri.parse(ImageUri)).into(imageview);
        }

        Integer[] urls = {
                (R.drawable.purple_background),
                (R.drawable.orange_background),
                (R.drawable.yellow_background),
                (R.drawable.blue_background),
                (R.drawable.green_background),
                (R.drawable.violet_background)
        };

        Integer[] colors_temp = {
                getResources().getColor(R.color.purple),
                getResources().getColor(R.color.orange),
                getResources().getColor(R.color.yellow),
                getResources().getColor(R.color.blue),
                getResources().getColor(R.color.green),
                getResources().getColor(R.color.violet)
        };

        imageSlider = findViewById(R.id.imageSlider);
        ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(currentPage, getContext(), urls, imageSlider);
        imageSlider.setAdapter(imageSliderAdapter);
        imageSlider.setPageMargin(dpToPx(8));
        imageSlider.setBackgroundResource(R.drawable.background_slide_pager);
        imageSliderAdapter.init();

        // Indicator:
        LinearLayout indicator = findViewById(R.id.indicator);
        for (int i = 0; i < urls.length; i++) {
            // COLOR_ACTIVE ứng với chấm ứng với vị trí hiện tại của ViewPager,
            // COLOR_INACTIVE ứng với các chấm còn lại
            // ViewPager có vị trí mặc định là 0, vì vậy color ở vị trí i == 0 sẽ là COLOR_ACTIVE
            indicator.addView(imageSliderAdapter.createDot(indicator.getContext(), i == 0 ? COLOR_ACTIVE : COLOR_INACTIVE));
        }

        // Thay đổi màu các chấm khi ViewPager thay đổi vị trí:
        imageSlider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                for (int i = 0; i < urls.length; i++) {
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
                    draw.setColor(colors_temp[colors_temp.length - 1]);
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

    public void loading() {
        try {
            if (lottieAnimationView.getVisibility() == View.VISIBLE) {
                lottieAnimationView = findViewById(R.id.loading1);
                lottieAnimationView.setVisibility(GONE);
            } else if (lottieAnimationView2.getVisibility() == View.VISIBLE) {
                lottieAnimationView2 = findViewById(R.id.loading2);
                lottieAnimationView2.setVisibility(GONE);
            }
        } catch (NullPointerException ignored) {
        }
    }

    public void updateList(){
        setRvCourses(rvCourses, topCourseString1);
        setRvCourses(rvCoursesBusiness, topCourseString2);
    }

    public void setRvCourses(RecyclerView recyclerView, String whereToLoad) {
        mCourseAdapter = new CourseRecyclerViewAdapter(getContext());
        mCourseAdapter.setData(new CourseHandler(getContext(), null, null, 1).loadCourseHandler(whereToLoad));
        mCourseAdapter.setClickedListener(this);

        recyclerView.setAdapter(mCourseAdapter);
        recyclerView.invalidate();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    public int dpToPx(int dp) {
        return Math.round(dp * (getActivity().getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
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
                Intent GalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                GalleryIntent.setType("image/*");
                GalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(GalleryIntent,0);
            }

        }).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        imageview = findViewById(R.id.avatar);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    loadAvatar(selectedImage, imageview, selectedImage.toString());
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Bitmap resized = Bitmap.createScaledBitmap((Bitmap) imageReturnedIntent.getExtras().get("data"), 40,40, true);
                    loadAvatar(getImageUri(getContext(), resized), imageview, getImageUri(getContext(), resized).toString());
                }
                break;
        }
    }

    public void loadAvatar(Uri uri, ImageView image, String sharedPreference) {
        Glide.with(getView()).load(uri).into(image);
        putStringPref("imagePreference", sharedPreference);
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

    public boolean checkPermissionWRITE_EXTERNAL_STORAGE(final Context context) {
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
                    ActivityCompat.requestPermissions(
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

    public void showDialog(final String msg, final Context context, final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                (dialog, which) -> ActivityCompat.requestPermissions((Activity) context,
                        new String[] { permission },
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE));
        alertBuilder.create().show();
    }

    private void switchToCourseView(Course course) {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.zoom_in, R.anim.zoom_in, R.anim.zoom_out, R.anim.zoom_out);
        fragmentTransaction.add(R.id.myContainer, new CourseView().newInstance(course.getCourseImage(), course.getCategory(), course.getBeforeSalePrice(), course.getAfterSalePrice(), course.getCourseName(), course.getRate()), "view");
        fragmentTransaction.addToBackStack("view").commit();
    }

    @Override
    public void onViewClicked(Course course) {
        switchToCourseView(course);
    }

    @Override
    public void onAddClicked(TextView textView) {
        startAnimation(textView);
    }

    @Override
    public void onCourseClicked(ImageView imageView) {
        startAnimation(imageView);
    }
}
