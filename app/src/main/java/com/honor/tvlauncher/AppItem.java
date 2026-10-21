package com.honor.tvlauncher;

import android.graphics.drawable.Drawable;

public class AppItem {
    public final String label;
    public final String pkg;
    public final Drawable icon;

    public AppItem(String label, String pkg, Drawable icon) {
        this.label = label;
        this.pkg = pkg;
        this.icon = icon;
    }
}
