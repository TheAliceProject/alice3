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

	private void handleEditCommitted( org.lgna.croquet.edits.Edit<?> edit ) {
		assert edit != null;
		org.lgna.croquet.undo.UndoHistory projectHistory = this.getGroupHistory( edit.getGroup() );
		if( projectHistory != null ) {
			projectHistory.push( edit );
		}
	}
}
