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

package org.lgna.croquet;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.PopupPrepStep;
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.imp.cascade.RtRoot;
import org.lgna.croquet.triggers.NullTrigger;
import org.lgna.croquet.triggers.Trigger;
import org.lgna.croquet.views.DragComponent;
import org.lgna.croquet.views.MenuItemContainer;
import org.lgna.croquet.views.imp.JDropProxy;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.event.ComponentEvent;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class Cascade<T> extends AbstractCompletionModel implements JDropProxy.Hider {
	public static final class InternalRoot<T> extends CascadeRoot<T, Cascade<T>> {
		private final Cascade<T> cascade;

		private InternalRoot( Cascade<T> cascade ) {
			super( UUID.fromString( "40fe9d1b-003d-4108-9f38-73fccb29b978" ) );
			this.cascade = cascade;
		}

		@Override
		public List<? extends CascadeBlank<T>> getBlanks() {
			return this.cascade.getBlanks();
		}

		@Override
		public Cascade<T> getCompletionModel() {
			return this.cascade;
		}

		@Override
		public Class<T> getComponentType() {
			return this.cascade.getComponentType();
		}

		@Override
		public void prologue() {
			super.prologue();
			this.cascade.prologue();
		}

		@Override
		public void epilogue() {
			this.cascade.epilogue();
			super.epilogue();
		}

		@Override
		public void handleCompletion( Trigger trigger, RtRoot<T, Cascade<T>> rtRoot ) {
			UserActivity activity = trigger.getUserActivity();
			try {

				T[] values = rtRoot.createValues( this.getComponentType() );
				Edit edit = cascade.createEdit( activity, values );
				if( edit != null ) {
					if (activity.getCompletionStep() == null) {
						recordCompletionModel( activity );
					}
					activity.commitAndInvokeDo( edit );
				} else {
					activity.cancel();
				}
			} finally {
				this.getPopupPrepModel().handleFinally();
			}
		}
	}

	private final Class<T> componentType;
	private final InternalRoot<T> root;

	public Cascade( Group group, UUID id, Class<T> componentType ) {
		super( group, id );
		this.componentType = componentType;
		this.root = new InternalRoot<T>( this );
	}

	protected abstract List<? extends CascadeBlank<T>> getBlanks();

	@Override
	public List<List<PrepModel>> getPotentialPrepModelPaths( Edit edit ) {
		return Lists.newArrayListOfSingleArrayList( this.root.getPopupPrepModel() );
	}

	public InternalRoot<T> getRoot() {
		return this.root;
	}

	@Override
	protected void localize() {
	}

	@Override
	public final void fire( Trigger trigger ) {
		Model surrogateModel = this.root.getPopupPrepModel();
		if( surrogateModel != null ) {
			Logger.errln( "todo: end use of surrogate", this );
			surrogateModel.fire( trigger );
		} else {
			super.fire( trigger );
		}
	}

	@Override
	protected void performInActivity( UserActivity userActivity ) {
		throw new UnsupportedOperationException();
	}

	public Class<T> getComponentType() {
		return this.componentType;
	}

	protected void prologue() {
	}

	protected void epilogue() {
		this.hideDropProxyIfNecessary();
	}

	protected abstract Edit createEdit( UserActivity userActivity, T[] values );

	//todo: reduce visibility
	public static final class InternalMenuModel<T> extends AbstractMenuModel {
		private final Cascade<T> cascade;

		private InternalMenuModel( Cascade<T> cascade ) {
			super( UUID.fromString( "d5ac0f5a-6f04-4c68-94c3-96d32775fd4e" ), cascade.getClass() );
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

		private static class ComponentListener<T> implements java.awt.event.ComponentListener {
			private PopupPrepStep prepStep;

			public ComponentListener( PopupPrepStep prepStep ) {
				this.prepStep = prepStep;
			}

			public PopupPrepStep getPrepStep() {
				return this.prepStep;
			}

			public void setPrepStep( PopupPrepStep prepStep ) {
				this.prepStep = prepStep;
			}

			@Override
			public void componentShown( ComponentEvent e ) {
			}

			@Override
			public void componentMoved( ComponentEvent e ) {
			}

			@Override
			public void componentResized( ComponentEvent e ) {
				prepStep.firePopupMenuResized();
			}

			@Override
			public void componentHidden( ComponentEvent e ) {
			}
		};

		private static class Listeners {
			private final PopupMenuListener popupMenuListener;
			private final ComponentListener componentListener;

			public Listeners( PopupMenuListener popupMenuListener, ComponentListener componentListener ) {
				this.popupMenuListener = popupMenuListener;
				this.componentListener = componentListener;
			}

			public PopupMenuListener getPopupMenuListener() {
				return this.popupMenuListener;
			}

			public ComponentListener getComponentListener() {
				return this.componentListener;
			}
		}

		private Map<MenuItemContainer, Listeners> map = Maps.newHashMap();

		@Override
		protected String findDefaultLocalizedText() {
			String rv = super.findDefaultLocalizedText();
			rv = this.cascade.modifyMenuTextIfDesired( rv );
			return rv;
		}

		@Override
		protected void handleShowing( MenuItemContainer menuItemContainer, PopupMenuEvent e ) {
			super.handleShowing( menuItemContainer, e );
			JPopupMenu jPopupMenu = (JPopupMenu)e.getSource();
			//javax.swing.JMenu jMenu = (javax.swing.JMenu)jPopupMenu.getInvoker();
			//org.lgna.croquet.components.MenuItemContainer menuItemContainer = (org.lgna.croquet.components.MenuItemContainer)org.lgna.croquet.components.Component.lookup( jMenu );
			final RtRoot<T, Cascade<T>> rtRoot = new RtRoot<T, Cascade<T>>( this.getCascade().getRoot() );
			if( rtRoot.isAutomaticallyDetermined() ) {
				throw new RuntimeException( "todo" );
			} else {
				final PopupPrepStep prepStep = PopupPrepStep.createAndAddToActivity( cascade.getRoot().getPopupPrepModel(),
																																						 NullTrigger.createUserInstance() );
				Listeners listeners = map.get( menuItemContainer );
				if( listeners != null ) {
					listeners.componentListener.setPrepStep( prepStep );
				} else {
					ComponentListener componentListener = new ComponentListener<T>( prepStep );
					PopupMenuListener popupMenuListener = rtRoot.createPopupMenuListener( menuItemContainer );
					listeners = new Listeners( popupMenuListener, componentListener );
					this.map.put( menuItemContainer, listeners );
				}
				jPopupMenu.addComponentListener( listeners.getComponentListener() );
				//jPopupMenu.addPopupMenuListener( listeners.getPopupMenuListener() );
				listeners.getPopupMenuListener().popupMenuWillBecomeVisible( e );
			}
		}

		@Override
		protected void handleHiding( MenuItemContainer menuItemContainer, PopupMenuEvent e ) {
			Listeners listeners = map.get( menuItemContainer );
			JPopupMenu jPopupMenu = ( (JMenu)menuItemContainer.getViewController().getAwtComponent() ).getPopupMenu();
			jPopupMenu.removeComponentListener( listeners.getComponentListener() );
			//jPopupMenu.removePopupMenuListener( listeners.getPopupMenuListener() );
			listeners.getPopupMenuListener().popupMenuWillBecomeInvisible( e );
			super.handleHiding( menuItemContainer, e );
		}

		@Override
		protected void handleCanceled( MenuItemContainer menuItemContainer, PopupMenuEvent e ) {
			Listeners listeners = map.get( menuItemContainer );
			listeners.getPopupMenuListener().popupMenuCanceled( e );
			super.handleCanceled( menuItemContainer, e );
		}
	}

	private InternalMenuModel<T> menuModel;

	protected String modifyMenuTextIfDesired( String text ) {
		return text;
	}

	public synchronized InternalMenuModel<T> getMenuModel() {
		if( this.menuModel != null ) {
			//pass
		} else {
			this.menuModel = new InternalMenuModel<T>( this );
		}
		return this.menuModel;
	}

	@Override
	public void appendUserRepr( StringBuilder sb ) {
		sb.append( this.getRoot().getPopupPrepModel().getName() );
	}

	private DragComponent dragSource;

	@Override
	public final void setDragSource( DragComponent dragSource ) {
		this.dragSource = dragSource;
	}

	private void hideDropProxyIfNecessary() {
		if( this.dragSource != null ) {
			this.dragSource.hideDropProxyIfNecessary();
			this.dragSource = null;
		}
	}
}
