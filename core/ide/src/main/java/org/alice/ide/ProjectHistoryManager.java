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
package org.alice.ide;

/**
 * @author Kyle J. Harms
 */
public class ProjectHistoryManager {

	private org.lgna.croquet.history.event.Listener listener;
	private java.util.Map<org.lgna.croquet.Group, org.lgna.croquet.undo.UndoHistory> map;

	public ProjectHistoryManager( ProjectDocument projectDocument ) {
		this.listener = new org.lgna.croquet.history.event.Listener() {
			@Override
			public void changing( org.lgna.croquet.history.event.Event<?> e ) {
			}

			@Override
			public void changed( org.lgna.croquet.history.event.Event<?> e ) {
				if( e instanceof org.lgna.croquet.history.event.EditCommittedEvent ) {
					org.lgna.croquet.history.event.EditCommittedEvent editCommittedEvent = (org.lgna.croquet.history.event.EditCommittedEvent)e;
					ProjectHistoryManager.this.handleEditCommitted( editCommittedEvent.getEdit() );
				}
			}
		};
		projectDocument.getRootTransactionHistory().addListener( this.listener );
		this.map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	}

	public org.lgna.croquet.undo.UndoHistory getGroupHistory( org.lgna.croquet.Group group ) {
		org.lgna.croquet.undo.UndoHistory rv;
		if( group != null ) {
			rv = this.map.get( group );
			if( rv != null ) {
				//pass
			} else {
				rv = new org.lgna.croquet.undo.UndoHistory( group );
				this.map.put( group, rv );
			}
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.warning( "group==null" );
			rv = null;
		}
		return rv;
	}

	private static int IS_POSSIBLY_OPENING_SCENE = 0x1;
	private static int IS_ANIMATED = 0x2;
	private static int IS_POSSIBLY_OPENING_SCENE_AND_ANIMATED = IS_POSSIBLY_OPENING_SCENE | IS_ANIMATED;

	private int isPossiblyOpeningSceneEdit( org.lgna.croquet.edits.Edit edit ) {
		if( edit instanceof org.alice.ide.properties.adapter.croquet.edits.PropertyValueEdit ) {
			return IS_POSSIBLY_OPENING_SCENE_AND_ANIMATED;
		}

		if( edit instanceof org.lgna.croquet.edits.StateEdit<?> ) {
			org.lgna.croquet.edits.StateEdit<?> stateEdit = (org.lgna.croquet.edits.StateEdit<?>)edit;
			if( stateEdit.getGroup() == IDE.PROJECT_GROUP ) {
				return IS_POSSIBLY_OPENING_SCENE_AND_ANIMATED;
			}
		}

		org.lgna.croquet.CompletionModel completionModel = ( (org.lgna.croquet.edits.AbstractEdit<?>)edit ).getModel();
		if( completionModel instanceof org.alice.stageide.sceneeditor.interact.croquet.AbstractFieldBasedManipulationActionOperation ) {
			return IS_POSSIBLY_OPENING_SCENE;
		}

		return 0;
	}

	private void markDirty( ProjectDocumentFrame projectDocumentFrame, org.alice.ide.instancefactory.ThisFieldAccessFactory thisFieldAccessFactory ) {
		org.lgna.project.ast.UserField userField = thisFieldAccessFactory.getField();
		projectDocumentFrame.getIconFactoryManager().markIconFactoryForFieldDirty( userField );
		org.alice.ide.instancefactory.croquet.InstanceFactoryFillIn.getInstance( thisFieldAccessFactory ).markDirty();
		for( org.lgna.croquet.views.SwingComponentView<?> component : org.lgna.croquet.views.ComponentManager.getComponents( projectDocumentFrame.getInstanceFactoryState().getCascadeRoot().getPopupPrepModel() ) ) {
			//note: rendering artifact for faux combo boxes when only invoking repaint.
			//component.repaint();
			component.revalidateAndRepaint();
		}
	}

	private void handleEditCommitted( org.lgna.croquet.edits.Edit edit ) {
		assert edit != null;
		org.lgna.croquet.undo.UndoHistory projectHistory = this.getGroupHistory( edit.getGroup() );
		if( projectHistory != null ) {
			projectHistory.push( edit );
		}

		int value = this.isPossiblyOpeningSceneEdit( edit );
		if( ( value & IS_POSSIBLY_OPENING_SCENE ) != 0 ) {
			final ProjectDocumentFrame projectDocumentFrame = IDE.getActiveInstance().getDocumentFrame();
			if( projectDocumentFrame != null ) {
				final org.alice.ide.instancefactory.croquet.InstanceFactoryState instanceFactoryState = projectDocumentFrame.getInstanceFactoryState();
				org.alice.ide.instancefactory.InstanceFactory instanceFactory = instanceFactoryState.getValue();
				if( instanceFactory instanceof org.alice.ide.instancefactory.ThisFieldAccessFactory ) {
					final org.alice.ide.instancefactory.ThisFieldAccessFactory thisFieldAccessFactory = (org.alice.ide.instancefactory.ThisFieldAccessFactory)instanceFactory;
					if( ( value & IS_ANIMATED ) != 0 ) {
						new Thread() {
							@Override
							public void run() {
								edu.cmu.cs.dennisc.java.lang.ThreadUtilities.sleep( 1100 );
								markDirty( projectDocumentFrame, thisFieldAccessFactory );
							}
						}.start();
					} else {
						this.markDirty( projectDocumentFrame, thisFieldAccessFactory );
					}
				}
			}
		}
	}
}
