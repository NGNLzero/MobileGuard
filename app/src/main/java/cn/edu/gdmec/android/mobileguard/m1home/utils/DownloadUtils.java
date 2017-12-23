package cn.edu.gdmec.android.mobileguard.m1home.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.webkit.MimeTypeMap;

/**
 * Created by Jack on 2017/10/12.
 */

public class DownloadUtils {
    public void downloadApk(String url, String targetFile, Context context){
        //创建下载任务
        DownloadManager.Request request = new DownloadManager.Request( Uri.parse(url));

        //漫游网络是否可以下载
        request.setAllowedOverRoaming(false);
        //设置文件类型，可以在下载结束后自动打开该文件
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(mimeTypeMap.getFileExtensionFromUrl(url));
        request.setMimeType(mimeString);

        //在通知栏中显示，默认就是显示的
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setVisibleInDownloadsUi(true);

        //sdcard的目录下的download文件夹，必须设置
        //request.setDestinationInExternalPublicDir("/download",targetFile);
        //request.setDestinationInExternalPublicDir("/data/data/"+context.getPackageName()+"/files/antivirus.db",targetFile);
        request.setDestinationInExternalPublicDir( Environment.DIRECTORY_DOWNLOADS,targetFile);

        //将下载请求加入下载队列
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        //加入下载队列后会给该任务返回一个long型的id，
        //通过该id可以取消任务，重启任务等等，看上面源码中框起来的方法
        long mTaskid = downloadManager.enqueue(request);

    }
}
