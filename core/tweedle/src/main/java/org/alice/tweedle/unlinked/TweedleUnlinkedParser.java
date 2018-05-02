package org.alice.tweedle.unlinked;

import org.alice.tweedle.*;
import org.alice.tweedle.ast.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	private class ClassBody
	{
		public List<TweedleField> properties = new ArrayList<>();
		public List<TweedleMethod> methods = new ArrayList<>();
		public List<TweedleConstructor> constructors = new ArrayList<>();
	}

	private class TypeVisitor extends TweedleParserBaseVisitor<TweedleType> {

		private ClassBody body;
		private ClassBodyDeclarationVisitor cbdVisitor;

		@Override public TweedleType visitTypeDeclaration( TweedleParser.TypeDeclarationContext context)
		{
			body = new ClassBody();
			cbdVisitor = new ClassBodyDeclarationVisitor(body);
			return super.visitChildren(context);
		}

		@Override public TweedleType visitClassDeclaration( TweedleParser.ClassDeclarationContext ctx ) {
			ctx.classBody().classBodyDeclaration().forEach( cbd -> cbd.accept( cbdVisitor ) );
			String className = ctx.identifier().getText();
			if (null != ctx.EXTENDS()) {
				String superclass = ctx.typeType().classType().getText();
				return new TweedleClass( className, superclass, body.properties, body.methods, body.constructors );
			} else {
				return new TweedleClass( className, body.properties, body.methods, body.constructors );
			}
		}

		@Override public TweedleType visitEnumDeclaration( TweedleParser.EnumDeclarationContext ctx ) {

			Map<String, TweedleEnumValue> values = new HashMap<>();
			ctx.enumConstants().enumConstant().forEach( enumConst -> {
				String name = enumConst.identifier().getText();
				Map<String, TweedleExpression> arguments = null;
				if (enumConst.arguments() != null)
				{
					 arguments = visitLabeledArguments(enumConst.arguments().labeledExpressionList());
					//TweedleMethod method = new TweedleMethod(name, null, )
				}
				TweedleEnumValue value = new TweedleEnumValue( null, name, arguments );
				values.put( name, value );
			});
			if (ctx.enumBodyDeclarations() != null)
			{
				ctx.enumBodyDeclarations().classBodyDeclaration().forEach(cbd -> cbd.accept(cbdVisitor));
			}
			return new TweedleEnum( ctx.identifier().getText(), values,
															body.properties, body.methods, body.constructors );
		}
	}

	private class ClassBodyDeclarationVisitor extends TweedleParserBaseVisitor<Void> {
		private final ClassBody body;

		ClassBodyDeclarationVisitor( ClassBody body ) {
			this.body = body;
		}

		@Override public Void visitMethodDeclaration( TweedleParser.MethodDeclarationContext ctx ) {
			return null;
		}

		@Override public Void visitClassBodyDeclaration( TweedleParser.ClassBodyDeclarationContext context ) {
			TweedleParser.MemberDeclarationContext memberDec = context.memberDeclaration();
			if (memberDec != null) {
				List<String> modifiers = context.classModifier().stream().map( mod -> (mod.STATIC() != null) ? "static" : null )
																				.collect( toList() );
				memberDec.accept( new MemberDeclarationVisitor( body, modifiers ) );
			}
			return super.visitClassBodyDeclaration( context );
		}
	}

	private class MemberDeclarationVisitor  extends TweedleParserBaseVisitor<Void>
	{
		private ClassBody body;
		private List<String> modifiers;

		MemberDeclarationVisitor( ClassBody body, List<String> modifiers )
		{
			this.body = body;
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
			body.properties.add(property);
			return super.visitFieldDeclaration( context );
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
			body.methods.add(method);
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
			body.constructors.add(constructor);
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

	private Map<String, TweedleExpression> visitLabeledArguments(TweedleParser.LabeledExpressionListContext context) {
		Map<String, TweedleExpression> arguments = new HashMap<>();
		final ExpressionVisitor visitor = new ExpressionVisitor();
		if (context != null) {
			context.labeledExpression().forEach( arg -> {
				TweedleExpression argValue = arg.expression().accept( visitor );
				arguments.put(arg.IDENTIFIER().getText(), argValue);
			});
		}
		return arguments;
	}

	private List<TweedleExpression> visitUnlabeledArguments( TweedleParser.UnlabeledExpressionListContext listContext,
																													 ExpressionVisitor expressionVisitor ) {
		return listContext == null
						? new ArrayList<>()
						: listContext.expression().stream().map( a -> a.accept( expressionVisitor ) ).collect( toList() );
	}

	private TweedleType getTypeOrVoid( TweedleParser.TypeTypeOrVoidContext context ) {
		if (context.VOID() != null) {
			return TweedleVoidType.VOID;
		}
		return getType( context.typeType() );
	}

	private TweedleType getType( TweedleParser.TypeTypeContext context ) {
		TweedleType baseType = context.classType() != null ?
						getTypeReference( context.classType().getText() ) :
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
				return new ThisExpression();
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
			} else if (context.bracket != null) {
				TweedleExpression array = context.expression(0).accept(new ExpressionVisitor(new TweedleArrayType()));
				TweedleExpression index = context.expression(1).accept(new ExpressionVisitor(TweedleTypes.WHOLE_NUMBER));
				return new ArrayIndexExpression(((TweedleArrayType)array.getType()).getValueType(), array, index);
			} else if ( context.lambdaCall() != null ) {
				TweedleExpression lambdaSourceExp = getFirstExpression(null, context);
				if ( context.lambdaCall().unlabeledExpressionList() == null ) {
					return new LambdaEvaluation(lambdaSourceExp);
				} else {
					final List<TweedleExpression> elements =
									visitUnlabeledArguments( context.lambdaCall().unlabeledExpressionList(), new ExpressionVisitor() );
					return new LambdaEvaluation(lambdaSourceExp, elements );
				}
			}

			// This will handle primary & lambda
			return visitChildren(context);
		}

		@Override public TweedleExpression visitLambdaExpression( TweedleParser.LambdaExpressionContext ctx ) {
			List<TweedleRequiredParameter> parameters = lambdaParameters( ctx.lambdaParameters() );
			List<TweedleStatement> stmts = collectBlockStatements( ctx.block().blockStatement() );
			return new LambdaExpression(parameters, stmts);
		}

		private List<TweedleRequiredParameter> lambdaParameters( TweedleParser.LambdaParametersContext context)
		{
			return context.requiredParameter().stream().map(
							field -> new TweedleRequiredParameter( getType( field.typeType() ),
																										 field.variableDeclaratorId().IDENTIFIER().getText() )
			).collect(toList());
		}

		@Override public TweedleExpression visitMethodCall(TweedleParser.MethodCallContext ctx) {

			return new MethodCallExpression( new ThisExpression(),
																			 ctx.IDENTIFIER().getText(),
																			 visitLabeledArguments(ctx.labeledExpressionList()));
		}

		@Override public TweedleExpression visitLambdaCall(TweedleParser.LambdaCallContext ctx) {
			if ( ctx.unlabeledExpressionList() == null ) {
				return new LambdaEvaluation(null);
			} else {
				final List<TweedleExpression> elements =
								visitUnlabeledArguments( ctx.unlabeledExpressionList(), new ExpressionVisitor() );
				return new LambdaEvaluation(null, elements );
			}
		}

		public @Override TweedleExpression visitSuperSuffix( TweedleParser.SuperSuffixContext context) {

			if (context.IDENTIFIER() != null)
			{
				if (context.arguments() != null)
				{
					return new MethodCallExpression( new SuperExpression(),
																					 context.IDENTIFIER().getText(),
																					 visitLabeledArguments(context.arguments().labeledExpressionList()));
				} else
				{
					return new FieldAccess( new SuperExpression(), context.IDENTIFIER().getText());
				}
			} else if (context.arguments() != null)
			{
				return new Instantiation( new SuperExpression(),
																  visitLabeledArguments(context.arguments().labeledExpressionList()));
			}
			throw new RuntimeException("Super suffix could not be constructed.");
		}

		public @Override TweedleExpression visitCreator( TweedleParser.CreatorContext context) {
			String typeName = context.createdName().getText();
			final TweedleParser.ArrayCreatorRestContext arrayDetails = context.arrayCreatorRest();
			if (arrayDetails != null) {
				TweedlePrimitiveType prim = getPrimitiveType( typeName );
				TweedleType memberType = prim == null ? getTypeReference( typeName ) : prim;
				TweedleArrayType arrayType = new TweedleArrayType(memberType);
				if (arrayDetails.arrayInitializer() != null) {
					final List<TweedleExpression> elements = visitUnlabeledArguments(
									arrayDetails.arrayInitializer().unlabeledExpressionList(), new ExpressionVisitor( memberType) );
					return new TweedleArrayInitializer( arrayType, elements );
				} else {
					return new TweedleArrayInitializer( arrayType, arrayDetails.expression().accept( new ExpressionVisitor() ) );
				}
			} else {
				TweedleTypeReference typeRef = getTypeReference(typeName);
				TweedleParser.LabeledExpressionListContext argsContext = context.classCreatorRest().arguments().labeledExpressionList();
				Map<String, TweedleExpression> arguments = visitLabeledArguments(argsContext);
				return new Instantiation( typeRef, arguments );
			}
		}

		private TweedleExpression fieldOrMethodRef( TweedleParser.ExpressionContext context ) {
			// Use untyped expression visitor for target
			TweedleExpression target = context.expression(0).accept( new ExpressionVisitor() );
			if (context.IDENTIFIER() != null) {
				return new FieldAccess( target, context.IDENTIFIER().getText());
			}
			if( context.methodCall() != null ) {
				//TweedleParser.LabeledExpressionListContext argsContext = context.methodCall().labeledExpressionList();
//				Map<String, TweedleExpression> arguments = visitLabeledArguments(argsContext);
//				return new MethodCallExpression(target,
//																				context.methodCall().IDENTIFIER().getText(),
//																				arguments );

				final MethodCallExpression methodCall =
								new MethodCallExpression( target, context.methodCall().IDENTIFIER() .getText() );
				TweedleParser.LabeledExpressionListContext argsContext = context.methodCall().labeledExpressionList();
				if (argsContext != null) {
					argsContext.labeledExpression().forEach( arg -> {
						final TweedleExpression argValue = arg.expression().accept( new ExpressionVisitor() );
						methodCall.addArgument( arg.IDENTIFIER().getText(), argValue );
					});
				}
				return methodCall;


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