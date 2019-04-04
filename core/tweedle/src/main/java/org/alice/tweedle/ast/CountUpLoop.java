package org.alice.tweedle.ast;

import org.alice.tweedle.TweedleStatement;
import org.alice.tweedle.TweedleTypes;

import java.util.List;

public class CountUpLoop extends TweedleStatement {
  private final TweedleLocalVariable loopVariable;
  private final TweedleExpression limitExpression;
  private final List<TweedleStatement> statements;

  public CountUpLoop(String variableName, TweedleExpression limitExpression, List<TweedleStatement> statements) {
    this.loopVariable = new TweedleLocalVariable(TweedleTypes.WHOLE_NUMBER, variableName);
    this.limitExpression = limitExpression;
    this.statements = statements;
  }

  public List<TweedleStatement> getStatements() {
    return statements;
  }

  public TweedleExpression getLimitExpression() {
    return limitExpression;
  }

  public TweedleLocalVariable getLoopVariable() {
    return loopVariable;
  }
}
