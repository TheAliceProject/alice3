/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.alice.ide.declarationseditor.events.components;

import org.lgna.project.ast.Expression;
import org.lgna.project.ast.ExpressionStatement;
import org.lgna.project.ast.MethodInvocation;

/**
 * @author Matt May
 */
public class EventsContentPanel extends org.alice.ide.codedrop.CodePanelWithDropReceptor {
	private static class RootStatementListPropertyPane extends org.alice.ide.x.components.StatementListPropertyView {
		public RootStatementListPropertyPane( org.lgna.project.ast.UserCode userCode ) {
			super( org.alice.ide.x.ProjectEditorAstI18nFactory.getInstance(), userCode.getBodyProperty().getValue().statements );
		}

		@Override
		public boolean isAcceptingOfAddEventListenerMethodInvocationStatements() {
			return true;
		}

		@Override
		protected int getBoxLayoutPad() {
			return 0;
		}

		@Override
		protected int getLeftInset() {
			return 0;
		}

		@Override
		protected int getRightInset() {
			return 0;
		}

		@Override
		protected org.lgna.croquet.components.Component<?> createComponent( org.lgna.project.ast.Statement statement ) {
			if( statement instanceof ExpressionStatement ) {
				ExpressionStatement expressionStatement = (ExpressionStatement)statement;
				Expression expression = expressionStatement.expression.getValue();
				if( expression instanceof MethodInvocation ) {
					MethodInvocation methodInvocation = (MethodInvocation)expression;
					org.alice.ide.common.AddEventListenerStatementPanel statementPanel = new org.alice.ide.common.AddEventListenerStatementPanel( expressionStatement );
					statementPanel.addComponent( new edu.cmu.cs.dennisc.matt.EventListenerComponent( methodInvocation ) );
					return statementPanel;
				}
			}
			return null;
		}
	}

	private final org.lgna.project.ast.AbstractCode code;
	private final RootStatementListPropertyPane rootPane;

	public EventsContentPanel( org.lgna.project.ast.AbstractCode code ) {
		this.code = code;
		this.rootPane = new RootStatementListPropertyPane( (org.lgna.project.ast.UserCode)code );
		this.addCenterComponent( this.rootPane );
		java.awt.Color color = org.alice.ide.IDE.getActiveInstance().getTheme().getProcedureColor();
		this.rootPane.setBackgroundColor( color );
		this.setBackgroundColor( color );
	}

	@Override
	public org.lgna.project.ast.AbstractCode getCode() {
		return this.code;
	}

	@Override
	protected org.lgna.croquet.components.Component<?> getAsSeenBy() {
		return this;
	}

	@Override
	public org.lgna.croquet.components.TrackableShape getTrackableShape( org.lgna.croquet.DropSite potentialDropSite ) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo( potentialDropSite );
		return null;
	}

	public int print( java.awt.Graphics g, java.awt.print.PageFormat pageFormat, int pageIndex ) throws java.awt.print.PrinterException {
		if( pageIndex > 0 ) {
			return NO_SUCH_PAGE;
		} else {
			//org.lgna.croquet.components.ScrollPane scrollPane = this.getScrollPane();
			org.lgna.croquet.components.Component<?> lineStart = this.getLineStartComponent();
			org.lgna.croquet.components.Component<?> lineEnd = this.getLineEndComponent();
			org.lgna.croquet.components.Component<?> pageStart = this.getPageStartComponent();
			org.lgna.croquet.components.Component<?> pageEnd = this.getPageEndComponent();

			//todo: this code will not suffice in the limit
			java.awt.Dimension scrollSize = this.rootPane.getAwtComponent().getPreferredSize();
			int width = scrollSize.width;
			int height = scrollSize.height;
			for( org.lgna.croquet.components.Component<?> component : new org.lgna.croquet.components.Component[] { lineStart, lineEnd } ) {
				if( component != null ) {
					java.awt.Dimension componentSize = component.getAwtComponent().getPreferredSize();
					width += componentSize.width;
					height = Math.max( height, componentSize.height );
				}
			}
			for( org.lgna.croquet.components.Component<?> component : new org.lgna.croquet.components.Component[] { pageStart, pageEnd } ) {
				if( component != null ) {
					java.awt.Dimension componentSize = component.getAwtComponent().getPreferredSize();
					width = Math.max( width, componentSize.width );
					height += componentSize.height;
				}
			}

			java.awt.Insets insets = this.getInsets();
			width += insets.left + insets.right;
			height += insets.top + insets.bottom;

			java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
			java.awt.geom.AffineTransform prevTransform = g2.getTransform();
			try {
				double scale = edu.cmu.cs.dennisc.java.awt.print.PageFormatUtilities.calculateScale( pageFormat, width, height );
				g2.translate( pageFormat.getImageableX(), pageFormat.getImageableY() );
				if( scale > 1.0 ) {
					g2.scale( 1.0 / scale, 1.0 / scale );
				}

				g2.setPaint( this.getBackgroundColor() );
				g2.fillRect( 0, 0, width, height );

				if( pageStart != null ) {
					int x = pageStart.getX();
					int y = pageStart.getY();
					g2.translate( x, y );
					try {
						pageStart.getAwtComponent().printAll( g2 );
					} finally {
						g2.translate( -x, -y );
					}
				}
				if( lineStart != null ) {
					int x = lineStart.getX();
					int y = lineStart.getY();
					g2.translate( x, y );
					try {
						lineStart.getAwtComponent().printAll( g2 );
					} finally {
						g2.translate( -x, -y );
					}
				}
				int x = this.rootPane.getX();
				int y = this.rootPane.getY();
				g2.translate( x, y );
				try {
					this.rootPane.getAwtComponent().printAll( g2 );
				} finally {
					g2.translate( -x, -y );
				}

				if( lineEnd != null ) {
					edu.cmu.cs.dennisc.java.util.logging.Logger.todo( lineEnd );
				}

				if( pageEnd != null ) {
					edu.cmu.cs.dennisc.java.util.logging.Logger.todo( pageEnd );
				}

			} finally {
				g2.setTransform( prevTransform );
			}
			return PAGE_EXISTS;
		}
	}
}
