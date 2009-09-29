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
public class ThisPane extends AccessiblePane {
	private static final edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava TYPE_FOR_NULL = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( Void.class );
	private edu.cmu.cs.dennisc.alice.ast.AbstractType type = TYPE_FOR_NULL;
	private org.alice.ide.event.IDEListener ideAdapter = new org.alice.ide.event.IDEListener() {
		public void fieldSelectionChanging( org.alice.ide.event.FieldSelectionEvent e ) {
		}
		public void fieldSelectionChanged( org.alice.ide.event.FieldSelectionEvent e ) {
		}
		public void focusedCodeChanging( org.alice.ide.event.FocusedCodeChangeEvent e ) {
		}
		public void focusedCodeChanged( org.alice.ide.event.FocusedCodeChangeEvent e ) {
			ThisPane.this.updateBasedOnFocusedCode( e.getNextValue() );
		}
		public void localeChanging( org.alice.ide.event.LocaleEvent e ) {
		}
		public void localeChanged( org.alice.ide.event.LocaleEvent e ) {
		}
		public void projectOpening( org.alice.ide.event.ProjectOpenEvent e ) {
		}
		public void projectOpened( org.alice.ide.event.ProjectOpenEvent e ) {
		}
		public void transientSelectionChanging( org.alice.ide.event.TransientSelectionEvent e ) {
		}
		public void transientSelectionChanged( org.alice.ide.event.TransientSelectionEvent e ) {
		}
	};

	public ThisPane() {
		edu.cmu.cs.dennisc.zoot.ZLabel label = edu.cmu.cs.dennisc.zoot.ZLabel.acquire( getIDE().getTextForThis() );
		this.add( label );
		this.setBackground( getIDE().getColorFor( edu.cmu.cs.dennisc.alice.ast.ThisExpression.class ) );
	}
	@Override
	public void addNotify() {
		super.addNotify();
		this.updateBasedOnFocusedCode( org.alice.ide.IDE.getSingleton().getFocusedCode() );
		getIDE().addIDEListener( this.ideAdapter );
	}
	@Override
	public void removeNotify() {
		getIDE().removeIDEListener( this.ideAdapter );
		super.removeNotify();
	}
	private void updateBasedOnFocusedCode( edu.cmu.cs.dennisc.alice.ast.AbstractCode code ) {
		if( code != null ) {
			this.type = code.getDeclaringType();
		} else {
			this.type = null;
		}
		if( this.type != null ) {
			this.setToolTipText( "the current instance of " + this.type.getName() + " is referred to as " + getIDE().getTextForThis() );
		} else {
			this.type = TYPE_FOR_NULL;
			this.setToolTipText( null );
		}
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.AbstractType getExpressionType() {
		return this.type;
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.Expression createExpression( edu.cmu.cs.dennisc.alice.ast.Expression... expressions ) {
		return new edu.cmu.cs.dennisc.alice.ast.ThisExpression();
	}

	@Override
	public void paint( java.awt.Graphics g ) {
		super.paint( g );
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		if( this.type == TYPE_FOR_NULL ) {
			g2.setPaint( edu.cmu.cs.dennisc.zoot.PaintUtilities.getDisabledTexturePaint() );
			this.fillBounds( g2 );
		}
	}
	
}
