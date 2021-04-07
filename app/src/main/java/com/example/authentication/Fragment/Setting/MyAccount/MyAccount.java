package com.example.authentication.Fragment.Setting.MyAccount;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.authentication.Language.LocaleHelper;
import com.example.authentication.Object.Badges.BadgeView;
import com.example.authentication.Object.Badges.Badge;
import com.example.authentication.Adapter.RecyclerView.BadgeRecyclerViewAdapter;
import com.example.authentication.Object.Badges.OnBadgeClickListener;
import com.example.authentication.Object.Course.CourseView;
import com.example.authentication.Object.CollectionItem.OnCollectionClickedListener;
import com.example.authentication.Object.Course.Course;
import com.example.authentication.Adapter.RecyclerView.CourseExplorerRecyclerViewAdapter;
import com.example.authentication.Model.Handler.CourseHandler;
import com.example.authentication.Object.CollectionItem.CollectionItem;
import com.example.authentication.Adapter.RecyclerView.MyCollectionRecyclerViewAdapter;
import com.example.authentication.Object.Course.OnItemClickedListener;
import com.example.authentication.R;

import java.util.ArrayList;
import java.util.List;

public class MyAccount extends Fragment implements OnCollectionClickedListener, OnItemClickedListener, OnBadgeClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    // TODO: Rename and change types of parameters
    private String mParam1, mParam2;

    private RecyclerView rvBadges, rvCollection;
    private List<CollectionItem> item;
    private List<Badge> badges;
    private List<Course> courses = new ArrayList<>();
    private MyCollectionRecyclerViewAdapter myCourseCollectionAdapter;
    private CourseExplorerRecyclerViewAdapter myCoursesAdapter;
    private BadgeRecyclerViewAdapter myBadgeAdapter;
    private SharedPreferences sharedPreferences ;
    private String removeEmailDomain = "", ImageUri;
    private TextView tvUsername;
    private ImageView imageview;

    public MyAccount() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Search.
     */
    // TODO: Rename and change types and number of parameters
    public static MyAccount newInstance(String param1, String param2) {
        MyAccount fragment = new MyAccount();
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
        return inflater.inflate(R.layout.profile_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        ImageUri = sharedPreferences.getString("imagePreference", "photo");
        removeEmailDomain = sharedPreferences.getString("username", "No name");

        rvBadges = view.findViewById(R.id.rv_my_badges_settings);
        rvCollection  = view.findViewById(R.id.rv_collection_settings);

        tvUsername = view.findViewById(R.id.username_settings);
        tvUsername.setText(removeEmailDomain.substring(0,1).toUpperCase() + removeEmailDomain.substring(1));

        imageview = requireView().findViewById(R.id.avatar_settings);
        if (checkPermissionWRITE_EXTERNAL_STORAGE(getContext())) {
            Glide.with(getView()).load(Uri.parse(ImageUri)).into(imageview);
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String language = sharedPreferences.getString("Locale.Helper.Selected.Language", "en");
        Context context = LocaleHelper.setLocale(getContext(), language);
        Resources resources = context.getResources();

        item = new ArrayList<>();
        item.add(new CollectionItem(resources.getString(R.string.collectionItemBadge)));
        item.add(new CollectionItem(resources.getString(R.string.collectionItemStudy)));
        item.add(new CollectionItem(resources.getString(R.string.collectionItemCollection)));

        myCourseCollectionAdapter = new MyCollectionRecyclerViewAdapter(getContext(), item);
        myCourseCollectionAdapter.setData(item);
        // listen to which item view is clicked on the badge or collection
        myCourseCollectionAdapter.setOnCollectionClickedListener(this);

        rvCollection.setAdapter(myCourseCollectionAdapter);
        rvCollection.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        badges = new ArrayList<>();
        badges.add(new Badge("First Step", "Pass your first quiz", R.drawable.first_step));
        badges.add(new Badge("Certificate Unlocked", "Confirm your name on certificate", R.drawable.certificate));
        badges.add(new Badge("Bookworm", "Complete all of the practice course lessons", R.drawable.bookworm));
        badges.add(new Badge("Training Wheels", "Complete the portal tutorial", R.drawable.training));
        badges.add(new Badge("Final Exam", "Pass the practice course final exam", R.drawable.final_exam));

        myBadgeAdapter = new BadgeRecyclerViewAdapter(getContext(), badges);
        myBadgeAdapter.setData(badges);
        myBadgeAdapter.setClickedListener(this);

        rvBadges.setAdapter(myBadgeAdapter);
        rvBadges.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        TextView searchButton = view.findViewById(R.id.back_btn_profile);
        searchButton.setOnClickListener(v -> backToSettings());

    }

    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //reload my activity with permission granted or use the features that required the permission
                Glide.with(getView()).load(Uri.parse(ImageUri)).into(imageview);
            } else {
                Toast.makeText(getContext(), "No permission", Toast.LENGTH_SHORT).show();
            }
        }
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
    public void getSelected(CollectionItem collectionItem, int position) {
        courses = new ArrayList<>();

        if (position == 1) {
            setUpRvCourse(mParam1);

        } else if (position == 2) {
            setUpRvCourse(mParam2);
        }
        else {
            myBadgeAdapter = new BadgeRecyclerViewAdapter(getContext(), badges);
            rvBadges.setAdapter(myBadgeAdapter);
            myBadgeAdapter.setData(badges);
            myBadgeAdapter.setClickedListener(this);
        }
        rvBadges.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

    }

    private void setUpRvCourse (String category) {
        courses = new CourseHandler(getContext(), null, null, 1).loadCourseHandler(category);
        myCoursesAdapter = new CourseExplorerRecyclerViewAdapter(getContext(), courses);
        myCoursesAdapter.setData(courses);
        myCoursesAdapter.setClickedListener(this);

        rvBadges.setAdapter(myCoursesAdapter);
        rvBadges.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onAddClicked(TextView textView) {
        Animation a = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in);
        a.reset();
        textView.clearAnimation();
        textView.startAnimation(a);
    }

    private void switchToCourseView(Course course) {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.zoom_in, R.anim.zoom_in, R.anim.zoom_out, R.anim.zoom_out);
        fragmentTransaction.add(R.id.myContainer, CourseView.newInstance(course.getCourseImage(), course.getCategory(), course.getBeforeSalePrice(), course.getAfterSalePrice(), course.getCourseName(), course.getRate()), "view");
        fragmentTransaction.addToBackStack("view").commit();
    }

    private void switchToBadgeView(Badge badge) {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.zoom_in, R.anim.zoom_in, R.anim.zoom_out, R.anim.zoom_out);
        fragmentTransaction.add(R.id.myContainer, BadgeView.newInstance(badge.getBadgeIcon(), badge.getBadgeName()), "view");
        fragmentTransaction.addToBackStack("badge").commit();
    }

    private void backToSettings() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onViewClicked(Course course) {
        switchToCourseView(course);
    }

    @Override
    public void onCourseClicked(ImageView imageView) {
        Animation a = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in);
        a.reset();
        imageView.clearAnimation();
        imageView.startAnimation(a);
    }

    @Override
    public void onViewClicked(Badge badge) {
        switchToBadgeView(badge);
    }

    @Override
    public void onBadgeClicked(ImageView imageView) {
        Animation a = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in);
        a.reset();
        imageView.clearAnimation();
        imageView.startAnimation(a);
    }
}
