package com.github.nathandelane.math.rechnerj;

import com.github.nathandelane.math.rechnerj.operations.MathematicalOperation;

import java.util.*;

import static com.github.nathandelane.math.rechnerj.TokenMath.*;

public final class Evaluation {

  private Evaluation() { }

  private static final Set<OperatorComparisonPair> operatorPrecedenceMap;
  static {
    operatorPrecedenceMap = new HashSet<>();
    operatorPrecedenceMap.add(new OperatorComparisonPair("^", "+"));
    operatorPrecedenceMap.add(new OperatorComparisonPair("^", "-"));
    operatorPrecedenceMap.add(new OperatorComparisonPair("^", "*"));
    operatorPrecedenceMap.add(new OperatorComparisonPair("^", "/"));
    operatorPrecedenceMap.add(new OperatorComparisonPair("*", "+"));
    operatorPrecedenceMap.add(new OperatorComparisonPair("*", "-"));
    operatorPrecedenceMap.add(new OperatorComparisonPair("/", "+"));
    operatorPrecedenceMap.add(new OperatorComparisonPair("/", "-"));
  }

  private static final Map<String, MathematicalOperation> operationsMap;
  static {
    operationsMap = new LinkedHashMap<>();
    operationsMap.put("^", EXPONENTIATION);
    operationsMap.put("*", MULTIPLY_TWO_TOKENS);
    operationsMap.put("+", ADD_TWO_TOKENS);
    operationsMap.put("-", SUBTRACT_TWO_TOKENS);
    operationsMap.put("/", DIVIDE_TWO_TOKENS);
  }

  private static boolean operatorHasHigherPrecedence(final Token token, final Token compareTo) {
    return token.tokenType == TokenType.INFIX_OPERATOR
      && (operatorPrecedenceMap.contains(new OperatorComparisonPair(token.value, compareTo.value)));
  }

  private static boolean isNumberOrVariableTokenType(final TokenType tokenType) {
    return (
      tokenType == TokenType.NUMBER
      || tokenType == TokenType.VARIABLE
    );
  }

  private static boolean isOperatorOrFunctionTokenType(final TokenType tokenType) {
    return (
      tokenType == TokenType.INFIX_OPERATOR
      || tokenType == TokenType.FUNCTION
    );
  }

  public static Token sanitizeResult(final Token input) {
    final Token result;

    if (input.value.indexOf('.') > -1) {
      final String afterDecimal = "0" + input.value.substring(input.value.indexOf('.'));
      final Double doubleAfterDecimal = Double.parseDouble(afterDecimal);

      if (doubleAfterDecimal == 0.0) {
        final String wholeNumValue = input.value.substring(0, input.value.indexOf('.'));

        result = new Token(input.tokenType, wholeNumValue);
      }
      else {
        result = input;
      }
    }
    else {
      result = input;
    }

    return result;
  }

  public static List<Token> rpnOrderTokens(final List<Token> infixOrderTokens) {
    final List<Token> rpnOrderedList = new ArrayList<>();
    final Stack<Token> unusedTokens = new Stack<>();

    Token lastToken = null;

    for (final Token nextToken : infixOrderTokens) {
      if (isNumberOrVariableTokenType(nextToken.tokenType)) {
        rpnOrderedList.add(nextToken);
        lastToken = nextToken;
        continue;
      }
      if (
        nextToken.tokenType == TokenType.INFIX_OPERATOR
        || nextToken.tokenType == TokenType.FUNCTION
      ) {
        while (
          !unusedTokens.empty()
          && !operatorHasHigherPrecedence(nextToken, unusedTokens.peek())
          && unusedTokens.peek().tokenType != TokenType.OPEN_PERENTHESIS
        ) {
          rpnOrderedList.add(unusedTokens.pop());
        }

        unusedTokens.push(nextToken);
        lastToken = nextToken;
        continue;
      }
      if (nextToken.tokenType == TokenType.OPEN_PERENTHESIS) {
        if (
          lastToken != null
          && (
            lastToken.tokenType == TokenType.NUMBER
            || lastToken.tokenType == TokenType.CLOSE_PERENTHESIS
            || lastToken.tokenType == TokenType.VARIABLE
          )
        ) {
          final Token subToken = new Token(TokenType.INFIX_OPERATOR, "*");

          while (
            !unusedTokens.empty()
              && !operatorHasHigherPrecedence(subToken, unusedTokens.peek())
              && unusedTokens.peek().tokenType != TokenType.OPEN_PERENTHESIS
          ) {
            rpnOrderedList.add(unusedTokens.pop());
          }

          unusedTokens.push(subToken);
        }

        unusedTokens.push(nextToken);
        lastToken = nextToken;
        continue;
      }
      if (nextToken.tokenType == TokenType.CLOSE_PERENTHESIS) {
        while (
          !unusedTokens.empty()
          && unusedTokens.peek().tokenType != TokenType.OPEN_PERENTHESIS
        ) {
          rpnOrderedList.add(unusedTokens.pop());
        }

        unusedTokens.pop();
        lastToken = nextToken;
      }
    }

    while (!unusedTokens.empty()) {
      rpnOrderedList.add(unusedTokens.pop());
    }

    return rpnOrderedList;
  }

  public static Token evaluate(final List<Token> rpn) {
    final Stack<Token> unusedTokens = new Stack<>();

    for (final Token nextRpnToken : rpn) {
      if (isNumberOrVariableTokenType(nextRpnToken.tokenType)) {
        unusedTokens.push(nextRpnToken);
        continue;
      }
      if (isOperatorOrFunctionTokenType(nextRpnToken.tokenType)) {
        final String operatorValue = nextRpnToken.value;
        final MathematicalOperation mathematicalOperation = operationsMap.get(operatorValue);
        final int requiredNumberOfOperands = mathematicalOperation.getNumberOfOperands();
        final Token[] operands = new Token[requiredNumberOfOperands];

        for (int count = requiredNumberOfOperands; count > 0; count--) {
          operands[(count - 1)] = unusedTokens.pop();
        }

        Token intermediateResult = mathematicalOperation.evaluate(operands);

        unusedTokens.push(intermediateResult);
      }
    }

    final Token lastToken = unusedTokens.pop();
    final Token result = sanitizeResult(lastToken);

    return result;
  }

}
