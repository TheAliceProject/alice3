package org.alice.tweedle.unlinked;

import org.alice.tweedle.TweedlePrimitiveValue;
import org.alice.tweedle.TweedleTypes;
import org.alice.tweedle.ast.AdditionExpression;
import org.alice.tweedle.ast.ThisExpression;
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
}