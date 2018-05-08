package org.alice.tweedle.unlinked;

import org.alice.tweedle.TweedlePrimitiveValue;
import org.alice.tweedle.TweedleTypes;
import org.alice.tweedle.ast.AdditionExpression;
import org.alice.tweedle.ast.AssignmentExpression;
import org.alice.tweedle.ast.FieldAccess;
import org.alice.tweedle.ast.IdentifierReference;
import org.alice.tweedle.ast.MethodCallExpression;
import org.alice.tweedle.ast.ThisExpression;
import org.alice.tweedle.ast.TweedleArrayInitializer;
import org.alice.tweedle.ast.TweedleExpression;
import org.junit.Test;

import static org.junit.Assert.*;

public class TweedleExpressionParseTest {

	private TweedleExpression parseExpression( String source) {
		return new TweedleUnlinkedParser().parseExpression( source );
	}

	@Test
	public void somethingShouldBeCreatedForParenthesizedNumber() {
		TweedleExpression tested = parseExpression( "(3)" );
		assertNotNull("The parser should have returned something.", tested );
	}

	@Test
	public void aNumberShouldBeCreatedForParenthesizedNumber() {
		TweedleExpression tested = parseExpression( "(3)" );
		assertTrue("The parser should have returned a TweedlePrimitiveValue.", tested instanceof TweedlePrimitiveValue);
	}

	@Test
	public void aNumberShouldBeCreatedForMultiplyParenthesizedNumber() {
		TweedleExpression tested = parseExpression( "(((3)))" );
		assertTrue("The parser should have returned aTweedlePrimitiveValue.", tested instanceof TweedlePrimitiveValue);
	}

	@Test
	public void somethingShouldBeCreatedForThis() {
		TweedleExpression tested = parseExpression( "this" );
		assertNotNull("The parser should have returned something.", tested );
	}

	@Test
	public void aThisExpressionShouldBeCreated() {
		TweedleExpression tested = parseExpression( "this" );
		assertTrue("The parser should have returned a ThisExpression.", tested instanceof ThisExpression );
	}

	@Test
	public void somethingShouldBeCreatedForAddition() {
		TweedleExpression tested = parseExpression( "3 + 4" );
		assertNotNull("The parser should have returned something.", tested );
	}

	@Test
	public void anAdditionExpressionShouldBeCreated() {
		TweedleExpression tested = parseExpression( "3 + 4" );
		assertTrue("The parser should have returned an AdditionExpression.", tested instanceof AdditionExpression );
	}

	@Test
	public void createdAdditionExpressionShouldEvaluate() {
		AdditionExpression tested = (AdditionExpression) parseExpression( "3 + 4" );
		assertNotNull("The AdditionExpression should evaluate to something.", tested.evaluate( null ));
	}

	@Test
	public void createdAdditionExpressionShouldEvaluateToAPrimitiveValue() {
		AdditionExpression tested = (AdditionExpression) parseExpression( "3 + 4" );
		assertTrue("The AdditionExpression should evaluate to a tweedle value.", tested.evaluate( null ) instanceof TweedlePrimitiveValue );
	}

	@Test
	public void createdAdditionExpressionShouldContainCorrectResult() {
		AdditionExpression tested = (AdditionExpression) parseExpression( "3 + 4" );
		assertEquals("The AdditionExpression should evaluate correctly.",
								 7,
								 ( (TweedlePrimitiveValue) tested.evaluate( null ) ).getPrimitiveValue() );
	}

	@Test
	public void wholeNumberAdditionShouldKnowItsType() {
		TweedleExpression tested = parseExpression( "3 + 4" );
		assertEquals( "The type should be WholeNumber", TweedleTypes.WHOLE_NUMBER, tested.getType() );
	}

	@Test
	public void decimalNumberMultiplicationShouldKnowItsType() {
		TweedleExpression tested = parseExpression( "3.4 + 4.1" );
		assertEquals( "The type should be WholeNumber", TweedleTypes.DECIMAL_NUMBER, tested.getType() );
	}

	@Test
	public void somethingShouldBeCreatedForIdentifier() {
		TweedleExpression tested = parseExpression( "x" );
		assertNotNull("The parser should have returned something.", tested );
	}

	@Test
	public void anIdentifierExpressionShouldBeCreated() {
		TweedleExpression tested = parseExpression( "x" );
		assertTrue("The parser should have returned an IdentifierReference.", tested instanceof IdentifierReference );
	}

	@Test
	public void anIdentifierExpressionShouldBeCreatedNamedX() {
		IdentifierReference tested = (IdentifierReference) parseExpression( "x" );
		assertEquals("The IdentifierReference should be named 'x'.", "x", tested.getName() );
	}

	@Test
	public void somethingShouldBeCreatedForAssignment() {
		TweedleExpression tested = parseExpression( "x <- 3" );
		assertNotNull("The parser should have returned something.", tested );
	}

	@Test
	public void aAssignmentExpressionShouldBeCreated() {
		TweedleExpression tested = parseExpression( "x <- 3" );
		assertTrue("The parser should have returned an AssignmentExpression.", tested instanceof AssignmentExpression );
	}

	@Test
	public void somethingShouldBeCreatedForFieldReadOnThis() {
		TweedleExpression tested = parseExpression( "this.myField" );
		assertNotNull("The parser should have returned something.", tested );
	}

