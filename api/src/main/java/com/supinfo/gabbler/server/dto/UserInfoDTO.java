package com.supinfo.gabbler.server.dto;

import com.supinfo.gabbler.server.entity.enums.Gender;

import java.sql.Timestamp;


public class UserInfoDTO {
    private Long id;

    private String nickname;

    private String firstname;
    private String lastname;

    private String email;

    private boolean emailValidated = false;

    private String phoneIndicator;
    private String phone;

    private Timestamp birthdate;

    private Gender gender;

    private String displayName;

    private Timestamp creationDate;

    private String profilePictureMimetype;
    private String backgroundPictureMimetype;

    private int nbFollowers;
    private int nbFollowings;

    /* END VARIABLE DECLARATION */

    public Long getId() {
        return id;
    }

    public UserInfoDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public UserInfoDTO setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getFirstname() {
        return firstname;
    }

    public UserInfoDTO setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public String getLastname() {
        return lastname;
    }

    public UserInfoDTO setLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserInfoDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhoneIndicator() {
        return phoneIndicator;
    }

    public UserInfoDTO setPhoneIndicator(String phoneIndicator) {
        this.phoneIndicator = phoneIndicator;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public UserInfoDTO setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Timestamp getBirthdate() {
        return birthdate;
    }

    public UserInfoDTO setBirthdate(Timestamp birthdate) {
        this.birthdate = birthdate;
        return this;
    }

    public Gender getGender() {
        return gender;
    }

    public UserInfoDTO setGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public UserInfoDTO setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public UserInfoDTO setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public boolean isEmailValidated() {
        return emailValidated;
    }

    public UserInfoDTO setEmailValidated(boolean emailValidated) {
        this.emailValidated = emailValidated;
        return this;
    }

    public String getProfilePictureMimetype() {
        return profilePictureMimetype;
    }

    public UserInfoDTO setProfilePictureMimetype(String profilePictureMimetype) {
        this.profilePictureMimetype = profilePictureMimetype;
        return this;
    }

    public String getBackgroundPictureMimetype() {
        return backgroundPictureMimetype;
    }

    public UserInfoDTO setBackgroundPictureMimetype(String backgroundPictureMimetype) {
        this.backgroundPictureMimetype = backgroundPictureMimetype;
        return this;
    }

    public int getNbFollowers() {
        return nbFollowers;
    }

    public UserInfoDTO setNbFollowers(int nbFollowers) {
        this.nbFollowers = nbFollowers;
        return this;
    }

    public int getNbFollowings() {
        return nbFollowings;
    }

    public UserInfoDTO setNbFollowings(int nbFollowings) {
        this.nbFollowings = nbFollowings;
        return this;
    }

}
