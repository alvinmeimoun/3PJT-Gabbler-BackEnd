package com.supinfo.gabbler.server.dto;

import com.supinfo.gabbler.server.entity.enums.Gender;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

/**
 * Created by Alvin on 02/05/2015.
 */
public class UserSearchResult {
    private Long id;

    private @NotNull
    String nickname;

    private String firstname;
    private String lastname;

    private @NotNull @Email
    String email;

    private @Size(max = 3) String phoneIndicator;
    private String phone;

    private @NotNull
    Timestamp birthdate;

    private Gender gender;

    private @NotNull String displayName;

    private @NotNull Timestamp creationDate;

    private Boolean isFollowing = false;


    private String profilePictureMimetype;
    private String backgroundPictureMimetype;

    public Long getId() {
        return id;
    }

    public UserSearchResult setId(Long id) {
        this.id = id;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public UserSearchResult setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getFirstname() {
        return firstname;
    }

    public UserSearchResult setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public String getLastname() {
        return lastname;
    }

    public UserSearchResult setLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserSearchResult setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhoneIndicator() {
        return phoneIndicator;
    }

    public UserSearchResult setPhoneIndicator(String phoneIndicator) {
        this.phoneIndicator = phoneIndicator;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public UserSearchResult setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Timestamp getBirthdate() {
        return birthdate;
    }

    public UserSearchResult setBirthdate(Timestamp birthdate) {
        this.birthdate = birthdate;
        return this;
    }

    public Gender getGender() {
        return gender;
    }

    public UserSearchResult setGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public UserSearchResult setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public UserSearchResult setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public String getProfilePictureMimetype() {
        return profilePictureMimetype;
    }

    public UserSearchResult setProfilePictureMimetype(String profilePictureMimetype) {
        this.profilePictureMimetype = profilePictureMimetype;
        return this;
    }

    public String getBackgroundPictureMimetype() {
        return backgroundPictureMimetype;
    }

    public UserSearchResult setBackgroundPictureMimetype(String backgroundPictureMimetype) {
        this.backgroundPictureMimetype = backgroundPictureMimetype;
        return this;
    }

    public Boolean isFollowing() {
        return isFollowing;
    }

    public UserSearchResult setIsFollowing(Boolean isFollowing) {
        this.isFollowing = isFollowing;
        return this;
    }
}
