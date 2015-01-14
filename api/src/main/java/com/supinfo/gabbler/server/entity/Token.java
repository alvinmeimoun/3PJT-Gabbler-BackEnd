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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token = (Token) o;

        if (!series.equals(token.series)) return false;
        if (!username.equals(token.username)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = username.hashCode();
        result = 31 * result + series.hashCode();
        return result;
    }
}
