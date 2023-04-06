package com.github.nathandelane.math.rechnerj.operations;

import com.github.nathandelane.math.rechnerj.Token;
import com.github.nathandelane.math.rechnerj.TokenType;
import com.github.nathandelane.math.rechnerj.exception.NotEnoughOperandsForOperationException;

import static com.github.nathandelane.math.rechnerj.Evaluation.sanitizeResult;
import static java.lang.String.format;

public class DivideTwoTokens implements MathematicalOperation {

  @Override
  public int getNumberOfOperands() {
    return 2;
  }

  @Override
  public Token evaluate(final Token ... operands) {
    if (operands.length != 2) throw new NotEnoughOperandsForOperationException(
      format("Division requires two operands, provided: {}", operands));

    final Token left = operands[0];
    final Token right = operands[1];
    final Double l = Double.parseDouble(left.value);
    final Double r = Double.parseDouble(right.value);

    if (r == 0.0) throw new ArithmeticException("Cannot divide by zero.");

    final Double sum = (l / r);

    final Token result = new Token(TokenType.NUMBER, sum.toString());

    return sanitizeResult(result);
  }

}
