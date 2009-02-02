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
package edu.cmu.cs.dennisc.alice.ide.editors.code;

import edu.cmu.cs.dennisc.alice.ide.editors.type.ArrayAccessTemplatePane;

/**
 * @author Dennis Cosgrove
 */
class EmptyAfforance extends edu.cmu.cs.dennisc.alice.ide.editors.common.StatementLikeSubstance {
	public EmptyAfforance() {
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8, 16, 8, 48 ) );
		edu.cmu.cs.dennisc.alice.ide.editors.common.Label label = new edu.cmu.cs.dennisc.alice.ide.editors.common.Label( "drop statement here" );
		label.setFont( label.getFont().deriveFont( java.awt.Font.ITALIC ) );
		this.add( label );
		//this.setBackground( edu.cmu.cs.dennisc.awt.ColorUtilities.createGray( 230 ) );
		this.setBackground( new java.awt.Color( 63, 63, 63, 63 ) );
	}
	@Override
	protected java.lang.Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > getStatementClass() {
		return edu.cmu.cs.dennisc.alice.ast.Statement.class;
	}
	@Override
	protected boolean isActuallyPotentiallyActive() {
		return false;
	}
	@Override
	protected boolean isActuallyPotentiallySelectable() {
		return false;
	}
	@Override
	protected boolean isKnurlDesired() {
		return false;
	}
	@Override
	protected edu.cmu.cs.dennisc.awt.BevelState getBevelState() {
		return edu.cmu.cs.dennisc.awt.BevelState.SUNKEN;
	}
}

/**
 * @author Dennis Cosgrove
 */
class StatementListPropertyPane extends AbstractListPropertyPane< edu.cmu.cs.dennisc.alice.ast.StatementListProperty > {
	public StatementListPropertyPane( final edu.cmu.cs.dennisc.alice.ast.StatementListProperty property ) {
		super( javax.swing.BoxLayout.PAGE_AXIS, property );
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 0, 12, 16 ) );
		this.addMouseListener( new java.awt.event.MouseListener() {
			public void mouseClicked( final java.awt.event.MouseEvent e ) {
				final edu.cmu.cs.dennisc.alice.ide.IDE ide = edu.cmu.cs.dennisc.alice.ide.IDE.getSingleton();
				if( ide != null ) {
					//final StatementListPropertyPane statementListPropertyPane = getStatementListPropertyPaneUnder( e, createStatementListPropertyPaneInfos( null ) );
					final StatementListPropertyPane statementListPropertyPane = StatementListPropertyPane.this;
					if( statementListPropertyPane != null ) {
						ide.promptUserForStatement( e, new edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Statement >() {
							public void handleCompletion( edu.cmu.cs.dennisc.alice.ast.Statement statement ) {
								java.awt.Point p = e.getPoint();
								//p = javax.swing.SwingUtilities.convertPoint( e.getComponent(), p, statementListPropertyPane );
								statementListPropertyPane.getProperty().add( statementListPropertyPane.calculateIndex( p ), statement );
								ide.markChanged( "statement" );
							}
							public void handleCancelation() {
							}
						} );
					}
				}
			}
			public void mouseEntered( java.awt.event.MouseEvent e ) {
			}
			public void mouseExited( java.awt.event.MouseEvent e ) {
			}
			public void mousePressed( java.awt.event.MouseEvent e ) {
			}
			public void mouseReleased( java.awt.event.MouseEvent e ) {
			}
		} );
	}
	@Override
	protected javax.swing.JComponent createComponent( Object instance ) {
		edu.cmu.cs.dennisc.alice.ast.Statement statement = (edu.cmu.cs.dennisc.alice.ast.Statement)instance;
		return AbstractStatementPane.createPane( statement, getProperty() );
	}
	@Override
	protected void refresh() {
		super.refresh();
		if( this.getComponentCount() == 0 ) {
			this.add( new EmptyAfforance() );
		}
	}
	public boolean isFigurativelyEmpty() {
		return this.getComponent( 0 ) instanceof EmptyAfforance;
	}

	private int getCenterYOfComponentAt( int i ) {
		java.awt.Component componentI = this.getComponent( i );
		return componentI.getY() + componentI.getHeight() / 2;
	}
	public int calculateIndex( java.awt.Point p ) {
		if( isFigurativelyEmpty() ) {
			return 0;
		} else {
			for( int i = 0; i < this.getComponentCount(); i++ ) {
				int yCenterI = this.getCenterYOfComponentAt( i );
				if( p.y < yCenterI ) {
					return i;
				}
			}
			return this.getComponentCount();
		}
	}

}
