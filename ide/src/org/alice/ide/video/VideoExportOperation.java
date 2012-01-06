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
public abstract class VideoExportOperation extends org.lgna.croquet.PlainDialogOperation {
	public VideoExportOperation( java.util.UUID id ) {
		super( org.alice.ide.IDE.EXPORT_GROUP, id );
	}
	
	private org.alice.stageide.program.VideoEncodingProgramContext programContext;
	protected abstract org.alice.ide.video.components.VideoExportPanel createVideoExportPanel();
	
	@Override
	protected org.lgna.croquet.components.Container< ? > createContentPane( org.lgna.croquet.history.PlainDialogOperationStep context, org.lgna.croquet.components.Dialog dialog ) {
		org.alice.ide.video.components.VideoExportPanel videoExportPanel = this.createVideoExportPanel();
		programContext = new org.alice.stageide.program.VideoEncodingProgramContext( 30.0 );
		programContext.initializeInContainer( videoExportPanel.getLookingGlassContainer() );
		programContext.invokeMethod0();
		return videoExportPanel;
	}
	@Override
	protected void releaseContentPane( org.lgna.croquet.history.PlainDialogOperationStep context, org.lgna.croquet.components.Dialog dialog, org.lgna.croquet.components.Container< ? > contentPane ) {
		programContext.cleanUpProgram();
	}
}
