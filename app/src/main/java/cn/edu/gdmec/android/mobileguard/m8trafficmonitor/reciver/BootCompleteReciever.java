package cn.edu.gdmec.android.mobileguard.m8trafficmonitor.reciver;

/**
 * Created by Jack on 2017/11/27.
 */


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import cn.edu.gdmec.android.mobileguard.m8trafficmonitor.service.TrafficMonitoringService;
import cn.edu.gdmec.android.mobileguard.m8trafficmonitor.utils.SystemInfoUtils;

/**监听开机的广播该类，更新数据库，开启服务*/
public class BootCompleteReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //开机广播
        //判断流量监控服务是否开启，如果没开启则开启
        if(!SystemInfoUtils.isServiceRunning(context,"cn.edu.gdmec.android.mobileguard.m8trafficmonitor.service.TrafficMonitoringService")){
            //开启服务
//            m8
            //新添加
            Log.d ( "traffic service","turn on" );

            context.startService(new Intent(context, TrafficMonitoringService.class));
        }
    }
}

