package com.tacton.candidate.service;

import static com.tacton.candidate.domain.TokenType.NUMBER;
import static com.tacton.candidate.domain.TokenType.OPERATOR;
import static java.util.stream.IntStream.range;

import com.tacton.candidate.domain.Token;
import com.tacton.candidate.domain.TokenType;
import com.tacton.candidate.exception.ValidationException;
import java.util.List;

public class TokenListValidator implements Validator<List<Token>> {

  @Override
  public List<Token> validate(List<Token> input) throws ValidationException {
    if (input == null || input.isEmpty()) {
      throw new ValidationException("Input is empty");
    }

    if (input.size() % 2 == 0) {
      throw new ValidationException("Input must contain an odd number of tokens");
    }

    range(0, input.size())
        .mapToObj(
            idx -> new TokenTypeValidation(idx, input.get(idx), idx % 2 == 0 ? NUMBER : OPERATOR))
        .forEach(TokenListValidator::validateTokenType);

    range(2, input.size())
        .filter(idx -> idx % 2 == 0)
        .mapToObj(idx -> new OperationValidation(input.get(idx - 1), input.get(idx)))
        .forEach(this::validateOperation);

    return input;
  }

  private void validateOperation(OperationValidation validation) {
    if (validation.operator().value().equals("/") && validation.operand.value().equals("0")) {
      throw new ValidationException("Division by zero!");
    }
  }

  private static void validateTokenType(TokenTypeValidation validation) {
    if (validation.token().type() != validation.expectedType()) {
      throw new ValidationException(
          "The type of token #%d must be %s"
              .formatted(validation.index(), validation.expectedType()));
    }
  }

  private record TokenTypeValidation(int index, Token token, TokenType expectedType) {}

  private record OperationValidation(Token operator, Token operand) {}
}
