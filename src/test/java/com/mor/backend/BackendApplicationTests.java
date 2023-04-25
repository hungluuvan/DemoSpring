package com.mor.backend;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class BackendApplicationTests {
    Calculator underTest = new Calculator();

    @Test
    void itShouldAddTwoNumber() {
        int a = 5;
        int b = 6;
        int result = underTest.add(a, b);
        int c = 11;
        assertThat(result).isEqualTo(c);
    }

    static class Calculator {
        int add(int a, int b) {
            return a + b;
        }
    }
}
