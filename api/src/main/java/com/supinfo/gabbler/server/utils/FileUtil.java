package com.supinfo.gabbler.server.utils;

public class FileUtil {

    public static String getFileExtensionFromMimetype(String mimetype){
        switch (mimetype){
            case "image/png":
                return "png";
            case "image/jpeg":
                return "jpg";
            default:
                return "unknown";
        }
    }

}