	@Test
	public void aFieldAccessExpressionOnThisShouldBeCreated() {
		TweedleExpression tested = parseExpression( "this.myField" );
		assertTrue("The parser should have returned a FieldAccesss.", tested instanceof FieldAccess );
	}

	@Test
	public void aFieldAccessOnThisShouldHaveMyField() {
		FieldAccess tested = (FieldAccess) parseExpression( "this.myField" );
		assertEquals("The field name should have been \"myField\".", "myField", tested.getFieldName() );
	}

	@Test
	public void aFieldAccessOnThisShouldTargetByThisExpression() {
		FieldAccess tested = (FieldAccess) parseExpression( "this.myField" );
		assertTrue("The target should have been a ThisExpression.", tested.getTarget() instanceof ThisExpression);
	}

	@Test
	public void somethingShouldBeCreatedForFieldRead() {
		TweedleExpression tested = parseExpression( "x.myField" );
		assertNotNull("The parser should have returned something.", tested );
	}

	@Test
	public void aFieldAccessExpressionShouldBeCreated() {
		TweedleExpression tested = parseExpression( "x.myField" );
		assertTrue("The parser should have returned a FieldAccesss.", tested instanceof FieldAccess );
	}

	@Test
	public void aFieldAccessShouldHaveMyField() {
		FieldAccess tested = (FieldAccess) parseExpression( "x.myField" );
		assertEquals("The field name should have been \"myField\".", "myField", tested.getFieldName() );
	}

	@Test
	public void aFieldAccessShouldTargetByIdentifier() {
		FieldAccess tested = (FieldAccess) parseExpression( "x.myField" );
		assertTrue("The target should have been an IdentifierReference.", tested.getTarget() instanceof IdentifierReference);
	}

	@Test
	public void aFieldAccessTargetShouldBeCreatedNamedX() {
		FieldAccess fa = (FieldAccess) parseExpression( "x.myField" );
		IdentifierReference tested = (IdentifierReference) fa.getTarget();
		assertEquals( "The IdentifierReference should be named 'x'.", "x", tested.getName() );
	}

	@Test
	public void somethingShouldBeCreatedForMethodCall() {
		TweedleExpression tested = parseExpression( "x.myMethod()" );
		assertNotNull("The parser should have returned something.", tested );
	}

	@Test
	public void aMethodCallExpressionShouldBeCreated() {
		TweedleExpression tested = parseExpression( "x.myMethod()" );
		assertTrue("The parser should have returned a MethodCallExpression.", tested instanceof MethodCallExpression );
	}

	@Test
	public void aMethodCallExpressionShouldHaveMyMethod() {
		MethodCallExpression tested = (MethodCallExpression) parseExpression( "x.myMethod()" );
		assertEquals("The method name should have been \"myMethod\".", "myMethod", tested.getMethodName() );
	}

	@Test
	public void aMethodCallShouldTargetByIdentifier() {
		MethodCallExpression tested = (MethodCallExpression) parseExpression( "x.myMethod()" );
		assertTrue("The target should have been an IdentifierReference.", tested.getTarget() instanceof IdentifierReference);
	}

	@Test
	public void aMethodCallTargetShouldBeCreatedNamedX() {
		MethodCallExpression fa = (MethodCallExpression) parseExpression( "x.myMethod()" );
		IdentifierReference tested = (IdentifierReference) fa.getTarget();
		assertEquals( "The IdentifierReference should be named 'x'.", "x", tested.getName() );
	}

	@Test
	public void somethingShouldBeCreatedForMethodCallOnThis() {
		TweedleExpression tested = parseExpression( "this.myMethod()" );
		assertNotNull("The parser should have returned something.", tested );
	}

	@Test
	public void aMethodCallExpressionOnThisShouldBeCreated() {
		TweedleExpression tested = parseExpression( "this.myMethod()" );
		assertTrue("The parser should have returned a MethodCallExpression.", tested instanceof MethodCallExpression );
	}

	@Test
	public void aMethodCallExpressionOnThisShouldHaveMyMethod() {
		MethodCallExpression tested = (MethodCallExpression) parseExpression( "this.myMethod()" );
		assertEquals("The method name should have been \"myMethod\".", "myMethod", tested.getMethodName() );
	}

	@Test
	public void aMethodCallShouldTargetThisExpression() {
		MethodCallExpression tested = (MethodCallExpression) parseExpression( "this.myMethod()" );
		assertTrue("The target should have been a ThisExpression.", tested.getTarget() instanceof ThisExpression);
	}

	@Test
	public void somethingShouldBeCreatedForPrimitiveArray() {
		TweedleExpression tested = parseExpression( "new WholeNumber[] {3, 4, 5}" );
		assertNotNull("The parser should have returned something.", tested );
	}

	@Test
	public void aPrimitiveArrayInitializerShouldBeCreated() {
		TweedleExpression tested = parseExpression( "new WholeNumber[] {3, 4, 5}" );
		assertTrue("The parser should have returned a TweedleArrayInitializer.", tested instanceof TweedleArrayInitializer );
	}

	@Test
	public void somethingShouldBeCreatedForObjectArray() {
		TweedleExpression tested = parseExpression( "new SModel[] {this.sphere, this.walrus}" );
		assertNotNull("The parser should have returned something.", tested );
	}

	@Test
	public void anObjectArrayInitializerShouldBeCreated() {
		TweedleExpression tested = parseExpression( "new SModel[] {this.sphere, this.walrus}" );
		assertTrue("The parser should have returned a TweedleArrayInitializer.", tested instanceof TweedleArrayInitializer );
	}
}