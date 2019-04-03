package org.alice.tweedle.unlinked;

import org.alice.tweedle.TweedleClass;
import org.alice.tweedle.TweedleMethod;
import org.alice.tweedle.TweedleRequiredParameter;
import org.alice.tweedle.TweedleStatement;
import org.alice.tweedle.TweedleType;
import org.alice.tweedle.TweedleTypeReference;
import org.alice.tweedle.ast.ExpressionStatement;
import org.alice.tweedle.ast.LambdaExpression;
import org.alice.tweedle.ast.MethodCallExpression;
import org.alice.tweedle.ast.TweedleExpression;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TweedleLambdaTest {
  private TweedleClass tested;

  @Before
  public void parsedType() {
    String sourceWithLambdas = "class Scene extends SScene {\n  Scene() {\n    super();\n  }\n\n  void initializeEventListeners() {\n" + "    this.addSceneActivationListener(listener: (SceneActivationEvent event)-> {\n" + "      this.myFirstMethod();\n    });\n" + "    this.addArrowKeyPressListener(listener: (ArrowKeyEvent event)-> {\n" + "      this.camera.move(direction: event.getMoveDirection(movedirectionplane: MoveDirectionPlane.FORWARD_BACKWARD_LEFT_RIGHT), amount: 0.25);\n" + "    });\n  } \n }";
    tested = (TweedleClass) new TweedleUnlinkedParser().parseType(sourceWithLambdas);
  }

  @Test
  public void classWithMethodShouldHaveMethod() {
    assertFalse("The class should have a method.", tested.getMethods().isEmpty());
  }

  @Test
  public void methodShouldBeNamed() {
    TweedleMethod initListeners = tested.getMethods().get(0);
    assertEquals("The method should be named.", "initializeEventListeners", initListeners.getName());
  }

  @Test
  public void methodShouldHaveTwoStatements() {
    TweedleMethod initListeners = tested.getMethods().get(0);
    assertEquals("The method should have two statements.", 2, initListeners.getBody().size());
  }

  @Test
  public void methodShouldHaveNonNullStatements() {
    TweedleMethod initListeners = tested.getMethods().get(0);
    assertNotNull("The first method statement should not be null.", initListeners.getBody().get(0));
    assertNotNull("The second method statement should not be null.", initListeners.getBody().get(1));
  }

  @Test
  public void methodsFirstStatementShouldBeAnExpressionStatement() {
    TweedleMethod initListeners = tested.getMethods().get(0);
    final TweedleStatement statement = initListeners.getBody().get(0);
    assertTrue("The first method statement should be an ExpressionStatement.", statement instanceof ExpressionStatement);
  }

  @Test
  public void methodsFirstExpressionStatementShouldHoldAMethodCall() {
    TweedleMethod initListeners = tested.getMethods().get(0);
    final ExpressionStatement statement = (ExpressionStatement) initListeners.getBody().get(0);
    assertTrue("The expression statement should hold a MethodCall.", statement.getExpression() instanceof MethodCallExpression);
  }

  @Test
  public void methodsFirstMethodCallShouldBeAddSceneActivationListener() {
    TweedleMethod initListeners = tested.getMethods().get(0);
    final ExpressionStatement statement = (ExpressionStatement) initListeners.getBody().get(0);
    final MethodCallExpression methodCall = (MethodCallExpression) statement.getExpression();
    assertEquals("The first method statement should be calling addSceneActivationListener.", "addSceneActivationListener", methodCall.getMethodName());
  }

  @Test
  public void addSceneActivationListenerShouldHaveOneArg() {
    TweedleMethod initListeners = tested.getMethods().get(0);
    final ExpressionStatement statement = (ExpressionStatement) initListeners.getBody().get(0);
    final MethodCallExpression methodCall = (MethodCallExpression) statement.getExpression();
    assertNotNull("The addSceneActivationListener should have a listener arg.", methodCall.getArg("listener"));
  }

  @Test
  public void addSceneActivationListenerOneArgShouldBeLambda() {
    TweedleMethod initListeners = tested.getMethods().get(0);
    final ExpressionStatement statement = (ExpressionStatement) initListeners.getBody().get(0);
    final MethodCallExpression methodCall = (MethodCallExpression) statement.getExpression();
    assertTrue("The addSceneActivationListener should have a listener arg.", methodCall.getArg("listener") instanceof LambdaExpression);
  }

  @Test
  public void addSceneActivationListenerLambdaShouldHaveOneParameter() {
    TweedleMethod initListeners = tested.getMethods().get(0);
    final ExpressionStatement statement = (ExpressionStatement) initListeners.getBody().get(0);
    final MethodCallExpression methodCall = (MethodCallExpression) statement.getExpression();
    final LambdaExpression listener = (LambdaExpression) methodCall.getArg("listener");
    assertEquals("The lambda listener should have a parameter.", 1, listener.getParameters().size());
  }

  @Test
  public void addSceneActivationListenerLambdaParameterShouldHaveOneParameterNamedEvent() {
    TweedleMethod initListeners = tested.getMethods().get(0);
    final ExpressionStatement statement = (ExpressionStatement) initListeners.getBody().get(0);
    final MethodCallExpression methodCall = (MethodCallExpression) statement.getExpression();
    final LambdaExpression listener = (LambdaExpression) methodCall.getArg("listener");
    final TweedleRequiredParameter parameter = listener.getParameters().get(0);
    assertEquals("The lambda listener should have a parameter.", "event", parameter.getName());
  }

  @Test
  public void addSceneActivationListenerLambdaParameterShouldHaveOneParameterTypedReference() {
    TweedleMethod initListeners = tested.getMethods().get(0);
    final ExpressionStatement statement = (ExpressionStatement) initListeners.getBody().get(0);
    final MethodCallExpression methodCall = (MethodCallExpression) statement.getExpression();
    final LambdaExpression listener = (LambdaExpression) methodCall.getArg("listener");
    final TweedleRequiredParameter parameter = listener.getParameters().get(0);
    assertTrue("The lambda listener should have a parameter.", parameter.getType() instanceof TweedleTypeReference);
  }

  @Test
  public void addSceneActivationListenerLambdaParameterShouldHaveOneParameterTypedSceneActivationEvent() {
    TweedleMethod initListeners = tested.getMethods().get(0);
    final ExpressionStatement statement = (ExpressionStatement) initListeners.getBody().get(0);
    final MethodCallExpression methodCall = (MethodCallExpression) statement.getExpression();
    final LambdaExpression listener = (LambdaExpression) methodCall.getArg("listener");
    final TweedleRequiredParameter parameter = listener.getParameters().get(0);
    final TweedleTypeReference type = (TweedleTypeReference) parameter.getType();
    assertEquals("The lambda listener should have parameter typed SceneActivationEvent.", "SceneActivationEvent", type.getName());
  }

  @Test
  public void addSceneActivationListenerSecondLambdaParameterShouldHaveOneParameterTypedArrowKeyEvent() {
    TweedleMethod initListeners = tested.getMethods().get(0);
    final ExpressionStatement statement = (ExpressionStatement) initListeners.getBody().get(1);
    final MethodCallExpression methodCall = (MethodCallExpression) statement.getExpression();
    final LambdaExpression listener = (LambdaExpression) methodCall.getArg("listener");
    final TweedleRequiredParameter parameter = listener.getParameters().get(0);
    final TweedleTypeReference type = (TweedleTypeReference) parameter.getType();
    assertEquals("The lambda listener should have parameter typed ArrowKeyEvent.", "ArrowKeyEvent", type.getName());
  }

  @Test
  public void addSceneActivationListenerLambdaParameterShouldHaveOneStatement() {
    TweedleMethod initListeners = tested.getMethods().get(0);
    final ExpressionStatement statement = (ExpressionStatement) initListeners.getBody().get(0);
    final MethodCallExpression methodCall = (MethodCallExpression) statement.getExpression();
    final LambdaExpression listener = (LambdaExpression) methodCall.getArg("listener");
    assertEquals("The lambda listener should have parameter typed SceneActivationEvent.", 1, listener.getStatements().size());
  }

  @Test
  public void addSceneActivationListenerLambdaParameterShouldHaveOneNonNullStatement() {
    TweedleMethod initListeners = tested.getMethods().get(0);
    final ExpressionStatement statement = (ExpressionStatement) initListeners.getBody().get(0);
    final MethodCallExpression methodCall = (MethodCallExpression) statement.getExpression();
    final LambdaExpression listener = (LambdaExpression) methodCall.getArg("listener");
    assertNotNull("The lambda listener should have parameter typed SceneActivationEvent.", listener.getStatements().get(0));
  }

  @Test
  public void addSceneActivationListenerLambdaParameterShouldHaveOneExpressionStatement() {
    TweedleMethod initListeners = tested.getMethods().get(0);
    final ExpressionStatement statement = (ExpressionStatement) initListeners.getBody().get(0);
    final MethodCallExpression methodCall = (MethodCallExpression) statement.getExpression();
    final LambdaExpression listener = (LambdaExpression) methodCall.getArg("listener");
    assertTrue("The lambda listener should have parameter typed SceneActivationEvent.", listener.getStatements().get(0) instanceof ExpressionStatement);
  }

}
