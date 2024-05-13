package com.pon02.Assignment10;

import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;

import static org.assertj.core.api.Assertions.assertThat;

@Component
public class SampleTest {
    @Test
    public void CIが稼働しているか確認する() throws Exception {
        assertThat(1).isEqualTo(1);
    }
}
