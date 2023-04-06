package com.github.nathandelane.math.rechnerj.operations;

import com.github.nathandelane.math.rechnerj.Token;
import com.github.nathandelane.math.rechnerj.TokenType;
import com.github.nathandelane.math.rechnerj.exception.NotEnoughOperandsForOperationException;
import com.github.nathandelane.math.rechnerj.exception.RationalExponentsNotSupportedException;

import static com.github.nathandelane.math.rechnerj.Evaluation.sanitizeResult;
import static com.github.nathandelane.math.rechnerj.Numbers.isFloatingPoint;
import static java.lang.String.format;

public class Exponentiation implements MathematicalOperation {

  @Override
  public int getNumberOfOperands() {
    return 2;
  }

  @Override
  public Token evaluate(final Token ... operands) {
    if (operands.length != 2) throw new NotEnoughOperandsForOperationException(
      format("Exponentiation requires two operands, provided: {}", operands));

    final Token result;

    final Token base = operands[0];
    final Token exponent = operands[1];

    final String baseValue = base.value;
    final String exponentValue = exponent.value;

    if (isFloatingPoint(exponentValue)) throw new RationalExponentsNotSupportedException(
      format("Rational exponents not currently supported: %s", exponentValue));

    final long exponentiation = Long.parseLong(exponentValue);

    if (exponentiation == 0L) {
      result = new Token(TokenType.NUMBER, "1");
    }
    else if (isFloatingPoint(baseValue)) {
      final int indexOfDecimal = baseValue.indexOf('.');
      final String wholePartAsString = baseValue.substring(0, indexOfDecimal);
      final String fractionalPartAsString = baseValue.substring(indexOfDecimal + 1);
      final long wholePart = Long.parseLong(wholePartAsString);

      final int lengthOfDecimalPart = fractionalPartAsString.length();
      final long denominator = Math.round(Math.pow(10.0, (double) lengthOfDecimalPart));
      final long numerator = (wholePart * denominator);

      long numCalc = numerator;
      long denomCalc = denominator;

      for (int counter = 1; counter < exponentiation; counter++) {
        numCalc *= numerator;
        denomCalc *= denominator;
      }

      final Double intermediateResult = ((double) numCalc / (double) denomCalc);

      result = new Token(TokenType.NUMBER, intermediateResult.toString());
    }
    else {
      Long baseCalc = Long.parseLong(baseValue);
      boolean isNegative = exponentiation < 0;

      for (int counter = 1; counter < Math.abs(exponentiation); counter++) {
        baseCalc *= baseCalc;
      }

      Double floatBaseCalc = baseCalc.doubleValue();

      if (isNegative) {
        floatBaseCalc = (1.0 / floatBaseCalc);
      }

      result = new Token(TokenType.NUMBER, floatBaseCalc.toString());
    }

    return sanitizeResult(result);
  }

}
