package com.example.authentication.Activity.Walkthrough;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.authentication.Fragment.AbstractFragment;
import com.example.authentication.Language.LocaleHelper;
import com.example.authentication.R;

public class SlideListFragment extends AbstractFragment {
    int mNum;
    int stopPosition;
    private VideoView videoView;
    private TextView title, desc;
    private ImageView ind1, ind2, ind3;
    private String path1, path2, path3;
    static SlideListFragment newInstance(int num) {
        SlideListFragment f = new SlideListFragment();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments() != null ? getArguments().getInt("num") : 0;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ind1 = view.findViewById(R.id.ind1);
        ind2 = view.findViewById(R.id.ind2);
        ind3 = view.findViewById(R.id.ind3);

        title= view.findViewById(R.id.title);
        desc= view.findViewById(R.id.desc);
        videoView = view.findViewById(R.id.video_intro);

        path1 = "android.resource://" + getContext().getPackageName() + "/" + R.raw.elearning;
        path2 = "android.resource://" + getContext().getPackageName() + "/" + R.raw.success;
        path3 = "android.resource://" + getContext().getPackageName() + "/" + R.raw.professional;
        MediaController mc;
        mc = new MediaController(getContext());
        mc.setAnchorView(videoView);
        mc.setMediaPlayer(videoView);
        videoView.setMediaController(mc);
        videoView.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                ((VideoView) v.findViewById(R.id.video_intro)).start();
            else
                ((VideoView) v.findViewById(R.id.video_intro)).pause();
        });

        switch (mNum)
        {
            case 0:
                ind1.setImageResource(R.drawable.walkthrough_selected);
                ind2.setImageResource(R.drawable.walkthrough_unselected);
                ind3.setImageResource(R.drawable.walkthrough_unselected);

                break;
            case 1:
                ind1.setImageResource(R.drawable.walkthrough_unselected);
                ind2.setImageResource(R.drawable.walkthrough_selected);
                ind3.setImageResource(R.drawable.walkthrough_unselected);

                break;
            case 2:
                ind1.setImageResource(R.drawable.walkthrough_unselected);
                ind2.setImageResource(R.drawable.walkthrough_unselected);
                ind3.setImageResource(R.drawable.walkthrough_selected);

                break;
        }
    }

    private void setUpSlide( String path, String titleText, String descText) {
        videoView.setVideoURI(Uri.parse(path));
        title.setText(titleText);
        desc.setText(descText);
    }

    @Override
    public int getLayoutId() {
        return R.layout.slide_screen;
    }

    @Override
    public void UpdateLanguage(String language) {
        Context context = LocaleHelper.setLocale(getContext(), language);
        Resources resources = context.getResources();

        if (mNum == 0) {
            setUpSlide(path1, resources.getString(R.string.title), resources.getString(R.string.desc));
        } else if (mNum == 1) {
            setUpSlide(path2, resources.getString(R.string.title2), resources.getString(R.string.desc2));
        } else {
            setUpSlide(path3, resources.getString(R.string.title3), resources.getString(R.string.desc3));
        }
    }

    @Override
    public void onPause() {
        Log.d("video", "onPause called");
        super.onPause();
        stopPosition = videoView.getCurrentPosition(); //stopPosition is an int
        videoView.pause();
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d("video", "onResume called");
        videoView.seekTo(stopPosition);
        videoView.start(); //Or use resume() if it doesn't work. I'm not sure
    }
}
