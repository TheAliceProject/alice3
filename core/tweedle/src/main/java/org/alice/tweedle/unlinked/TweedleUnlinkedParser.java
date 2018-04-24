package org.alice.tweedle.unlinked;

import org.alice.tweedle.*;
import org.alice.tweedle.ast.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class TweedleUnlinkedParser {

	TweedleType parseType( String sourceForType ) {
		return new TypeVisitor().visit( tweedleParserForSource( sourceForType ).typeDeclaration() );
	}

	TweedleStatement parseStatement( String sourceForExpression ) {
		return new StatementVisitor().visit( tweedleParserForSource( sourceForExpression ).blockStatement() );
	}

	TweedleExpression parseExpression( String sourceForExpression ) {
		return new ExpressionVisitor().visit( tweedleParserForSource( sourceForExpression ).expression() );
	}

	private TweedleParser tweedleParserForSource( String source ) {
		CharStream charStream = new ANTLRInputStream( source );
		TweedleLexer lexer = new TweedleLexer( charStream );
		TokenStream tokens = new CommonTokenStream( lexer );
		return new TweedleParser( tokens );
	}

	private class TypeVisitor extends TweedleParserBaseVisitor<TweedleType> {

		@Override public TweedleType visitClassDeclaration( TweedleParser.ClassDeclarationContext ctx ) {
			super.visitClassDeclaration( ctx );
			String className = ctx.identifier().getText();
			final TweedleClass tweedleClass;
			if (null != ctx.EXTENDS()) {
				String superclass = ctx.typeType().classOrInterfaceType().getText();
				tweedleClass = new TweedleClass( className, superclass );
			} else {
				tweedleClass = new TweedleClass( className );
			}
			ClassBodyDeclarationVisitor cbdVisitor = new ClassBodyDeclarationVisitor( tweedleClass );
			ctx.classBody().classBodyDeclaration().forEach( cbd -> cbd.accept( cbdVisitor ) );
			return tweedleClass;
		}

		@Override public TweedleType visitEnumDeclaration( TweedleParser.EnumDeclarationContext ctx ) {
			super.visitEnumDeclaration( ctx );
			List<String> values = new ArrayList<>();
			ctx.enumConstants().enumConstant().forEach( enumConst -> values.add( enumConst.identifier().getText() ) );
			return new TweedleEnum( ctx.identifier().getText(), values );
		}
	}

	private class ClassBodyDeclarationVisitor extends TweedleParserBaseVisitor<Void> {
		private final TweedleClass tweedleClass;

		ClassBodyDeclarationVisitor( TweedleClass tweedleClass ) {
			this.tweedleClass = tweedleClass;
		}

		@Override public Void visitMethodDeclaration( TweedleParser.MethodDeclarationContext ctx ) {
			return null;
		}

		@Override public Void visitClassBodyDeclaration( TweedleParser.ClassBodyDeclarationContext context ) {
			TweedleParser.MemberDeclarationContext memberDec = context.memberDeclaration();
			if (memberDec != null) {
				List<String> modifiers = context.classModifier().stream().map( mod -> (mod.STATIC() != null) ? "static" : null )
																				.collect( toList() );
				memberDec.accept( new MemberDeclarationVisitor( tweedleClass, modifiers ) );
			}
			return super.visitClassBodyDeclaration( context );
		}
	}

	private class MemberDeclarationVisitor  extends TweedleParserBaseVisitor<Void>
	{
		private TweedleClass tweedleClass;
		private List<String> modifiers;

		MemberDeclarationVisitor( TweedleClass tweedleClass, List<String> modifiers )
		{
			this.tweedleClass = tweedleClass;
			this.modifiers = modifiers;
		}

		@Override public Void visitFieldDeclaration( TweedleParser.FieldDeclarationContext context )
		{
			TweedleType type = getType( context.typeType() );
			final String name = context.variableDeclarator().variableDeclaratorId().IDENTIFIER().getText();
			TweedleField property;
			if (context.variableDeclarator().variableInitializer() != null) {
				ExpressionVisitor initVisitor = new ExpressionVisitor( type );
				TweedleExpression init =
								context.variableDeclarator().variableInitializer().accept( initVisitor );
				property = new TweedleField( modifiers, type, name, init);
			} else {
				property = new TweedleField( modifiers, type, name );
			}
			tweedleClass.properties.add(property);
			return null;
		}

		@Override public Void visitMethodDeclaration( TweedleParser.MethodDeclarationContext context)
		{
			TweedleMethod method = new TweedleMethod(
							modifiers,
							getTypeOrVoid( context.typeTypeOrVoid() ),
							context.IDENTIFIER().getText(),
							requiredParameters(context.formalParameters()),
							optionalParameters(context.formalParameters()),
							collectBlockStatements( context.methodBody().block().blockStatement() ) );
			tweedleClass.methods.add(method);
			return super.visitMethodDeclaration(context);
		}

		@Override public Void visitConstructorDeclaration( TweedleParser.ConstructorDeclarationContext context)
		{
			TweedleConstructor constructor = new TweedleConstructor(
							getTypeReference( context.IDENTIFIER().getText() ),
							context.IDENTIFIER().getText(),
							requiredParameters(context.formalParameters()),
							optionalParameters(context.formalParameters()),
							collectBlockStatements( context.constructorBody.blockStatement() ) );
			tweedleClass.constructors.add(constructor);
			return super.visitConstructorDeclaration(context);
		}

		private List<TweedleRequiredParameter> requiredParameters(TweedleParser.FormalParametersContext context)
		{
			if (context.formalParameterList() == null || context.formalParameterList().requiredParameter() == null)
			{
				return new ArrayList<>();
			}
			return context.formalParameterList().requiredParameter()
										.stream().map(field -> new TweedleRequiredParameter( getType( field.typeType() ),
																																				 field.variableDeclaratorId().IDENTIFIER().getText()
							)).collect(toList());
		}

		private List<TweedleOptionalParameter> optionalParameters(TweedleParser.FormalParametersContext context)
		{
			if (context.formalParameterList() == null || context.formalParameterList().optionalParameter() == null)
			{
				return new ArrayList<>();
			}
			return context.formalParameterList().optionalParameter().stream().map( field -> {
				final TweedleType type = getType( field.typeType() );
				final String varName = field.variableDeclaratorId().IDENTIFIER().getText();
				return new TweedleOptionalParameter( type, varName, field.accept( new ExpressionVisitor(type) ) );
			} ).collect( toList());
		}
	}

	private List<TweedleStatement> collectBlockStatements( List<TweedleParser.BlockStatementContext> contexts ) {
		StatementVisitor statementVisitor = new StatementVisitor();
		return contexts.stream().map( stmt -> stmt.accept( statementVisitor ) ).collect(toList());
	}

	private TweedleType getTypeOrVoid( TweedleParser.TypeTypeOrVoidContext context ) {
		if (context.VOID() != null) {
			return TweedleVoidType.VOID;
		}
		return getType( context.typeType() );
	}

	private TweedleType getType( TweedleParser.TypeTypeContext context ) {
		TweedleType baseType = context.classOrInterfaceType() != null ?
						getTypeReference( context.classOrInterfaceType().getText() ) :
						getPrimitiveType( context.primitiveType().getText() );
		if ( context.getChildCount() > 1 && baseType != null) {
			return new TweedleArrayType( baseType );
		}
		return baseType;
	}

	private TweedleTypeReference getTypeReference( String typeName ) {
		return new TweedleTypeReference( typeName );
	}

	private TweedlePrimitiveType getPrimitiveType( String typeName ) {
		for (TweedlePrimitiveType prim : TweedleTypes.PRIMITIVE_TYPES) {
			if (prim.getName().equals( typeName )) {
				return prim;
			}
		}
		return null;
	}

	private class ExpressionVisitor extends TweedleParserBaseVisitor<TweedleExpression>
	{
		private TweedleType expectedType;

		ExpressionVisitor() {
			this.expectedType = null;
		}

		ExpressionVisitor( TweedleType expectedType ) {
			this.expectedType = expectedType;
		}

		@Override public TweedleExpression visitPrimary( TweedleParser.PrimaryContext context ) {
			if ( context.expression() != null ) {
				// Parenthesized child expression
				return this.visitExpression( context.expression() );
			}
			if ( context.THIS() != null ) {
				return new ThisExpression(null);
			}
			if ( context.IDENTIFIER() != null ) {
				return new IdentifierReference( context.IDENTIFIER().getText());
			}
			// TODO parse super superSuffix

			// Visit children to handle literals
			return super.visitPrimary( context );
		}

		@Override public TweedleExpression visitLiteral( TweedleParser.LiteralContext context ) {
			TerminalNode wholeNumber = context.DECIMAL_LITERAL();
			if (wholeNumber != null) {
				int value = Integer.parseInt( wholeNumber.getSymbol().getText() );
				return TweedleTypes.WHOLE_NUMBER.createValue( value );
			}

			TerminalNode flt = context.FLOAT_LITERAL();
			if (flt != null) {
				double value = Double.parseDouble( flt.getSymbol().getText() );
				return TweedleTypes.DECIMAL_NUMBER.createValue( value );
			}

			if (context.NULL_LITERAL() != null) {
				return TweedleNull.NULL;
			}

			TerminalNode bool = context.BOOL_LITERAL();
			if (bool != null) {
				boolean value = Boolean.parseBoolean( bool.getSymbol().getText() );
				return TweedleTypes.BOOLEAN.createValue( value );
			}

			TerminalNode str = context.STRING_LITERAL();
			if (str != null) {
				final String quotedString = str.getSymbol().getText();
				return TweedleTypes.TEXT_STRING.createValue( quotedString.substring( 1, quotedString.length()-1 ) );
			}

			return super.visitLiteral( context );
		}

		@Override public TweedleExpression visitExpression( TweedleParser.ExpressionContext context )
		{
			TweedleExpression expression = buildExpression( context );
			if (expectedType != null && expression != null && expression.getType() != null
							&& !expectedType.willAcceptValueOfType( expression.getType() )) {
				System.out.println(
								"Had been expecting expression of type " + expectedType + ", but it is typed as " + expression.getType() );
			}
			return expression;
		}

		private TweedleExpression buildExpression( TweedleParser.ExpressionContext context )
		{
			if (context.NEW() != null) {
				return instantiationExpression( context );
			}
			Token prefix = context.prefix;
			Token operation = context.bop;

			if (prefix != null)
			{
				switch (prefix.getText())
				{
				case "+":
					// A positive number, or at least not changing the sign. Send along child.
					return getFirstExpression( TweedleTypes.NUMBER, context );
				case "-":
					// A negative number, or a sign flip. Send along negated child.
					return getNegativeOfExpression( getFirstExpression( TweedleTypes.NUMBER, context ) );
				case "!":
					return new LogicalNotExpression( getFirstExpression( TweedleTypes.BOOLEAN, context ) );
				default:
					throw new RuntimeException( "Unrecognized prefix operation: " + prefix.getText() );
				}
			}
			else if (operation != null) {
				switch (operation.getText()) {
				case ".":
					return fieldOrMethodRef(context);
				case "==":
					return binaryExpression( EqualToExpression::new, null, context ); //XxY=>B
				case "!=":
					return binaryExpression( NotEqualToExpression::new, null, context ); //XxY=>B
				case "*":
					return binaryExpression( MultiplicationExpression::new, TweedleTypes.NUMBER, context );
				case "/":
					return binaryExpression( DivisionExpression::new, TweedleTypes.NUMBER, context );
				case "%":
					return binaryExpression( ModuloExpression::new, TweedleTypes.WHOLE_NUMBER, context );
				case "+":
					return binaryExpression( AdditionExpression::new, TweedleTypes.NUMBER, context );
				case "-":
					return binaryExpression( SubtractionExpression::new, TweedleTypes.NUMBER, context );
				case "<=":
					return binaryExpression( LessThanOrEqualExpression::new, TweedleTypes.NUMBER, context );
				case ">=":
					return binaryExpression( GreaterThanOrEqualExpression::new, TweedleTypes.NUMBER, context );
				case ">":
					return binaryExpression( GreaterThanExpression::new, TweedleTypes.NUMBER, context );
				case "<":
					return binaryExpression( LessThanExpression::new, TweedleTypes.NUMBER, context );
				case "&&":
					return binaryExpression( LogicalAndExpression::new, TweedleTypes.BOOLEAN, context );
				case "||":
					return binaryExpression( LogicalOrExpression::new, TweedleTypes.BOOLEAN, context );
				case "<-":
					List<TweedleExpression> expressions = getTypedExpressions( context, null );
					return new AssignmentExpression( expressions.get( 0 ), expressions.get( 1 ) );
				default:
					throw new RuntimeException( "No such operation as " + operation.getText());
				}
			}
			// TODO parse array value reading
			// TODO parse lambda

			// This will handle primary
			return visitChildren(context);
		}

		private TweedleExpression instantiationExpression( TweedleParser.ExpressionContext context ) {
			String typeName = context.creator().createdName().getText();
			final TweedleParser.ArrayCreatorRestContext arrayDetails = context.creator().arrayCreatorRest();
			if (arrayDetails != null) {
				TweedlePrimitiveType prim = getPrimitiveType( typeName );
				TweedleType memberType = prim == null ? getTypeReference( typeName ) : prim;
				if (arrayDetails.arrayInitializer() != null) {
					return new TweedleArrayInitializer(
									new TweedleArrayType( memberType ),
									arrayDetails.arrayInitializer().expression().stream().map( a -> a.accept( new ExpressionVisitor( memberType ) ) )
												 .collect( toList() ) );
				}
				// TODO return sized array
			} else {
				//TODO Object instantiation
			}
			return null;
		}

		private TweedleExpression fieldOrMethodRef( TweedleParser.ExpressionContext context ) {
			// Use untyped expression visitor for target
			TweedleExpression target = context.expression(0).accept( new ExpressionVisitor() );
			if (context.IDENTIFIER() != null) {
				return new FieldAccess( target, context.IDENTIFIER().getText());
			}
			if( context.methodCall() != null ) {
				// TODO read labeledExpressionList
				return new MethodCallExpression(target, context.methodCall().IDENTIFIER().getText());
			}
			throw new RuntimeException( "Unexpected details on context " + context);
		}

		private TweedleExpression getNegativeOfExpression( TweedleExpression exp ) {
			final NegativeExpression negativeExpression = new NegativeExpression( exp );
			if (exp instanceof TweedlePrimitiveValue) {
				return negativeExpression.evaluate( null );
			}
			return negativeExpression;
		}

		private TweedleExpression getFirstExpression( TweedlePrimitiveType type, TweedleParser.ExpressionContext context ) {
			return getTypedExpressions( context, type ).get( 0 );
		}

		private TweedleExpression binaryExpression( BinaryConstructor constructor,
																								TweedlePrimitiveType type,
																								TweedleParser.ExpressionContext context ) {
			List<TweedleExpression> expressions = getTypedExpressions( context, type );
			return constructor.newBinExp( expressions.get( 0 ), expressions.get( 1 ) );
		}

		private List<TweedleExpression> getTypedExpressions( TweedleParser.ExpressionContext context, TweedleType type ) {
			final ExpressionVisitor visitor = new ExpressionVisitor( type );
			return context.expression().stream().map( exp -> exp.accept( visitor ) ).collect( toList() );
		}
	}

	public interface BinaryConstructor {
		BinaryExpression newBinExp( TweedleExpression lhs, TweedleExpression rhs );
	}

	private class StatementVisitor extends TweedleParserBaseVisitor<TweedleStatement> {

		@Override public TweedleStatement visitLocalVariableDeclaration( TweedleParser.LocalVariableDeclarationContext context)
		{
			TweedleType type = getType( context.typeType() );
			final String name = context.variableDeclarator().variableDeclaratorId().IDENTIFIER().getText();
			TweedleLocalVariable decl;
			if (context.variableDeclarator().variableInitializer() != null) {
				ExpressionVisitor initVisitor = new ExpressionVisitor( type );
				TweedleExpression init =
								context.variableDeclarator().variableInitializer().accept( initVisitor );
				decl = new TweedleLocalVariable( type, name, init);
			} else {
				decl = new TweedleLocalVariable( type, name);
			}
			return new LocalVariableDeclaration( context.CONSTANT() != null, decl );
		}

		@Override public TweedleStatement visitBlockStatement( TweedleParser.BlockStatementContext context)
		{
			if (context.NODE_DISABLE() != null) {
				TweedleStatement stmt = context.blockStatement().accept( this );
				stmt.disable();
				return stmt;
			}
			if (context.localVariableDeclaration() != null) {
				return context.localVariableDeclaration().accept( this );
			}
			return super.visitBlockStatement(context);
		}

		@Override public TweedleStatement visitStatement( TweedleParser.StatementContext context)
		{
			if (context.COUNT_UP_TO() != null) {
				return new CountUpLoop( context.IDENTIFIER().getText(),
																context.expression().accept( new ExpressionVisitor(TweedleTypes.WHOLE_NUMBER) ),
																collectBlockStatements( context.block(0).blockStatement() ) );
			}
			if (context.IF()!= null) {
				TweedleExpression condition = context.parExpression().expression().accept( new ExpressionVisitor( TweedleTypes.BOOLEAN ) );
				List<TweedleStatement> thenBlock = collectBlockStatements( context.block( 0).blockStatement() );
				List<TweedleStatement> elseBlock = context.ELSE() != null ?
								collectBlockStatements( context.block( 1 ).blockStatement() ) : new ArrayList<>();
				return new ConditionalStatement(condition, thenBlock, elseBlock);
			}
			if (context.forControl() != null) {
				final TweedleType valueType = getType( context.forControl().typeType() );
				final TweedleArrayType arrayType = new TweedleArrayType( valueType );
				final TweedleLocalVariable loopVar =
								new TweedleLocalVariable( valueType, context.forControl().variableDeclaratorId().getText());
				final TweedleExpression loopValues = context.forControl().expression().accept( new ExpressionVisitor( arrayType ) );
				final List<TweedleStatement> statements = collectBlockStatements( context.block( 0 ).blockStatement() );
				if (context.FOR_EACH() != null) {
					return new ForEachLoop( loopVar, loopValues, statements );
				}
				if (context.EACH_TOGETHER() != null) {
					return new ForEachTogether( loopVar, loopValues, statements );
				}
				throw new RuntimeException( "Found a forControl in a statement where it was not expected: " + context);
			}
			if (context.WHILE() != null) {
				return new WhileLoop(context.parExpression().expression().accept( new ExpressionVisitor(TweedleTypes.BOOLEAN) ),
														 collectBlockStatements( context.block(0).blockStatement() ) );
			}
			if (context.DO_IN_ORDER() != null) {
				return new DoInOrder( collectBlockStatements( context.block(0).blockStatement() ) );
			}
			if (context.DO_TOGETHER() != null) {
				return new DoTogether( collectBlockStatements( context.block(0).blockStatement() ) );
			}
			if (context.RETURN() != null) {
				if (context.expression() != null) {
					return new ReturnStatement( context.expression().accept( new ExpressionVisitor() ) );
				} else {
					return new ReturnStatement();
				}
			}
			TweedleParser.ExpressionContext expContext = context.statementExpression;
			if (expContext != null) {
				return new ExpressionStatement( context.expression().accept( new ExpressionVisitor() ) );
			}
			throw new RuntimeException( "Found a statement that was not expected: " + context);
		}

	}
}