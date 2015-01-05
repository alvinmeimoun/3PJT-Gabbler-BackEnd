package com.supinfo.gabbler.server.repository;

import com.excilys.ebi.spring.dbunit.config.DBOperation;
import com.excilys.ebi.spring.dbunit.test.DataSet;
import com.excilys.ebi.spring.dbunit.test.DataSetTestExecutionListener;
import com.supinfo.gabbler.server.config.JPAConfig;
import com.supinfo.gabbler.server.entity.User;
import com.supinfo.gabbler.server.repository.specifications.UserSpecifications;
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
@DataSet(value = "/data/UserRepositoryDataset.xml", tearDownOperation = DBOperation.DELETE_ALL)
public class UserRepositoryTest {

    @Autowired UserRepository userRepository;

    @Test public void should_find_4_users(){
        List<User> lUser = userRepository.findAll();
        assertThat(lUser).isNotEmpty().hasSize(4);
    }

    @Test public void should_find_a_user_by_login(){
        User user = userRepository.findByNickname("alvinm93");
        assertThat(user).isNotNull();
    }

    @SuppressWarnings("unchecked")
    @Test public void should_find_a_user_by_email(){
        User user = (User) userRepository.findOne(UserSpecifications.userEqualEmail("chevre.asiatiquer@gabbler.com"));
        assertThat(user).isNotNull();
    }

    @Test public void should_find_2_users_by_email_or_nickname(){
        List<User> lUser = userRepository.findByEmailOrNickname("alvin.meimoun@gabbler.com", "foobar");
        assertThat(lUser).isNotNull().hasSize(2);
    }

}
