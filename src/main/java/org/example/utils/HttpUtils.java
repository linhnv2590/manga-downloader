package org.example.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class HttpUtils {
    private HttpUtils() {
    }
    public static boolean isSameConnectorDomain(String inputUrl, List<String> connectorUrls) {
        boolean isSameConnectorDomain = false;
        for (String connectorUrl: connectorUrls) {
            try {
                URL thisUrl = new URL(connectorUrl);
                URL inputUrlObj = new URL(inputUrl);
                isSameConnectorDomain = thisUrl.getHost().equalsIgnoreCase(inputUrlObj.getHost());
            } catch (MalformedURLException e) {
                return false;
            }
        }
        return isSameConnectorDomain;
    }
}
