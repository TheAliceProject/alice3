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
public final class StandardPopupPrepModel extends PopupPrepModel {
	private MenuModel menuModel;
	/*package-private*/ StandardPopupPrepModel( MenuModel menuModel ) {
		super( java.util.UUID.fromString( "34efc403-9eff-4151-b1c6-53dd1249a325" ) );
		this.menuModel = menuModel;
	}
	
	@Override
	public Iterable< ? extends org.lgna.croquet.Model > getChildren() {
		return edu.cmu.cs.dennisc.java.util.Collections.newLinkedList( this.menuModel );
	}
	@Override
	protected Class< ? extends org.lgna.croquet.Model > getClassUsedForLocalization() {
		return this.menuModel.getClassUsedForLocalization();
	}
	public MenuModel getMenuModel() {
		return this.menuModel;
	}
		
//	@Override
//	public String getTutorialStepTitle( ModelContext< ? > modelContext, Edit< ? > edit, UserInformation userInformation ) {
//		SuccessfulCompletionEvent successfulCompletionEvent = modelContext != null ? modelContext.getSuccessfulCompletionEvent() : null;
//		if( successfulCompletionEvent != null ) {
//			ModelContext< ? > descendantContext = successfulCompletionEvent.getParent();
//			return descendantContext.getModel().getTutorialStepTitle( descendantContext, userInformation );
//		} else {
//			return super.getTutorialStepTitle( modelContext, edit, userInformation );
//		}
//	}
//	@Override
//	public String getTutorialNoteText( ModelContext< ? > modelContext, Edit< ? > edit, UserInformation userInformation ) {
//		SuccessfulCompletionEvent successfulCompletionEvent = modelContext != null ? modelContext.getSuccessfulCompletionEvent() : null;
//		if( successfulCompletionEvent != null ) {
//			ModelContext< ? > descendantContext = successfulCompletionEvent.getParent();
//			return descendantContext.getModel().getTutorialNoteText( descendantContext, userInformation );
//		} else {
//			return super.getTutorialNoteText( modelContext, edit, userInformation );
//		}
//	}
	
	public static class FromOwnerResolver implements org.lgna.croquet.resolvers.CodableResolver< StandardPopupPrepModel > {
		private final StandardPopupPrepModel model;
		public FromOwnerResolver( StandardPopupPrepModel model ) {
			this.model = model;
		}
		public FromOwnerResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			org.lgna.croquet.resolvers.CodableResolver<MenuModel> menuModelResolver = binaryDecoder.decodeBinaryEncodableAndDecodable();
			MenuModel menuModel = menuModelResolver.getResolved();
			this.model = menuModel.getPopupPrepModel();
		}
		public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
			org.lgna.croquet.resolvers.CodableResolver<MenuModel> menuModelResolver = this.model.menuModel.getCodableResolver();
			binaryEncoder.encode( menuModelResolver );
		}
		public StandardPopupPrepModel getResolved() {
			return this.model;
		}
	}
	@Override
	protected org.lgna.croquet.resolvers.CodableResolver< StandardPopupPrepModel > createCodableResolver() {
		return new FromOwnerResolver( this );
	}
	
	
	@Override
	protected org.lgna.croquet.history.StandardPopupPrepStep perform( org.lgna.croquet.triggers.Trigger trigger, final org.lgna.croquet.PopupPrepModel.PerformObserver performObserver ) {
		final org.lgna.croquet.history.StandardPopupPrepStep step = org.lgna.croquet.history.TransactionManager.addStandardPopupOperationStep( this, trigger );

		final org.lgna.croquet.components.PopupMenu popupMenu = new org.lgna.croquet.components.PopupMenu( this ) {
			@Override
			protected void handleDisplayable() {
				//todo: investigate
				super.handleDisplayable();
				//PopupMenuOperation.this.menuModel.addPopupMenuListener( this );
				StandardPopupPrepModel.this.addComponent( this );
			}
			@Override
			protected void handleUndisplayable() {
				StandardPopupPrepModel.this.removeComponent( this );
				StandardPopupPrepModel.this.menuModel.removePopupMenuListener( this );
				super.handleUndisplayable();
			}
		};
		//todo: investigate
		this.menuModel.addPopupMenuListener( popupMenu );
		
		popupMenu.addPopupMenuListener( new javax.swing.event.PopupMenuListener() {
			private javax.swing.event.PopupMenuEvent cancelEvent = null;
			public void popupMenuWillBecomeVisible( javax.swing.event.PopupMenuEvent e ) {
				this.cancelEvent = null;
			}
			public void popupMenuWillBecomeInvisible( javax.swing.event.PopupMenuEvent e ) {
				if( this.cancelEvent != null ) {
					System.err.println( "todo: cancel" );
					//step.getParent().cancel();
					this.cancelEvent = null;
				} else {
					System.err.println( "todo: finish" );
					//step.getParent().finish();
				}
				StandardPopupPrepModel.this.menuModel.handlePopupMenuEpilogue( popupMenu, step );
				performObserver.handleFinally();
			}
			public void popupMenuCanceled( javax.swing.event.PopupMenuEvent e ) {
				this.cancelEvent = e;
			}
		} );

		popupMenu.addComponentListener( new java.awt.event.ComponentListener() {
			public void componentShown( java.awt.event.ComponentEvent e ) {
//				java.awt.Component awtComponent = e.getComponent();
//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "componentShown", awtComponent.getLocationOnScreen(), awtComponent.getSize() );
			}
			public void componentMoved( java.awt.event.ComponentEvent e ) {
//				java.awt.Component awtComponent = e.getComponent();
//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "componentMoved", awtComponent.getLocationOnScreen(), awtComponent.getSize() );
			}
			public void componentResized( java.awt.event.ComponentEvent e ) {
//				java.awt.Component awtComponent = e.getComponent();
//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "componentResized", awtComponent.getLocationOnScreen(), awtComponent.getSize() );
				step.handleResized( e );
			}
			public void componentHidden( java.awt.event.ComponentEvent e ) {
//				java.awt.Component awtComponent = e.getComponent();
//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "componentHidden", awtComponent.getLocationOnScreen(), awtComponent.getSize() );
			}
		} );

		
		this.menuModel.handlePopupMenuPrologue( popupMenu, step );
		
		step.showPopupMenu( popupMenu );
		return step;
	}
}
