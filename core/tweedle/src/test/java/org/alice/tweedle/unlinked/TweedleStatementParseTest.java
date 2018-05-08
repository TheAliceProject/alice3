package org.alice.tweedle.unlinked;

import org.alice.tweedle.TweedleArrayType;
import org.alice.tweedle.TweedleNull;
import org.alice.tweedle.TweedlePrimitiveValue;
import org.alice.tweedle.TweedleStatement;
import org.alice.tweedle.TweedleTypes;
import org.alice.tweedle.TweedleVoidType;
import org.alice.tweedle.ast.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class TweedleStatementParseTest {

	private TweedleStatement parseStatement( String source) {
		return new TweedleUnlinkedParser().parseStatement( source );
	}

	@Test
	public void somethingShouldBeCreatedForCountUpTo() {
		TweedleStatement tested = parseStatement("countUpTo( indexB < 2 ) {}" );
		assertNotNull("The parser should have returned something.", tested );
	}

	@Test
	public void aCountUpLoopShouldBeCreatedForCountUpTo() {
		TweedleStatement tested = parseStatement("countUpTo( indexB < 2 ) {}" );
		assertTrue("The parser should have returned a CountUpLoop.", tested instanceof CountUpLoop );
	}

	@Test
	public void aCountUpLoopShouldHaveBlockOfStatements() {
		CountUpLoop tested = (CountUpLoop) parseStatement( "countUpTo( indexB < 2 ) {}" );
		assertNotNull("The CountUpLoop should have a list of statements.", tested.getStatements());
	}

	@Test
	public void aCountUpLoopShouldHaveEmptyBlockOfStatements() {
		CountUpLoop tested = (CountUpLoop) parseStatement( "countUpTo( indexB < 2 ) {}" );
		assertTrue("The CountUpLoop should have an empty list of statements.", tested.getStatements().isEmpty());
	}

	@Test
	public void aCountUpLoopShouldHaveVariableDeclaration() {
		CountUpLoop tested = (CountUpLoop) parseStatement( "countUpTo( indexB < 2 ) {}" );
		assertNotNull("The CountUpLoop should have a Variable declaration.", tested.getLoopVariable());
	}

	@Test
	public void aCountUpLoopShouldHaveWholeNumberVariable() {
		CountUpLoop tested = (CountUpLoop) parseStatement( "countUpTo( indexB < 2 ) {}" );
		assertEquals("The CountUpLoop should have a Variable of type WholeNumber.", TweedleTypes.WHOLE_NUMBER, tested.getLoopVariable().getType());
	}

	@Test
	public void aCountUpLoopShouldHaveCorrectlyNamedVariable() {
		CountUpLoop tested = (CountUpLoop) parseStatement( "countUpTo( indexB < 2 ) {}" );
		assertEquals("The CountUpLoop should have a Variable named indexB.", "indexB", tested.getLoopVariable().getName());
	}

	@Test
	public void somethingShouldBeCreatedForIfThen() {
		TweedleStatement tested = parseStatement("if(true) { }" );
		assertNotNull("The parser should have returned something.", tested );
	}

	@Test
	public void aConditionalShouldBeCreatedForIfThen() {
		TweedleStatement tested = parseStatement("if(true) { }" );
		assertTrue("The parser should have returned a ConditionalStatement.", tested instanceof ConditionalStatement );
	}

	@Test
	public void aConditionalCreatedForIfThenShouldHaveCondition() {
		ConditionalStatement tested = (ConditionalStatement) parseStatement( "if(true) { }" );
		assertEquals("The parser should have returned a ConditionalStatement with a true condition.", TweedleTypes.TRUE, tested.getCondition() );
	}

	@Test
	public void aConditionalCreatedForIfThenShouldHaveThenStatementList() {
		ConditionalStatement tested = (ConditionalStatement) parseStatement( "if(true) { }" );
		assertTrue("The parser should have returned a ConditionalStatement with an empty then block.", tested.getThenBlock().isEmpty() );
	}

	@Test
	public void aConditionalCreatedForIfThenShouldHaveElseStatementList() {
		ConditionalStatement tested = (ConditionalStatement) parseStatement( "if(true) { }" );
		assertTrue("The parser should have returned a ConditionalStatement with an empty else block.", tested.getElseBlock().isEmpty() );
	}

	@Test
	public void somethingShouldBeCreatedForIfThenElse() {
		TweedleStatement tested = parseStatement("if(true) { } else { }" );
		assertNotNull("The parser should have returned something.", tested );
	}

	@Test
	public void aConditionalShouldBeCreatedForIfThenElse() {
		TweedleStatement tested = parseStatement("if(true) { } else { }" );
		assertTrue("The parser should have returned a ConditionalStatement.", tested instanceof ConditionalStatement );
	}

	@Test
	public void aConditionalCreatedForIfThenElseShouldHaveThenStatementList() {
		ConditionalStatement tested = (ConditionalStatement) parseStatement( "if(true) { } else { }" );
		assertTrue("The parser should have returned a ConditionalStatement with an empty then block.", tested.getThenBlock().isEmpty() );
	}

	@Test
	public void aConditionalCreatedForIfThenElseShouldHaveElseStatementList() {
		ConditionalStatement tested = (ConditionalStatement) parseStatement( "if(true) { } else { }" );
		assertTrue("The parser should have returned a ConditionalStatement with an empty else block.", tested.getElseBlock().isEmpty() );
	}

	@Test
	public void somethingShouldBeCreatedForForEach() {
		TweedleStatement tested = parseStatement("forEach(SModel x in new SModel[] {this.sphere, this.walrus} ) {}" );
		assertNotNull("The parser should have returned something.", tested );
	}

	@Test
	public void aForEachLoopShouldBeCreatedForForEach() {
		TweedleStatement tested = parseStatement("forEach(SModel x in new SModel[] {this.sphere, this.walrus} ) {}" );
		assertTrue("The parser should have returned a ForEachLoop.", tested instanceof ForEachLoop );
	}

	@Test
	public void somethingShouldBeCreatedForEachTogether() {
		TweedleStatement tested = parseStatement("eachTogether(SModel x in new SModel[] {this.sphere, this.walrus} ) {}" );
		assertNotNull("The parser should have returned something.", tested );
	}

	@Test
	public void aForEachTogetherShouldBeCreatedForEachTogether() {
		TweedleStatement tested = parseStatement("eachTogether(SModel x in new SModel[] {this.sphere, this.walrus} ) {}" );
		assertTrue("The parser should have returned a ForEachTogether.", tested instanceof ForEachTogether );
	}

	@Test
	public void anEachTogetherShouldHaveTypedVariable() {
		ForEachTogether tested = (ForEachTogether) parseStatement( "eachTogether(SModel x in new SModel[] {this.sphere, this.walrus} ) {}" );
		assertEquals("The EachTogether should have an SModel variable.", "SModel", tested.getLoopVar().getType().getName());
	}

	@Test
	public void anEachTogetherValuesShouldHaveArrayType() {
		ForEachTogether tested = (ForEachTogether) parseStatement( "eachTogether(SModel x in new SModel[] {this.sphere, this.walrus} ) {}" );
		assertTrue("The EachTogether values should be typed as an array.", tested.getLoopValues().getType() instanceof TweedleArrayType );
	}

	@Test
	public void anEachTogetherValuesShouldHaveElementTypeSModel() {
		ForEachTogether tested = (ForEachTogether) parseStatement( "eachTogether(SModel x in new SModel[] {this.sphere, this.walrus} ) {}" );
		final TweedleArrayType valArrayType = (TweedleArrayType) tested.getLoopValues().getType();
		assertEquals( "The EachTogether individual values should be typed as SModel.", valArrayType.getValueType(),
									tested.getLoopVar().getType() );
	}

	@Test
	public void anEachTogetherShouldHaveBlockOfStatements() {
		ForEachTogether tested = (ForEachTogether) parseStatement( "eachTogether(SModel x in new SModel[] {this.sphere, this.walrus} ) {}" );
		assertNotNull("The EachTogether should have a list of statements.", tested.getStatements());
	}

	@Test
	public void anEachTogetherShouldHaveEmptyBlockOfStatements() {
		ForEachTogether tested = (ForEachTogether) parseStatement( "eachTogether(SModel x in new SModel[] {this.sphere, this.walrus} ) {}" );
		assertTrue("The EachTogether should have an empty list of statements.", tested.getStatements().isEmpty());
	}

	@Test
	public void somethingShouldBeCreatedForWhile() {
		TweedleStatement tested = parseStatement("while(true) { }" );
		assertNotNull("The parser should have returned something.", tested );
	}

	@Test
	public void aWhileLoopShouldBeCreatedForWhile() {
		TweedleStatement tested = parseStatement("while(true) { }" );
		assertTrue("The parser should have returned a WhileLoop.", tested instanceof WhileLoop );
	}

	@Test
	public void aWhileLoopShouldHaveRunCondition() {
		WhileLoop tested = (WhileLoop) parseStatement( "while(true) { }" );
		assertNotNull("The WhileLoop should have a run condition.", tested.getRunCondition());
	}

	@Test
	public void thisWhileLoopRunConditionShouldBeABooleanTrue() {
		WhileLoop tested = (WhileLoop) parseStatement( "while(true) { }" );
		assertEquals("The WhileLoop run condition should be the value True.", TweedleTypes.TRUE, tested.getRunCondition());
	}

	@Test
	public void aWhileLoopShouldHaveBlockOfStatements() {
		WhileLoop tested = (WhileLoop) parseStatement( "while(true) { }" );
		assertNotNull("The WhileLoop should have a list of statements.", tested.getStatements());
	}

	@Test
	public void aWhileLoopShouldHaveEmptyBlockOfStatements() {
		WhileLoop tested = (WhileLoop) parseStatement( "while(true) { }" );
		assertTrue("The WhileLoop should have an empty list of statements.", tested.getStatements().isEmpty());
	}

	@Test
	public void somethingShouldBeCreatedForDoInOrder() {
		TweedleStatement tested = parseStatement("doInOrder { }" );
		assertNotNull("The parser should have returned something.", tested );
	}

	@Test
	public void aDoInOrderShouldBeCreated() {
		TweedleStatement tested = parseStatement("doInOrder { }" );
		assertTrue("The parser should have returned a DoInOrder.", tested instanceof DoInOrder );
	}

	@Test
	public void aDoInOrderShouldHaveBlockOfStatements() {
		DoInOrder tested = (DoInOrder) parseStatement( "doInOrder { }" );
		assertNotNull("The DoInOrder should have a list of statements.", tested.getStatements());
	}

	@Test
	public void aDoInOrderShouldHaveEmptyBlockOfStatements() {
		DoInOrder tested = (DoInOrder) parseStatement( "doInOrder { }" );
		assertTrue("The DoInOrder should have an empty list of statements.", tested.getStatements().isEmpty());
	}

	@Test
	public void aDoInOrderShouldHaveBlockOfTwoStatements() {
		DoInOrder tested = (DoInOrder) parseStatement( "doInOrder { doInOrder {} return; }" );
		assertEquals("The DoInOrder should have a list of 2 statements.", 2, tested.getStatements().size());
	}

	@Test
	public void aDoInOrdersFirstStatementShouldBeDoInOrder() {
		DoInOrder tested = (DoInOrder) parseStatement( "doInOrder { doInOrder {} return; }" );
		assertTrue("The block's first statement should be a DoInorder.", tested.getStatements().get( 0 ) instanceof DoInOrder);
	}

	@Test
	public void aDoInOrdersSecondStatementShouldBeReturn() {
		DoInOrder tested = (DoInOrder) parseStatement( "doInOrder { doInOrder {} return; }" );
		assertTrue("The block's first statement should be a Return.", tested.getStatements().get( 1 ) instanceof ReturnStatement);
	}

	@Test
	public void somethingShouldBeCreatedForDoTogether() {
		TweedleStatement tested = parseStatement("doTogether { }" );
		assertNotNull("The parser should have returned something.", tested );
	}

	@Test
	public void aDoTogetherShouldBeCreated() {
		TweedleStatement tested = parseStatement("doTogether { }" );
		assertTrue("The parser should have returned a DoTogether.", tested instanceof DoTogether );
	}

	@Test
	public void aDoTogetherShouldHaveBlockOfStatements() {
		DoTogether tested = (DoTogether) parseStatement( "doTogether { }" );
		assertNotNull("The DoTogether should have a list of statements.", tested.getStatements());
	}

	@Test
	public void aDoTogetherShouldHaveEmptyBlockOfStatements() {
		DoTogether tested = (DoTogether) parseStatement( "doTogether { }" );
		assertTrue("The DoTogether should have an empty list of statements.", tested.getStatements().isEmpty());
	}

	@Test
	public void aDoTogetherShouldHaveBlockOfTwoStatements() {
		DoTogether tested = (DoTogether) parseStatement( "doTogether { doInOrder {} return; }" );
		assertEquals("The DoTogether should have a list of 2 statements.", 2, tested.getStatements().size());
	}

	@Test
	public void aDoTogethersFirstStatementShouldBeDoInOrder() {
		DoTogether tested = (DoTogether) parseStatement( "doTogether { doInOrder {} return; }" );
		assertTrue("The block's first statement should be a DoInorder.", tested.getStatements().get( 0 ) instanceof DoInOrder);
	}

	@Test
	public void aDoTogethersSecondStatementShouldBeReturn() {
		DoTogether tested = (DoTogether) parseStatement( "doTogether { doInOrder {} return; }" );
		assertTrue("The block's first statement should be a Return.", tested.getStatements().get( 1 ) instanceof ReturnStatement);
	}

	@Test
	public void somethingShouldBeCreatedForEmptyReturn() {
		TweedleStatement tested = parseStatement("return;" );
		assertNotNull("The parser should have returned something.", tested );
	}

	@Test
	public void aReturnShouldBeCreatedForEmptyReturn() {
		TweedleStatement tested = parseStatement("return;" );
		assertTrue("The parser should have returned a ReturnStatement.", tested instanceof ReturnStatement );
	}

	@Test
	public void anEmptyReturnShouldBeCreatedForEmptyReturn() {
		ReturnStatement tested = (ReturnStatement) parseStatement( "return;" );
		assertEquals( "The ReturnStatement should hold TweedleNull.", TweedleNull.NULL, tested.getExpression() );
	}

	@Test
	public void anVoidTypeReturnShouldBeCreatedForEmptyReturn() {
		ReturnStatement tested = (ReturnStatement) parseStatement( "return;" );
		assertEquals( "The ReturnStatement should be type void.", TweedleVoidType.VOID, tested.getType() );
	}

	@Test
	public void somethingShouldBeCreatedForReturnWithValue() {
		TweedleStatement tested = parseStatement("return 4;" );
		assertNotNull("The parser should have returned something.", tested );
	}

	@Test
	public void aReturnShouldBeCreatedForReturnWithValue() {
		TweedleStatement tested = parseStatement("return 4;" );
		assertTrue("The parser should have returned a ReturnStatement.", tested instanceof ReturnStatement );
	}

	@Test
	public void aReturnExpressionShouldBeCreatedForReturnWithValue() {
		ReturnStatement tested = (ReturnStatement) parseStatement( "return 4;" );
		assertNotNull("The ReturnStatement should hold no value.", tested.getExpression() );
	}

	@Test
	public void aWholeNumberTypeReturnShouldBeCreatedForReturnWithValue() {
		ReturnStatement tested = (ReturnStatement) parseStatement( "return 4;" );
		assertEquals( "The ReturnStatement should be type void.", TweedleTypes.WHOLE_NUMBER, tested.getType() );
	}

	@Test
	public void somethingShouldBeCreatedForAssignment() {
		TweedleStatement tested = parseStatement( "x <- 3;" );
		assertNotNull("The parser should have returned something.", tested );
	}

	@Test
	public void anExpressionStatementShouldBeCreated() {
		TweedleStatement tested = parseStatement( "x <- 3;" );
		assertTrue("The parser should have returned an ExpressionStatement.", tested instanceof ExpressionStatement );
	}

	@Test
	public void anAssignmentExpressionShouldBeCreated() {
		ExpressionStatement tested = (ExpressionStatement) parseStatement( "x <- 3;" );
		assertTrue("The Statement should have contained an AssignmentExpression.", tested.getExpression() instanceof AssignmentExpression );
	}

	@Test
	public void somethingShouldBeCreatedForVariableDeclaration() {
		TweedleStatement tested = parseStatement("WholeNumber a <- 2;" );
		assertNotNull("The parser should have returned something.", tested );
	}

	@Test
	public void aVariableDeclarationShouldBeCreatedForVariableDeclaration() {
		TweedleStatement tested = parseStatement("WholeNumber a <- 2;" );
		assertTrue("The parser should have returned a LocalVariableDeclaration.", tested instanceof LocalVariableDeclaration );
	}

	@Test
	public void aVariableDeclarationShouldBeConstant() {
		LocalVariableDeclaration tested = (LocalVariableDeclaration) parseStatement( "constant WholeNumber a <- 2;" );
		assertTrue("The LocalVariableDeclaration .", tested.isConstant() );
	}

	@Test
	public void aVariableDeclarationShouldNotBeConstant() {
		LocalVariableDeclaration tested = (LocalVariableDeclaration) parseStatement( "WholeNumber a <- 2;" );
		assertFalse("The LocalVariableDeclaration should not be constant.", tested.isConstant() );
	}

	@Test
	public void aVariableDeclarationShouldHaveAnInitializer() {
		LocalVariableDeclaration tested = (LocalVariableDeclaration) parseStatement( "WholeNumber a <- 2;" );
		assertNotNull("The LocalVariableDeclaration should be constant.", tested.getDeclaration().getInitializer() );
	}

	@Test
	public void aVariableDeclarationShouldHaveAnInitializerValue() {
		TweedleExpression tested = ((LocalVariableDeclaration) parseStatement( "WholeNumber a <- 2;" ))
						.getDeclaration().getInitializer();
		assertTrue("The LocalVariableDeclaration should hold a primitive value.", tested instanceof TweedlePrimitiveValue );
	}

	@Test
	public void aVariableDeclarationShouldHaveAnInitializerOfTwo() {
		TweedlePrimitiveValue tested = (TweedlePrimitiveValue) ((LocalVariableDeclaration) parseStatement( "WholeNumber a <- 2;" ))
						.getDeclaration().getInitializer();
		assertEquals("The LocalVariableDeclaration should hold 2.", 2, tested.getPrimitiveValue() );
	}

	@Test
	public void nestedDoInOrdersOuterOneShouldBeEnabled() {
		DoInOrder tested = (DoInOrder) parseStatement( "doInOrder { doInOrder {} }" );
		assertTrue("The outer doInOrder should be enabled.", tested.isEnabled());
	}

	@Test
	public void nestedDoInOrdersInnerOneShouldBeEnabled() {
		DoInOrder tested = (DoInOrder) parseStatement( "doInOrder { doInOrder {} }" );
		assertTrue("The inner doInOrder should be enabled.", tested.getStatements().get( 0 ).isEnabled());
	}

	@Test
	public void disabledNestedDoInOrdersOuterOneShouldBeDisabled() {
		DoInOrder tested = (DoInOrder) parseStatement( "*< doInOrder { doInOrder {} } >*" );
		assertFalse("The outer doInOrder should be disabled.", tested.isEnabled());
	}

	@Test
	public void disabledNestedDoInOrdersInnerOneShouldBeEnabled() {
		DoInOrder tested = (DoInOrder) parseStatement( "*< doInOrder { doInOrder {} } >*" );
		assertTrue("The inner doInOrder should be enabled.", tested.getStatements().get( 0 ).isEnabled());
	}

	@Test
	public void disabledInnerDoInOrdersOuterOneShouldBeEnabled() {
		DoInOrder tested = (DoInOrder) parseStatement( "doInOrder { *< doInOrder {} >* }" );
		assertTrue("The outer doInOrder should be enabled.", tested.isEnabled());
	}

	@Test
	public void disabledInnerDoInOrdersInnerOneShouldBeDisabled() {
		DoInOrder tested = (DoInOrder) parseStatement( "doInOrder { *< doInOrder {} >* }" );
		assertFalse("The inner doInOrder should be disabled.", tested.getStatements().get( 0 ).isEnabled());
	}
}