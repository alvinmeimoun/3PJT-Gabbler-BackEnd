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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((id == null) ? 0 : id.hashCode());
        result = prime * result
                + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Role other = (Role) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
}
