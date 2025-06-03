package com.tacton.candidate.service;

import static com.tacton.candidate.domain.TokenType.NUMBER;
import static com.tacton.candidate.domain.TokenType.OPERATOR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.tacton.candidate.domain.Token;
import com.tacton.candidate.exception.ValidationException;
import java.util.List;
import org.junit.jupiter.api.Test;

class TokenListValidatorTest {

  private final Validator<List<Token>> validator = new TokenListValidator();

  @Test
  void testNull() {
    assertThatThrownBy(() -> validator.validate(null)).isInstanceOf(ValidationException.class);
  }

  @Test
  void testEmptyList() {
    var input = List.<Token>of();
    assertThatThrownBy(() -> validator.validate(input)).isInstanceOf(ValidationException.class);
  }

  @Test
  void testOnlyOperationList() {
    var input = List.of(new Token(OPERATOR, "*"));
    assertThatThrownBy(() -> validator.validate(input)).isInstanceOf(ValidationException.class);
  }

  @Test
  void testEvenSizeList() {
    var input =
        List.of(
            new Token(NUMBER, "432"),
            new Token(OPERATOR, "-"),
            new Token(NUMBER, "111"),
            new Token(OPERATOR, "+"));
    assertThatThrownBy(() -> validator.validate(input)).isInstanceOf(ValidationException.class);
  }

  @Test
  void testUnorderedList() {
    var input =
        List.of(new Token(NUMBER, "755"), new Token(NUMBER, "87"), new Token(OPERATOR, "+"));
    assertThatThrownBy(() -> validator.validate(input)).isInstanceOf(ValidationException.class);
  }

  @Test
  void testDivisionByZero() {
    var input =
        List.of(
            new Token(NUMBER, "110"),
            new Token(OPERATOR, "-"),
            new Token(NUMBER, "13"),
            new Token(OPERATOR, "/"),
            new Token(NUMBER, "0"));
    assertThatThrownBy(() -> validator.validate(input)).isInstanceOf(ValidationException.class);
  }

  @Test
  void testOneElementList() throws ValidationException {
    var tokenList = List.of(new Token(NUMBER, "232"));
    assertThat(validator.validate(tokenList)).isEqualTo(tokenList);
  }

  @Test
  void testOneOperationList() throws ValidationException {
    var tokenList =
        List.of(new Token(NUMBER, "546"), new Token(OPERATOR, "-"), new Token(NUMBER, "17"));
    assertThat(validator.validate(tokenList)).isEqualTo(tokenList);
  }

  @Test
  void testMultipleOperationsList() throws ValidationException {
    var tokenList =
        List.of(
            new Token(NUMBER, "123"),
            new Token(OPERATOR, "-"),
            new Token(NUMBER, "45"),
            new Token(OPERATOR, "*"),
            new Token(NUMBER, "2"));
    assertThat(validator.validate(tokenList)).isEqualTo(tokenList);
  }
}
