package org.alice.tweedle.ast;

import org.alice.tweedle.TweedleValue;
import org.alice.tweedle.run.Frame;

import java.util.HashMap;
import java.util.Map;

public class MethodCallExpression extends MemberAccessExpression {

  private final String methodName;
  private final Map<String, TweedleExpression> arguments;

  public MethodCallExpression(TweedleExpression target, String methodName) {
    super(target);
    this.methodName = methodName;
    arguments = new HashMap<>();
  }

  public MethodCallExpression(TweedleExpression target, String methodName, Map<String, TweedleExpression> arguments) {
    super(target);
    this.methodName = methodName;
    this.arguments = arguments;
  }

  @Override
  public TweedleValue evaluate(Frame frame) {
    evaluateTarget(frame);
    // TODO invoke the method on the target.
    return null;
  }

  public String getMethodName() {
    return methodName;
  }

  public void addArgument(String argName, TweedleExpression argValue) {
    arguments.put(argName, argValue);
  }

  public TweedleExpression getArg(String argName) {
    return arguments.get(argName);
  }
}
