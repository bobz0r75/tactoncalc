package com.tacton.candidate.functional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.tacton.candidate.controller.CalculationController;
import com.tacton.candidate.controller.DefaultCalculationController;
import com.tacton.candidate.exception.ValidationException;
import com.tacton.candidate.service.DefaultCalculator;
import com.tacton.candidate.service.SequentialTokenizer;
import com.tacton.candidate.service.TokenListValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CalculatorFunctionalTest {

  private CalculationController calculatorEntryPoint;

  @BeforeEach
  void setUp() {
    // e.g., Google Guice could be used to manage dependency injection
    calculatorEntryPoint =
        new DefaultCalculationController(
            new SequentialTokenizer(), new TokenListValidator(), new DefaultCalculator());
  }

  @ParameterizedTest
  @CsvSource({
    "6, 6",
    "6 + 3, 9",
    "8 - 11, -3",
    "-3 * +2, -6",
    "12 340 - 10 520, 1820",
    "13 + 6 - 4, 15",
    "2 * -3 * 5 / 10, -3",
    "3 + 4 * 12 / 8 + 12 - 1, 20"
  })
  void testCalculator(String inputString, Double expectedResult) {
    assertThat(calculatorEntryPoint.calculate(inputString)).isEqualTo(expectedResult);
  }

  @Test
  void testSyntacticallyInvalidInput() {
    assertThatThrownBy(() -> calculatorEntryPoint.calculate("13 + 2b"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Invalid character");
  }

  @Test
  void testLogicallyInvalidInput() {
    assertThatThrownBy(() -> calculatorEntryPoint.calculate("45 + 3 *"))
        .isInstanceOf(ValidationException.class)
        .hasMessageContaining("odd number of tokens");
  }
}
