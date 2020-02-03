package org.alice.tweedle.ast;

import org.alice.tweedle.TweedleStatement;

import java.util.List;

public class WhileLoop extends TweedleStatement {
  private final TweedleExpression runCondition;
  private final List<TweedleStatement> statements;

  public WhileLoop(TweedleExpression runCondition, List<TweedleStatement> statements) {
    this.runCondition = runCondition;
    this.statements = statements;
  }

  public TweedleExpression getRunCondition() {
    return runCondition;
  }

  public List<TweedleStatement> getStatements() {
    return statements;
  }
}
