package com.supinfo.gabbler.server.utils;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EncryptionUtilTest {

    @Test public void should_encode_to_sha256(){
        assertThat(EncryptionUtil.encodeToSHA256("test")).isEqualTo("9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08");
    }

}
