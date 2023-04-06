package com.github.nathandelane.math.rechnerj;

import java.util.HashMap;
import java.util.Map;

public class VariableStore {

  private static VariableStore INSTANCE = new VariableStore();

  private final Map<String, Token> mapOfTokens;

  private VariableStore() {
    this.mapOfTokens = new HashMap<>();
  }

  public void setVariable(final String name, final Token token) {
    mapOfTokens.put(name, token);
  }

  public Token getVariable(final String name) {
    return mapOfTokens.get(name);
  }

  public static VariableStore get() {
    return INSTANCE;
  }

}
