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
	protected void handleDisplayable() {
		super.handleDisplayable();
		this.factory.getStatementMap().put( this.statement, this );
		this.statement.isEnabled.addPropertyListener( this.isEnabledListener );
	}
	@Override
	protected void handleUndisplayable() {
		this.statement.isEnabled.removePropertyListener( this.isEnabledListener );
		this.factory.getStatementMap().remove( this.statement );
		super.handleUndisplayable();
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
	protected void paintEpilogue(java.awt.Graphics2D g2, int x, int y, int width, int height) {
		super.paintEpilogue(g2, x, y, width, height);
		if( this.statement.isEnabled.getValue() ) {
			//pass
		} else {
			g2.setPaint( org.lgna.croquet.components.PaintUtilities.getDisabledTexturePaint() );
			this.fillBounds( g2 );
		}
	}
	
//	@Override
//	protected boolean isActuallyPotentiallyDraggable() {
//		return true;
//	}
}
