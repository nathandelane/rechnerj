package com.github.nathandelane.math.rechnerj;

public final class Numbers {

  private Numbers() { }

  public static boolean isFloatingPoint(final String tokenValue) {
    return tokenValue != null && tokenValue.indexOf('.') > -1;
  }

}
