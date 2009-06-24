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
package org.alice.ide.common;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractStatementPane extends org.alice.ide.common.StatementLikeSubstance {
	private Factory factory;
	public Factory getFactory() {
		return this.factory;
	}
	
	private edu.cmu.cs.dennisc.property.event.PropertyListener isEnabledListener = new edu.cmu.cs.dennisc.property.event.PropertyListener() {
		public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
		}
		public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			AbstractStatementPane.this.repaint();
		}
	}; 
	private edu.cmu.cs.dennisc.alice.ast.Statement statement;
	private edu.cmu.cs.dennisc.alice.ast.StatementListProperty owner;
	public AbstractStatementPane( Factory factory, edu.cmu.cs.dennisc.alice.ast.Statement statement, edu.cmu.cs.dennisc.alice.ast.StatementListProperty owner ) {
		super( org.alice.ide.common.StatementLikeSubstance.getClassFor(statement), javax.swing.BoxLayout.LINE_AXIS );
		this.factory = factory;
		this.statement = statement;
		this.owner = owner;
	}

	@Override
	public void addNotify() {
		super.addNotify();
		this.factory.getStatementMap().put( this.statement, this );
		this.statement.isEnabled.addPropertyListener( this.isEnabledListener );
	}
	@Override
	public void removeNotify() {
		this.statement.isEnabled.removePropertyListener( this.isEnabledListener );
		this.factory.getStatementMap().remove( this.statement );
		super.removeNotify();
	}
	public edu.cmu.cs.dennisc.alice.ast.Statement getStatement() {
		return this.statement;
	}
	public edu.cmu.cs.dennisc.alice.ast.StatementListProperty getOwner() {
		return this.owner;
	}
	
//	@Override
//	protected boolean isClickReservedForSelection() {
//		return true;
//	}

//	//todo?
//	protected java.util.List< org.alice.ide.operations.AbstractActionOperation > updateOperationsListForAltMenu( java.util.List< org.alice.ide.operations.AbstractActionOperation > rv ) {
//		if( this.statement instanceof edu.cmu.cs.dennisc.alice.ast.Comment ) {
//			//pass
//		} else {
//			if( this.statement.isEnabled.getValue() ) {
//				rv.add(  new org.alice.ide.operations.ast.DisableStatementOperation( this.statement ) );
//			} else {
//				rv.add(  new org.alice.ide.operations.ast.EnableStatementOperation( this.statement ) );
//			}
//		}
//		if( this.owner != null ) {
//			rv.add(  new org.alice.ide.operations.ast.DeleteStatementOperation( this.statement, this.owner ) );
//		}
//		return rv;
//	}
//	@Override
//	protected void handleRightMousePress( java.awt.event.MouseEvent e ) {
//		super.handleRightMousePress( e );
//		java.util.List< org.alice.ide.AbstractActionOperation > operations = new java.util.LinkedList< org.alice.ide.AbstractActionOperation >();
//		this.updateOperationsListForAltMenu( operations );
//		if( operations.size() > 0 ) {
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: popup menu" );
////			javax.swing.JPopupMenu popupMenu = getIDE().createJPopupMenu( operations );
////			popupMenu.show( this, e.getX(), e.getY() );
//		}
//	}

	@Override
	public void paint( java.awt.Graphics g ) {
		super.paint( g );
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		if( this.statement.isEnabled.getValue() ) {
			//pass
		} else {
			g2.setPaint( zoot.PaintUtilities.getDisabledTexturePaint() );
			this.fillBounds( g2 );
		}
	}
	
//	@Override
//	protected boolean isActuallyPotentiallyDraggable() {
//		return true;
//	}
}
