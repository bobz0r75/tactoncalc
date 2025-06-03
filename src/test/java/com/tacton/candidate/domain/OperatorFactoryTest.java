package com.tacton.candidate.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class OperatorFactoryTest {

  @ParameterizedTest
  @CsvSource({
    "1, 2, 3",
    "3, 2.5, 5.5",
    "-3, -2, -5",
  })
  void testAdditionOperator(Double firstOperand, Double secondOperand, Double expectedResult) {
    var token = new Token(TokenType.OPERATOR, "+");
    assertThat(OperatorFactory.getOperator(token).apply(firstOperand, secondOperand))
        .isEqualTo(expectedResult);
  }

  @ParameterizedTest
  @CsvSource({
    "1, 2, -1",
    "3, 8, -5",
    "-3, -2, -1",
  })
  void testSubtractionOperator(Double firstOperand, Double secondOperand, Double expectedResult) {
    var token = new Token(TokenType.OPERATOR, "-");
    assertThat(OperatorFactory.getOperator(token).apply(firstOperand, secondOperand))
        .isEqualTo(expectedResult);
  }

  @ParameterizedTest
  @CsvSource({
    "1, 2, 2",
    "-3, 8, -24",
    "7, 3, 21",
  })
  void testMultiplicationOperator(
      Double firstOperand, Double secondOperand, Double expectedResult) {
    var token = new Token(TokenType.OPERATOR, "*");
    assertThat(OperatorFactory.getOperator(token).apply(firstOperand, secondOperand))
        .isEqualTo(expectedResult);
  }

  @ParameterizedTest
  @CsvSource({
    "2, 1, 2",
    "-24, -3, 8",
    "21, 7, 3",
  })
  void testDivisionOperator(Double firstOperand, Double secondOperand, Double expectedResult) {
    var token = new Token(TokenType.OPERATOR, "/");
    assertThat(OperatorFactory.getOperator(token).apply(firstOperand, secondOperand))
        .isEqualTo(expectedResult);
  }
}
