package alvarofpp.study.launcher.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

import alvarofpp.study.launcher.objects.PagerObject;
import alvarofpp.study.launcher.R;

public class ViewPagerAdapter extends PagerAdapter {
    Context context;
    ArrayList<PagerObject> pagerAppList;
    int cellHeight;
    ArrayList<AppAdapter> appAdapterList = new ArrayList<>();
    int numColumn;

    public ViewPagerAdapter(Context context, ArrayList<PagerObject> pagerAppList, int cellHeight, int numColumn) {
        this.context = context;
        this.pagerAppList = pagerAppList;
        this.cellHeight = cellHeight;
        this.numColumn = numColumn;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.pager_layout, container, false);

        final GridView mGridView = layout.findViewById(R.id.grid);
        mGridView.setNumColumns(this.numColumn);

        AppAdapter mGridAdapter = new AppAdapter(this.context, this.pagerAppList.get(position).getAppList(), this.cellHeight);
        mGridView.setAdapter(mGridAdapter);

        this.appAdapterList.add(mGridAdapter);

        container.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return this.pagerAppList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public void notifyGridChanged()
    {
        for (int i = 0; i < this.appAdapterList.size(); i++) {
            this.appAdapterList.get(i).notifyDataSetChanged();
        }
    }
}
