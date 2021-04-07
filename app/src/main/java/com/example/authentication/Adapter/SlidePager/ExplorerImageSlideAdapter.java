package com.example.authentication.Adapter.SlidePager;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.authentication.Adapter.SlidePager.Abstract.AbstractSlidePagerAdapter;
import com.example.authentication.Language.LocaleHelper;
import com.example.authentication.R;

public class ExplorerImageSlideAdapter extends AbstractSlidePagerAdapter<Integer> {

    private Integer[] background ;
    private String[] title;
    private String[] category;
    private Context context;

    public ExplorerImageSlideAdapter(Context context, Integer[] background) {
        super(background);
        this.background = background;
        this.context = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.slide_pager_explorer;
    }

    @Override
    public void bind(View itemView, @NonNull ViewGroup container, int position, String language) {
        LinearLayout layout = itemView.findViewById(R.id.background_slide_pager);
        TextView titleText = (TextView) itemView.findViewById(R.id.explorer_slide_pager_text);
        TextView categoryText = (TextView) itemView.findViewById(R.id.category_slide_pager_text);
        TextView viewText = (TextView) itemView.findViewById(R.id.view_slide_pager_text);
        Context context = LocaleHelper.setLocale(this.context, language);
        Resources resources = context.getResources();

        String[] title = {
                resources.getString(R.string.title_explore1),
                resources.getString(R.string.title_explore2),
                resources.getString(R.string.title_explore3),
                resources.getString(R.string.title_explore4),
                resources.getString(R.string.title_explore5)
        };

        String[] category = {
                resources.getString(R.string.category_explore1),
                resources.getString(R.string.category_explore2),
                resources.getString(R.string.category_explore3),
                resources.getString(R.string.category_explore4),
                resources.getString(R.string.category_explore5)
        };

        layout.setBackgroundResource(background[position]);

        titleText.setText(title[position]);
        categoryText.setText(category[position]);

        viewText.setText(resources.getString(R.string.view_title_explore));
        viewText.setBackgroundResource(R.drawable.text_field);
    }
}
