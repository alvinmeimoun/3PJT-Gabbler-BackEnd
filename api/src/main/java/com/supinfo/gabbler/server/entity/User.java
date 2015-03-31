package com.supinfo.gabbler.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.supinfo.gabbler.server.entity.enums.Gender;
import com.supinfo.gabbler.server.entity.enums.PasswordCryptMode;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Email;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Table(name = "USER")
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    public User(){} //jpa only

    private Long id;

    private @NotNull String nickname;

    private String firstname;
    private String lastname;

    private @NotNull @Email String email;

    private @NotNull boolean emailValidated = false;

    @JsonIgnore
    private @NotNull String password;

    private @Size(max = 3) String phoneIndicator;
    private String phone;

    private @NotNull Timestamp birthdate;

    private Gender gender;

    private @NotNull String displayName;

    private @NotNull Timestamp creationDate;

    @JsonIgnore
    private String activationCode;

    @JsonIgnore
    private @NotNull
    PasswordCryptMode passwordCryptMode = PasswordCryptMode.Plain;

    @JsonIgnore
    private @NotNull Set<User> followings = new HashSet<>();

    @JsonIgnore
    private @NotNull Set<User> followers = new HashSet<>();

    private String profilePictureMimetype;
    private String backgroundPictureMimetype;

    //Spring security declaration
    @JsonIgnore
    private @NotNull Boolean enabled = true;

    @JsonIgnore
    private @NotNull Boolean accountNonExpired = true;

    @JsonIgnore
    private @NotNull Boolean credentialsNonExpired = true;

    @JsonIgnore
    private @NotNull Boolean accountNonLocked = true;

    @JsonIgnore
    private @NotNull Set<Role> roles = new HashSet<>();

    /* END VARIABLE DECLARATION */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public User setId(Long id) {
        this.id = id;
        return this;
    }

    @Column(name = "NICKNAME", unique = true)
    @Basic
    public String getNickname() {
        return nickname;
    }

    public User setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    @Column(name = "FIRSTNAME")
    @Basic
    public String getFirstname() {
        return firstname;
    }

    public User setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    @Column(name = "LASTNAME")
    @Basic
    public String getLastname() {
        return lastname;
    }

    public User setLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    @Column(name = "EMAIL", unique = true)
    @Basic
    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    @JsonIgnore
    @Column(name = "PASSWORD")
    @Basic
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    @Column(name = "PHONE_INDICATOR")
    @Basic
    public String getPhoneIndicator() {
        return phoneIndicator;
    }

    public User setPhoneIndicator(String phoneIndicator) {
        this.phoneIndicator = phoneIndicator;
        return this;
    }

    @Column(name = "PHONE")
    @Basic
    public String getPhone() {
        return phone;
    }

    public User setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    @Column(name = "BIRTHDATE")
    @Basic
    public Timestamp getBirthdate() {
        return birthdate;
    }

    public User setBirthdate(Timestamp birthdate) {
        this.birthdate = birthdate;
        return this;
    }

    @Column(name = "GENDER")
    @Enumerated(EnumType.STRING)
    public Gender getGender() {
        return gender;
    }

    public User setGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    @Column(name = "DISPLAY_NAME")
    @Basic
    public String getDisplayName() {
        return displayName;
    }

    public User setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    @Column(name = "CREATION_DATE")
    @Basic
    public Timestamp getCreationDate() {
        return creationDate;
    }

    public User setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    @Column(name = "ACTIVATION_CODE")
    @Basic
    public String getActivationCode() {
        return activationCode;
    }

    public User setActivationCode(String activationCode) {
        this.activationCode = activationCode;
        return this;
    }

    @Column(name = "PASSWORD_CRYPT_MODE")
    @Enumerated(EnumType.STRING)
    public PasswordCryptMode getPasswordCryptMode() {
        return passwordCryptMode;
    }

    public User setPasswordCryptMode(PasswordCryptMode passwordCryptMode) {
        this.passwordCryptMode = passwordCryptMode;
        return this;
    }

    @Column(name = "EMAIL_VALIDATED")
    @Basic
    public boolean isEmailValidated() {
        return emailValidated;
    }

    public User setEmailValidated(boolean emailValidated) {
        this.emailValidated = emailValidated;
        return this;
    }

    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(name = "USER_FOLLOW",
            joinColumns = { @JoinColumn(name = "ID_FOLLOWER", nullable = false, updatable = false) },
            inverseJoinColumns = {@JoinColumn(name = "ID_FOLLOWED", nullable = false, updatable = false)}
    )
    @Fetch(FetchMode.SUBSELECT)
    public Set<User> getFollowings() {
        return followings;
    }

    public User setFollowings(Set<User> followings) {
        this.followings = followings;
        return this;
    }

    public User addFollowing(User user) {
        if (!followings.contains(user)) {
            followings.add(user);
        }
        return this;
    }

    public User removeFollowing(User user) {
        if (followings.contains(user)) {
            followings.remove(user);
        }
        return this;
    }

    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(name = "USER_FOLLOW",
            joinColumns = { @JoinColumn(name = "ID_FOLLOWED", nullable = false, updatable = false) },
            inverseJoinColumns = {@JoinColumn(name = "ID_FOLLOWER", nullable = false, updatable = false)}
    )
    @Fetch(FetchMode.SUBSELECT)
    public Set<User> getFollowers() {
        return followers;
    }

    public User setFollowers(Set<User> followers) {
        this.followers = followers;
        return this;
    }

    @Column(name = "PROFILE_PICTURE_MIMETYPE")
    @Basic
    public String getProfilePictureMimetype() {
        return profilePictureMimetype;
    }

    public User setProfilePictureMimetype(String profilePictureMimetype) {
        this.profilePictureMimetype = profilePictureMimetype;
        return this;
    }

    @Column(name = "BACKGROUND_PICTURE_MIMETYPE")
    @Basic
    public String getBackgroundPictureMimetype() {
        return backgroundPictureMimetype;
    }

    public User setBackgroundPictureMimetype(String backgroundPictureMimetype) {
        this.backgroundPictureMimetype = backgroundPictureMimetype;
        return this;
    }

    //Spring Security
    @Column(name = "ENABLED")
    public Boolean isEnabled() {
        return enabled;
    }

    public User setEnabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Column(name = "ACCOUNT_NON_EXPIRED")
    public Boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public User setAccountNonExpired(Boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
        return this;
    }

    @Column(name = "CREDENTIALS_NON_EXPIRED")
    public Boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public User setCredentialsNonExpired(Boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
        return this;
    }

    @Column(name = "ACCOUNT_NON_LOCKED")
    public Boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public User setAccountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
        return this;
    }

    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(name = "USER_ROLE",
            joinColumns = { @JoinColumn(name = "ID_USER", nullable = false, updatable = false) },
            inverseJoinColumns = {@JoinColumn(name = "ID_ROLE", nullable = false, updatable = false)}
    )
    @Fetch(FetchMode.SUBSELECT)
    public Set<Role> getRoles() {
        return roles;
    }

    public User setRoles(Set<Role> roles) {
        this.roles = roles;
        return this;
    }

    public User addRoles(Role role) {
        if (!roles.contains(role)) {
            roles.add(role);
            role.addUser(this);
        }
        return this;
    }

    @Transient
    @JsonIgnore
    public List<GrantedAuthority> getRolesAutorithies() {
        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
        if (this.roles != null && !this.roles.isEmpty())
            for (Role role : this.roles) {
                authList.add(new SimpleGrantedAuthority(role.getName()));
            }
        return authList;
    }

    //END Spring Security


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (nickname != null ? !nickname.equals(user.nickname) : user.nickname != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (nickname != null ? nickname.hashCode() : 0);
        return result;
    }
}
