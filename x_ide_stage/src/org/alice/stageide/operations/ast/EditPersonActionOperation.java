/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.stageide.operations.ast;

/**
 * @author Dennis Cosgrove
 */
public class EditPersonActionOperation extends AbstractFieldTileActionOperation {
	public EditPersonActionOperation( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		super( field );
		this.putValue( javax.swing.Action.NAME, "Edit..." );
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
				actionContext.commitAndInvokeRedoIfAppropriate( new edu.cmu.cs.dennisc.zoot.AbstractEdit() {
					@Override
					public void doOrRedo() {
						EditPersonActionOperation.set( person, nextGender, nextSkinTone, nextEyeColor, nextOutfit, nextHair, nextFitnessLevel );
					}
					@Override
					public void undo() {
						EditPersonActionOperation.set( person, prevGender, prevSkinTone, prevEyeColor, prevOutfit, prevHair, prevFitnessLevel );
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
