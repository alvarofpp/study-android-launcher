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
    BottomSheetBehavior mBottomSheetBehavior;
    GridView mDrawerGridView;
    List<AppObject> installedAppList = new ArrayList<>();
    ViewPager mViewPager;
    int cellHeight;
    AppObject mAppDrag = null;
    ViewPagerAdapter mViewPagerAdapter;

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

        ArrayList<AppObject> appList1 = new ArrayList<>();
        ArrayList<AppObject> appList2 = new ArrayList<>();
        ArrayList<AppObject> appList3 = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            appList1.add(new AppObject("", "", getResources().getDrawable(R.drawable.ic_launcher_foreground)));
        }
        for (int i = 0; i < 20; i++) {
            appList2.add(new AppObject("", "", getResources().getDrawable(R.drawable.ic_launcher_foreground)));
        }
        for (int i = 0; i < 20; i++) {
            appList3.add(new AppObject("", "", getResources().getDrawable(R.drawable.ic_launcher_foreground)));
        }
        pagerAppList.add(new PagerObject(appList1));
        pagerAppList.add(new PagerObject(appList2));
        pagerAppList.add(new PagerObject(appList3));


        cellHeight = (getDisplayContextHeight() - this.DRAWER_PEEK_HEIGHT) / this.NUMBER_OF_ROWS;

        this.mViewPager = findViewById(R.id.viewPager);
        this.mViewPagerAdapter = new ViewPagerAdapter(this, pagerAppList, this.cellHeight);
        this.mViewPager.setAdapter(mViewPagerAdapter);
    }

    private void initializeDrawer() {
        View mBottomSheet = findViewById(R.id.bottomSheet);
        this.mDrawerGridView = findViewById(R.id.drawerGrid);
        this.mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);

        this.mBottomSheetBehavior.setHideable(false);
        this.mBottomSheetBehavior.setPeekHeight(this.DRAWER_PEEK_HEIGHT);

        this.installedAppList = getInstalledAppList();
        this.mDrawerGridView.setAdapter(new AppAdapter(getApplicationContext(), this.installedAppList, this.cellHeight));

        this.mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                if (mAppDrag != null) {
                    return;
                }
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

    public void itemPress(AppObject app)
    {
        if (this.mAppDrag != null) {
            app.setPackageName(this.mAppDrag.getPackageName());
            app.setName(this.mAppDrag.getName());
            app.setImage(this.mAppDrag.getImage());
            this.mViewPagerAdapter.notifyGridChanged();

            this.mAppDrag = null;
            return;
        } else {
            Intent launchAppIntent = getApplicationContext().getPackageManager().getLaunchIntentForPackage(
                    app.getPackageName()
            );

            if (launchAppIntent != null) {
                getApplicationContext().startActivity(launchAppIntent);
            }
        }
    }

    public void itemLongPress(AppObject app)
    {
        collapseDrawer();
        this.mAppDrag = app;
    }

    private void collapseDrawer() {
        this.mDrawerGridView.setY(0);
        this.mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
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
