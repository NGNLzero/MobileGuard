package cn.edu.gdmec.android.mobileguard.m9advancedtools.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by pc on 2017/12/16.
 */

public class Appinfo {
    public String packageName;
    public Drawable icon;
    public String appName;
    public String apkPath;
    public long appSize;
    public boolean isInRoom;
    public boolean isUserApp;
    public boolean isSelected = false;
    public String versionName;
    public long firstInstallTime;
    public String signature;
    public String requestedPermissions;
    public String activities;

    public String getAppLocation(boolean isInRoom) {
        if (isInRoom){
            return "手机内存";
        }else {
            return "外部存储";
        }
    }
    public boolean isLock;
}
