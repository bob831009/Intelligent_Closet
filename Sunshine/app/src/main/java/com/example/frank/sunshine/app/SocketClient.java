package com.example.frank.sunshine.app;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by bob on 2015/5/31.
 */
public class SocketClient {
    public SocketClient(String address, int port, String led_number) {
        Log.d("happy", "0");
        Socket client = new Socket();
        InetSocketAddress isa = new InetSocketAddress(address, port);
        try {
            client.connect(isa, 10000);
            Log.d("happy", "1");
            BufferedOutputStream out = new BufferedOutputStream(client
                    .getOutputStream());
            Log.d("happy", "2");
            // 送出字串
            out.write((led_number).getBytes());
            out.flush();
            out.write("end".getBytes());
            out.flush();
            out.close();
            out = null;
            client.close();
            client = null;

        } catch (java.io.IOException e) {
            System.out.println("Socket error");
            System.out.println("IOException :" + e.toString());
        }
    }
}
