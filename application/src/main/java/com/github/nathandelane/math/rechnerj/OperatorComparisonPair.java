package com.github.nathandelane.math.rechnerj;

import java.util.Objects;

public class OperatorComparisonPair {

  public final String tokenValue;

  public final String compareToValue;

  public OperatorComparisonPair(final String tokenValue, final String compareToValue) {
    this.tokenValue = tokenValue;
    this.compareToValue = compareToValue;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    OperatorComparisonPair that = (OperatorComparisonPair) o;
    return Objects.equals(tokenValue, that.tokenValue) && Objects.equals(compareToValue, that.compareToValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tokenValue, compareToValue);
  }

  @Override
  public String toString() {
    return "OperatorComparisonPair{" +
      "tokenValue='" + tokenValue + '\'' +
      ", compareToValue='" + compareToValue + '\'' +
      '}';
  }

}
