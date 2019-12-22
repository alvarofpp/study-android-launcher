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

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }
}
