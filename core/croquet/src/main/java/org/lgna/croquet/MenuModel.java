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

import org.lgna.croquet.history.PopupPrepStep;
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.views.ComponentManager;
import org.lgna.croquet.views.PopupMenu;

import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class MenuModel extends AbstractMenuModel {
	public MenuModel( UUID individualId, Class<? extends AbstractElement> clsForI18N ) {
		super( individualId, clsForI18N );
	}

	public MenuModel( UUID individualId ) {
		this( individualId, null );
	}

	public final static class InternalPopupPrepModel extends PopupPrepModel {
		private MenuModel menuModel;

		private InternalPopupPrepModel( MenuModel menuModel ) {
			super( UUID.fromString( "34efc403-9eff-4151-b1c6-53dd1249a325" ) );
			this.menuModel = menuModel;
		}

		@Override
		protected Class<? extends Element> getClassUsedForLocalization() {
			return this.menuModel.getClassUsedForLocalization();
		}

		public MenuModel getMenuModel() {
			return this.menuModel;
		}

		@Override
		protected void perform( final UserActivity activity ) {
			final PopupPrepStep step = PopupPrepStep.createAndAddToActivity( this, activity );

			final PopupMenu popupMenu = new PopupMenu( this, activity ) {
				@Override
				protected void handleDisplayable() {
					prologue( activity.getTrigger() );
					//todo: investigate
					super.handleDisplayable();
					//PopupMenuOperation.this.menuModel.addPopupMenuListener( this );
					ComponentManager.addComponent( InternalPopupPrepModel.this, this );
				}

				@Override
				protected void handleUndisplayable() {
					ComponentManager.removeComponent( InternalPopupPrepModel.this, this );
					InternalPopupPrepModel.this.menuModel.removePopupMenuListener( this );
					super.handleUndisplayable();
					epilogue();
				}
			};
			//todo: investigate
			this.menuModel.addPopupMenuListener( popupMenu );

			popupMenu.addPopupMenuListener( new PopupMenuListener() {
				private PopupMenuEvent cancelEvent = null;

				@Override
				public void popupMenuWillBecomeVisible( PopupMenuEvent e ) {
					this.cancelEvent = null;
				}

				@Override
				public void popupMenuWillBecomeInvisible( PopupMenuEvent e ) {
					if( this.cancelEvent != null ) {
						System.err.println( "todo: cancel" );
						//step.getParent().cancel();
						this.cancelEvent = null;
					} else {
						System.err.println( "todo: finish" );
						//step.getParent().finish();
					}
					InternalPopupPrepModel.this.menuModel.handlePopupMenuEpilogue( popupMenu, step );

					System.err.println( "TODO: handleFinally?" );
					//					performObserver.handleFinally();
				}

				@Override
				public void popupMenuCanceled( PopupMenuEvent e ) {
					this.cancelEvent = e;
				}
			} );

			popupMenu.addComponentListener( new ComponentListener() {
				@Override
				public void componentShown( ComponentEvent e ) {
					//					java.awt.Component awtComponent = e.getComponent();
					//					edu.cmu.cs.dennisc.print.PrintUtilities.println( "componentShown", awtComponent.getLocationOnScreen(), awtComponent.getSize() );
				}

				@Override
				public void componentMoved( ComponentEvent e ) {
					//					java.awt.Component awtComponent = e.getComponent();
					//					edu.cmu.cs.dennisc.print.PrintUtilities.println( "componentMoved", awtComponent.getLocationOnScreen(), awtComponent.getSize() );
				}

				@Override
				public void componentResized( ComponentEvent e ) {
					//					java.awt.Component awtComponent = e.getComponent();
					//					edu.cmu.cs.dennisc.print.PrintUtilities.println( "componentResized", awtComponent.getLocationOnScreen(), awtComponent.getSize() );
					step.handleResized( e );
				}

				@Override
				public void componentHidden( ComponentEvent e ) {
					//					java.awt.Component awtComponent = e.getComponent();
					//					edu.cmu.cs.dennisc.print.PrintUtilities.println( "componentHidden", awtComponent.getLocationOnScreen(), awtComponent.getSize() );
				}
			} );

			this.menuModel.handlePopupMenuPrologue( popupMenu, step );

			step.showPopupMenu( popupMenu );
		}
	}

	private InternalPopupPrepModel popupPrepModel;

	public synchronized InternalPopupPrepModel getPopupPrepModel() {
		if( this.popupPrepModel != null ) {
			//pass
		} else {
			this.popupPrepModel = new InternalPopupPrepModel( this );
		}
		return this.popupPrepModel;
	}
}
