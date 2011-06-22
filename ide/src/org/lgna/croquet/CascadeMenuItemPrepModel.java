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
public class CascadeMenuItemPrepModel<T> extends AbstractMenuModel {
	public static class CascadeMenuPrepModelResolver<T> implements org.lgna.croquet.resolvers.CodableResolver< CascadeMenuItemPrepModel<T> > {
		private final CascadeMenuItemPrepModel<T> model;
		public CascadeMenuPrepModelResolver( CascadeMenuItemPrepModel<T> model ) {
			this.model = model;
		}
		public CascadeMenuPrepModelResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			org.lgna.croquet.resolvers.CodableResolver<Cascade<T>> resolver = binaryDecoder.decodeBinaryEncodableAndDecodable();
			Cascade<T> model = resolver.getResolved();
			this.model = model.getMenuItemPrepModel();
		}
		public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
			org.lgna.croquet.resolvers.CodableResolver<Cascade<T>> resolver = this.model.cascade.getCodableResolver();
			binaryEncoder.encode( resolver );
		}
		public CascadeMenuItemPrepModel<T> getResolved() {
			return this.model;
		}
	}
	private final Cascade<T> cascade;
	/*package-private*/ CascadeMenuItemPrepModel( Cascade<T> cascade ) {
		super( java.util.UUID.fromString( "a6d47082-8859-4b7c-b654-37e928aa67ed" ), cascade.getClass() );
		assert cascade != null;
		this.cascade = cascade;
	}
	public Cascade<T> getCompletionModel() {
		return this.cascade;
	}
	@Override
	protected CascadeMenuPrepModelResolver<T> createCodableResolver() {
		return new CascadeMenuPrepModelResolver<T>( this );
	}
	private static class ComponentListener<T> implements java.awt.event.ComponentListener {
		private org.lgna.croquet.history.CascadePopupPrepStep< T > prepStep;
		public ComponentListener( org.lgna.croquet.history.CascadePopupPrepStep< T > prepStep ) {
			this.prepStep = prepStep;
		}
		public org.lgna.croquet.history.CascadePopupPrepStep< T > getPrepStep() {
			return this.prepStep;
		}
		public void setPrepStep( org.lgna.croquet.history.CascadePopupPrepStep< T > prepStep ) {
			this.prepStep = prepStep;
		}
		public void componentShown( java.awt.event.ComponentEvent e ) {
		}
		public void componentMoved( java.awt.event.ComponentEvent e ) {
		}
		public void componentResized( java.awt.event.ComponentEvent e ) {
			org.lgna.croquet.history.TransactionManager.firePopupMenuResized( this.prepStep );
		}
		public void componentHidden( java.awt.event.ComponentEvent e ) {
		}
	};
	private static class Listeners {
		private final javax.swing.event.PopupMenuListener popupMenuListener;
		private final ComponentListener componentListener;
		public Listeners( javax.swing.event.PopupMenuListener popupMenuListener, ComponentListener componentListener ) {
			this.popupMenuListener = popupMenuListener;
			this.componentListener = componentListener;
		}
		public javax.swing.event.PopupMenuListener getPopupMenuListener() {
			return this.popupMenuListener;
		}
		public ComponentListener getComponentListener() {
			return this.componentListener;
		}
	}
	private java.util.Map< org.lgna.croquet.components.MenuItemContainer, Listeners > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	@Override
	protected void handleShowing( org.lgna.croquet.components.MenuItemContainer menuItemContainer, javax.swing.event.PopupMenuEvent e ) {
		super.handleShowing( menuItemContainer, e );
		javax.swing.JPopupMenu jPopupMenu = (javax.swing.JPopupMenu)e.getSource();
		//javax.swing.JMenu jMenu = (javax.swing.JMenu)jPopupMenu.getInvoker();
		//org.lgna.croquet.components.MenuItemContainer menuItemContainer = (org.lgna.croquet.components.MenuItemContainer)org.lgna.croquet.components.Component.lookup( jMenu );
		final org.lgna.croquet.cascade.RtRoot< T,org.lgna.croquet.history.CascadeCompletionStep< T > > rtRoot = new org.lgna.croquet.cascade.RtRoot< T,org.lgna.croquet.history.CascadeCompletionStep< T > >( this.getCompletionModel().getRoot() );
		if( rtRoot.isGoodToGo() ) {
			throw new RuntimeException( "todo" );
		} else {
			final org.lgna.croquet.history.CascadePopupPrepStep< T > prepStep = org.lgna.croquet.history.TransactionManager.addCascadePopupPrepStep( cascade.getPopupPrepModel(), null );

			Listeners listeners = map.get( menuItemContainer );
			if( listeners != null ) {
				listeners.componentListener.setPrepStep( prepStep );
			} else {
				ComponentListener componentListener = new ComponentListener< T >( prepStep );
				javax.swing.event.PopupMenuListener popupMenuListener = rtRoot.createPopupMenuListener( menuItemContainer );
				listeners = new Listeners( popupMenuListener, componentListener );
				this.map.put( menuItemContainer, listeners );
			}
			jPopupMenu.addComponentListener( listeners.getComponentListener() );
			//jPopupMenu.addPopupMenuListener( listeners.getPopupMenuListener() );
			listeners.getPopupMenuListener().popupMenuWillBecomeVisible( e );
			this.cascade.prologue();
		}
	}
	@Override
	protected void handleHiding( org.lgna.croquet.components.MenuItemContainer menuItemContainer, javax.swing.event.PopupMenuEvent e ) {
		Listeners listeners = map.get( menuItemContainer );
		javax.swing.JPopupMenu jPopupMenu = ((javax.swing.JMenu)menuItemContainer.getViewController().getAwtComponent()).getPopupMenu();
		jPopupMenu.removeComponentListener( listeners.getComponentListener() );
		//jPopupMenu.removePopupMenuListener( listeners.getPopupMenuListener() );
		listeners.getPopupMenuListener().popupMenuWillBecomeInvisible( e );
		super.handleHiding( menuItemContainer, e );
	}
	@Override
	protected void handleCanceled( org.lgna.croquet.components.MenuItemContainer menuItemContainer, javax.swing.event.PopupMenuEvent e ) {
		Listeners listeners = map.get( menuItemContainer );
		listeners.getPopupMenuListener().popupMenuCanceled( e );
		super.handleCanceled( menuItemContainer, e );
	}
}
