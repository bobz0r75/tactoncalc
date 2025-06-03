package com.tacton.candidate.controller;

import static com.tacton.candidate.domain.TokenType.NUMBER;
import static com.tacton.candidate.domain.TokenType.OPERATOR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.tacton.candidate.domain.Token;
import com.tacton.candidate.exception.ValidationException;
import com.tacton.candidate.service.Calculator;
import com.tacton.candidate.service.Tokenizer;
import com.tacton.candidate.service.Validator;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DefaultCalculationControllerTest {

  @InjectMocks private DefaultCalculationController controller;

  @Mock private Tokenizer tokenizer;
  @Mock private Validator<List<Token>> validator;
  @Mock private Calculator calculator;

  @Test
  void testHappyControlFlow() {
    var input = "1+2";
    var expectedTokens =
        List.of(new Token(NUMBER, "1"), new Token(OPERATOR, "+"), new Token(NUMBER, "2"));
    when(tokenizer.tokenize(anyString())).thenReturn(expectedTokens);
    when(validator.validate(anyList())).thenAnswer(invocation -> invocation.getArgument(0));
    when(calculator.calculate(anyList())).thenReturn(3d);

    var result = controller.calculate(input);

    assertThat(result).isNotNull().isEqualTo(3d);
    verify(tokenizer).tokenize(input);
    verify(validator).validate(expectedTokens);
    verify(calculator).calculate(expectedTokens);
  }

  @Test
  void testInvalidInputString() {
    var input = "1+2";
    when(tokenizer.tokenize(anyString())).thenThrow(new IllegalArgumentException("Invalid input"));

    assertThatThrownBy(() -> controller.calculate(input))
        .isInstanceOf(IllegalArgumentException.class);
    verify(tokenizer).tokenize(input);
    verifyNoInteractions(validator);
    verifyNoInteractions(calculator);
  }

  @Test
  void testInvalidCalculationLogic() {
    var input = "1+2";
    var expectedTokens =
        List.of(new Token(NUMBER, "1"), new Token(OPERATOR, "+"), new Token(NUMBER, "2"));
    when(tokenizer.tokenize(anyString())).thenReturn(expectedTokens);
    when(validator.validate(anyList())).thenThrow(new ValidationException("Invalid logic."));

    assertThatThrownBy(() -> controller.calculate(input)).isInstanceOf(ValidationException.class);
    verify(tokenizer).tokenize(input);
    verify(validator).validate(expectedTokens);
    verifyNoInteractions(calculator);
  }
}
