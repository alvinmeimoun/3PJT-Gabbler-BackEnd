package com.supinfo.gabbler.server.dto;

import java.io.File;

/**
 * Created by Alvin on 18/03/2015.
 */
public class PictureDTO {

    private File file;
    private String contentType;

    public File getFile() {
        return file;
    }

    public PictureDTO setFile(File file) {
        this.file = file;
        return this;
    }

    public String getContentType() {
        return contentType;
    }

    public PictureDTO setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }
}
