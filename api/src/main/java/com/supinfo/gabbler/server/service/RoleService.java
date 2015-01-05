package com.supinfo.gabbler.server.service;

import com.supinfo.gabbler.server.entity.Role;
import com.supinfo.gabbler.server.repository.RoleRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.supinfo.gabbler.server.repository.specifications.RoleSpecifications.roleEqualName;

@Service
public class RoleService {

    @Resource
    RoleRepository roleRepository;

    public Role findOne(Integer integer) {
        return roleRepository.findOne(integer);
    }

    public boolean exists(Integer integer) {
        return roleRepository.exists(integer);
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Set<Role> findAll(Iterable<Integer> integers) {
        return new HashSet<>(roleRepository.findAll(integers));
    }

    public Role findOrCreateUserRole(){
        Role userRole = findByName("ROLE_USER");
        if(userRole == null){
            userRole = new Role().setName("ROLE_USER");
            userRole = roleRepository.save(userRole);
        }

        return userRole;
    }

    /* Repository layer */

    @SuppressWarnings("unchecked")
    public Role findByName(String name){
        return (Role) roleRepository.findOne(roleEqualName(name));
    }
}
