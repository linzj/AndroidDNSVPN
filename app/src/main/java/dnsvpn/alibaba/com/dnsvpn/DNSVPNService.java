package dnsvpn.alibaba.com.dnsvpn;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.VpnService;
import android.os.ParcelFileDescriptor;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;

public class DNSVPNService extends VpnService {
    private VpnService.Builder builder = new VpnService.Builder();
    private ParcelFileDescriptor mInterface;
    private Thread mThread;
    private boolean shouldRun = true;
    private final BroadcastReceiver stopServiceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent) {
            if (paramAnonymousIntent.getAction().equals("STOP_DNS_CHANGER"))
                DNSVPNService.this.stopThisService();
        }
    };
    private DatagramChannel tunnel;

    private void registerBroadcast() {
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction("STOP_DNS_CHANGER");
        try {
            registerReceiver(this.stopServiceReceiver, localIntentFilter);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    private void stopThisService() {
        this.shouldRun = false;
        try {
            this.tunnel.close();
            this.mInterface.close();
            if (this.mThread != null)
                this.mThread.interrupt();
        } catch (Exception localException1) {
            try {
                unregisterReceiver(this.stopServiceReceiver);
                stopSelf();
                localException1.printStackTrace();
            } catch (Exception localException2) {
                stopSelf();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setTunnel(DatagramChannel tunnel) {
        this.tunnel = tunnel;
    }

    private void setInterface(ParcelFileDescriptor myinterface) {
        mInterface = myinterface;
    }

    @Override
    public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2) {
        registerBroadcast();
        this.mThread = new Thread(new Runnable() {
            public void run() {
                try {
                    DNSVPNService.this.setInterface(DNSVPNService.this.builder.setSession(DNSVPNService.this.getText(R.string.app_name).toString()).
                            addAddress("192.168.0.1", 24).addDnsServer("8.8.8.8").addDnsServer("8.8.4.4").establish());
                    DNSVPNService.this.setTunnel(DatagramChannel.open());
                    DNSVPNService.this.tunnel.connect(new InetSocketAddress("127.0.0.1", 8087));
                    DNSVPNService.this.protect(DNSVPNService.this.tunnel.socket());
                    while (DNSVPNService.this.shouldRun)
                        Thread.sleep(100L);
                } catch (Exception localException1) {
                    try {
                        if (DNSVPNService.this.mInterface != null) {
                            DNSVPNService.this.mInterface.close();
                            DNSVPNService.this.setInterface(null);
                        }

                        try {
                            if (DNSVPNService.this.mInterface != null) {
                                DNSVPNService.this.mInterface.close();
                                DNSVPNService.this.setInterface(null);
                                return;
                            }
                        } catch (Exception localException2) {
                            localException2.printStackTrace();
                            return;
                        }
                    } catch (Exception localException3) {
                        localException3.printStackTrace();
                        return;
                    }
                }
                try {
                    if (DNSVPNService.this.mInterface != null) {
                        DNSVPNService.this.mInterface.close();
                        DNSVPNService.this.setInterface(null);
                    }
                } catch (Exception localException4) {
                        localException4.printStackTrace();
                }
            }
        }
                , "DNS Changer");
        this.mThread.start();
        return Service.START_STICKY;
    }
}