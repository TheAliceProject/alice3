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

package org.alice.stageide.personresource;

/**
 * @author Dennis Cosgrove
 */
public class IngredientsComposite extends org.lgna.croquet.SimpleComposite<org.alice.stageide.personresource.views.IngredientsView> {
	private final org.lgna.croquet.Operation randomize = this.createActionOperation( this.createKey( "randomize" ), new Action() {
		public org.lgna.croquet.edits.Edit perform( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws org.lgna.croquet.CancelException {
			return null;
		}
	} );
	private final org.lgna.croquet.ListSelectionState<org.lgna.story.resources.sims2.LifeStage> lifeStageState = this.createListSelectionState( this.createKey( "lifeStageState" ), org.lgna.story.resources.sims2.LifeStage.class, edu.cmu.cs.dennisc.toolkit.croquet.codecs.EnumCodec.getInstance( org.lgna.story.resources.sims2.LifeStage.class ), 0, org.lgna.story.resources.sims2.LifeStage.ADULT, org.lgna.story.resources.sims2.LifeStage.CHILD );
	private final org.lgna.croquet.ListSelectionState<org.lgna.story.resources.sims2.Gender> genderState = this.createListSelectionStateForEnum( this.createKey( "genderState" ), org.lgna.story.resources.sims2.Gender.class, org.lgna.story.resources.sims2.Gender.getRandom() );
	private final org.lgna.croquet.ListSelectionState<org.lgna.story.resources.sims2.BaseSkinTone> skinToneState = this.createListSelectionStateForEnum( this.createKey( "skinToneState" ), org.lgna.story.resources.sims2.BaseSkinTone.class, org.lgna.story.resources.sims2.BaseSkinTone.getRandom() );
	private final org.lgna.croquet.TabSelectionState<org.lgna.croquet.SimpleTabComposite> bodyHeadTabState = this.createTabSelectionState( this.createKey( "bodyHeadTabState" ), 0, BodyTabComposite.getInstance(), HeadTabComposite.getInstance() );
	public IngredientsComposite() {
		super( java.util.UUID.fromString( "dd127381-09a8-4f78-bfd5-f3bffc1af98b" ) );
	}
	@Override
	protected org.alice.stageide.personresource.views.IngredientsView createView() {
		return new org.alice.stageide.personresource.views.IngredientsView( this );
	}
	public org.lgna.croquet.Operation getRandomize() {
		return this.randomize;
	}
	public org.lgna.croquet.ListSelectionState<org.lgna.story.resources.sims2.LifeStage> getLifeStageState() {
		return this.lifeStageState;
	}
	public org.lgna.croquet.ListSelectionState<org.lgna.story.resources.sims2.Gender> getGenderState() {
		return this.genderState;
	}
	public org.lgna.croquet.ListSelectionState<org.lgna.story.resources.sims2.BaseSkinTone> getSkinToneState() {
		return this.skinToneState;
	}
	public org.lgna.croquet.TabSelectionState<org.lgna.croquet.SimpleTabComposite> getBodyHeadTabState() {
		return this.bodyHeadTabState;
	}
}
