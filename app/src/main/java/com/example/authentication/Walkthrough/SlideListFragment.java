package com.example.authentication.Walkthrough;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;

import com.example.authentication.R;

public class SlideListFragment extends Fragment {
    int mNum;
    int stopPosition;
    private VideoView videoView;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.slide_screen, container, false);
        ImageView ind1 = view.findViewById(R.id.ind1);
        ImageView ind2 = view.findViewById(R.id.ind2);
        ImageView ind3 = view.findViewById(R.id.ind3);

        TextView title= view.findViewById(R.id.title);
        TextView desc= view.findViewById(R.id.desc);

        videoView = view.findViewById(R.id.video_intro);
        String path1 = "android.resource://" + getContext().getPackageName() + "/" + R.raw.elearning;
        String path2 = "android.resource://" + getContext().getPackageName() + "/" + R.raw.success;
        String path3 = "android.resource://" + getContext().getPackageName() + "/" + R.raw.professional;
        MediaController mc;
        Uri uri;
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

                title.setText("Elearning Platform");
                desc.setText("Contrary to popular belief. lorem ipsum tesing puspose");

                uri = Uri.parse(path1);
                videoView.setVideoURI(uri);
                break;
            case 1:
                ind1.setImageResource(R.drawable.walkthrough_unselected);
                ind2.setImageResource(R.drawable.walkthrough_selected);
                ind3.setImageResource(R.drawable.walkthrough_unselected);

                title.setText("Be Focus");
                desc.setText("To achieve success in your life. lorem ipsum tesing puspose");

                uri = Uri.parse(path2);
                videoView.setVideoURI(uri);
                break;
            case 2:
                ind1.setImageResource(R.drawable.walkthrough_unselected);
                ind2.setImageResource(R.drawable.walkthrough_unselected);
                ind3.setImageResource(R.drawable.walkthrough_selected);

                uri = Uri.parse(path3);
                videoView.setVideoURI(uri);

                title.setText("Professional");
                desc.setText("Most courses are from top lectures. lorem ipsum tesing puspose");
                break;
        }

        return view;
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
