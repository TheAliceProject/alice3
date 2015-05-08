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

package org.lgna.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractMenuModel extends StandardMenuItemPrepModel {
	public static final StandardMenuItemPrepModel SEPARATOR = null;
	private final Class<? extends AbstractElement> clsForI18N;
	private javax.swing.Action action = new javax.swing.AbstractAction() {
		@Override
		public void actionPerformed( java.awt.event.ActionEvent e ) {
		}
	};

	public AbstractMenuModel( java.util.UUID individualId, Class<? extends AbstractElement> clsForI18N ) {
		super( individualId );
		this.clsForI18N = clsForI18N;
	}

	@Override
	protected Class<? extends Element> getClassUsedForLocalization() {
		if( this.clsForI18N != null ) {
			return this.clsForI18N;
		} else {
			return this.getClass();
		}
	}

	@Override
	protected void localize() {
		safeSetNameAndMnemonic( this.action, this.findDefaultLocalizedText(), this.getLocalizedMnemonicKey() );
		this.action.putValue( javax.swing.Action.ACCELERATOR_KEY, this.getLocalizedAcceleratorKeyStroke() );
	}

	@Override
	public Iterable<? extends MenuItemPrepModel> getChildren() {
		return null;
	}

	public javax.swing.Action getAction() {
		return this.action;
	}

	private String getName() {
		return (String)this.action.getValue( javax.swing.Action.NAME );
	}

	public void setName( String name ) {
		this.action.putValue( javax.swing.Action.NAME, name );
	}

	private javax.swing.Icon getSmallIcon() {
		return (javax.swing.Icon)this.action.getValue( javax.swing.Action.SMALL_ICON );
	}

	public void setSmallIcon( javax.swing.Icon icon ) {
		this.action.putValue( javax.swing.Action.SMALL_ICON, icon );
	}

	@Override
	public boolean isEnabled() {
		return this.action.isEnabled();
	}

	@Override
	public void setEnabled( boolean isEnabled ) {
		this.action.setEnabled( isEnabled );
	}

	public void handlePopupMenuPrologue( org.lgna.croquet.views.PopupMenu popupMenu, org.lgna.croquet.history.PopupPrepStep step ) {
	}

	public void handlePopupMenuEpilogue( org.lgna.croquet.views.PopupMenu popupMenu, org.lgna.croquet.history.PopupPrepStep step ) {
	}

	protected void handleShowing( org.lgna.croquet.views.MenuItemContainer menuItemContainer, javax.swing.event.PopupMenuEvent e ) {
	}

	protected void handleHiding( org.lgna.croquet.views.MenuItemContainer menuItemContainer, javax.swing.event.PopupMenuEvent e ) {
	}

	protected void handleCanceled( org.lgna.croquet.views.MenuItemContainer menuItemContainer, javax.swing.event.PopupMenuEvent e ) {
	}

	private class PopupMenuListener implements javax.swing.event.PopupMenuListener {
		private org.lgna.croquet.views.MenuItemContainer menuItemContainer;

		public PopupMenuListener( org.lgna.croquet.views.MenuItemContainer menuItemContainer ) {
			this.menuItemContainer = menuItemContainer;
		}

		@Override
		public void popupMenuWillBecomeVisible( javax.swing.event.PopupMenuEvent e ) {
			AbstractMenuModel.this.handleShowing( this.menuItemContainer, e );
		}

		@Override
		public void popupMenuWillBecomeInvisible( javax.swing.event.PopupMenuEvent e ) {
			AbstractMenuModel.this.handleHiding( this.menuItemContainer, e );
		}

		@Override
		public void popupMenuCanceled( javax.swing.event.PopupMenuEvent e ) {
			AbstractMenuModel.this.handleCanceled( this.menuItemContainer, e );
		}
	}

	private PopupMenuListener popupMenuListener;

	public final void addPopupMenuListener( org.lgna.croquet.views.MenuItemContainer menuItemContainer ) {
		assert this.popupMenuListener == null : this;
		this.popupMenuListener = new PopupMenuListener( menuItemContainer );
		menuItemContainer.addPopupMenuListener( this.popupMenuListener );
	}

	public final void removePopupMenuListener( org.lgna.croquet.views.MenuItemContainer menuItemContainer ) {
		assert this.popupMenuListener != null : this;
		menuItemContainer.removePopupMenuListener( this.popupMenuListener );
		this.popupMenuListener = null;
	}

	public org.lgna.croquet.views.Menu createMenu() {
		return new org.lgna.croquet.views.Menu( this );
	};

	@Override
	public org.lgna.croquet.views.Menu createMenuItemAndAddTo( org.lgna.croquet.views.MenuItemContainer menuItemContainer ) {
		org.lgna.croquet.views.Menu rv = this.createMenu();
		menuItemContainer.addMenu( rv );
		return rv;
	}
}
