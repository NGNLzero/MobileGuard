package cn.edu.gdmec.android.mobileguard.m4appmanager.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.text.format.DateFormat;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import cn.edu.gdmec.android.mobileguard.m4appmanager.entity.AppInfo;

/**
 * Created by pc on 2017/11/6.
 */

public class EngineUtils {


    public static void shareApplication(Context context, AppInfo appInfo){
        Intent intent = new Intent("android.intent.action.SEND");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"推荐您使用一款软件,名称叫:"+
        appInfo.appName+"下载路径:https://play.google.com/store/apps/details?id="+
        appInfo.packageName);
        context.startActivity(intent);
    }

    public static void startApplication(Context context,AppInfo appInfo){
        PackageManager pm = context.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(appInfo.packageName);
        if (intent!= null){
            context.startActivity(intent);
        }else {
            Toast.makeText(context,"该应用没有启动界面",Toast.LENGTH_SHORT).show();
        }
    }

    public static void SettingAppDetail(Context context,AppInfo appInfo){
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:"+ appInfo.packageName));
        context.startActivity(intent);
    }
    //获取App信息方法
    public static long AboutAppDetail(Context context, AppInfo appInfo){

        try {

            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(appInfo.packageName,0);
            PackageInfo packageInfo1 = packageManager.getPackageInfo(appInfo.packageName,PackageManager.GET_PERMISSIONS);
            PackageInfo packageInfo2 = packageManager.getPackageInfo(appInfo.packageName,PackageManager.GET_SIGNATURES);

            String certMsg = "";
            Signature[] signatures = packageInfo2.signatures;
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate)certificateFactory.generateCertificate(
                    new ByteArrayInputStream(signatures[0].toByteArray()));
            certMsg += cert.getIssuerDN().toString();
            certMsg += cert.getSubjectDN().toString();








            AlertDialog builder = new AlertDialog.Builder(context).setTitle(appInfo.appName).
                    setMessage("Version:"+"\n"+packageInfo.versionName+"\n"+
                            "\n"+ "Install time:"+"\n"+DateFormat.format("yyyy年MM月dd号 hh:mm:ss",packageInfo.firstInstallTime)
                            +"\n"+"Permissions:"+"\n"+ Arrays.toString(packageInfo1.requestedPermissions)+
                            "\n"+"\n"+"Certificate issuer:"+"\n"+certMsg+"hhh"

                    ).setNegativeButton("返回", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i){}
            }).show();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static void uninstallApplication(Context context,AppInfo appInfo){
        if (appInfo.isUserApp){
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DELETE);
            intent.setData(Uri.parse("package:"+appInfo.packageName));
            context.startActivity(intent);
        }else{
            Toast.makeText(context,"系统应用无法卸载",Toast.LENGTH_LONG).show();
        }
    }
}
