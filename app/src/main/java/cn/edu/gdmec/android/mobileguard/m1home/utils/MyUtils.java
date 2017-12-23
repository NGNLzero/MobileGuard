package cn.edu.gdmec.android.mobileguard.m1home.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

/**
 * Created by Jack on 2017/10/12.
 */

public class MyUtils {
    //获取版本号,return 返回版本号
    public static String getVersion(Context context){
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(),0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /*private static String dbname;
    public static String getVersion(Context context){
        String desc = null;
        PackageManager packageManager = context.getPackageManager();
        dbname = "/data/data/"+context.getPackageName()+"/files/antivirus.db";
        SQLiteDatabase db = SQLiteDatabase.openDatabase(
                dbname, null,
                SQLiteDatabase.OPEN_READONLY);

        Cursor cursor = db.rawQuery("select * from version", null);
        if (cursor.moveToNext()){
            desc = cursor.getString(0)+".";
            desc +=cursor.getString(1)+".";
            desc +=cursor.getString(2);
        }
        cursor.close();
        db.close();
        return desc;
    }*/
  //安装新版本
  public static void installApk(Activity activity, String apkFile){
      Intent intent = new Intent (Intent.ACTION_VIEW);
      intent.addCategory(Intent.CATEGORY_DEFAULT);
      intent.setDataAndType( Uri.fromFile(
              new File ( Environment.getExternalStoragePublicDirectory("/download/").getPath()+"/"+apkFile)),"application/vnd.android.package-archive");
      activity.startActivityForResult(intent,0);
  }

}
