package com.supinfo.gabbler.server.repository;

import com.excilys.ebi.spring.dbunit.config.DBOperation;
import com.excilys.ebi.spring.dbunit.test.DataSet;
import com.excilys.ebi.spring.dbunit.test.DataSetTestExecutionListener;
import com.supinfo.gabbler.server.config.JPAConfig;
import com.supinfo.gabbler.server.entity.Role;
import com.supinfo.gabbler.server.repository.specifications.RoleSpecifications;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JPAConfig.class}, loader = AnnotationConfigContextLoader.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DataSetTestExecutionListener.class })
@DataSet(value = "/data/RoleRepositoryDataset.xml", tearDownOperation = DBOperation.DELETE_ALL)
public class RoleRepositoryTest {

    @Autowired RoleRepository roleRepository;

    @Test public void should_find_2_roles(){
        List<Role> lRole = roleRepository.findAll();
        assertThat(lRole).isNotEmpty().hasSize(2);
    }

    @SuppressWarnings("unchecked")
    @Test public void should_find_a_role_by_login(){
        Role role = (Role) roleRepository.findOne(RoleSpecifications.roleEqualName("ROLE_USER"));
        assertThat(role).isNotNull();
    }

}
