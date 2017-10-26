package cn.edu.gdmec.android.mobileguard.m2theftguard.service;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;

/**
 * Created by Lenovo on 2017/10/24.
 */
//测试一波
public class GPSLocationService extends Service {
    private  LocationManager lm;
    private MyListener listener;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void  onCreate(){
        super.onCreate();
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener = new MyListener();
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);//获取准确位置
        criteria.setCostAllowed(true);//允许产生开销
        String name = lm.getBestProvider(criteria, true);
        //
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            return;
        }
        lm.requestLocationUpdates(name, 0 , 0 ,listener);
    }
    private class MyListener implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {
            StringBuilder sb = new StringBuilder();
            sb.append("accuracy:"+location.getAccuracy()+"\n");
            sb.append("speed:"+location.getSpeed()+"\n");
            sb.append("Logitude:"+location.getLongitude()+"\n");
            sb.append("Latitude:"+location.getLatitude()+"\n");
            String result = sb.toString();
            SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
            String safenumber = sp.getString("safephone","");
            SmsManager.getDefault().sendTextMessage(safenumber, null, result,null,null);
            stopSelf();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        lm.removeUpdates(listener);
        listener = null;
    }
}

