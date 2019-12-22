package alvarofpp.study.launcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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
    int numRow = 0;
    int numColumn = 0;

    int NUMBER_OF_ROWS = 5;
    int DRAWER_PEEK_HEIGHT = 100;
    String PREFS_NAME = "NovaPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        getPermissions();
        getData();

        final LinearLayout mTopDrawerLayout = findViewById(R.id.topDrawerLayout);
        mTopDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                DRAWER_PEEK_HEIGHT = mTopDrawerLayout.getHeight();
                initializeHome();
                initializeDrawer();
            }
        });

        ImageButton mSettings = findViewById(R.id.settings);
        mSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            }
        });
    }

    private void initializeHome() {
        ArrayList<PagerObject> pagerAppList = new ArrayList<>();

        ArrayList<AppObject> appList1 = new ArrayList<>();
        ArrayList<AppObject> appList2 = new ArrayList<>();
        ArrayList<AppObject> appList3 = new ArrayList<>();
        for (int i = 0; i < this.numColumn*this.numRow; i++) {
            appList1.add(new AppObject("", "", getResources().getDrawable(R.drawable.ic_launcher_foreground), false));
        }
        for (int i = 0; i < this.numColumn*this.numRow; i++) {
            appList2.add(new AppObject("", "", getResources().getDrawable(R.drawable.ic_launcher_foreground), false));
        }
        for (int i = 0; i < this.numColumn*this.numRow; i++) {
            appList3.add(new AppObject("", "", getResources().getDrawable(R.drawable.ic_launcher_foreground), false));
        }
        pagerAppList.add(new PagerObject(appList1));
        pagerAppList.add(new PagerObject(appList2));
        pagerAppList.add(new PagerObject(appList3));


        cellHeight = (getDisplayContextHeight() - this.DRAWER_PEEK_HEIGHT) / this.numRow;

        this.mViewPager = findViewById(R.id.viewPager);
        this.mViewPagerAdapter = new ViewPagerAdapter(this, pagerAppList, this.cellHeight, this.numColumn);
        this.mViewPager.setAdapter(mViewPagerAdapter);
    }

    private void initializeDrawer() {
        View mBottomSheet = findViewById(R.id.bottomSheet);
        this.mDrawerGridView = findViewById(R.id.drawerGrid);
        this.mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);

        this.mBottomSheetBehavior.setHideable(false);
        this.mBottomSheetBehavior.setPeekHeight(this.DRAWER_PEEK_HEIGHT);

        this.installedAppList = getInstalledAppList();
        this.mDrawerGridView.setAdapter(new AppAdapter(this, this.installedAppList, this.cellHeight));

        this.mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                if (mAppDrag != null) {
                    return;
                }
                if (newState == BottomSheetBehavior.STATE_COLLAPSED && mDrawerGridView.getChildAt(0).getY() != 0) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
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
        if (this.mAppDrag != null && !app.getName().equals("")) {
            Toast.makeText(this, "Cell Already Occupied", Toast.LENGTH_SHORT).show();
            return;
        }

        if (this.mAppDrag != null && !app.getAppInDrawer()) {
            app.setPackageName(this.mAppDrag.getPackageName());
            app.setName(this.mAppDrag.getName());
            app.setImage(this.mAppDrag.getImage());

            if (!this.mAppDrag.getAppInDrawer()) {
                this.mAppDrag.setPackageName("");
                this.mAppDrag.setName("");
                this.mAppDrag.setImage(getResources().getDrawable(R.drawable.ic_launcher_foreground));
                this.mAppDrag.setAppInDrawer(false);
            }

            this.mAppDrag = null;
            this.mViewPagerAdapter.notifyGridChanged();
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
        this.mDrawerGridView.setY(this.DRAWER_PEEK_HEIGHT);
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

            AppObject app = new AppObject(appPackageName, appName, appImage, true);
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

    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    private void getData() {
        ImageView mHomeScreenImage = findViewById(R.id.homeScreenImage);

        SharedPreferences sharedPreferences = getSharedPreferences(this.PREFS_NAME, MODE_PRIVATE);
        String imageUri = sharedPreferences.getString("imageUri", null);
        int numRow = sharedPreferences.getInt("numRow", 7);
        int numColumn = sharedPreferences.getInt("numColumn", 5);

        if (this.numRow != numRow || this.numColumn != numColumn) {
            this.numRow = numRow;
            this.numColumn = numColumn;
            initializeHome();
        }

        if (imageUri != null) {
            mHomeScreenImage.setImageURI(Uri.parse(imageUri));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }
}
