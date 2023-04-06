package com.github.nathandelane.math.rechnerj;

import com.github.nathandelane.math.rechnerj.operations.*;

public final class TokenMath {

  private TokenMath() { }

  public static final MathematicalOperation ADD_TWO_TOKENS = new AddTwoTokens();

  public static final MathematicalOperation SUBTRACT_TWO_TOKENS = new SubtractTwoTokens();

  public static final MathematicalOperation MULTIPLY_TWO_TOKENS = new MultiplyTwoTokens();

  public static final MathematicalOperation DIVIDE_TWO_TOKENS = new DivideTwoTokens();

  public static final MathematicalOperation EXPONENTIATION = new Exponentiation();

}
