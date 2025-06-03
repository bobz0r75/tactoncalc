package com.tacton.candidate.controller;

import static java.util.function.Predicate.not;

import com.tacton.candidate.domain.Token;
import com.tacton.candidate.service.Calculator;
import com.tacton.candidate.service.Tokenizer;
import com.tacton.candidate.service.Validator;
import java.util.List;
import java.util.Optional;

public class DefaultCalculationController implements CalculationController {

  private final Tokenizer tokenizer;
  private final Validator<List<Token>> validator;
  private final Calculator calculator;

  public DefaultCalculationController(
      Tokenizer tokenizer, Validator<List<Token>> validator, Calculator calculator) {
    this.tokenizer = tokenizer;
    this.validator = validator;
    this.calculator = calculator;
  }

  @Override
  public Double calculate(String input) {
    return Optional.ofNullable(input)
        .filter(not(String::isBlank))
        .map(tokenizer::tokenize)
        .map(validator::validate)
        .map(calculator::calculate)
        .orElseThrow(() -> new IllegalArgumentException("Input is blank."));
  }
}
