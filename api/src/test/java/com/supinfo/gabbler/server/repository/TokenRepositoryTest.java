package com.supinfo.gabbler.server.repository;

import com.excilys.ebi.spring.dbunit.config.DBOperation;
import com.excilys.ebi.spring.dbunit.test.DataSet;
import com.excilys.ebi.spring.dbunit.test.DataSetTestExecutionListener;
import com.supinfo.gabbler.server.config.JPAConfig;
import com.supinfo.gabbler.server.entity.Token;
import com.supinfo.gabbler.server.repository.specifications.TokenSpecifications;
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
@DataSet(value = "/data/TokenRepositoryDataset.xml", tearDownOperation = DBOperation.DELETE_ALL)
public class TokenRepositoryTest {

    @Autowired TokenRepository tokenRepository;

    @SuppressWarnings("unchecked")
    @Test public void should_find_2_token_for_username(){
        List<Token> lToken = tokenRepository.findAll(TokenSpecifications.tokenEqualUsername("user"));
        assertThat(lToken).isNotNull().hasSize(2);
    }

}
