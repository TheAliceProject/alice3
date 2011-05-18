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

package org.lgna.croquet.components;

/**
 * @author Dennis Cosgrove
 */
public abstract class ViewController< J extends javax.swing.JComponent, M extends edu.cmu.cs.dennisc.croquet.Model > extends JComponent< J > {
	private M model;
	
	public ViewController( M model ) {
		this.model = model;
		if( this.model != null ) {
			this.model.initializeIfNecessary();
		}
	}
	public M getModel() {
		return model;
	}
	private boolean isPopupMenuOperationLimitedToRightMouseButton = true;
//	public boolean isPopupMenuOperationLimitedToRightMouseButton() {
//		return this.isPopupMenuOperationLimitedToRightMouseButton;
//	}
//	public void setPopupMenuOperationLimitedToRightMouseButton(boolean isPopupMenuOperationLimitedToRightMouseButton) {
//		this.isPopupMenuOperationLimitedToRightMouseButton = isPopupMenuOperationLimitedToRightMouseButton;
//	}
	
	private edu.cmu.cs.dennisc.croquet.StandardPopupOperation popupMenuOperation;
	public final edu.cmu.cs.dennisc.croquet.StandardPopupOperation getPopupMenuOperation() {
		if( this.popupMenuOperation != null ) {
			this.popupMenuOperation.setFirstComponentHint( this );
		}
		return this.popupMenuOperation;
	}
	public final void setPopupMenuOperation( edu.cmu.cs.dennisc.croquet.StandardPopupOperation popupMenuOperation ) {
		if( this.getAwtComponent().getParent() == null ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "warning: setPopupMenuOperation" );
		}
		if( this.popupMenuOperation != null ) {
			this.getAwtComponent().removeMouseListener( this.lenientMouseClickAdapter );
			this.getAwtComponent().removeMouseMotionListener( this.lenientMouseClickAdapter );
			this.popupMenuOperation.removeComponent( this );
		}
		this.popupMenuOperation = popupMenuOperation;
		if( this.popupMenuOperation != null ) {
			this.popupMenuOperation.addComponent( this );
			this.getAwtComponent().addMouseListener( this.lenientMouseClickAdapter );
			this.getAwtComponent().addMouseMotionListener( this.lenientMouseClickAdapter );
		}
	}

	private edu.cmu.cs.dennisc.java.awt.event.LenientMouseClickAdapter lenientMouseClickAdapter = new edu.cmu.cs.dennisc.java.awt.event.LenientMouseClickAdapter() {
		@Override
		protected void mouseQuoteClickedUnquote(java.awt.event.MouseEvent e, int quoteClickCountUnquote) {
			if( quoteClickCountUnquote == 1 ) {
				if( ViewController.this.popupMenuOperation != null ) {
					if( ViewController.this.isPopupMenuOperationLimitedToRightMouseButton==false || edu.cmu.cs.dennisc.java.awt.event.MouseEventUtilities.isQuoteRightUnquoteMouseButton( e ) ) {
						ViewController.this.popupMenuOperation.fire( e, ViewController.this );
					}
				}
			}
		}
	};
	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		M model = this.getModel();
		if( model != null ) {
			model.addComponent( this );
		}
	}
	@Override
	protected void handleUndisplayable() {
		M model = this.getModel();
		if( model != null ) {
			model.removeComponent( this );
		}
		super.handleUndisplayable();
	}
	
//	@Override
//	protected void handleAddedTo( Component<?> parent ) {
//		super.handleAddedTo( parent );
//		if( this.popupMenuOperation != null ) {
//			this.getAwtComponent().addMouseListener( this.lenientMouseClickAdapter );
//			this.getAwtComponent().addMouseMotionListener( this.lenientMouseClickAdapter );
//		}
//	}
//	@Override
//	protected void handleRemovedFrom( Component<?> parent ) {
//		if( this.popupMenuOperation != null ) {
//			this.getAwtComponent().removeMouseListener( this.lenientMouseClickAdapter );
//			this.getAwtComponent().removeMouseMotionListener( this.lenientMouseClickAdapter );
//		}
//		super.handleRemovedFrom( parent );
//	}
}
