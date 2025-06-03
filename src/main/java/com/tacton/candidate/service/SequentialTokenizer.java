package com.tacton.candidate.service;

import static com.tacton.candidate.domain.TokenType.NUMBER;
import static com.tacton.candidate.domain.TokenType.OPERATOR;
import static java.util.function.Predicate.not;

import com.tacton.candidate.domain.Token;
import com.tacton.candidate.domain.TokenType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class SequentialTokenizer implements Tokenizer {

  private static final List<Character> NUMBERS =
      List.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
  private static final List<Character> SIGNS = List.of('+', '-');
  private static final List<Character> OTHER_OPERATORS = List.of('*', '/');
  private static final List<Character> WHITESPACES = List.of(' ', '\t');

  @Override
  public List<Token> tokenize(String input) {
    var result = new ArrayList<Token>();
    var chars = input.toCharArray();
    var bufferRef = new AtomicReference<TokenBuffer>();

    for (char currentChar : chars) {
      if (NUMBERS.contains(currentChar)) {
        addToBuffer(bufferRef, result, NUMBER, currentChar);
      } else if (SIGNS.contains(currentChar)) {
        addSignToBuffer(bufferRef, result, currentChar);
      } else if (OTHER_OPERATORS.contains(currentChar)) {
        addToBuffer(bufferRef, result, OPERATOR, currentChar);
      } else if (!WHITESPACES.contains(currentChar)) {
        throw new IllegalArgumentException("Invalid character: " + currentChar);
      }
    }
    flushBufferToResult(bufferRef, result);
    return result;
  }

  private void addSignToBuffer(
      AtomicReference<TokenBuffer> bufferRef, ArrayList<Token> result, char character) {
    Optional.ofNullable(bufferRef.get())
        .filter(buffer -> buffer.type() == NUMBER)
        .ifPresentOrElse(
            ignored -> addToExistingBuffer(bufferRef, result, OPERATOR, character),
            () -> addToBuffer(bufferRef, result, NUMBER, character));
  }

  private void addToBuffer(
      AtomicReference<TokenBuffer> bufferRef, List<Token> result, TokenType type, char character) {
    Optional.ofNullable(bufferRef.get())
        .ifPresentOrElse(
            buffer -> addToExistingBuffer(bufferRef, result, type, character),
            () -> {
              createBuffer(bufferRef, type);
              appendToBuffer(bufferRef, character);
            });
  }

  private void addToExistingBuffer(
      AtomicReference<TokenBuffer> bufferRef, List<Token> result, TokenType type, char character) {
    Optional.of(bufferRef.get())
        .filter(buffer -> buffer.type() == type)
        .ifPresentOrElse(
            buffer -> buffer.builder().append(character),
            () -> flushAndCreateNewBuffer(bufferRef, result, type, character));
  }

  private void flushAndCreateNewBuffer(
      AtomicReference<TokenBuffer> bufferRef, List<Token> result, TokenType type, char character) {
    flushBufferToResult(bufferRef, result);
    createBuffer(bufferRef, type);
    appendToBuffer(bufferRef, character);
  }

  private void flushBufferToResult(AtomicReference<TokenBuffer> bufferRef, List<Token> result) {
    Optional.of(bufferRef)
        .map(AtomicReference::get)
        .filter(not(buf -> buf.builder().isEmpty()))
        .map(this::createToken)
        .ifPresent(result::add);
  }

  private void appendToBuffer(AtomicReference<TokenBuffer> bufferRef, char character) {
    bufferRef.get().builder().append(character);
  }

  private void createBuffer(AtomicReference<TokenBuffer> bufferRef, TokenType type) {
    bufferRef.set(new TokenBuffer(type, new StringBuilder()));
  }

  private Token createToken(TokenBuffer buffer) {
    return new Token(buffer.type(), buffer.builder().toString());
  }

  private record TokenBuffer(TokenType type, StringBuilder builder) {}
}
