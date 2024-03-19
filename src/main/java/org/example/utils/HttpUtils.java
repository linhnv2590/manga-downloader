package org.example.utils;

import java.net.MalformedURLException;
import java.net.URL;

public class HttpUtils {
    private HttpUtils() {
    }
    public static boolean isSameConnectorDomain(String inputUrl, String connectorUrl) {
        boolean isSameConnectorDomain;
        try {
            URL thisUrl = new URL(connectorUrl);
            URL inputUrlObj = new URL(inputUrl);
            isSameConnectorDomain = thisUrl.getHost().equalsIgnoreCase(inputUrlObj.getHost());
        } catch (MalformedURLException e) {
            return false;
        }
        return isSameConnectorDomain;
    }
}
