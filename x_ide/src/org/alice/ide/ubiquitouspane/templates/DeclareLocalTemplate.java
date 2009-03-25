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
package org.alice.ide.ubiquitouspane.templates;

/**
 * @author Dennis Cosgrove
 */
public class DeclareLocalTemplate extends org.alice.ide.templates.StatementTemplate implements UbiquitousStatementTemplate {
	private javax.swing.JToolTip toolTip;
	public DeclareLocalTemplate() {
		super( edu.cmu.cs.dennisc.alice.ast.LocalDeclarationStatement.class );
	}
	
	@Override
	public java.awt.Component getSubject() {
		return this.toolTip.getComponent( 0 );
	}

	@Override
	public void addNotify() {
		super.addNotify();
		if( this.toolTip != null ) {
			//pass
		} else {
			UbiquitousStatementUtilities.adorn( this, org.alice.ide.ast.NodeUtilities.createIncompleteVariableDeclarationStatement() );
		}
	}
	
	public javax.swing.JComponent getJComponent() {
		return this;
	}
	public void setToolTip( javax.swing.JToolTip toolTip ) {
		this.toolTip = toolTip;
	}
	@Override
	public javax.swing.JToolTip createToolTip() {
		return this.toolTip;
	}
	@Override
	public void createStatement( zoot.event.DragAndDropEvent e, edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Statement > taskObserver ) {
		taskObserver.handleCancelation();
	}
}
