package org.alice.tweedle.unlinked;

import org.alice.tweedle.*;
import org.alice.tweedle.ast.*;
import org.antlr.v4.runtime.*;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class TweedleUnlinkedParser {

	public TweedleType parse( String someLangSourceCode ) {
		CharStream charStream = new ANTLRInputStream( someLangSourceCode );
		TweedleLexer lexer = new TweedleLexer( charStream );
		TokenStream tokens = new CommonTokenStream( lexer );
		TweedleParser parser = new TweedleParser( tokens );

		TypeVisitor typeVisitor = new TypeVisitor();
		return typeVisitor.visit( parser.typeDeclaration() );
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
			TweedleType type = getTypeForName( context.typeType().getText() );
			VariableListVisitor variableVisitor = new VariableListVisitor(modifiers, type);
			List<TweedleField> properties = context.variableDeclarators().accept( variableVisitor );
			tweedleClass.properties.addAll(properties);
			return super.visitFieldDeclaration(context);
		}

		@Override public Void visitMethodDeclaration( TweedleParser.MethodDeclarationContext context)
		{
			StatementVisitor statementVisitor = new StatementVisitor();
			List<TweedleStatement> body =
							context.methodBody().block().blockStatement().stream()
							.map( stmt -> stmt.accept( statementVisitor ) ).collect(toList());
			TweedleMethod method = new TweedleMethod(
							modifiers,
							getTypeForName( context.typeTypeOrVoid().getText() ),
							context.IDENTIFIER().getText(),
							requiredParameters(context.formalParameters()),
							optionalParameters(context.formalParameters()),
							body );
			tweedleClass.methods.add(method);
			return super.visitMethodDeclaration(context);
		}

		@Override public Void visitConstructorDeclaration( TweedleParser.ConstructorDeclarationContext context)
		{
			StatementVisitor statementVisitor = new StatementVisitor();
			List<TweedleStatement> body =
							context.constructorBody.blockStatement().stream()
										 .map( stmt -> stmt.accept( statementVisitor ) ).collect(toList());
			TweedleConstructor constructor = new TweedleConstructor( getTypeForName( context.IDENTIFIER().getText() ),
							context.IDENTIFIER().getText(),
							requiredParameters(context.formalParameters()),
							optionalParameters(context.formalParameters()),
							body );
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
										.stream().map(field -> new TweedleRequiredParameter( getTypeForName( field.typeType().getText() ),
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
				final TweedleType type = getTypeForName( field.typeType().getText() );
				final String varName = field.variableDeclaratorId().IDENTIFIER().getText();
				return new TweedleOptionalParameter( type, varName, field.accept( new ExpressionVisitor(type) ) );
			} ).collect( toList());
		}
	}

	private TweedleType getTypeForName( String typeName ) {
		for (TweedlePrimitiveType prim : TweedleTypes.PRIMITIVE_TYPES) {
			if (prim.getName().equals( typeName )) {
				return prim;
			}
		}
		return new TweedleTypeReference( typeName );
	}

	private class VariableListVisitor extends TweedleParserBaseVisitor<List<TweedleField>> {
		private List<String> modifiers;
		private TweedleType type;

		VariableListVisitor( List<String> modifiers, TweedleType type ) {
			this.modifiers = modifiers;
			this.type = type;
		}

		@Override public List<TweedleField> visitVariableDeclarators( TweedleParser.VariableDeclaratorsContext context )
		{
			ExpressionVisitor expressionVisitor = new ExpressionVisitor(type);
			return context.variableDeclarator()
						.stream().map(field -> new TweedleField(
							modifiers,
							type,
							field.variableDeclaratorId().IDENTIFIER().getText(),
							expressionVisitor.visit(field)
			)).collect(toList());
		}
	}

	private class ExpressionVisitor extends TweedleParserBaseVisitor<TweedleExpression>
	{
		private TweedleType expectedType;

		ExpressionVisitor( TweedleType expectedType ) {
			this.expectedType = expectedType;
		}

		@Override public TweedleExpression visitExpression( TweedleParser.ExpressionContext context )
		{
			TweedleExpression expression = buildExpression( context );
			if (expectedType == null || expression == null ||
							expectedType.willAcceptValueOfType( expression.getResultType()) ) {
				return expression;
			}
			return null;
		}

		private TweedleExpression buildExpression( TweedleParser.ExpressionContext context )
		{
			TweedleExpression expression = context.accept( new ExpressionChildVisitor());
			if (expression != null)
			{
				return expression;
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
						return new NegativeExpression( getFirstExpression( TweedleTypes.NUMBER, context ) );
					case "!":
						return new LogicalNotExpression( getFirstExpression( TweedleTypes.BOOLEAN, context ) );
					default:
						throw new RuntimeException( "Unrecognized prefix operation: " + prefix.getText() );
				}
			}
			else if (operation != null) {
				switch (operation.getText()) {
				case ".":
//					return new MethodInvocation(); //Object x [otherstuff]=>Y
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
				default:
					throw new RuntimeException( "No such operation as " + operation.getText());
				}
			}
			return null;
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

		@Override public TweedleExpression visitArrayInitializer( TweedleParser.ArrayInitializerContext context)
		{
			return new TweedleArrayInitializer( context.variableInitializer().stream().map( a -> a.accept( this)).collect( toList()));
		}
	}

	public interface BinaryConstructor {
		BinaryExpression newBinExp( TweedleExpression lhs, TweedleExpression rhs );
	}


	private class ExpressionChildVisitor extends TweedleParserBaseVisitor<TweedleExpression>
	{
		@Override public TweedleExpression visitMethodCall( TweedleParser.MethodCallContext context)
		{
			return super.visitMethodCall(context);
		}

		@Override public TweedleExpression visitCreator( TweedleParser.CreatorContext context)
		{
			return super.visitCreator(context);
		}
	}
	
	private class StatementVisitor extends TweedleParserBaseVisitor<TweedleStatement> {

		@Override public TweedleStatement visitLocalVariableDeclaration( TweedleParser.LocalVariableDeclarationContext context)
		{
			List<String> modifiers = context.variableModifier().stream().map(modifier -> modifier.getText()).collect(toList());
			TweedleType type = getTypeForName( context.typeType().getText() );
			VariableListVisitor variableVisitor = new VariableListVisitor(modifiers, type);
			List<TweedleField> variables = context.variableDeclarators().accept(variableVisitor);
			return null;
//			return new TweedleLocalVariableDeclaration(variables);
		}

		@Override public TweedleStatement visitBlockStatement( TweedleParser.BlockStatementContext context)
		{
			return super.visitBlockStatement(context);
		}
	}
}

	
/*			String methodName = ctx.methodName().getText();
			StatementVisitor statementVisitor = new StatementVisitor();
			List<TweedleStatement> statements = ctx.statement()
							.stream()
							.map(statement -> statement.accept(statementVisitor))
							.collect(toList());
			return new TweedleMethod(methodName, statements);*/

/*	private static class StatementVisitor extends TweedleParserBaseVisitor<TweedleStatement> {

		@Override
		public TweedleStatement visitStatement(TweedleParser.StatementContext ctx) {
			String statementName = ctx.getText();
			return new TweedleStatement(statementName);
		}
	}*/