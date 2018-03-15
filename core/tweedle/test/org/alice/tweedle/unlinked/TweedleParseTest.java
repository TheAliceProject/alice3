package org.alice.tweedle.unlinked;

import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.junit.Test;

import static org.junit.Assert.*;

public class TweedleParseTest {

	private UnlinkedType parseString(String source) {
		TweedleUnlinkedParser t = new TweedleUnlinkedParser();
		return t.parse( source );
	}


	@Test
	public void somethingShouldBeCreatedForARootClass() {
		UnlinkedType tested = parseString( "class SThing {}" );

		assertNotNull("The parser should have returned something.", tested );
	}

	@Test
	public void aRootClassShouldBeCreated() {
		UnlinkedType tested = parseString( "class SThing {}" );

		assertTrue("The parser should have returned an UnlinkedClass.", tested instanceof UnlinkedClass);
	}

	@Test
	public void classShouldKnowItsName() {
		UnlinkedType tested = parseString( "class SThing {}" );

		assertEquals( "The name should be 'SThing'", "SThing", tested.getName() );
	}

	@Test(expected=NullPointerException.class)
	public void subclassOfBooleanPrimitiveShouldFail() {
		parseString( "class SScene extends Boolean {}" );
	}

	@Test(expected=NullPointerException.class)
	public void subclassOfDecimalPrimitiveShouldFail() {
		parseString( "class SScene extends DecimalNumber {}" );
	}

	@Test(expected=NullPointerException.class)
	public void subclassOfWholePrimitiveShouldFail() {
		parseString( "class SScene extends WholeNumber {}" );
	}

	@Test(expected=NullPointerException.class)
	public void subclassOfNumberPrimitiveShouldFail() {
		parseString( "class SScene extends Number {}" );
	}

	@Test(expected=NullPointerException.class)
	public void subclassOfStringPrimitiveShouldFail() {
		parseString( "class SScene extends String {}" );
	}

	@Test(expected=ParseCancellationException.class)
	public void enumNamedSameAsBooleanPrimitiveShouldFail() {
		parseString( "enum Boolean {TRUE, FALSE}" );
	}

	@Test(expected=ParseCancellationException.class)
	public void classNamedSameAsBooleanPrimitiveShouldFail() {
		parseString( "class Boolean {}" );
	}

	@Test
	public void somethingShouldBeCreatedForASubclass() {
		UnlinkedType tested = parseString( "class SScene extends SThing {}" );

		assertNotNull("The parser should have returned something.", tested );
	}

	@Test
	public void aSubclassShouldBeCreated() {
		UnlinkedType tested = parseString( "class SScene extends SThing {}" );

		assertTrue("The parser should have returned an UnlinkedClass.", tested instanceof UnlinkedClass);
	}

	@Test
	public void classNameShouldBeReturnedOnSubclass() {
		UnlinkedType tested = parseString( "class SScene extends SThing {}" );

		assertEquals("The class name should have been SScene.",
								 "SScene",
									tested.getName() );
	}

	@Test
	public void superclassNameShouldBeReturnedOnSubclass() {
		UnlinkedClass sScene = (UnlinkedClass) parseString( "class SScene extends SThing {}" );

		assertEquals("The class SScene should have a superclass SThing.",
						     "SThing",
						      sScene.getSuperclassName() );
	}

	@Test
	public void somethingShouldBeCreatedForAnEnum() {
		UnlinkedType tested = parseString( "enum Direction {UP, DOWN}" );

		assertNotNull("The parser should have returned something.", tested );
	}

	@Test
	public void anEnumShouldBeCreated() {
		UnlinkedType tested = parseString( "enum Direction {UP, DOWN}" );

		assertTrue("The parser should have returned an UnlinkedEnum.", tested instanceof UnlinkedEnum);
	}

	@Test
	public void nameShouldBeReturnedOnEnum() {
		UnlinkedType tested = parseString( "enum Direction {UP, DOWN}" );

		assertEquals("The enum name should have been Direction.",
						"Direction",
						tested.getName() );
	}

	@Test
	public void enumShouldIncludeTwoValues() {
		UnlinkedEnum directionEnum = (UnlinkedEnum) parseString( "enum Direction {UP, DOWN}" );

		assertEquals("The enum Direction should have two values.",
						2,
						directionEnum.getValues().size() );
	}

	@Test
	public void enumShouldIncludeUpValue() {
		UnlinkedEnum directionEnum = (UnlinkedEnum) parseString( "enum Direction {UP, DOWN}" );

		assertTrue("The enum Direction should include UP.",
						directionEnum.getValues().contains( "UP" ) );
	}

	@Test
	public void enumShouldIncludeDownValue() {
		UnlinkedEnum directionEnum = (UnlinkedEnum) parseString( "enum Direction {UP, DOWN}" );

		assertTrue("The enum Direction should include DOWN.",
						directionEnum.getValues().contains( "DOWN" ) );
	}

	@Test
	public void enumShouldNotIncludeLeftValue() {
		UnlinkedEnum directionEnum = (UnlinkedEnum) parseString( "enum Direction {UP, DOWN}" );

		assertFalse("The enum Direction should not include LEFT.",
						directionEnum.getValues().contains( "LEFT" ) );
	}

}