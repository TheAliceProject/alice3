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
package org.lgna.croquet.edits;

import org.lgna.croquet.CompletionModel;
import org.lgna.croquet.Group;
import org.lgna.croquet.Manager;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractEdit<M extends CompletionModel> implements Edit, edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable {
	public static <E extends AbstractEdit<?>> E createCopy( E original, org.lgna.croquet.history.CompletionStep<?> step ) {
		assert step != null : original;
		original.preCopy();
		edu.cmu.cs.dennisc.codec.ByteArrayBinaryEncoder encoder = new edu.cmu.cs.dennisc.codec.ByteArrayBinaryEncoder();
		encoder.encode( original );
		edu.cmu.cs.dennisc.codec.BinaryDecoder decoder = encoder.createDecoder();
		E rv = decoder.decodeBinaryEncodableAndDecodable( step );
		original.postCopy( rv );
		return rv;
	}

	private transient org.lgna.croquet.history.CompletionStep<M> completionStep;

	public AbstractEdit( org.lgna.croquet.history.CompletionStep<M> completionStep ) {
		this.completionStep = completionStep;
	}

	public AbstractEdit( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder, Object step ) {
		this.completionStep = (org.lgna.croquet.history.CompletionStep<M>)step;
	}

	@Override
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
	}

	protected void preCopy() {
	}

	protected void postCopy( AbstractEdit<?> result ) {
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

	@Override
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

	@Override
	public boolean canUndo() {
		return true;
	}

	@Override
	public boolean canRedo() {
		return true;
	}

	protected abstract void doOrRedoInternal( boolean isDo );

	protected abstract void undoInternal();

	@Override
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

	@Override
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

	protected static enum DescriptionStyle {
		TERSE( false, false ),
		DETAILED( true, false ),
		LOG( true, true );
		private final boolean isDetailed;
		private final boolean isLog;

		private DescriptionStyle( boolean isDetailed, boolean isLog ) {
			this.isDetailed = isDetailed;
			this.isLog = isLog;
		}

		public boolean isDetailed() {
			return this.isDetailed;
		}

		public boolean isLog() {
			return this.isLog;
		}
	}

	protected abstract void appendDescription( StringBuilder rv, DescriptionStyle descriptionStyle );

	@Override
	public String getRedoPresentation() {
		StringBuilder sb = new StringBuilder();
		sb.append( "Redo:" );
		this.appendDescription( sb, DescriptionStyle.TERSE );
		return sb.toString();
	}

	@Override
	public String getUndoPresentation() {
		StringBuilder sb = new StringBuilder();
		sb.append( "Undo:" );
		this.appendDescription( sb, DescriptionStyle.TERSE );
		return sb.toString();
	}

	@Override
	public final String getTerseDescription() {
		StringBuilder sb = new StringBuilder();
		this.appendDescription( sb, DescriptionStyle.TERSE );
		if( sb.length() == 0 ) {
			sb.append( edu.cmu.cs.dennisc.java.lang.ClassUtilities.getTrimmedClassName( this.getClass() ) );
		}
		return sb.toString();
	}

	@Override
	public final String getDetailedDescription() {
		StringBuilder sb = new StringBuilder();
		sb.append( this.getClass().getName() );
		sb.append( ": " );
		this.appendDescription( sb, DescriptionStyle.DETAILED );
		return sb.toString();
	}

	@Override
	public final String getLogDescription() {
		StringBuilder sb = new StringBuilder();
		sb.append( this.getClass().getName() );
		sb.append( ": " );
		this.appendDescription( sb, DescriptionStyle.LOG );
		return sb.toString();
	}

	@Override
	public String toString() {
		return this.getDetailedDescription();
	}
}
