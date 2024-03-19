package org.example.connectors;

import com.google.common.reflect.ClassPath;
import org.example.utils.Constant;
import org.example.utils.HttpUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ConnectorFactory {
    static List<Connector> connectorList = new ArrayList<>();
    private ConnectorFactory() {
    }

    public static Connector getConnector(String url) {
        Optional<Connector> matchingConnector = connectorList.stream()
                .filter(connector -> HttpUtils.isSameConnectorDomain(url, connector.getDomain()))
                .findFirst();
        return matchingConnector.orElse(null);
    }

    public static void loadConnector() {
        try {
            Set<Class<?>> classSet = ConnectorFactory.findAllClassesUsingGoogleGuice();
            for (Class<?> aClass : classSet) {
                Object object = aClass.newInstance();
                connectorList.add((Connector) object);
            }
        } catch (Exception e) {
            System.out.println("Load connector failed: " + e.getMessage());
        }
    }

    private static Set<Class<?>> findAllClassesUsingGoogleGuice() throws IOException {
        return ClassPath.from(ClassLoader.getSystemClassLoader())
                .getAllClasses()
                .stream()
                .filter(clazz -> clazz.getPackageName().equalsIgnoreCase(Constant.CONNECTOR_DIR))
                .map(ClassPath.ClassInfo::load)
                .collect(Collectors.toSet());
    }
}
