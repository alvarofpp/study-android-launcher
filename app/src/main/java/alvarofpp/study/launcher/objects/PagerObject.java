package alvarofpp.study.launcher.objects;

import android.content.Context;

import java.util.ArrayList;

import alvarofpp.study.launcher.R;

public class PagerObject {
    private ArrayList<AppObject> appList;

    public PagerObject(int numRow, int numColumn, Context context) {
        this.appList = new ArrayList<>();
        for (int i = 0; i < numColumn*numRow; i++) {
            appList.add(new AppObject("", "", context.getResources().getDrawable(R.drawable.ic_launcher_foreground), false));
        }
    }

    public ArrayList<AppObject> getAppList() {
        return this.appList;
    }
}
