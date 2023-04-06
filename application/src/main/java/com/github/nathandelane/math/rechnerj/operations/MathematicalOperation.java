package com.github.nathandelane.math.rechnerj.operations;

import com.github.nathandelane.math.rechnerj.Token;

public interface MathematicalOperation {

  int getNumberOfOperands();

  Token evaluate(final Token... operands);

}
