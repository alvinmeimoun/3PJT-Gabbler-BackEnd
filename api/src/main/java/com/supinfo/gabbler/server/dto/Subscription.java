package com.supinfo.gabbler.server.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supinfo.gabbler.server.entity.enums.Gender;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Subscription {

    private @NotNull String nickname;

    private String firstname;
    private String lastname;
    private @NotNull String email;
    private String displayName;
    private @NotNull Gender gender;
    private @NotNull Timestamp birthdate;
    private @NotNull String password;

    public String getNickname() {
        return nickname;
    }

    public Subscription setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getFirstname() {
        return firstname;
    }

    public Subscription setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public String getLastname() {
        return lastname;
    }

    public Subscription setLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Subscription setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Subscription setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public Gender getGender() {
        return gender;
    }

    public Subscription setGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public Timestamp getBirthdate() {
        return birthdate;
    }

    public Subscription setBirthdate(Timestamp birthdate) {
        this.birthdate = birthdate;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Subscription setPassword(String password) {
        this.password = password;
        return this;
    }
}
