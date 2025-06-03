package com.tacton.candidate.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ValidationExceptionTest {

  @Test
  void testExceptionMessage() {
    var message = "Validation failed";
    var exception = new ValidationException(message);
    assertThat(exception.getMessage()).isEqualTo(message);
  }
}
