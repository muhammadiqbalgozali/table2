package com.example.ilgozali.table_pizzana.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.example.ilgozali.table_pizzana.R;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<SliderUtlis> sliderImage;
    private ImageLoader imageLoader;

//    private Integer [] images = {R.drawable.slide1,R.drawable.slide2,R.drawable.slide3};

    public ViewPagerAdapter(List<SliderUtlis> sliderImage,Context context) {
        this.sliderImage = sliderImage;
        this.context = context;
    }

    @Override
    public int getCount() {
        return sliderImage.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slideimage_layout, null);
        SliderUtlis utlis = sliderImage.get(position);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        imageLoader = CustomVollyRequest.getInstance(context).getImageLoader();
        imageLoader.get(utlis.getSliderImageUrl(), ImageLoader.getImageListener(imageView,R.mipmap.ic_launcher,android.R.drawable.ic_dialog_alert));


        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}
