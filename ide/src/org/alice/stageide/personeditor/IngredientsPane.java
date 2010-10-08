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
package org.alice.stageide.personeditor;

/**
 * @author Dennis Cosgrove
 */
abstract class IngredientsPane extends edu.cmu.cs.dennisc.croquet.BorderPanel {
///	protected abstract void handleTabSelection( int tabIndex );
//	protected abstract void handleLifeStageSelection( int tabIndex );
//	protected abstract void handleGenderSelection( int tabIndex );
//
//	@Override
//	protected void handleAddedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
//		super.handleAddedTo( parent );
//		this.tabbedPaneSelection.addValueObserver( this.tabChangeAdapter );
//	}
//	@Override
//	protected void handleRemovedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
//		this.tabbedPaneSelection.removeValueObserver( this.tabChangeAdapter );
//		super.handleRemovedFrom( parent );
//	}
//
//	public void refresh() {
//		final PersonViewer personViewer = PersonViewer.getSingleton();
//		org.alice.apis.stage.LifeStage lifeStage = personViewer.getLifeStage();
//		org.alice.apis.stage.Gender gender = personViewer.getGender();
//		org.alice.apis.stage.Hair hair = personViewer.getHair();
//		if( hair != null ) {
//			String hairColor = hair.toString();
//			this.lifeStageSelection.setValue( lifeStage );
//			this.genderSelection.setValue( gender );
//			
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: IngredientsPane investigate ordering" );
//			this.fullBodyOutfitCardPanel.handleEpicChange( lifeStage, gender, hairColor );
//			this.hairCardPanel.handleEpicChange( lifeStage, gender, hairColor );
//			
//			this.hairCardPanel.setValue( hair );
//			
//			this.hairColorCardPanel.handleEpicChange( lifeStage, gender, hairColor );
//			
//			this.hairColorCardPanel.setValue( hairColor );
//			this.fullBodyOutfitCardPanel.setValue( personViewer.getFullBodyOutfit() );
//			
//			this.baseSkinToneSelection.setValue( personViewer.getBaseSkinTone() );
//			this.baseEyeColorSelection.setValue( personViewer.getBaseEyeColor() );
//			this.fitnessLevelPane.setFitnessLevel( personViewer.getFitnessLevel() );
//		} else {
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( "hair is null" );
//		}
//	}
}
