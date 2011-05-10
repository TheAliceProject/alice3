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
package edu.cmu.cs.dennisc.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class Edit<M extends CompletionModel> implements edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable {
	private transient final org.lgna.croquet.steps.CompletionStep< M > completionStep;
	public Edit( org.lgna.croquet.steps.CompletionStep< M > completionStep ) {
		this.completionStep = completionStep;
	}
	public Edit( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder, Object step ) {
		this.completionStep = (org.lgna.croquet.steps.CompletionStep< M >) step;
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
	public org.lgna.croquet.steps.CompletionStep< M > getCompletionStep() {
		return this.completionStep;
	}
//	public void setCompletionStep( org.lgna.croquet.steps.CompletionStep< M > completionStep ) {
//		this.completionStep = completionStep;
//	}
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

	protected StringBuilder updateTutorialTransactionTitle( StringBuilder rv, UserInformation userInformation ) {
		return rv;
	}

	protected abstract StringBuilder updatePresentation( StringBuilder rv, java.util.Locale locale );
	public final String getPresentation( java.util.Locale locale ) {
		StringBuilder sb = new StringBuilder();
		this.updatePresentation( sb, locale );
		if( sb.length() == 0 ) {
			Class<?> cls = this.getClass();
			sb.append( edu.cmu.cs.dennisc.java.lang.ClassUtilities.getTrimmedClassName( cls ) );
		}
		if( sb.length() == 0 ) {
			sb.append( this.getClass() );
		}
		return sb.toString();
	}
	public String getRedoPresentation( java.util.Locale locale ) {
		StringBuilder sb = new StringBuilder();
		sb.append( "Redo:" );
		this.updatePresentation( sb, locale );
		return sb.toString();
	}
	public String getUndoPresentation( java.util.Locale locale ) {
		StringBuilder sb = new StringBuilder();
		sb.append( "Undo:" );
		this.updatePresentation( sb, locale );
		return sb.toString();
	}
	public ReplacementAcceptability getReplacementAcceptability( Edit< ? > replacementCandidate, UserInformation userInformation ) {
		if( replacementCandidate != null ) {
			return ReplacementAcceptability.PERFECT_MATCH;
		} else {
			return ReplacementAcceptability.createRejection( "replacement is null" );
		}
	}
	public void retarget( Retargeter retargeter ) {
	}
	public void addKeyValuePairs( Retargeter retargeter, Edit< ? > edit ) {
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( this.getClass().getName() );
		sb.append( ": " );
		this.updatePresentation( sb, java.util.Locale.getDefault() );
		return sb.toString();
	}
}
