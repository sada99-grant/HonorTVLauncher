package com.honor.tvlauncher;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class LauncherActivity extends Activity {

    private static final Set<String> SYSTEM_PKGS = Collections.unmodifiableSet(
            new java.util.HashSet<>(java.util.Arrays.asList(
                    "android", "com.android.systemui", "com.huawei.systemmanager",
                    "com.huawei.hwid", "com.huawei.hwstartupguide"
            )));

    private GridView gridView;
    private AppAdapter adapter;
    private TextView memText;
    private TextView timeText;
    private final Handler clockHandler = new Handler();
    private final Runnable clockRunnable = new Runnable() {
        @Override
        public void run() {
            updateClock();
            clockHandler.postDelayed(this, 30000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        gridView = findViewById(R.id.grid);
        memText = findViewById(R.id.mem);
        timeText = findViewById(R.id.time);

        findViewById(R.id.btn_all_apps).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LauncherActivity.this, AppPickerActivity.class));
            }
        });

        loadApps();
        updateMemory();
        updateClock();
        clockHandler.postDelayed(clockRunnable, 30000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clockHandler.removeCallbacks(clockRunnable);
    }

    private void updateClock() {
        if (timeText != null) {
            timeText.setText(DateFormat.format("HH:mm", System.currentTimeMillis()).toString());
        }
    }

    private void updateMemory() {
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        if (am == null || memText == null) return;
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        try {
            am.getMemoryInfo(info);
            long availMb = info.availMem / (1024 * 1024);
            long totalMb = info.totalMem / (1024 * 1024);
            memText.setText(getString(R.string.mem_format, availMb, totalMb));
        } catch (Throwable t) {
            memText.setText(R.string.mem_unknown);
        }
    }

    private void loadApps() {
        PackageManager pm = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);

        List<AppItem> items = new ArrayList<>();
        for (ResolveInfo ri : resolveInfos) {
            String pkg = ri.activityInfo.packageName;
            if (SYSTEM_PKGS.contains(pkg)) continue;
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

        adapter = new AppAdapter(this, items);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {
                launch(items.get(position).pkg);
            }
        });

        if (items.isEmpty()) {
            Toast.makeText(this, R.string.no_apps, Toast.LENGTH_LONG).show();
        }
    }

    private void launch(String pkg) {
        PackageManager pm = getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(pkg);
        if (intent == null) {
            try {
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
            } catch (Throwable t) {
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
