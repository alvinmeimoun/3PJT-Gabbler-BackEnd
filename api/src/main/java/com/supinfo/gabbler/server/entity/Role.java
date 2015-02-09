package com.supinfo.gabbler.server.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Table(name = "ROLE")
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Role {
    private Integer id;
    private String name;

    private Set<User> users;

    public Role() {
        users = new HashSet<>();
    }

    public Role(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public Role setId(Integer id) {
        this.id = id;
        return this;
    }

    @ManyToMany(mappedBy = "roles")
    @JsonBackReference
    public Set<User> getUser() {
        return users;
    }

    public Role setUser(Set<User> users) {
        this.users = users;
        return this;
    }

    @Column(name = "NAME")
    @Basic
    public String getName() {
        return name;
    }

    public Role setName(String name) {
        this.name = name;
        return this;
    }



    public Role addUser(User user) {
        if (!users.contains(user)) {
            users.add(user);
            user.addRoles(this);
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role role = (Role) o;

        if (!name.equals(role.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
