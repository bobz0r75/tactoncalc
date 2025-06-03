package com.tacton.candidate.service;

import com.tacton.candidate.exception.ValidationException;

public interface Validator<T> {

  /**
   * Validates a type and returns it if it's valid.
   *
   * @param input The list of tokens.
   * @return The original, validated list of tokens.
   * @throws ValidationException If the object is not valid.
   */
  T validate(T input) throws ValidationException;
}
