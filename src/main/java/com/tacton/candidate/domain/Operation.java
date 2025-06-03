package com.tacton.candidate.domain;

import java.util.function.BiFunction;

public record Operation(
    double firstOperand, double secondOperand, BiFunction<Double, Double, Double> operator) {

  public double execute() {
    return operator.apply(firstOperand, secondOperand);
  }
}
