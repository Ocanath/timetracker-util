package edu.cs4730.workmanagerdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.StrictMode;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * need a simple worker, parameter worker.  Then chain them together for the last example.  maybe a parallel
 * Make sure you are looking at the logcat as well.  You can see what the workers are doing.
 * <p>
 * see https://developer.android.com/topic/libraries/architecture/adding-components
 * note, no toasts in workers, so POST_NOTIFICATION permission is not needed.
 */
@SuppressLint("SetTextI18n")

public class Blaster {
    Context mContext;
    public Blaster(Context mContext) {
        this.mContext = mContext;
        initSoc();
    }

    private DatagramSocket soc;
    private int port;
    private int onoffstate;
    private InetAddress getBroadcastAddress() throws IOException {
        WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = wifi.getDhcpInfo();
        // handle null somehow

        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
            quads[k] = (byte) (broadcast >> (k * 8));
        return InetAddress.getByAddress(quads);
    }
    public void initSoc(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            this.soc = new DatagramSocket();
            this.soc.setBroadcast(true);
            this.port = 4953;
            this.onoffstate = 0;
            Log.d("Blaster", "Initialized soc");
        } catch (IOException e){
            Log.e("errors", "Init IOException: "+e.getMessage());
        }
    }
    public void blastMessage(){
        try {
            byte[] sendData = "WAZZAP BIATCHS ITS YA BOY JESSE".getBytes();
            DatagramPacket pkt = new DatagramPacket(sendData, sendData.length, getBroadcastAddress(), this.port);
            this.soc.send(pkt);
            Log.d("Blaster", "SENT TO: "+getBroadcastAddress().getHostAddress());
        } catch (IOException e){
            Log.e("errors", "Transmit IOException: "+e.getMessage());
        }
    }
    public void toggleLights(){
        try {
            byte[] sendData;
            if(this.onoffstate == 0) {
               sendData ="lightsoff".getBytes();
               this.onoffstate = 1;
            }
            else{
                sendData = "lightson".getBytes();
                this.onoffstate = 0;
            }
            DatagramPacket pkt = new DatagramPacket(sendData, sendData.length, getBroadcastAddress(), this.port);
            this.soc.send(pkt);
            Log.d("Blaster", "SENT TO: "+getBroadcastAddress().getHostAddress());
        } catch (IOException e){
            Log.e("errors", "Transmit IOException: "+e.getMessage());
        }
    }

}
