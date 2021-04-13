package com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.Internet;

import java.net.InetAddress;

public class NetworkMonitor {

    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }
}
