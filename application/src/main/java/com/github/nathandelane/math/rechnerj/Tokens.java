package com.github.nathandelane.math.rechnerj;

import java.util.*;

import static com.github.nathandelane.math.rechnerj.Logger.logError;

public final class Tokens {

  private Tokens() { }

  private static final char NULL_CHAR = '\0';

  private static Map<TokenCharType, TokenType> TOKEN_TYPES_MAP;
  static {
    TOKEN_TYPES_MAP = new HashMap<>();
    TOKEN_TYPES_MAP.put(TokenCharType.DIGIT, TokenType.NUMBER);
    TOKEN_TYPES_MAP.put(TokenCharType.OPERATOR, TokenType.INFIX_OPERATOR);
    TOKEN_TYPES_MAP.put(TokenCharType.OPEN_PERENTHESIS, TokenType.OPEN_PERENTHESIS);
    TOKEN_TYPES_MAP.put(TokenCharType.CLOSE_PERENTHESIS, TokenType.CLOSE_PERENTHESIS);
    TOKEN_TYPES_MAP.put(TokenCharType.ASSIGNMENT, TokenType.ASSIGNMENT);
    TOKEN_TYPES_MAP.put(TokenCharType.END_OF_STATEMENT, TokenType.END_OF_STATEMENT);
    TOKEN_TYPES_MAP.put(TokenCharType.NON_DIGIT, TokenType.VARIABLE);
  }

  private static final char[] whiteSpace = {
    '\t', /* horizontal tab */
    '\n', /* line feed */
    11, /* vertical tab */
    12, /* form feed */
    '\r', /* carriage return */
    ' ' /* space */
  };

  private static final char[] arithmeticOperators = { '*', '+', '-', '/', '^' };

  private static final char[] digits = { '.', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

  private static final char[] stringChars = "$ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_".toCharArray();

  public static boolean isNumeric(final char nextChar) {
    return Arrays.binarySearch(digits, nextChar) > -1;
  }

  public static boolean isDecimal(final char nextChar) {
    return '.' == nextChar;
  }

  public static boolean isOperator(final char nextChar) {
    return Arrays.binarySearch(arithmeticOperators, nextChar) > -1;
  }

  public static boolean isStringChar(final char nextChar) {
    return Arrays.binarySearch(stringChars, nextChar) > -1;
  }

  public static boolean isOpenPerenthesis(final char nextChar) {
    return nextChar == '(';
  }

  public static boolean isClosedPerenthesis(final char nextChar) {
    return nextChar == ')';
  }

  public static boolean isAssignmentOperator(final char nextChar) {
    return nextChar == '=';
  }

  public static boolean isEndOfStatementOperator(final char nextChar) {
    return nextChar == ';';
  }

  public static TokenCharType getTokenCharType(final char nextChar, final TokenCharType lastCharType) {
    final TokenCharType tokenCharType;

    if (
      (
        lastCharType == TokenCharType.NONE
        || lastCharType == TokenCharType.OPERATOR
        || lastCharType == TokenCharType.WHITESPACE
        || lastCharType == TokenCharType.OPEN_PERENTHESIS
      )
      && nextChar == '-'
    ) tokenCharType = TokenCharType.DIGIT;
    else if (isNumeric(nextChar)) tokenCharType = TokenCharType.DIGIT;
    else if (isOperator(nextChar)) tokenCharType = TokenCharType.OPERATOR;
    else if (isOpenPerenthesis(nextChar)) tokenCharType = TokenCharType.OPEN_PERENTHESIS;
    else if (isClosedPerenthesis(nextChar)) tokenCharType = TokenCharType.CLOSE_PERENTHESIS;
    else if (isAssignmentOperator(nextChar)) tokenCharType = TokenCharType.ASSIGNMENT;
    else if (isEndOfStatementOperator(nextChar)) tokenCharType = TokenCharType.END_OF_STATEMENT;
    else if (isStringChar(nextChar)) tokenCharType = TokenCharType.NON_DIGIT;
    else tokenCharType = TokenCharType.WHITESPACE;

    return tokenCharType;
  }

  public static Token createToken(final TokenCharType charType, final String value) {
    final TokenType tokenType = TOKEN_TYPES_MAP.get(charType);
    final Token token;

    if (tokenType == null) {
      token = new Token(TokenType.UNSUPPORTED, value);
    }
    else {
      token = new Token(tokenType, value);
    }

    return token;
  }

  public static List<Token> tokenizeString(final String input) {
    final List<Token> myTokens = new ArrayList<>();
    final char[] inputAsCharArray = input.toCharArray();

    StringBuilder tokenBuilder = new StringBuilder();
    TokenCharType lastCharType = TokenCharType.NONE;
    TokenCharType nextCharType = TokenCharType.NONE;

    int charIndex = 0;
    boolean hasDecimal = false;

    do {
      final char nextChar;

      if (charIndex < inputAsCharArray.length) {
        nextChar = inputAsCharArray[charIndex];
        nextCharType = getTokenCharType(nextChar, lastCharType);

        if (isDecimal(nextChar)) {
          if (!hasDecimal) {
            hasDecimal = true;
          } else {
            logError("Multiple decimals cannot be in the same number. Parsing halted.");
            return new ArrayList<>();
          }
        }
      }
      else {
        nextChar = NULL_CHAR;
      }

      if (nextChar == NULL_CHAR || (lastCharType != TokenCharType.NONE && nextCharType != lastCharType)) {
        final String value = tokenBuilder.toString().trim();
        final TokenCharType charType = lastCharType;
        final Token token = createToken(charType, value);

        if (!value.isEmpty()) {
          if (token.tokenType == TokenType.UNSUPPORTED) {
            logError("Unsupported token parsed: %s", token);
          } else {
            myTokens.add(token);
          }
        }

        tokenBuilder.setLength(0);
        hasDecimal = false;
      }

      tokenBuilder.append(nextChar);

      lastCharType = nextCharType;
      charIndex++;
    }  while (charIndex <= inputAsCharArray.length);

    return myTokens;
  }

}
