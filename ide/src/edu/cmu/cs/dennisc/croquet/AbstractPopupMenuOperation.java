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
package edu.cmu.cs.dennisc.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractPopupMenuOperation extends Operation<PopupMenuOperationContext> {
	public static final edu.cmu.cs.dennisc.croquet.Group POPUP_MENU_GROUP = new edu.cmu.cs.dennisc.croquet.Group( java.util.UUID.fromString( "4fe7cbeb-627f-4965-a2d3-f4bf42796c59" ) );

	public AbstractPopupMenuOperation( java.util.UUID individualUUID ) {
		super( POPUP_MENU_GROUP, individualUUID );
	}
	public abstract Model[] getOperations();
	@Override
	protected PopupMenuOperationContext createContext( ModelContext parent ) {
		return new PopupMenuOperationContext( parent );
	}
 	@Override
	protected final void perform(PopupMenuOperationContext context) {
		PopupMenu popupMenu = this.createPopupMenu();
//		javax.swing.JPopupMenu popupMenu = new javax.swing.JPopupMenu();
//		popupMenu.addPopupMenuListener( new javax.swing.event.PopupMenuListener() {
//			public void popupMenuWillBecomeVisible( javax.swing.event.PopupMenuEvent e ) {
//			}
//			public void popupMenuWillBecomeInvisible( javax.swing.event.PopupMenuEvent e ) {
//			}
//			public void popupMenuCanceled( javax.swing.event.PopupMenuEvent e ) {
//			}
//		} );

		Component<?> component = context.getComponent();
		java.awt.Point pt = context.getPoint();
		if (pt != null) {
			popupMenu.showAtLocation( component, pt.x, pt.y );
		} else {
			popupMenu.showBelow( component );
		}
	}

	private void addPopupMenu( PopupMenu popupMenu ) {
		this.addComponent( popupMenu );
	}
	private void removePopupMenu( PopupMenu popupMenu ) {
		this.removeComponent( popupMenu );
	}

	private PopupMenu createPopupMenu() {
		Application.getSingleton().register( this );
		PopupMenu rv = new PopupMenu( this ) {
			@Override
			protected void handleAddedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
				super.handleAddedTo( parent );
				AbstractPopupMenuOperation.this.addPopupMenu(this);
			}

			@Override
			protected void handleRemovedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
				AbstractPopupMenuOperation.this.removePopupMenu(this);
				super.handleRemovedFrom( parent );
			}
		};
		Application.addMenuElements( rv, this.getOperations() );
		return rv;
	}
}
