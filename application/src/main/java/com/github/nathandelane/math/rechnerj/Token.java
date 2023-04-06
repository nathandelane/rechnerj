package com.github.nathandelane.math.rechnerj;

public class Token {

  public final TokenType tokenType;

  public final String value;

  public Token(final TokenType tokenType, final String value) {
    this.tokenType = tokenType;
    this.value = value;
  }

  @Override
  public String toString() {
    return "Token{" +
      "tokenType=" + tokenType +
      ", value='" + value + '\'' +
      '}';
  }
}
