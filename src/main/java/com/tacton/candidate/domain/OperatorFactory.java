package com.tacton.candidate.domain;

import static com.tacton.candidate.domain.TokenType.OPERATOR;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

public final class OperatorFactory {

  private static final Map<String, BiFunction<Double, Double, Double>> operatorMap =
      Map.of(
          "+", Double::sum,
          "-", (a, b) -> a - b,
          "*", (a, b) -> a * b,
          "/", (a, b) -> a / b);

  private OperatorFactory() {
    // Do nothing
  }

  public static BiFunction<Double, Double, Double> getOperator(Token operatorToken) {
    return Optional.of(operatorToken)
        .filter(token -> token.type().equals(OPERATOR))
        .map(Token::value)
        .map(operatorMap::get)
        .orElseThrow(() -> new IllegalArgumentException("Invalid token: " + operatorToken));
  }
}
