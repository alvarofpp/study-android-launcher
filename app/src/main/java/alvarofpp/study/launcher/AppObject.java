package alvarofpp.study.launcher;

import android.graphics.drawable.Drawable;

public class AppObject {
    private String packageName;
    private String name;
    private Drawable image;

    public AppObject(String packageName, String name, Drawable image) {
        this.packageName = packageName;
        this.name = name;
        this.image = image;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getName() {
        return name;
    }

    public Drawable getImage() {
        return image;
    }
}
