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
package org.lgna.croquet;


/**
 * @author Dennis Cosgrove
 */
public abstract class InputDialogMainComposite<V extends org.lgna.croquet.components.View<?,?>> extends GatedCommitMainComposite<V> {
	private static class InternalControlsComposite extends GatedCommitDialogComposite.ControlsComposite {
		public InternalControlsComposite( InternalInputDialogComposite composite ) {
			super( java.util.UUID.fromString( "d36cd73f-20dd-45ed-8151-163c44033f8b" ), composite );
		}
		@Override
		protected String getCommitUiKey() {
			return "OptionPane.okButtonText";
		}
		@Override
		protected String getDefaultCommitText() {
			return "OK";
		}
		@Override
		protected void addComponentsToControlLine( org.lgna.croquet.components.LineAxisPanel controlLine, org.lgna.croquet.components.Button leadingOkCancelButton, org.lgna.croquet.components.Button trailingOkCancelButton ) {
			controlLine.addComponent( org.lgna.croquet.components.BoxUtilities.createHorizontalGlue() );
			controlLine.addComponent( leadingOkCancelButton );
			controlLine.addComponent( org.lgna.croquet.components.BoxUtilities.createHorizontalSliver( 4 ) );
			controlLine.addComponent( trailingOkCancelButton );
		}
	}

	private static class InternalInputDialogComposite extends GatedCommitDialogComposite<InputDialogMainComposite,InternalControlsComposite> {
		private final InternalControlsComposite controlsComposite = new InternalControlsComposite( this );
		public InternalInputDialogComposite( Group operationGroup, InputDialogMainComposite<?> mainComposite ) {
			super( java.util.UUID.fromString( "d98ddc2f-b344-4b38-b4dd-e55a7b703054" ), operationGroup, mainComposite );
		}
		@Override
		protected InternalControlsComposite getControlsComposite() {
			assert this.controlsComposite != null : this;
			return this.controlsComposite;
		}
	}

	private final InternalInputDialogComposite gatedCommitDialogComposite;
	public InputDialogMainComposite( java.util.UUID migrationId, Group operationGroup ) {
		super( migrationId );
		this.gatedCommitDialogComposite = new InternalInputDialogComposite( operationGroup, this );
	}
	@Override
	public GatedCommitDialogComposite getGatedCommitDialogComposite() {
		return this.gatedCommitDialogComposite;
	}
}
