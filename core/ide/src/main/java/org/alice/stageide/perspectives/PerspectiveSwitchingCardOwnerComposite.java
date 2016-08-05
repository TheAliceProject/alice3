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
package org.alice.stageide.perspectives;

/**
 * @author Dennis Cosgrove
 */
public abstract class PerspectiveSwitchingCardOwnerComposite extends org.lgna.croquet.CardOwnerComposite {
	private final org.lgna.croquet.event.ValueListener<org.alice.ide.perspectives.ProjectPerspective> perspectiveListener = new org.lgna.croquet.event.ValueListener<org.alice.ide.perspectives.ProjectPerspective>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<org.alice.ide.perspectives.ProjectPerspective> e ) {
			PerspectiveSwitchingCardOwnerComposite.this.handlePerspectiveChanged( e.getNextValue() );
		}
	};

	public static class MapBuilder {
		private org.lgna.croquet.Composite<?> codePerspecitiveCard;
		private org.lgna.croquet.Composite<?> setupScenePerspecitiveCard;

		public void codePerspecitive( org.lgna.croquet.Composite<?> codePerspecitiveCard ) {
			this.codePerspecitiveCard = codePerspecitiveCard;
		}

		public void setupScenePerspective( org.lgna.croquet.Composite<?> setupScenePerspecitiveCard ) {
			this.setupScenePerspecitiveCard = setupScenePerspecitiveCard;
		}

		public java.util.Map<org.alice.ide.perspectives.ProjectPerspective, org.lgna.croquet.Composite<?>> build() {
			java.util.Map<org.alice.ide.perspectives.ProjectPerspective, org.lgna.croquet.Composite<?>> rv = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
			rv.put( org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getCodePerspective(), this.codePerspecitiveCard );
			rv.put( org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getSetupScenePerspective(), this.setupScenePerspecitiveCard );
			return rv;
		}
	}

	private final java.util.Map<org.alice.ide.perspectives.ProjectPerspective, org.lgna.croquet.Composite<?>> map;

	private PerspectiveSwitchingCardOwnerComposite( java.util.UUID migrationId, java.util.Map<org.alice.ide.perspectives.ProjectPerspective, org.lgna.croquet.Composite<?>> map ) {
		super( migrationId );
		this.map = map;
	}

	public PerspectiveSwitchingCardOwnerComposite( java.util.UUID migrationId, MapBuilder mapBuilder ) {
		this( migrationId, mapBuilder.build() );
	}

	private void handlePerspectiveChanged( org.alice.ide.perspectives.ProjectPerspective nextValue ) {
		org.lgna.croquet.Composite<?> nextCard = this.map.get( nextValue );
		this.showCard( nextCard );
	}

	@Override
	public void handlePreActivation() {
		super.handlePreActivation();
		org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getPerspectiveState().addAndInvokeNewSchoolValueListener( this.perspectiveListener );
	}

	@Override
	public void handlePostDeactivation() {
		org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getPerspectiveState().removeNewSchoolValueListener( this.perspectiveListener );
		super.handlePostDeactivation();
	}

}
