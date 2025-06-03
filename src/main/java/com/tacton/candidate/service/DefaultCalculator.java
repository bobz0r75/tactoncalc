package com.tacton.candidate.service;

import static com.tacton.candidate.domain.OperatorFactory.getOperator;
import static com.tacton.candidate.domain.TokenType.NUMBER;
import static java.lang.Double.parseDouble;
import static java.util.stream.IntStream.range;

import com.tacton.candidate.domain.Operation;
import com.tacton.candidate.domain.Token;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class DefaultCalculator implements Calculator {

  public static final int OPERATOR_POSITION = 1;
  public static final int SECOND_OPERAND_POSITION = 2;
  public static final int NEXT_OPERATOR_POSITION = 3;
  public static final List<List<String>> PRECEDENCE_LIST =
      List.of(List.of("*", "/"), List.of("+", "-"));

  @Override
  public double calculate(List<Token> input) {
    var bufferRef = new AtomicReference<>(input);
    PRECEDENCE_LIST.forEach(operations -> calculate(bufferRef, operations, 0));
    return parseDouble(bufferRef.get().getFirst().value());
  }

  private void calculate(
      AtomicReference<List<Token>> bufferRef, List<String> operations, int startPos) {
    var newBuffer = new ArrayList<Token>();
    Optional.of(bufferRef)
        .map(AtomicReference::get)
        .map(List::size)
        .filter(size -> size > startPos + OPERATOR_POSITION)
        .ifPresent(size -> examineOperation(bufferRef, newBuffer, operations, startPos));
  }

  private void examineOperation(
      AtomicReference<List<Token>> bufferRef,
      ArrayList<Token> newBuffer,
      List<String> operations,
      int startPos) {
    var buffer = bufferRef.get();
    var firstOperand = buffer.get(startPos);
    var operator = buffer.get(startPos + OPERATOR_POSITION);
    var secondOperand = buffer.get(startPos + SECOND_OPERAND_POSITION);

    if (operations.contains(operator.value())) {
      range(0, startPos).forEach(idx -> newBuffer.add(buffer.get(idx)));
      newBuffer.add(getExecutedToken(firstOperand, secondOperand, operator));
      if (buffer.size() > startPos + SECOND_OPERAND_POSITION) {
        restartAfterExecution(bufferRef, newBuffer, operations, startPos);
      }
    } else {
      restartWithoutExecution(bufferRef, operations, startPos);
    }
  }

  private void restartAfterExecution(
      AtomicReference<List<Token>> bufferRef,
      ArrayList<Token> newBuffer,
      List<String> operations,
      int startPos) {
    Optional.of(bufferRef.get())
        .ifPresent(
            buffer -> {
              range(startPos + NEXT_OPERATOR_POSITION, buffer.size())
                  .forEach(idx -> newBuffer.add(buffer.get(idx)));
              bufferRef.set(newBuffer);
              calculate(bufferRef, operations, startPos);
            });
  }

  private void restartWithoutExecution(
      AtomicReference<List<Token>> bufferRef, List<String> operations, int startPos) {
    var nextStartPos = startPos + SECOND_OPERAND_POSITION;
    Optional.of(bufferRef.get())
        .map(List::size)
        .filter(size -> size >= nextStartPos)
        .ifPresent(size -> calculate(bufferRef, operations, nextStartPos));
  }

  private static Token getExecutedToken(
      Token firstOperand, Token secondOperand, Token currentOperator) {
    return new Token(
        NUMBER, Double.toString(executeOperation(firstOperand, secondOperand, currentOperator)));
  }

  private static double executeOperation(Token firstOperand, Token secondOperand, Token operator) {
    return new Operation(
            parseDouble(firstOperand.value()),
            parseDouble(secondOperand.value()),
            getOperator(operator))
        .execute();
  }
}
