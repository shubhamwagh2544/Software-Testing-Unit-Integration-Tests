package com.springboot.tests;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class TestsApplicationTests {

	Calculator underTest = new Calculator();
	@Test
	void itShouldAddNumbers() {
		//Given
		int a = 10;
		int b = 20;

		//When
		int result = underTest.addNumber(a, b);

		//Then
		assertThat(result).isEqualTo(31);

	}

	static class Calculator {
		int addNumber(int a, int b) {
			int c = a + b;
			return c;
		}
	}

}
