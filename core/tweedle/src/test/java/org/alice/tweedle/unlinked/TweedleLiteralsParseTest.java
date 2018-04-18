package org.alice.tweedle.unlinked;

import org.alice.tweedle.TweedleNull;
import org.alice.tweedle.TweedlePrimitiveValue;
import org.alice.tweedle.ast.TweedleExpression;
import org.junit.Test;

import static org.junit.Assert.*;

public class TweedleLiteralsParseTest {

	private TweedleExpression parseExpression( String source) {
		return new TweedleUnlinkedParser().parseExpression( source );
	}

	@Test
	public void somethingShouldBeCreatedForNullLiteral() {
		TweedleExpression tested = parseExpression( "null" );
		assertNotNull("The parser should have returned something.", tested );
	}

	@Test
	public void aTweedleNullShouldBeReturnedForNullLiteral() {
		TweedleExpression tested = parseExpression( "null" );
		assertTrue("The parser should have returned a TweedleNull.", tested instanceof TweedleNull );
	}

	@Test
	public void theNullLiteralShouldParseToTheNullSingleton() {
		TweedleExpression tested = parseExpression( "null" );
		assertSame( "The literal should be the single null object.", TweedleNull.NULL, tested );
	}

	@Test
	public void somethingShouldBeCreatedForBooleanLiteral() {
		TweedleExpression tested = parseExpression( "true" );
		assertNotNull("The parser should have returned something.", tested );
	}

	@Test
	public void aPrimitiveValueShouldBeCreatedForBooleanLiteral() {
		TweedleExpression tested = parseExpression( "true" );
		assertTrue("The parser should have returned a TweedlePrimitiveValue.", tested instanceof TweedlePrimitiveValue );
	}

	@Test
	public void theValueInTheLiteralCreatedForBooleanLiteralTrueShouldMatch() {
		TweedlePrimitiveValue tested = (TweedlePrimitiveValue) parseExpression( "true" );
		assertEquals( "The literal should hold the value true.", true, tested.getPrimitiveValue());
	}

	@Test
	public void theValueInTheLiteralCreatedForBooleanLiteralFalseShouldMatch() {
		TweedlePrimitiveValue tested = (TweedlePrimitiveValue) parseExpression( "false" );
		assertEquals( "The literal should hold the value false.", false, tested.getPrimitiveValue());
	}

	@Test
	public void somethingShouldBeCreatedForStringLiteral() {
		TweedleExpression tested = parseExpression( "\"hello\"" );
		assertNotNull("The parser should have returned something.", tested );
	}

	@Test
	public void aPrimitiveValueShouldBeCreatedForStringLiteral() {
		TweedleExpression tested = parseExpression( "\"hello\"" );
		assertTrue("The parser should have returned a TweedlePrimitiveValue.", tested instanceof TweedlePrimitiveValue );
	}

	@Test
	public void theValueInTheLiteralCreatedForStringLiteralShouldMatch() {
		TweedlePrimitiveValue tested = (TweedlePrimitiveValue) parseExpression( "\"hello\"" );
		assertEquals( "The literal should hold the value.", "hello", tested.getPrimitiveValue());
	}

	@Test
	public void stringLiteralShouldAllowEscapes() {
		TweedlePrimitiveValue tested = (TweedlePrimitiveValue) parseExpression( "\"hello \\\"someone\\\"\"" );
		assertEquals( "The literal should hold the value.", "hello \\\"someone\\\"", tested.getPrimitiveValue());
	}

	@Test
	public void somethingShouldBeCreatedForDecimalLiteral() {
		TweedleExpression tested = parseExpression( "4.731" );
		assertNotNull("The parser should have returned something.", tested );
	}

	@Test
	public void aPrimitiveValueShouldBeCreatedForDecimalLiteral() {
		TweedleExpression tested = parseExpression( "4.731" );
		assertTrue("The parser should have returned a TweedlePrimitiveValue.", tested instanceof TweedlePrimitiveValue );
	}

	@Test
	public void aPrimitiveValueShouldBeInTheLiteralCreatedForDecimalLiteral() {
		TweedlePrimitiveValue tested = (TweedlePrimitiveValue) parseExpression( "4.731" );
		assertTrue("The parser should have returned a TweedlePrimitiveValue.", tested.evaluate( null ) instanceof TweedlePrimitiveValue);
	}

	@Test
	public void theValueInTheLiteralCreatedForDecimalLiteralShouldMatch() {
		TweedlePrimitiveValue tested = (TweedlePrimitiveValue) parseExpression( "4.731" );

		assertEquals( "The literal should hold the value.", 4.731, tested.getPrimitiveValue());
	}

	@Test
	public void somethingShouldBeCreatedForNumberLiteral() {
		TweedleExpression tested = parseExpression( "4" );
		assertNotNull("The parser should have returned something.", tested );
	}

	@Test
	public void aPrimitiveValueShouldBeCreatedForNumberLiteral() {
		TweedleExpression tested = parseExpression( "4" );
		assertTrue("The parser should have returned a TweedlePrimitiveValue.", tested instanceof TweedlePrimitiveValue );
	}

	@Test
	public void aPrimitiveValueShouldBeInTheLiteralCreatedForNumberLiteral() {
		TweedlePrimitiveValue tested = (TweedlePrimitiveValue) parseExpression( "4" );
		assertTrue("The parser should have returned a TweedlePrimitiveValue.", tested.evaluate( null ) instanceof TweedlePrimitiveValue);
	}

	@Test
	public void theValueInTheLiteralCreatedForNumberLiteralShouldMatch() {
		TweedlePrimitiveValue tested = (TweedlePrimitiveValue) parseExpression( "4" );
		final TweedlePrimitiveValue tweedleValue = (TweedlePrimitiveValue) tested.evaluate( null );
		assertEquals( "The literal should hold the value.", 4, tweedleValue.getPrimitiveValue());
	}

	@Test
	public void somethingShouldBeCreatedForNegativeNumberLiteral() {
		TweedleExpression tested = parseExpression( "-4" );
		assertNotNull("The parser should have returned something.", tested );
	}

	@Test
	public void aPrimitiveValueShouldBeCreatedForNegativeNumberLiteral() {
		TweedleExpression tested = parseExpression( "-4" );
		assertTrue("The parser should have returned a TweedlePrimitiveValue.", tested instanceof TweedlePrimitiveValue );
	}
}