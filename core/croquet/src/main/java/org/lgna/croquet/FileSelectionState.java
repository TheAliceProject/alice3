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
package org.lgna.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class FileSelectionState extends ItemState<java.io.File> {
	public static class SwingModel {
		private final javax.swing.JFileChooser jFileChooser = new javax.swing.JFileChooser();

		private SwingModel() {
			this.jFileChooser.setControlButtonsAreShown( false );
		}

		public javax.swing.JFileChooser getJFileChooser() {
			return this.jFileChooser;
		}
	}

	private final SwingModel swingModel = new SwingModel();
	private final org.lgna.croquet.views.FileChooser oneAndOnlyOneFileChooser = new org.lgna.croquet.views.FileChooser( this );
	private final java.beans.PropertyChangeListener propertyChangeListener = new java.beans.PropertyChangeListener() {
		@Override
		public void propertyChange( java.beans.PropertyChangeEvent e ) {
			//java.io.File prevValue = (java.io.File)e.getOldValue();
			java.io.File nextValue = (java.io.File)e.getNewValue();
			FileSelectionState.this.changeValueFromSwing( nextValue, IsAdjusting.FALSE, org.lgna.croquet.triggers.PropertyChangeEventTrigger.createUserInstance( oneAndOnlyOneFileChooser, e ) );
		}
	};

	public FileSelectionState( Group group, java.util.UUID migrationId, java.io.File initialValue ) {
		super( group, migrationId, initialValue, org.lgna.croquet.codecs.FileCodec.SINGLETON );
		this.swingModel.jFileChooser.addPropertyChangeListener( "SelectedFileChangedProperty", this.propertyChangeListener );
	}

	@Override
	protected void localize() {
	}

	@Override
	public java.util.List<java.util.List<PrepModel>> getPotentialPrepModelPaths( org.lgna.croquet.edits.Edit edit ) {
		return java.util.Collections.emptyList();
	}

	@Override
	protected java.io.File getSwingValue() {
		return this.swingModel.jFileChooser.getSelectedFile();
	}

	@Override
	protected void setSwingValue( java.io.File nextValue ) {
		if( this.swingModel != null ) {
			this.swingModel.jFileChooser.setSelectedFile( nextValue );
		}
	}

	public SwingModel getSwingModel() {
		return this.swingModel;
	}

	public org.lgna.croquet.views.FileChooser getOneAndOnlyOneFileChooser() {
		return this.oneAndOnlyOneFileChooser;
	}

	public void addChoosableFileFilter( javax.swing.filechooser.FileFilter fileFilter ) {
		this.swingModel.jFileChooser.addChoosableFileFilter( fileFilter );
	}

	public void removeChoosableFileFilter( javax.swing.filechooser.FileFilter fileFilter ) {
		this.swingModel.jFileChooser.addChoosableFileFilter( fileFilter );
	}

	public void setFileFilter( javax.swing.filechooser.FileFilter fileFilter ) {
		this.swingModel.jFileChooser.setFileFilter( fileFilter );
	}
}
