package com.supinfo.gabbler.server.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Table(name = "TOKEN")
@Entity
public class Token {

    public Token(){}

    private @NotNull String username;
    private @NotNull String series;
    private @NotNull Date lastUsed;

    @Column(name = "USERNAME", nullable = false)
    @Basic
    public String getUsername() {
        return username;
    }

    public Token setUsername(String username) {
        this.username = username;
        return this;
    }

    @Column(name = "SERIES", nullable = false, length = 64)
    @Id
    public String getSeries() {
        return series;
    }

    public Token setSeries(String series) {
        this.series = series;
        return this;
    }

    @Column(name = "LAST_USED", nullable = false)
    @Basic
    public Date getLastUsed() {
        return lastUsed;
    }

    public Token setLastUsed(Date lastUsed) {
        this.lastUsed = lastUsed;
        return this;
    }
}
