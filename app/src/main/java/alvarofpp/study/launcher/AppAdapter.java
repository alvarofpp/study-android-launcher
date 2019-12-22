package alvarofpp.study.launcher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class AppAdapter extends BaseAdapter {
    private Context context;
    private List<AppObject> appList;
    private int cellHeight;

    public AppAdapter(Context context, List<AppObject> appList, int cellHeight) {
        this.context = context;
        this.appList = appList;
        this.cellHeight = cellHeight;
    }

    @Override
    public int getCount() {
        return this.appList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.appList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.item_app, parent, false);
        } else {
            v = convertView;
        }

        LinearLayout mLayout = v.findViewById(R.id.layout);
        ImageView mImage = v.findViewById(R.id.image);
        TextView mLabel = v.findViewById(R.id.label);

        mImage.setImageDrawable(this.appList.get(position).getImage());
        mLabel.setText(this.appList.get(position).getName());

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, this.cellHeight);
        mLayout.setLayoutParams(lp);

        mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).itemPress(appList.get(position));
            }
        });
        mLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((MainActivity) context).itemLongPress(appList.get(position));
                return false;
            }
        });

        return v;
    }
}
