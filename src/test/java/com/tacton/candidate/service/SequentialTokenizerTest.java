package com.tacton.candidate.service;

import static com.tacton.candidate.domain.TokenType.NUMBER;
import static com.tacton.candidate.domain.TokenType.OPERATOR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.tacton.candidate.domain.Token;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SequentialTokenizerTest {

  private final Tokenizer tokenizer = new SequentialTokenizer();

  @Test
  void testOneDigitNumber() {
    var input = "5";
    assertThat(tokenizer.tokenize(input))
        .isNotNull()
        .isEqualTo(List.of(new Token(NUMBER, input)));
  }

  @Test
  void testMultiDigitNumber() {
    var input = "513";
    assertThat(tokenizer.tokenize(input))
        .isNotNull()
        .isEqualTo(List.of(new Token(NUMBER, input)));
  }

  @Test
  void testAccidentallySeparatedNumbers() {
    var input = "13 677";
    assertThat(tokenizer.tokenize(input))
        .isNotNull()
        .isEqualTo(List.of(new Token(NUMBER, "13677")));
  }

  @ParameterizedTest
  @ValueSource(strings = {"780*", "780 *"})
  void testNumberAndOperator(String input) {
    assertThat(tokenizer.tokenize(input))
        .isNotNull()
        .isEqualTo(List.of(
            new Token(NUMBER, "780"),
            new Token(OPERATOR, "*")));
  }

  @ParameterizedTest
  @ValueSource(strings = {"130-74", "130 - 74"})
  void testNumbersAndOperator(String input) {
    assertThat(tokenizer.tokenize(input))
        .isNotNull()
        .isEqualTo(List.of(
            new Token(NUMBER, "130"),
            new Token(OPERATOR, "-"),
            new Token(NUMBER, "74")));
  }

  @ParameterizedTest
  @ValueSource(strings = {"880 * -9", "880*-9"})
  void testSignedNumbersAndOperator(String input) {
    assertThat(tokenizer.tokenize(input))
        .isNotNull()
        .isEqualTo(List.of(
            new Token(NUMBER, "880"),
            new Token(OPERATOR, "*"),
            new Token(NUMBER, "-9")));
  }

  @ParameterizedTest
  @ValueSource(strings = {"-775 - -9", "-775--9"})
  void testSignedNumberAtFirstPosition(String input) {
    assertThat(tokenizer.tokenize(input))
        .isNotNull()
        .isEqualTo(List.of(
            new Token(NUMBER, "-775"),
            new Token(OPERATOR, "-"),
            new Token(NUMBER, "-9")));
  }

  @ParameterizedTest
  @ValueSource(strings = {"-144 - +42", "-144-+42"})
  void testSignedNumberAtAnyPosition(String input) {
    assertThat(tokenizer.tokenize(input))
        .isNotNull()
        .isEqualTo(List.of(
            new Token(NUMBER, "-144"),
            new Token(OPERATOR, "-"),
            new Token(NUMBER, "+42")));
  }

  @ParameterizedTest
  @ValueSource(strings = {"@", "130-d", "130-#", "%130+3"})
  void testNumbersAndExtraChar(String input) {
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> tokenizer.tokenize(input))
        .withMessageContaining("Invalid character");
  }
}
