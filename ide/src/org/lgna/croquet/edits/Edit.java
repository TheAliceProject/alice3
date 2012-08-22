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
package org.lgna.croquet.edits;

import org.lgna.croquet.CompletionModel;
import org.lgna.croquet.Group;
import org.lgna.croquet.Manager;
import org.lgna.croquet.Retargeter;

/**
 * @author Dennis Cosgrove
 */
public abstract class Edit<M extends CompletionModel> implements edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable {
	private transient org.lgna.croquet.history.CompletionStep<M> completionStep;

	public Edit( org.lgna.croquet.history.CompletionStep<M> completionStep ) {
		this.completionStep = completionStep;
	}

	public Edit( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder, Object step ) {
		this.completionStep = (org.lgna.croquet.history.CompletionStep<M>)step;
	}

	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
	}

	public boolean isValid() {
		return true;
	}

	public M getModel() {
		if( this.completionStep != null ) {
			return this.completionStep.getModel();
		} else {
			return null;
		}
	}

	public Group getGroup() {
		M model = this.getModel();
		if( model != null ) {
			return model.getGroup();
		} else {
			return null;
		}
	}

	public org.lgna.croquet.history.CompletionStep<M> getCompletionStep() {
		return this.completionStep;
	}

	public void setCompletionStep( org.lgna.croquet.history.CompletionStep<M> completionStep ) {
		assert this.completionStep == null : this.completionStep;
		this.completionStep = completionStep;
	}

	public boolean canUndo() {
		return true;
	}

	public boolean canRedo() {
		return true;
	}

	protected abstract void doOrRedoInternal( boolean isDo );

	protected abstract void undoInternal();

	public final void doOrRedo( boolean isDo ) {
		if( isDo ) {
			//pass
		} else {
			if( canRedo() ) {
				//pass
			} else {
				throw new javax.swing.undo.CannotRedoException();
			}
		}
		Manager.pushUndoOrRedo();
		try {
			this.doOrRedoInternal( isDo );
		} finally {
			Manager.popUndoOrRedo();
		}
	}

	public final void undo() {
		if( canUndo() ) {
			//pass
		} else {
			throw new javax.swing.undo.CannotRedoException();
		}
		Manager.pushUndoOrRedo();
		try {
			this.undoInternal();
		} finally {
			Manager.popUndoOrRedo();
		}
	}

	protected StringBuilder updateTutorialTransactionTitle( StringBuilder title ) {
		return title;
	}

	public final String getTutorialTransactionTitle() {
		StringBuilder sb = new StringBuilder();
		this.updateTutorialTransactionTitle( sb );
		if( sb.length() == 0 ) {
			return null;
		} else {
			return sb.toString();
		}
	}

	protected abstract StringBuilder updatePresentation( StringBuilder rv );

	public final String getPresentation() {
		StringBuilder sb = new StringBuilder();
		this.updatePresentation( sb );
		if( sb.length() == 0 ) {
			sb.append( edu.cmu.cs.dennisc.java.lang.ClassUtilities.getTrimmedClassName( this.getClass() ) );
		}
		return sb.toString();
	}

	public String getRedoPresentation() {
		StringBuilder sb = new StringBuilder();
		sb.append( "Redo:" );
		this.updatePresentation( sb );
		return sb.toString();
	}

	public String getUndoPresentation() {
		StringBuilder sb = new StringBuilder();
		sb.append( "Undo:" );
		this.updatePresentation( sb );
		return sb.toString();
	}

	public ReplacementAcceptability getReplacementAcceptability( Edit<?> replacementCandidate ) {
		if( replacementCandidate != null ) {
			return ReplacementAcceptability.PERFECT_MATCH;
		} else {
			return ReplacementAcceptability.createRejection( "replacement is null" );
		}
	}

	public void retarget( Retargeter retargeter ) {
	}

	public void addKeyValuePairs( Retargeter retargeter, Edit<?> edit ) {
	}

	protected <D extends org.lgna.croquet.DropSite> D findFirstDropSite( Class<D> cls ) {
		org.lgna.croquet.history.Step<?> step = this.getCompletionStep();
		while( step != null ) {
			org.lgna.croquet.triggers.Trigger trigger = step.getTrigger();
			if( trigger instanceof org.lgna.croquet.triggers.DropTrigger ) {
				org.lgna.croquet.triggers.DropTrigger dropTrigger = (org.lgna.croquet.triggers.DropTrigger)trigger;
				org.lgna.croquet.DropSite dropSite = dropTrigger.getDropSite();
				if( cls.isAssignableFrom( dropSite.getClass() ) ) {
					return cls.cast( dropSite );
				} else {
					edu.cmu.cs.dennisc.java.util.logging.Logger.warning( dropSite );
				}
			}
			step = step.getPreviousStep();
		}
		return null;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( this.getClass().getName() );
		sb.append( ": " );
		this.updatePresentation( sb );
		return sb.toString();
	}
}
