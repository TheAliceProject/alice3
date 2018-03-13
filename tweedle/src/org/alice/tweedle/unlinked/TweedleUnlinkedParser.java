package org.alice.tweedle.unlinked;

import org.alice.tweedle.TweedleLexer;
import org.alice.tweedle.TweedleParser;
import org.alice.tweedle.TweedleParserBaseVisitor;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import java.util.ArrayList;
import java.util.List;

public class TweedleUnlinkedParser {

	public UnlinkedType parse(String someLangSourceCode) {
		CharStream charStream = new ANTLRInputStream(someLangSourceCode);
		TweedleLexer lexer = new TweedleLexer(charStream);
		TokenStream tokens = new CommonTokenStream(lexer);
		TweedleParser parser = new TweedleParser(tokens);
		parser.setErrorHandler( new ThrowOnParseErrorStrategy());

		TypeVisitor typeVisitor = new TypeVisitor();
		return typeVisitor.visit(parser.typeDeclaration());
	}

	private static class TypeVisitor extends TweedleParserBaseVisitor<UnlinkedType> {

		@Override
		public UnlinkedType visitClassDeclaration( TweedleParser.ClassDeclarationContext ctx) {
		  super.visitClassDeclaration( ctx );
			String className = ctx.identifier().getText();
			if (null != ctx.EXTENDS() ) {
				String superclass = ctx.typeType().classOrInterfaceType().getText();
				return new UnlinkedClass(className, superclass);
			} else {
				return new UnlinkedClass(className);
			}
/*
			MethodVisitor methodVisitor = new MethodVisitor();
			List<TweedleMethod> methods = ctx.method()
							.stream()
							.map(method -> method.accept(methodVisitor))
							.collect(toList());
			return new TweedleClass(typeName, methods);
}*/

		}

		@Override
		public UnlinkedType visitEnumDeclaration( TweedleParser.EnumDeclarationContext ctx) {
			super.visitEnumDeclaration( ctx );
			List<String> values = new ArrayList<>();
			ctx.enumConstants().enumConstant().forEach(enumConst -> values.add( enumConst.identifier().getText() ));
			return new UnlinkedEnum(ctx.identifier().getText(), values);
		}
	}

/*	private static class MethodVisitor extends TweedleParserBaseVisitor<TweedleMethod> {
		@Override
		public TweedleMethod visitMethod( TweedleParser.MethodContext ctx) {
			String methodName = ctx.methodName().getText();
			InstructionVisitor instructionVisitor = new InstructionVisitor();
			List<Statement> instructions = ctx.instruction()
							.stream()
							.map(instruction -> instruction.accept(instructionVisitor))
							.collect(toList());
			return new TweedleMethod(methodName, instructions);
		}
	}

	private static class InstructionVisitor extends TweedleParserBaseVisitor<Statement> {

		@Override
		public Statement visitInstruction(TweedleParser.InstructionContext ctx) {
			String instructionName = ctx.getText();
			return new Statement(instructionName);
		}
	}*/

	class ThrowOnParseErrorStrategy extends DefaultErrorStrategy
	{
		// Wrap and rethrow so it is not caught by the rule function catches.
		@Override
		public void recover(Parser recognizer, RecognitionException e) {
			addExceptionToAllContexts( recognizer, e );
			throw new ParseCancellationException(e);
		}

		// Don't try to recover inline; if the parser recovers, it won't throw an exception.
		@Override
		public Token recoverInline(Parser recognizer) throws RecognitionException
		{
			InputMismatchException e = new InputMismatchException(recognizer);
			addExceptionToAllContexts( recognizer, e );
			throw new ParseCancellationException(e);
		}

		private void addExceptionToAllContexts( Parser recognizer, RecognitionException e ) {
			for (ParserRuleContext context = recognizer.getContext(); context != null; context = context.getParent()) {
				context.exception = e;
			}
		}
	}
}
