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

package org.alice.app.operations.file;

/**
 * @author Dennis Cosgrove
 */
public abstract class SelectAndLoadProjectOperation extends org.alice.app.operations.file.UriInputDialogOperation {
	private org.alice.app.openprojectpane.SelectProjectToOpenPanel selectProjectToOpenPanel;
	public SelectAndLoadProjectOperation( java.util.UUID individualUUID ) {
		super( individualUUID, "Load Project" );
	}
	protected abstract boolean isNew();
//	@Override
//	public boolean isOKButtonValid() {
//		return super.isOKButtonValid() && this.getActualInputValue() != null;
//	}
	
	@Override
	protected void packDialog(edu.cmu.cs.dennisc.croquet.Dialog dialog) {
		dialog.getAwtWindow().setSize( new java.awt.Dimension( 600, 430 ) );
	}
	@Override
	protected edu.cmu.cs.dennisc.croquet.Component<?> prologue(edu.cmu.cs.dennisc.croquet.Context context) {
		if( this.selectProjectToOpenPanel != null ) {
			//pass
		} else {
			this.selectProjectToOpenPanel = new org.alice.app.openprojectpane.SelectProjectToOpenPanel();
		}
		this.selectProjectToOpenPanel.refresh();
		return this.selectProjectToOpenPanel;
	}
	@Override
	protected void epilogue(edu.cmu.cs.dennisc.croquet.Context context, boolean isOk) {
		if( isOk ) {
			java.net.URI uri = this.selectProjectToOpenPanel.getSelectedURI();
			if (uri != null) {
				context.commitAndInvokeDo( new LoadUriEdit( context, uri ) );
			} else {
				context.cancel();
			}
		} else {
			context.cancel();
		}
	}
//	@Override
//	protected void perform( edu.cmu.cs.dennisc.croquet.Context context, java.awt.event.ActionEvent e, edu.cmu.cs.dennisc.croquet.AbstractButton< ? > button ) {
//		org.alice.app.ProjectApplication application = this.getProjectApplication();
//		org.alice.app.openprojectpane.OpenProjectPane openProjectPane = application.getOpenProjectPane();
//		openProjectPane.selectAppropriateTab( this.isNew() );
//		while( true ) {
//			java.net.URI uri = openProjectPane.showInJDialog( button, "Open Project" );
//			//todo: just load default project
//			if( uri != null ) {
//				context.commitAndInvokeDo( new LoadUriEdit( context, uri ) );
//				break;
//			} else {
//				if( application.getFile() == null ) {
//					application.showMessageDialog( "Please select a project to open." );
//				} else {
//					context.cancel();
//					break;
//				}
//			}
//		}
//	}
}
