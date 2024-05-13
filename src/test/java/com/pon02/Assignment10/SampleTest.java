package com.pon02.Assignment10;

import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;

import static org.assertj.core.api.Assertions.assertThat;

public class SampleTest {
    @Test
    void CIが稼働しているか確認する()  {
        assertThat(1).isEqualTo(2);
    }
}
