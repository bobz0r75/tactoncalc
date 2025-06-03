package com.tacton.candidate.service;

import com.tacton.candidate.domain.Token;
import java.util.List;

public interface Tokenizer {

  /**
   * Creates a list of tokens out of a String value.
   *
   * @param input The String input.
   * @return The tokenized string.
   */
  List<Token> tokenize(String input);
}
