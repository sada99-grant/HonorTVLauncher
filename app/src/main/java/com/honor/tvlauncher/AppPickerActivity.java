package com.honor.tvlauncher;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppPickerActivity extends Activity {

    private List<AppItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);

        GridView grid = findViewById(R.id.grid_all);
        loadAllApps();

        final AppAdapter adapter = new AppAdapter(this, items);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {
                launch(items.get(position).pkg);
            }
        });

        if (items.isEmpty()) {
            Toast.makeText(this, R.string.no_apps, Toast.LENGTH_LONG).show();
        }
    }

    private void loadAllApps() {
        PackageManager pm = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);
        List<ResolveInfo> resolved = pm.queryIntentActivities(intent, 0);

        items = new ArrayList<>();
        for (ResolveInfo ri : resolved) {
            String pkg = ri.activityInfo.packageName;
            ApplicationInfo ai;
            try {
                ai = pm.getApplicationInfo(pkg, 0);
            } catch (PackageManager.NameNotFoundException e) {
                continue;
            }
            if ((ai.flags & ApplicationInfo.FLAG_IS_GAME) != 0) continue;
            items.add(new AppItem(ri.loadLabel(pm).toString(), pkg, ri.loadIcon(pm)));
        }
        Collections.sort(items, new java.util.Comparator<AppItem>() {
            @Override
            public int compare(AppItem a, AppItem b) {
                return a.label.compareToIgnoreCase(b.label);
            }
        });
    }

    private void launch(String pkg) {
        PackageManager pm = getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(pkg);
        if (intent == null) {
            intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);
            intent.setPackage(pkg);
            List<ResolveInfo> list = pm.queryIntentActivities(intent, 0);
            if (!list.isEmpty()) {
                intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);
                intent.setClassName(pkg, list.get(0).activityInfo.name);
            } else {
                Toast.makeText(this, R.string.cannot_launch, Toast.LENGTH_SHORT).show();
                return;
            }
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch (Throwable t) {
            Toast.makeText(this, R.string.cannot_launch, Toast.LENGTH_SHORT).show();
        }
    }
}
