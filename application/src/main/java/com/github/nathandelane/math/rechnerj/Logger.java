package com.github.nathandelane.math.rechnerj;

public final class Logger {

  private Logger() { }

  public static void logError(final String formatString, final Object ... values) {
    if (!formatString.endsWith("%n")) {
      System.err.format(formatString + "%n", values);
    } else {
      System.err.format(formatString, values);
    }
  }

  public static void logMessage(final String formatString, final Object ... values) {
    if (!formatString.endsWith("%n")) {
      System.out.format(formatString + "%n", values);
    } else {
      System.out.format(formatString, values);
    }
  }

}
