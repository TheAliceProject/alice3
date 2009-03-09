/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package alice.ide.memberseditor;

/**
 * @author Dennis Cosgrove
 */
public abstract class SetArrayAtIndexTemplatePane extends MemberStatementTemplatePane {
	public SetArrayAtIndexTemplatePane( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		super( field );
		Class< ? extends edu.cmu.cs.dennisc.alice.ast.Node > cls = edu.cmu.cs.dennisc.alice.ast.ExpressionStatement.class;
		this.setBackground( alice.ide.IDE.getColorForASTClass( cls ) );
		this.add( this.getInstanceOrTypeExpressionPane() );
		this.add( new zoot.ZLabel( "." ) );
		this.add( this.getNameLabel() );
		this.add( new zoot.ZLabel( "[" ) );
		this.add( new alice.ide.codeeditor.EmptyExpressionPane( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE ) );
		this.add( new zoot.ZLabel( "]" ) );
		this.add( new alice.ide.codeeditor.GetsPane( true ) );
		this.add( new alice.ide.codeeditor.EmptyExpressionPane( field.getValueType() ) );
		this.add( javax.swing.Box.createHorizontalGlue() );
	}
	protected edu.cmu.cs.dennisc.alice.ast.AbstractField getField() {
		return (edu.cmu.cs.dennisc.alice.ast.AbstractField)this.getMember();
	}
	protected abstract void promptUserForExpression( edu.cmu.cs.dennisc.alice.ast.AbstractType type, edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression > taskObserver,
			alice.ide.ast.DropAndDropEvent e );
	@Override
	public edu.cmu.cs.dennisc.alice.ast.Statement createStatement( final alice.ide.ast.DropAndDropEvent e ) {
		assert javax.swing.SwingUtilities.isEventDispatchThread() == false;
		final edu.cmu.cs.dennisc.alice.ast.AbstractField field = this.getField();
		edu.cmu.cs.dennisc.task.BlockingTaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression > taskObserver = new edu.cmu.cs.dennisc.task.BlockingTaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression >() {
			@Override
			public void run() {
				SetArrayAtIndexTemplatePane.this.promptUserForExpression( field.getDesiredValueType(), this, e );
			}
		};
		edu.cmu.cs.dennisc.alice.ast.Expression expression = taskObserver.getResult();

		edu.cmu.cs.dennisc.alice.ast.ExpressionStatement rv;
		if( expression != null ) {
			rv = new edu.cmu.cs.dennisc.alice.ast.ExpressionStatement();
			rv.expression.setValue( expression );
		} else {
			rv = null;
		}
		return rv;
	}

}
