package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

public class Network {
    static String internal() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "?.?.?.?";
    }

    static String external() {
        try( BufferedReader in = new BufferedReader( new InputStreamReader( new URL(
            "https://checkip.amazonaws.com"
        ).openStream()))) {
            return in.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return internal();
    }
}
