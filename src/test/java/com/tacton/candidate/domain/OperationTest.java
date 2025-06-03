package com.tacton.candidate.domain;

import static java.lang.Double.isInfinite;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.BiFunction;
import org.junit.jupiter.api.Test;

class OperationTest {

  @Test
  void testHappyFlow() {
    double operand1 = 10.0;
    double operand2 = 5.0;
    BiFunction<Double, Double, Double> operator = Double::sum;

    var operation = new Operation(operand1, operand2, operator);
    assertEquals(15.0, operation.execute());
  }

  @Test
  void testDivisionByZero() {
    double operand1 = 10.0;
    BiFunction<Double, Double, Double> sum = (a, b) -> a / b;

    var operation = new Operation(operand1, 0.0, sum);
    assertTrue(isInfinite(operation.execute()));
  }
}
