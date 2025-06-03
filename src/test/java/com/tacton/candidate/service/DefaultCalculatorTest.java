package com.tacton.candidate.service;

import static com.tacton.candidate.domain.TokenType.NUMBER;
import static com.tacton.candidate.domain.TokenType.OPERATOR;
import static org.assertj.core.api.Assertions.assertThat;

import com.tacton.candidate.domain.Token;
import java.util.List;
import org.junit.jupiter.api.Test;

class DefaultCalculatorTest {

  private final Calculator calculator = new DefaultCalculator();

  @Test
  void testSingleNumber() {
    var tokens = List.of(new Token(NUMBER, "99"));
    assertThat(calculator.calculate(tokens)).isEqualTo(99d);
  }

  @Test
  void testSingleOperation() {
    var tokens = List.of(
        new Token(NUMBER, "10"),
        new Token(OPERATOR, "*"),
        new Token(NUMBER, "3"));
    assertThat(calculator.calculate(tokens)).isEqualTo(30d);
  }

  @Test
  void testTwoOperations() {
    var tokens = List.of(
        new Token(NUMBER, "10"),
        new Token(OPERATOR, "*"),
        new Token(NUMBER, "3"),
        new Token(OPERATOR, "*"),
        new Token(NUMBER, "8"));
    assertThat(calculator.calculate(tokens)).isEqualTo(240d);
  }

  @Test
  void testTwoDifferentOperations() {
    var tokens = List.of(
        new Token(NUMBER, "10"),
        new Token(OPERATOR, "*"),
        new Token(NUMBER, "4"),
        new Token(OPERATOR, "+"),
        new Token(NUMBER, "13"));
    assertThat(calculator.calculate(tokens)).isEqualTo(53d);
  }

  @Test
  void testTwoDifferentOperationsWithPrecedence() {
    var tokens = List.of(
        new Token(NUMBER, "8"),
        new Token(OPERATOR, "+"),
        new Token(NUMBER, "3"),
        new Token(OPERATOR, "*"),
        new Token(NUMBER, "17"));
    assertThat(calculator.calculate(tokens)).isEqualTo(59d);
  }

  @Test
  void testMoreDifferentOperationsWithPrecedence() {
    var tokens = List.of(
        new Token(NUMBER, "23"),
        new Token(OPERATOR, "+"),
        new Token(NUMBER, "18"),
        new Token(OPERATOR, "/"),
        new Token(NUMBER, "6"),
        new Token(OPERATOR, "+"),
        new Token(NUMBER, "6"));
    assertThat(calculator.calculate(tokens)).isEqualTo(32d);
  }
}
