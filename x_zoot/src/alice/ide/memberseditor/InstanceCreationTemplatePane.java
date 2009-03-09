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
public abstract class InstanceCreationTemplatePane extends MemberExpressionTemplatePane {
	public InstanceCreationTemplatePane( edu.cmu.cs.dennisc.alice.ast.AbstractConstructor constructor ) {
		super( constructor );
		Class< ? extends edu.cmu.cs.dennisc.alice.ast.Node > cls = edu.cmu.cs.dennisc.alice.ast.InstanceCreation.class;
		this.setBackground( alice.ide.IDE.getColorForASTClass( cls ) );
		zoot.ZLabel label = new zoot.ZLabel( "new" );
		label.setFontToScaledFont( 1.75f );
		this.add( label );
		this.add( javax.swing.Box.createHorizontalStrut( 8 ) );
		this.add( new alice.ide.ast.TypePane( constructor.getDeclaringType() ) );
		for( edu.cmu.cs.dennisc.alice.ast.AbstractParameter parameter : constructor.getParameters() ) {
			this.add( javax.swing.Box.createHorizontalStrut( 8 ) );
			this.add( new alice.ide.codeeditor.EmptyExpressionPane( parameter.getValueType() ) );
		}
		this.add( javax.swing.Box.createHorizontalGlue() );
	}
	protected edu.cmu.cs.dennisc.alice.ast.AbstractConstructor getConstructor() {
		return (edu.cmu.cs.dennisc.alice.ast.AbstractConstructor)this.getMember();
	}

	@Override
	public edu.cmu.cs.dennisc.alice.ast.AbstractType getExpressionType() {
		return this.getConstructor().getDeclaringType();
	}
	
	protected abstract void promptUserForInstanceCreation( edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.InstanceCreation > taskObserver, alice.ide.ast.DropAndDropEvent e );
	@Override
	public edu.cmu.cs.dennisc.alice.ast.Expression createExpression( final alice.ide.ast.DropAndDropEvent e ) {
		assert javax.swing.SwingUtilities.isEventDispatchThread() == false;
		edu.cmu.cs.dennisc.task.BlockingTaskObserver< edu.cmu.cs.dennisc.alice.ast.InstanceCreation > taskObserver = new edu.cmu.cs.dennisc.task.BlockingTaskObserver< edu.cmu.cs.dennisc.alice.ast.InstanceCreation >() {
			@Override
			public void run() {
				InstanceCreationTemplatePane.this.promptUserForInstanceCreation( this, e );
			}
		};
		edu.cmu.cs.dennisc.alice.ast.InstanceCreation rv = taskObserver.getResult();
		return rv;
	}
}
