package cn.edu.gdmec.android.mobileguard.m9advancedtools.service;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.List;

import cn.edu.gdmec.android.mobileguard.App;
import cn.edu.gdmec.android.mobileguard.m9advancedtools.EnterPswActivity;
import cn.edu.gdmec.android.mobileguard.m9advancedtools.db.AppLockDao;

/**
 * Created by pc on 2017/12/16.
 */

public class AppLockService extends Service {
    private boolean flag = false;
    private AppLockDao dao;
    private Uri uri = Uri.parse(App.APPLOCK_CONTENT_URI);
    private List<String>packagenames;
    private Intent intent;
    private ActivityManager am;
    private List<ActivityManager.RunningTaskInfo>taskInfos;
    private ActivityManager.RunningTaskInfo taskInfo;
    private String packagename;
    private String tempStopProtectPackname;
    private AppLockReceiver receiver;
    private MyObserver observer;
    
    
    
    
    
    
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class AppLockReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if (App.APPLOCK_ACTION.equals(intent.getAction())){
                tempStopProtectPackname = intent.getStringExtra("packagename");
            }else if(Intent.ACTION_SCREEN_OFF.equals(intent.getAction())){
                tempStopProtectPackname = null;
                flag = false;
            }else if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())){
                if (flag == false){
                    startApplockService();
                }
            }
            
        }
    }

    private void startApplockService() {
        new Thread(){
            @Override
            public void run() {
                flag = true;
                while (flag){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                        UsageStatsManager m = (UsageStatsManager)getSystemService(Context.USAGE_STATS_SERVICE);
                        if (m != null){
                            long now = System.currentTimeMillis();
                            List<UsageStats> stats = m.queryUsageStats(
                                    UsageStatsManager.INTERVAL_BEST,now-60*1000,now);
                            String topActivity = "";
//                            取得最近运行的一个app,即当前运行的app
                            if ((stats != null) && (!stats.isEmpty())){
                                int j = 0;
                                for (int i = 0; i < stats.size(); i++){
                                    if (stats.get(i).getLastTimeUsed() > stats.get(j).getLastTimeUsed()){
                                        j = i;
                                    }
                                }
                                packagename = stats.get(j).getPackageName();
                            }
                        }
                    }else{
//                        任务栈的情况,最近使用的打开的任务栈在集合的最前面
                         taskInfos = am.getRunningTasks(1);
                         taskInfo = taskInfos.get(0);
                        packagename = taskInfo.topActivity.getPackageName();
                    }
                    System.out.println(packagename);
                    //判断该包名是否需要被保护
                    if (packagenames.contains(packagename)){
                        //判断应用程序是否需要临时停止保护,即输入了正确的密码
                        if (!packagename.equals(tempStopProtectPackname)){
                            //
                            intent.putExtra("packagename",packagename);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private class MyObserver extends ContentObserver {
        public MyObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            packagenames = dao.findAll();
            super.onChange(selfChange);
        }
    }

    @Override
    public void onCreate() {
        dao = new AppLockDao(this);
        packagenames = dao.findAll();
        if (packagenames.size() == 0){
            return;
        }
        observer = new MyObserver(new Handler());
        getContentResolver().registerContentObserver(uri,true,observer);
        receiver = new AppLockReceiver();
        IntentFilter filter = new IntentFilter(App.APPLOCK_ACTION);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(receiver,filter);
        //输入密码
        intent = new Intent(AppLockService.this, EnterPswActivity.class);
        am = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
        startApplockService();
        super.onCreate();

    }
}
