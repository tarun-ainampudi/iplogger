package com.iplogger;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.lang.reflect.Type;
import java.util.List;

public class Helper {
    public static final String IP_LIST_FILENAME = "ip_list.json";
    public static List<String> ipList = new ArrayList<>();

    public static void logIPv4Address(Context context) {
        // Get the Wi-Fi manager service
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        // Get Wi-Fi connection info
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        // Get the IP address as an integer and convert it to a human-readable format
        int ipAddress = wifiInfo.getIpAddress();
        String ipString = (ipAddress & 0xFF) + "." +
                ((ipAddress >> 8) & 0xFF) + "." +
                ((ipAddress >> 16) & 0xFF) + "." +
                ((ipAddress >> 24) & 0xFF);

        // Add IP address to the list
        addIpToList(ipString, context);
    }

    public static void addIpToList(String ipAddress, Context context) {
        loadIpList(context);
        if (ipList.isEmpty() || ipList.size() < 10) {
            // Add new IP address if list size is less than 10
           if(!ipList.isEmpty()){
               if(!ipList.get(ipList.size() - 1).equals(ipAddress)){
                   ipList.add(ipAddress);
               }
           }
           else {
               ipList.add(ipAddress);
           }
        } else {
            // If list size is 10, rotate the list (move last to first)
            String lastIp = ipList.get(ipList.size() - 1); // Get the last IP
            ipList.clear(); // Clear the list
            ipList.add(lastIp);// Add the last IP to the first position
            ipList.add(ipAddress);
        }
        saveIpList(context);
    }

    public static void saveIpList(Context context) {
        File file = new File(context.getFilesDir(), IP_LIST_FILENAME);  // Create file in internal storage
        Gson gson = new Gson();
        String json = gson.toJson(ipList);  // Convert the list to JSON

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(json);  // Write JSON to file
        } catch (IOException ignored) {

        }
    }

    // Method to load the IP list from SharedPreferences
    public static void loadIpList(Context context) {
        File file = new File(context.getFilesDir(), IP_LIST_FILENAME);  // File in internal storage

        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<String>>() {
                }.getType();
                ipList = gson.fromJson(reader, type);  // Convert JSON back to List
            } catch (IOException ignored) {
            }
        } else {
            ipList = new ArrayList<>();  // If file doesn't exist, start with an empty list
        }
    }
    public static String getblock(String s){
        String[] arr=s.split("\\.");
        int num = Integer.parseInt(arr[2]);
        if(num>=32 && num<64){
            return "AB-1";
        } else if (num>=64 && num<96) {
            return "AB-2";
        } else if (num>=96&&num<128) {
            return "CB";
        }
        else if (num>=128&&num<136){
            return "MH-5";
        }
        else {
            return "none";
        }
    }
}
