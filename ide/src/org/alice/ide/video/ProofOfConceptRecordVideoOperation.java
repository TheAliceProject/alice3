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

package org.alice.ide.video;

/**
 * @author Dennis Cosgrove
 */
public class ProofOfConceptRecordVideoOperation extends RecordVideoOperation {
	private static class SingletonHolder {
		private static ProofOfConceptRecordVideoOperation instance = new ProofOfConceptRecordVideoOperation();
	}
	public static ProofOfConceptRecordVideoOperation getInstance() {
		return SingletonHolder.instance;
	}
	
	private final org.lgna.croquet.State.ValueObserver< Boolean > isRecordingListener = new org.lgna.croquet.State.ValueObserver< Boolean >() {
		public void changing( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			ProofOfConceptRecordVideoOperation.this.setRecording( nextValue );
		}
	};
	private ProofOfConceptRecordVideoOperation() {
		super( java.util.UUID.fromString( "63876374-ce69-44f0-a454-2bedda151818" ) );
	}
	@Override
	protected org.alice.ide.video.components.RecordVideoPanel createVideoExportPanel() {
		return new org.alice.ide.video.components.ProofOfConceptRecordVideoPanel();
	}
	@Override
	protected org.lgna.croquet.components.Component< ? > createControlsPanel( org.lgna.croquet.history.InputDialogOperationStep< java.lang.Void > step, org.lgna.croquet.components.Dialog dialog ) {
		IsRecordingState.getInstance().setValue( false );
		IsRecordingState.getInstance().addValueObserver( this.isRecordingListener );
		return super.createControlsPanel( step, dialog );
	}
	@Override
	protected void handleFinally( org.lgna.croquet.history.InputDialogOperationStep< java.lang.Void > context, org.lgna.croquet.components.Dialog dialog, org.lgna.croquet.components.Container< ? > contentPane ) {
		IsRecordingState.getInstance().removeValueObserver( this.isRecordingListener );
		super.handleFinally( context, dialog, contentPane );
	}
	@Override
	protected void handleImage( java.awt.image.BufferedImage image, int i ) {
		java.io.File directory = new java.io.File( edu.cmu.cs.dennisc.java.io.FileUtilities.getDefaultDirectory(), "ProofOfConceptVideoExport" );
		java.io.File file = new java.io.File( directory, "image" + new java.text.DecimalFormat( "#0000" ).format( i ) + ".png" );
		edu.cmu.cs.dennisc.java.io.FileUtilities.createParentDirectoriesIfNecessary( file );
		edu.cmu.cs.dennisc.image.ImageUtilities.write( file, image );
	}
}
