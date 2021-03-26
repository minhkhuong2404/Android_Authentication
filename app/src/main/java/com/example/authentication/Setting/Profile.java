package com.example.authentication.Setting;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.authentication.Badges.Badge;
import com.example.authentication.Badges.BadgeAdapter;
import com.example.authentication.Home.Course;
import com.example.authentication.Explorer.CourseAdapterExplorer;
import com.example.authentication.Handler.CourseHandler;
import com.example.authentication.Explorer.Explorer;
import com.example.authentication.MyCourses.CollectionItem;
import com.example.authentication.MyCourses.OnItemClickedListener;
import com.example.authentication.R;

import java.util.ArrayList;
import java.util.List;

public class Profile extends Fragment implements OnCollectionClickedListener, OnItemClickedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Uri selectedImage;
    private RecyclerView rvBadges;
    private RecyclerView rvCollection;
    private List<CollectionItem> item;
    private List<Badge> badges;
    private List<Course> courses;
    private MyProfileCollectionAdapter myCourseCollectionAdapter;
    private CourseAdapterExplorer myCoursesAdapter;
    private BadgeAdapter myBadgeAdapter;
    private SharedPreferences sharedPreferences ;
    private String removeEmailDomain = "";
    private TextView tvUsername;
    private ImageView imageview;
    private Intent intent;
    private String ImageUri;

    public Profile() {
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
    public static Explorer newInstance(String param1, String param2) {
        Explorer fragment = new Explorer();
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

        imageview = getView().findViewById(R.id.avatar_settings);
        if (checkPermissionWRITE_EXTERNAL_STORAGE(getContext())) {
            Glide.with(getView()).load(Uri.parse(ImageUri)).into(imageview);
        }

        item = new ArrayList<>();
        item.add(new CollectionItem("BADGES"));
        item.add(new CollectionItem("STUDYING"));
        item.add(new CollectionItem("COLLECTION"));

        myCourseCollectionAdapter = new MyProfileCollectionAdapter(getContext(), item);
        rvCollection.setAdapter(myCourseCollectionAdapter);
        // listen to which item view is clicked on the badge or collection
        myCourseCollectionAdapter.setOnCollectionClickedListener(this);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvCollection.setLayoutManager(linearLayoutManager2);

        badges = new ArrayList<>();
        badges.add(new Badge("First Step", "Pass your first quiz", R.drawable.first_step));
        badges.add(new Badge("Certificate Unlocked", "Confirm your name on certificate", R.drawable.certificate));
        badges.add(new Badge("Bookworm", "Complete all of the practice course lessons", R.drawable.bookworm));
        badges.add(new Badge("Training Wheels", "Complete the portal tutorial", R.drawable.training));
        badges.add(new Badge("Final Exam", "Pass the practice course final exam", R.drawable.final_exam));

        myBadgeAdapter = new BadgeAdapter(getContext(), badges);
        rvBadges.setAdapter(myBadgeAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvBadges.setLayoutManager(linearLayoutManager);

        TextView searchButton = view.findViewById(R.id.back_btn_profile);
        searchButton.setOnClickListener(v -> backToSettings());

    }

    private void backToSettings() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //reload my activity with permission granted or use the features that required the permission
                    Glide.with(getView()).load(Uri.parse(ImageUri)).into(imageview);
                } else {
                    Toast.makeText(getContext(), "No permission", Toast.LENGTH_SHORT).show();
                }
                break;
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
            courses = new CourseHandler(getContext(), null, null, 1).loadDataHandler("Hacking");

            myCoursesAdapter = new CourseAdapterExplorer(getContext(), courses);
            myCoursesAdapter.setClickedListener(this);
            rvBadges.setAdapter(myCoursesAdapter);

        } else if (position == 2) {
            courses = new CourseHandler(getContext(), null, null, 1).loadDataHandler("Design");

            myCoursesAdapter = new CourseAdapterExplorer(getContext(), courses);
            myCoursesAdapter.setClickedListener(this);
            rvBadges.setAdapter(myCoursesAdapter);
        }
        else {
            myBadgeAdapter = new BadgeAdapter(getContext(), badges);
            rvBadges.setAdapter(myBadgeAdapter);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvBadges.setLayoutManager(linearLayoutManager);


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
