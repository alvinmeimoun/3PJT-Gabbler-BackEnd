package com.supinfo.gabbler.server.dto;

import com.supinfo.gabbler.server.entity.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Alvin on 08/03/2015.
 */
public class GabsDTO {

    private Long id;

    private @NotNull
    Long userId;

    private @NotNull @Size(max = 255) String content;
    private @NotNull
    Date postDate;

    private @NotNull
    Set<GabsLikerDTO> likers = new HashSet<>();

    public GabsDTO(){
        this.likers = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public Set<GabsLikerDTO> getLikers() {
        return likers;
    }

    public void setLikers(Set<GabsLikerDTO> likers) {
        this.likers = likers;
    }
}
