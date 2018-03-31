package org.alice.tweedle.unlinked;

import org.alice.tweedle.TweedleLexer;
import org.alice.tweedle.TweedleParser;
import org.alice.tweedle.TweedleParserBaseVisitor;
import org.antlr.v4.runtime.*;

import java.util.ArrayList;
import java.util.List;

public class TweedleUnlinkedParser {

	public UnlinkedType parse(String someLangSourceCode) {
		CharStream charStream = new ANTLRInputStream(someLangSourceCode);
		TweedleLexer lexer = new TweedleLexer(charStream);
		TokenStream tokens = new CommonTokenStream(lexer);
		TweedleParser parser = new TweedleParser(tokens);

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
			List<UnlinkedMethod> methods = ctx.method()
							.stream()
							.map(method -> method.accept(methodVisitor))
							.collect(toList());
			return new UnlinkedClass(typeName, methods);
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

/*	private static class MethodVisitor extends TweedleParserBaseVisitor<UnlinkedMethod> {
		@Override
		public UnlinkedMethod visitMethod( TweedleParser.MethodContext ctx) {
			String methodName = ctx.methodName().getText();
			StatementVisitor statementVisitor = new StatementVisitor();
			List<UnlinkedStatement> statements = ctx.statement()
							.stream()
							.map(statement -> statement.accept(statementVisitor))
							.collect(toList());
			return new UnlinkedMethod(methodName, statements);
		}
	}

	private static class StatementVisitor extends TweedleParserBaseVisitor<UnlinkedStatement> {

		@Override
		public UnlinkedStatement visitStatement(TweedleParser.StatementContext ctx) {
			String statementName = ctx.getText();
			return new UnlinkedStatement(statementName);
		}
	}*/
}
