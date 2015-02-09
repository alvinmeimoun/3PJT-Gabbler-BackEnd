package com.supinfo.gabbler.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Table(name = "GABS")
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Gabs {

    private Long id;

    private @NotNull Long userId;

    private @NotNull String content;
    private @NotNull Date postDate;

    public Gabs(){}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public Gabs setId(Long id) {
        this.id = id;
        return this;
    }

    @Column(name = "CONTENT")
    @Basic
    public String getContent() {
        return content;
    }

    public Gabs setContent(String content) {
        this.content = content;
        return this;
    }

    @Column(name = "POST_DATE")
    @Basic
    public Date getPostDate() {
        return postDate;
    }

    public Gabs setPostDate(Date postDate) {
        this.postDate = postDate;
        return this;
    }

    @Column(name="USER_ID")
    @Basic
    public Long getUserId() {
        return userId;
    }

    public Gabs setUserId(Long userId) {
        this.userId = userId;
        return this;
    }
}
