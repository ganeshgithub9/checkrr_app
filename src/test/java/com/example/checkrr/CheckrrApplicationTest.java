package com.example.checkrr;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;


class CheckrrApplicationTest {


        @Test
        void testMainMethod() {
            try (MockedStatic<SpringApplication> mockSpringApp = Mockito.mockStatic(SpringApplication.class)) {
                mockSpringApp.when(() -> SpringApplication.run(CheckrrApplication.class, new String[]{})).thenReturn(null);

                CheckrrApplication.main(new String[]{});

                mockSpringApp.verify(() -> SpringApplication.run(CheckrrApplication.class, new String[]{}), Mockito.times(1));
            }
        }
}
