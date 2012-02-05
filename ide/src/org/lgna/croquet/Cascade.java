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
public abstract class Cascade<T> extends CompletionModel {
	public static class InternalRootResolver<T> extends IndirectResolver< InternalRoot<T>, Cascade<T> > {
		private InternalRootResolver( Cascade<T> indirect ) {
			super( indirect );
		}
		public InternalRootResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}
		@Override
		protected InternalRoot<T> getDirect( Cascade<T> indirect ) {
			return indirect.getRoot();
		}
	}

	public static final class InternalRoot<T> extends CascadeRoot< T, org.lgna.croquet.history.CascadeCompletionStep< T > > {
		private final Cascade< T > cascade;
		private InternalRoot( Cascade< T > cascade, CascadeBlank< T >[] blanks ) {
			super( java.util.UUID.fromString( "40fe9d1b-003d-4108-9f38-73fccb29b978" ), blanks );
			this.cascade = cascade;
		}
		@Override
		protected InternalRootResolver<T> createResolver() {
			return new InternalRootResolver<T>( this.cascade );
		}
		@Override
		public Cascade< T > getCompletionModel() {
			return this.cascade;
		}
		@Override
		public Class< T > getComponentType() {
			return this.cascade.getComponentType();
		}

		@Override
		public void prologue() {
			this.cascade.prologue();
		}
		@Override
		public void epilogue() {
			this.cascade.epilogue();
		}
		@Override
		public org.lgna.croquet.history.CascadeCompletionStep< T > createCompletionStep( org.lgna.croquet.triggers.Trigger trigger ) {
			return org.lgna.croquet.history.TransactionManager.addCascadeCompletionStep( this.cascade, trigger );
		}
		@Override
		protected org.lgna.croquet.edits.Edit createEdit(org.lgna.croquet.history.CascadeCompletionStep<T> completionStep, T[] values) {
			return this.cascade.createEdit( completionStep, values );
		}
	}

	private final Class< T > componentType;
	private final InternalRoot< T > root;

	public Cascade( Group group, java.util.UUID id, Class< T > componentType, CascadeBlank< T >[] blanks ) {
		super( group, id );
		this.componentType = componentType;
		this.root = new InternalRoot< T >( this, blanks );
	}
	public Cascade( Group group, java.util.UUID id, Class< T > componentType, CascadeBlank< T > blank ) {
		this( group, id, componentType, new CascadeBlank[] { blank } );
	}
	public InternalRoot< T > getRoot() {
		return this.root;
	}
	@Override
	protected void localize() {
	}
	@Override
	public org.lgna.croquet.history.Step< ? > fire( org.lgna.croquet.triggers.Trigger trigger ) {
		return this.root.getPopupPrepModel().fire( trigger );
	}
	@Override
	public boolean isAlreadyInState( org.lgna.croquet.edits.Edit< ? > edit ) {
		return false;
	}
	public Class< T > getComponentType() {
		return this.componentType;
	}
	protected void prologue() {
	}
	protected void epilogue() {
	}
	protected abstract org.lgna.croquet.edits.Edit< ? extends Cascade< T > > createEdit( org.lgna.croquet.history.CascadeCompletionStep< T > completionStep, T[] values );

	public static final class InternalMenuModelResolver<T> extends IndirectResolver< InternalMenuModel<T>, Cascade< T > > {
		private InternalMenuModelResolver( Cascade< T > indirect ) {
			super( indirect );
		}
		public InternalMenuModelResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}
		@Override
		protected InternalMenuModel<T> getDirect( Cascade< T > indirect ) {
			return indirect.getMenuModel();
		}
	}
	//todo: reduce visibility
	public static final class InternalMenuModel<T> extends AbstractMenuModel {
		private final Cascade<T> cascade;
		private InternalMenuModel( Cascade<T> cascade ) {
			super( java.util.UUID.fromString( "d5ac0f5a-6f04-4c68-94c3-96d32775fd4e" ), cascade.getClass() );
			assert cascade != null;
			this.cascade = cascade;
		}
		public Cascade<T> getCascade() {
			return this.cascade;
		}
		@Override
		public boolean isEnabled() {
			return this.cascade.isEnabled();
		}
		@Override
		public void setEnabled( boolean isEnabled ) {
			this.cascade.setEnabled( isEnabled );
		}
		@Override
		protected InternalMenuModelResolver<T> createResolver() {
			return new InternalMenuModelResolver<T>( this.cascade );
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
			final org.lgna.croquet.cascade.RtRoot< T,org.lgna.croquet.history.CascadeCompletionStep< T > > rtRoot = new org.lgna.croquet.cascade.RtRoot< T,org.lgna.croquet.history.CascadeCompletionStep< T > >( this.getCascade().getRoot() );
			if( rtRoot.isAutomaticallyDetermined() ) {
				throw new RuntimeException( "todo" );
			} else {
				final org.lgna.croquet.history.CascadePopupPrepStep< T > prepStep = org.lgna.croquet.history.TransactionManager.addCascadePopupPrepStep( cascade.getRoot().getPopupPrepModel(), null );

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
	private InternalMenuModel<T> menuModel;
	public synchronized InternalMenuModel<T> getMenuModel() {
		if( this.menuModel != null ) {
			//pass
		} else {
			this.menuModel = new InternalMenuModel<T>( this );
		}
		return this.menuModel;
	}
	@Override
	protected StringBuilder updateTutorialStepText( StringBuilder rv, org.lgna.croquet.history.Step< ? > step, org.lgna.croquet.edits.Edit< ? > edit, org.lgna.croquet.UserInformation userInformation ) {
		return rv;
	}
}