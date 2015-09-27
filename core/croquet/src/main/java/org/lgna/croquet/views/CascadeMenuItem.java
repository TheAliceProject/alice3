/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/

package org.lgna.croquet.views;

/**
 * @author Dennis Cosgrove
 */
public class CascadeMenuItem extends ViewController<javax.swing.JMenuItem, org.lgna.croquet.CascadeItem<?, ?>> {
	private final org.lgna.croquet.imp.cascade.RtRoot<?, ?> rtRoot;
	private boolean isIconSet;
	private javax.swing.Icon setIcon;

	public CascadeMenuItem( org.lgna.croquet.CascadeItem<?, ?> model, org.lgna.croquet.imp.cascade.RtRoot<?, ?> rtRoot ) {
		super( model );
		this.rtRoot = rtRoot;
	}

	protected javax.swing.Icon getSetIcon() {
		return this.setIcon;
	}

	public boolean isIconSet() {
		return this.isIconSet;
	}

	public void setIconSet( boolean isIconSet ) {
		this.isIconSet = isIconSet;
	}

	public javax.swing.Icon getIcon() {
		return this.getAwtComponent().getIcon();
	}

	public void setIcon( javax.swing.Icon icon ) {
		this.setIconSet( true );
		this.setIcon = icon;
	}

	@Override
	protected javax.swing.JMenuItem createAwtComponent() {
		return new javax.swing.JMenuItem() {
			private java.awt.Cursor pushedCursor;

			@Override
			public javax.swing.Icon getIcon() {
				//note: much of the cascading menu system leverages icons
				//				if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.areIconsDisplayedInMenus() ) {
				if( CascadeMenuItem.this.isIconSet() ) {
					return CascadeMenuItem.this.getSetIcon();
				} else {
					return super.getIcon();
				}
				//				} else {
				//					return null;
				//				}
			}
			//			@Override
			//			protected void processMouseEvent( java.awt.event.MouseEvent e ) {
			//				int id = e.getID();
			//				boolean isSuperRequired = true;
			//				if( id == java.awt.event.MouseEvent.MOUSE_PRESSED ) {
			//					if( rtRoot != null ) {
			//						if( rtRoot.getElement().getCascadeRejectorCount() > 0 ) {
			//							this.pushedCursor = java.awt.dnd.DragSource.DefaultMoveNoDrop;
			//							edu.cmu.cs.dennisc.java.awt.CursorUtilities.pushAndSet( e.getComponent(), this.pushedCursor );
			//							isSuperRequired = false;
			//						}
			//					}
			//				} else if( id == java.awt.event.MouseEvent.MOUSE_RELEASED ) {
			//					if( this.pushedCursor != null ) {
			//						java.awt.Cursor poppedCursor = edu.cmu.cs.dennisc.java.awt.CursorUtilities.popAndSet( e.getComponent() );
			//						if( this.pushedCursor != poppedCursor ) {
			//							edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this.pushedCursor, poppedCursor );
			//						}
			//						this.pushedCursor = null;
			//						isSuperRequired = false;
			//					}
			//				}
			//				if( isSuperRequired ) {
			//					super.processMouseEvent( e );
			//				}
			//			}
		};
	}
}
