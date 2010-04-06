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
package org.alice.stageide.operations.ast;

/**
 * @author Dennis Cosgrove
 */
public class EditPersonActionOperation extends AbstractFieldTileActionOperation {
	public EditPersonActionOperation( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		super( field );
		this.setName( "Edit..." );
	}
	
	public void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
		final org.alice.apis.stage.Person person = this.getMoveAndTurnSceneEditor().getInstanceInJavaForField( this.getField(), org.alice.apis.stage.Person.class );
		if( person != null ) {
			final org.alice.apis.stage.Gender prevGender;
			final org.alice.apis.stage.SkinTone prevSkinTone;
			final org.alice.apis.stage.EyeColor prevEyeColor;
			final org.alice.apis.stage.Outfit prevOutfit;
			final org.alice.apis.stage.Hair prevHair;
			final Double prevFitnessLevel;
			prevGender = person.getGender(); 
			prevSkinTone = person.getSkinTone(); 
			prevEyeColor = person.getEyeColor(); 
			prevOutfit = person.getOutfit(); 
			prevHair = person.getHair(); 
			prevFitnessLevel = person.getFitnessLevel(); 

			org.alice.stageide.personeditor.PersonEditorInputPane inputPane = new org.alice.stageide.personeditor.PersonEditorInputPane( person );
			org.alice.apis.stage.Person result = inputPane.showInJDialog( this.getIDE() );
			if( result != null ) {
				final org.alice.apis.stage.Gender nextGender;
				final org.alice.apis.stage.SkinTone nextSkinTone;
				final org.alice.apis.stage.EyeColor nextEyeColor;
				final org.alice.apis.stage.Outfit nextOutfit;
				final org.alice.apis.stage.Hair nextHair;
				final Double nextFitnessLevel;
				nextGender = result.getGender();
				nextSkinTone = result.getSkinTone();
				nextEyeColor = result.getEyeColor();
				nextOutfit = result.getOutfit();
				nextHair = result.getHair();
				nextFitnessLevel = result.getFitnessLevel();
				actionContext.commitAndInvokeDo( new edu.cmu.cs.dennisc.zoot.AbstractEdit() {
					@Override
					public void doOrRedo( boolean isDo ) {
						EditPersonActionOperation.set( person, nextGender, nextSkinTone, nextEyeColor, nextOutfit, nextHair, nextFitnessLevel );
					}
					@Override
					public void undo() {
						EditPersonActionOperation.set( person, prevGender, prevSkinTone, prevEyeColor, prevOutfit, prevHair, prevFitnessLevel );
					}
					@Override
					protected StringBuffer updatePresentation( StringBuffer rv, java.util.Locale locale ) {
						rv.append( "edit: " );
						edu.cmu.cs.dennisc.alice.ast.Node.safeAppendRepr( rv, getField(), locale );
						return rv;
					}
				} );
			} else {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: remove" );
				EditPersonActionOperation.set( person, prevGender, prevSkinTone, prevEyeColor, prevOutfit, prevHair, prevFitnessLevel );
				actionContext.cancel();
			}
		} else {
			actionContext.cancel();
		}
	}
	private static void set( org.alice.apis.stage.Person person, org.alice.apis.stage.Gender gender, org.alice.apis.stage.SkinTone skinTone, org.alice.apis.stage.EyeColor eyeColor, org.alice.apis.stage.Outfit outfit, org.alice.apis.stage.Hair hair, Double fitnessLevel ) {
		person.setGender( gender );
		person.setSkinTone( skinTone );
		person.setEyeColor( eyeColor );
		person.setOutfit( outfit );
		person.setHair( hair );
		person.setFitnessLevel( fitnessLevel );
	}
}
