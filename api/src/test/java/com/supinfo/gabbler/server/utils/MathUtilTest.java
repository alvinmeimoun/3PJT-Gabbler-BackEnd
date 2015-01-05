package com.supinfo.gabbler.server.utils;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MathUtilTest {

    @Test
    public void should_generate_random_number(){
        assertThat(MathUtil.randInt(1,15)).isGreaterThanOrEqualTo(1).isLessThanOrEqualTo(15);
    }

}
