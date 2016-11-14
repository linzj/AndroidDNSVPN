package dnsvpn.alibaba.com.dnsvpn;

/**
 * Created by linzj on 16-11-14.
 */

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.util.Iterator;
import java.util.List;

public class ServiceManager
{
    public static boolean isWorking()
    {
        Object localObject = (ActivityManager)StaticContext.AppContext.getSystemService("activity");
        String str = DNSVPNService.class.getName();
        for (ActivityManager.RunningServiceInfo info : ((ActivityManager)localObject).getRunningServices(R.string.app_name)) {
            if (str.equals(info.service.getClassName())) {
                Log.w("VPN STATE", "enabled");
                return true;
            }
        }
        Log.w("VPN STATE", "disabled");
        return false;
    }

    public static void start()
    {
        StaticContext.AppContext.sendBroadcast(new Intent("START_DNS_CHANGER"));
    }

    public static void stop()
    {
        StaticContext.AppContext.sendBroadcast(new Intent("STOP_DNS_CHANGER"));
    }
}
