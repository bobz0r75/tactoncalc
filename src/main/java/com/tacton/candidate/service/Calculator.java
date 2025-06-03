package com.tacton.candidate.service;

import com.tacton.candidate.domain.Token;
import java.util.List;

public interface Calculator {

  /**
   * Executes a sequence of mathematical operations based on a list of input tokens.
   *
   * @param input The sequence of input tokens.
   * @return The calculation result.
   */
  double calculate(List<Token> input);
}
