package com.supinfo.gabbler.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Table(name = "GABS")
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Gabs {

    private Long id;

    private @NotNull Long userId;

    private @NotNull String content;
    private @NotNull Date postDate;

    private @NotNull Set<User> likers = new HashSet<>();

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

    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(name = "GABS_USER_LIKE",
            joinColumns = { @JoinColumn(name = "ID_USER", nullable = false, updatable = false) },
            inverseJoinColumns = {@JoinColumn(name = "ID_GABS", nullable = false, updatable = false)}
    )
    @Fetch(FetchMode.SUBSELECT)
    public Set<User> getLikers() {
        return likers;
    }

    public Gabs setLikers(Set<User> likers) {
        this.likers = likers;
        return this;
    }

    public Gabs addLiker(User user) {
        if (!likers.contains(user)) {
            likers.add(user);
        }
        return this;
    }

    public Gabs removeLiker(User user) {
        if (likers.contains(user)) {
            likers.remove(user);
        }
        return this;
    }
}
