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
package org.alice.imageeditor.croquet;

/**
 * @author Dennis Cosgrove
 */
public final class SaveOperation extends org.lgna.croquet.SingleThreadIteratingOperation {
	private final ImageEditorFrame owner;
	private final SaveOverComposite saveOverComposite = new SaveOverComposite( this );
	private java.io.File file;

	public SaveOperation( ImageEditorFrame owner ) {
		super( org.lgna.croquet.Application.INHERIT_GROUP, java.util.UUID.fromString( "754c7a0e-8aec-4760-83e0-dff2817ac7a0" ) );
		this.owner = owner;
	}

	public ImageEditorFrame getOwner() {
		return this.owner;
	}

	@Override
	protected boolean hasNext( org.lgna.croquet.history.CompletionStep<?> step, java.util.List<org.lgna.croquet.history.Step<?>> subSteps, Object iteratingData ) {
		if( subSteps.size() == 0 ) {
			this.file = this.owner.getFile();
			if( this.file != null ) {
				return this.file.exists();
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	@Override
	protected org.lgna.croquet.Model getNext( org.lgna.croquet.history.CompletionStep<?> step, java.util.List<org.lgna.croquet.history.Step<?>> subSteps, Object iteratingData ) {
		//note: could return from within switch, but switches without breaks seem ill advised at the moment
		org.lgna.croquet.Model rv;
		switch( subSteps.size() ) {
		case 0:
			if( owner.isGoodToGoCroppingIfNecessary() ) {
				if( this.file != null ) {
					if( this.file.exists() ) {
						rv = this.saveOverComposite.getValueCreator();
					} else {
						rv = null;
					}
				} else {
					new edu.cmu.cs.dennisc.javax.swing.option.OkDialog.Builder( "Please select a file" )
							.buildAndShow();
					throw new org.lgna.croquet.CancelException();
				}
			} else {
				throw new org.lgna.croquet.CancelException();
			}
			break;
		case 1:
			rv = this.saveOverComposite.getValueCreator();
			break;
		default:
			rv = null;
		}
		return rv;
	}

	@Override
	protected void handleSuccessfulCompletionOfSubModels( org.lgna.croquet.history.CompletionStep<?> step, java.util.List<org.lgna.croquet.history.Step<?>> subSteps ) {
		if( this.file != null ) {
			java.awt.Image image = owner.getView().render();
			try {
				edu.cmu.cs.dennisc.image.ImageUtilities.write( this.file, image );
			} catch( java.io.IOException ioe ) {
				throw new RuntimeException( this.file.getAbsolutePath(), ioe );
			}
			this.getOwner().getIsFrameShowingState().setValueTransactionlessly( false );
		}
	}
}
