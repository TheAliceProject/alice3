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

package org.alice.ide.openprojectpane;

/**
 * @author Dennis Cosgrove
 */
public class FileSystemPane extends TabContentPanel {
	private edu.cmu.cs.dennisc.croquet.StringState textState = new edu.cmu.cs.dennisc.croquet.StringState( edu.cmu.cs.dennisc.croquet.Application.INHERIT_GROUP, java.util.UUID.fromString( "a0051988-1f98-4401-a054-f87547d3faf3" ), "" );
	public FileSystemPane() {
//		this.textField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
//			private void handleUpdate(javax.swing.event.DocumentEvent e) {
//				FileSystemPane.this.updateOKButton();
//			}
//
//			public void changedUpdate(javax.swing.event.DocumentEvent e) {
//				this.handleUpdate(e);
//			}
//
//			public void insertUpdate(javax.swing.event.DocumentEvent e) {
//				this.handleUpdate(e);
//			}
//
//			public void removeUpdate(javax.swing.event.DocumentEvent e) {
//				this.handleUpdate(e);
//			}
//		});

		class BrowseOperation extends edu.cmu.cs.dennisc.croquet.ActionOperation {
			public BrowseOperation() {
				super( edu.cmu.cs.dennisc.croquet.Application.INHERIT_GROUP, java.util.UUID.fromString("67936a64-be55-44d5-9441-4cc3cce5cc75"));
			}
			@Override
			protected void localize() {
				super.localize();
				this.setName("browse...");
			}

			@Override
			protected final void perform( org.lgna.croquet.steps.ActionOperationStep step ) {
				java.io.File file = edu.cmu.cs.dennisc.croquet.Application.getSingleton().showOpenFileDialog(org.alice.ide.ProjectApplication.getSingleton().getMyProjectsDirectory(), null, edu.cmu.cs.dennisc.alice.project.ProjectUtilities.PROJECT_EXTENSION, true);
				if (file != null) {
					FileSystemPane.this.textState.setValue(edu.cmu.cs.dennisc.java.io.FileUtilities.getCanonicalPathIfPossible(file));
				}
				step.finish();
			}
		}

		BrowseOperation browseOperation = new BrowseOperation();
		edu.cmu.cs.dennisc.croquet.BorderPanel pane = new edu.cmu.cs.dennisc.croquet.BorderPanel();
		pane.addComponent( new edu.cmu.cs.dennisc.croquet.Label("file:"), edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.LINE_START );
		pane.addComponent( this.textState.createTextField(), edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.CENTER );
		pane.addComponent( browseOperation.createButton(), edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.LINE_END );

		this.addComponent(pane, Constraint.PAGE_START);
	}

	@Override
	public java.net.URI getSelectedURI() {
		String path = this.textState.getValue();
		java.io.File file = new java.io.File(path);
		if (file.exists()) {
			return file.toURI();
		} else {
			return null;
		}
	}
}
