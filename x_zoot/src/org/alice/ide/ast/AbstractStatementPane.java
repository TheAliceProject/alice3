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
package org.alice.ide.ast;

import org.alice.ide.codeeditor.CommentPane;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractStatementPane extends org.alice.ide.ast.StatementLikeSubstance {
	private static java.util.Map< edu.cmu.cs.dennisc.alice.ast.Statement, AbstractStatementPane > map = new java.util.HashMap< edu.cmu.cs.dennisc.alice.ast.Statement, AbstractStatementPane >();
	public static AbstractStatementPane lookup( edu.cmu.cs.dennisc.alice.ast.Statement statement ) {
		return AbstractStatementPane.map.get( statement );
	}
	
	public static AbstractStatementPane createPane( edu.cmu.cs.dennisc.alice.ast.Statement statement, edu.cmu.cs.dennisc.alice.ast.StatementListProperty statementListProperty ) {
		AbstractStatementPane rv;
		if( statement instanceof edu.cmu.cs.dennisc.alice.ast.ExpressionStatement ) {
			rv = new ExpressionStatementPane( (edu.cmu.cs.dennisc.alice.ast.ExpressionStatement)statement, statementListProperty );
		} else if( statement instanceof edu.cmu.cs.dennisc.alice.ast.Comment ) {
			rv = new CommentPane( (edu.cmu.cs.dennisc.alice.ast.Comment)statement, statementListProperty );
		} else {
			rv = new DefaultStatementPane( statement, statementListProperty );
		}
		return rv;
	}
	
	private edu.cmu.cs.dennisc.alice.ast.Statement statement;
	private edu.cmu.cs.dennisc.alice.ast.StatementListProperty owner;
	public AbstractStatementPane( edu.cmu.cs.dennisc.alice.ast.Statement statement, edu.cmu.cs.dennisc.alice.ast.StatementListProperty owner ) {
		super( org.alice.ide.ast.StatementLikeSubstance.getClassFor(statement) );
		this.statement = statement;
		
		AbstractStatementPane.map.put( this.statement, this );
		
		this.owner = owner;
		this.statement.isEnabled.addPropertyListener( new edu.cmu.cs.dennisc.property.event.PropertyListener() {
			public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
				AbstractStatementPane.this.repaint();
			}
			public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			}
		} );
		this.setBackground( org.alice.ide.IDE.getColorForASTInstance( this.statement ) );
	}
	
	public edu.cmu.cs.dennisc.alice.ast.Statement getStatement() {
		return this.statement;
	}
	public edu.cmu.cs.dennisc.alice.ast.StatementListProperty getOwner() {
		return this.owner;
	}
	protected java.util.List< org.alice.ide.AbstractActionOperation > updateOperationsListForAltMenu( java.util.List< org.alice.ide.AbstractActionOperation > rv ) {
		if( this.statement instanceof edu.cmu.cs.dennisc.alice.ast.Comment ) {
			//pass
		} else {
			if( this.statement.isEnabled.getValue() ) {
				rv.add(  new org.alice.ide.operations.ast.DisableStatementOperation( this.statement ) );
			} else {
				rv.add(  new org.alice.ide.operations.ast.EnableStatementOperation( this.statement ) );
			}
		}
		rv.add(  new org.alice.ide.operations.ast.DeleteStatementOperation( this.statement, this.owner ) );
		return rv;
	}
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

	private static java.awt.TexturePaint disabledTexturePaint = null;

	private static java.awt.TexturePaint getDisabledTexturePaint() {
		if( AbstractStatementPane.disabledTexturePaint != null ) {
			//pass
		} else {
			int width = 8;
			int height = 8;
			java.awt.image.BufferedImage image = new java.awt.image.BufferedImage( width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB );
			java.awt.Graphics g = image.getGraphics();
			g.setColor( new java.awt.Color( 128, 128, 128, 96 ) );
			g.fillRect( 0, 0, width, height );
			g.setColor( java.awt.Color.DARK_GRAY );
			g.drawLine( 0, height, width, 0 );
			g.drawLine( 0, 0, 0, 0 );
			g.dispose();
			AbstractStatementPane.disabledTexturePaint = new java.awt.TexturePaint( image, new java.awt.Rectangle( 0, 0, width, height ) );
		}
		return AbstractStatementPane.disabledTexturePaint;
	}
	
	@Override
	public void paint( java.awt.Graphics g ) {
		super.paint( g );
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		if( this.statement.isEnabled.getValue() ) {
			//pass
		} else {
			g2.setPaint( AbstractStatementPane.getDisabledTexturePaint() );
			this.fillBounds( g2 );
		}
	}
	
	@Override
	protected boolean isActuallyPotentiallyDraggable() {
		return true;
	}
}
