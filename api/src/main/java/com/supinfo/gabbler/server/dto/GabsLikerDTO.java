package com.supinfo.gabbler.server.dto;

/**
 * Created by Alvin on 08/03/2015.
 */
public class GabsLikerDTO {

    private String displayName;
    private Long gabsID;
    private Long userID;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }
}
