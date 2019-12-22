package alvarofpp.study.launcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<AppObject> installedAppList = new ArrayList<>();
    ViewPager mViewPager;
    int cellHeight;

    int NUMBER_OF_ROWS = 5;
    int DRAWER_PEEK_HEIGHT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        initializeHome();
        initializeDrawer();
    }

    private void initializeHome() {
        ArrayList<PagerObject> pagerAppList = new ArrayList<>();
        ArrayList<AppObject> appList = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            appList.add(new AppObject("", "", getResources().getDrawable(R.drawable.ic_launcher_foreground)));
        }

        pagerAppList.add(new PagerObject(appList));
        pagerAppList.add(new PagerObject(appList));
        pagerAppList.add(new PagerObject(appList));

        cellHeight = (getDisplayContextHeight() - this.DRAWER_PEEK_HEIGHT) / this.NUMBER_OF_ROWS;

        this.mViewPager = findViewById(R.id.viewPager);
        this.mViewPager.setAdapter(new ViewPagerAdapter(this, pagerAppList, this.cellHeight));
    }

    private void initializeDrawer() {
        View mBottomSheet = findViewById(R.id.bottomSheet);
        final GridView mDrawerGridView = findViewById(R.id.drawerGrid);
        final BottomSheetBehavior mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);

        mBottomSheetBehavior.setHideable(false);
        mBottomSheetBehavior.setPeekHeight(this.DRAWER_PEEK_HEIGHT);

        this.installedAppList = getInstalledAppList();

        mDrawerGridView.setAdapter(new AppAdapter(getApplicationContext(), this.installedAppList, this.cellHeight));

        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN && mDrawerGridView.getChildAt(0).getY() != 0) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                if (newState == BottomSheetBehavior.STATE_DRAGGING && mDrawerGridView.getChildAt(0).getY() != 0) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View view, float slideOffset) {

            }
        });
    }

    private List<AppObject> getInstalledAppList() {
        List<AppObject> list = new ArrayList<>();

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> untreatedAppList = getApplicationContext().getPackageManager().queryIntentActivities(intent, 0);

        for (ResolveInfo untreatedApp : untreatedAppList) {
            String appName = untreatedApp.activityInfo.loadLabel(getPackageManager()).toString();
            String appPackageName = untreatedApp.activityInfo.packageName;
            Drawable appImage = untreatedApp.activityInfo.loadIcon(getPackageManager());

            AppObject app = new AppObject(appPackageName, appName, appImage);
            if (!list.contains(app)) {
                list.add(app);
            }
        }

        return list;
    }

    private int getDisplayContextHeight() {
        final WindowManager windowManager = getWindowManager();
        final Point size = new Point();
        int screenHeight = 0;
        int actionBarHeight = 0;
        int statusBarHeight = 0;

        if (getActionBar() != null) {
            actionBarHeight = getActionBar().getHeight();
        }

        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        int contentTop = (findViewById(android.R.id.content)).getTop();
        windowManager.getDefaultDisplay().getSize(size);
        screenHeight = size.y;

        return screenHeight - contentTop - actionBarHeight - statusBarHeight;
    }
}
