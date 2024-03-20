package org.example.utils;

import org.example.connectors.ConnectorServicesImp;

public class Constant {
    private Constant() {
    }
    public static final String CHAPTER = "chapter";
    public static final String CHAP = "Chap";
    public static final String DOWNLOAD_STATUS_TITLE = "Download status";
    public static final String DOWNLOAD_COMPLETED = "Download completed";
    public static final Integer MAX_LENGTH_FOLDER_NAME = 255;
    public static final String DEFAULT_SAVE_DIRECTORY = ConnectorServicesImp.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    public static final Integer CHROME_WAIT_TIMEOUT = 5;
    public static final Integer NUMBER_OF_TIMES_RETRY = 20;
    public static final Integer FIND_ELEMENT_TIMEOUT = 1000;
    public static final Integer TOO_MANY_REQUEST_TIMEOUT = 500;
    public static final Integer[] NUMBER_OF_TASK_DOWNLOAD = {1, 2, 3, 4, 5};
    public static final String CONNECTOR_DIR = "org.example.connectors.templates";
}
