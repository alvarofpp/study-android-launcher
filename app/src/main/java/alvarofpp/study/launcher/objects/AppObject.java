package alvarofpp.study.launcher.objects;

import android.content.Context;
import android.graphics.drawable.Drawable;

import alvarofpp.study.launcher.R;

public class AppObject {
    private String packageName;
    private String name;
    private Drawable image;
    private Boolean isAppInDrawer;

    public AppObject(String packageName, String name, Drawable image, Boolean isAppInDrawer) {
        this.packageName = packageName;
        this.name = name;
        this.image = image;
        this.isAppInDrawer = isAppInDrawer;
    }

    public void setDefaultValues(Context context) {
        this.setPackageName("");
        this.setName("");
        this.setImage(context.getResources().getDrawable(R.drawable.ic_launcher_foreground));
        this.setAppInDrawer(false);
    }

    public String getPackageName() {
        return packageName;
    }
    
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public Boolean getAppInDrawer() {
        return isAppInDrawer;
    }

    public void setAppInDrawer(Boolean appInDrawer) {
        isAppInDrawer = appInDrawer;
    }
}
