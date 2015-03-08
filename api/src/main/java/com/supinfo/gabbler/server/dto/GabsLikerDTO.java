package com.supinfo.gabbler.server.dto;

/**
 * Created by Alvin on 08/03/2015.
 */
public class GabsLikerDTO {

    private String username;
    private Long gabsID;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getGabsID() {
        return gabsID;
    }

    public void setGabsID(Long gabsID) {
        this.gabsID = gabsID;
    }
}
