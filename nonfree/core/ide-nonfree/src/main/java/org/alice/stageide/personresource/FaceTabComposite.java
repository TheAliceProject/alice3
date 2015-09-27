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
package org.alice.stageide.personresource;

/**
 * @author Dennis Cosgrove
 */
public final class FaceTabComposite extends org.lgna.croquet.SimpleTabComposite<org.alice.stageide.personresource.views.FaceTabView> {
	private final org.lgna.croquet.ImmutableDataSingleSelectListState<org.lgna.story.resources.sims2.BaseEyeColor> baseEyeColorState = this.createImmutableListStateForEnum( "baseEyeColorState", org.lgna.story.resources.sims2.BaseEyeColor.class, org.lgna.story.resources.sims2.BaseEyeColor.getRandom() );
	private final org.lgna.croquet.ImmutableDataSingleSelectListState<org.lgna.story.resources.sims2.BaseFace> baseFaceState = this.createImmutableListStateForEnum( "baseFaceState", org.lgna.story.resources.sims2.BaseFace.class, org.lgna.story.resources.sims2.BaseFace.getRandom() );

	public FaceTabComposite() {
		super( java.util.UUID.fromString( "44c44e61-7bcb-4891-a631-2142a49ac73c" ), IsCloseable.FALSE );
	}

	@Override
	protected org.lgna.croquet.views.ScrollPane createScrollPaneIfDesired() {
		return null;
	}

	@Override
	protected org.alice.stageide.personresource.views.FaceTabView createView() {
		return new org.alice.stageide.personresource.views.FaceTabView( this );
	}

	public org.lgna.croquet.ImmutableDataSingleSelectListState<org.lgna.story.resources.sims2.BaseEyeColor> getBaseEyeColorState() {
		return this.baseEyeColorState;
	}

	public org.lgna.croquet.ImmutableDataSingleSelectListState<org.lgna.story.resources.sims2.BaseFace> getBaseFaceState() {
		return this.baseFaceState;
	}

	public static void main( String[] args ) throws Exception {
		edu.cmu.cs.dennisc.javax.swing.UIManagerUtilities.setLookAndFeel( "Nimbus" );

		org.lgna.croquet.simple.SimpleApplication app = new org.lgna.croquet.simple.SimpleApplication();
		org.lgna.croquet.DocumentFrame documentFrame = app.getDocumentFrame();
		org.lgna.croquet.views.Frame frame = documentFrame.getFrame();

		FaceTabComposite faceTabComposite = new FaceTabComposite();
		frame.getContentPane().addCenterComponent( faceTabComposite.getRootComponent() );
		frame.setSize( 600, 400 );
		frame.setVisible( true );
	}
};
