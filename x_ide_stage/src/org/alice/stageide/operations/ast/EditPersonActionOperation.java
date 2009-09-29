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
	private org.alice.apis.stage.Person person;
	private org.alice.apis.stage.Gender prevGender;
	private org.alice.apis.stage.SkinTone prevSkinTone;
	private org.alice.apis.stage.EyeColor prevEyeColor;
	private org.alice.apis.stage.Outfit prevOutfit;
	private org.alice.apis.stage.Hair prevHair;
	private Double prevFitnessLevel;
	private org.alice.apis.stage.Gender nextGender;
	private org.alice.apis.stage.SkinTone nextSkinTone;
	private org.alice.apis.stage.EyeColor nextEyeColor;
	private org.alice.apis.stage.Outfit nextOutfit;
	private org.alice.apis.stage.Hair nextHair;
	private Double nextFitnessLevel;
	public EditPersonActionOperation( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		super( field );
		this.putValue( javax.swing.Action.NAME, "Edit..." );
	}
	
	public void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
		this.person = this.getMoveAndTurnSceneEditor().getInstanceInJavaForField( this.getField(), org.alice.apis.stage.Person.class );
		if( this.person != null ) {
			this.prevGender = person.getGender(); 
			this.prevSkinTone = person.getSkinTone(); 
			this.prevEyeColor = person.getEyeColor(); 
			this.prevOutfit = person.getOutfit(); 
			this.prevHair = person.getHair(); 
			this.prevFitnessLevel = person.getFitnessLevel(); 

			org.alice.stageide.personeditor.PersonEditorInputPane inputPane = new org.alice.stageide.personeditor.PersonEditorInputPane( this.person );
			org.alice.apis.stage.Person result = inputPane.showInJDialog( this.getIDE() );
			if( result != null ) {
				this.nextGender = result.getGender();
				this.nextSkinTone = result.getSkinTone();
				this.nextEyeColor = result.getEyeColor();
				this.nextOutfit = result.getOutfit();
				this.nextHair = result.getHair();
				this.nextFitnessLevel = result.getFitnessLevel();
				actionContext.commitAndInvokeRedoIfAppropriate();
			} else {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: remove" );
				this.set( this.prevGender, this.prevSkinTone, this.prevEyeColor, this.prevOutfit, this.prevHair, this.prevFitnessLevel );
				actionContext.cancel();
			}
		} else {
			actionContext.cancel();
		}
	}
	private void set( org.alice.apis.stage.Gender gender, org.alice.apis.stage.SkinTone skinTone, org.alice.apis.stage.EyeColor eyeColor, org.alice.apis.stage.Outfit outfit, org.alice.apis.stage.Hair hair, Double fitnessLevel ) {
		this.person.setGender( gender );
		this.person.setSkinTone( skinTone );
		this.person.setEyeColor( eyeColor );
		this.person.setOutfit( outfit );
		this.person.setHair( hair );
		this.person.setFitnessLevel( fitnessLevel );
	}
	@Override
	public void doOrRedo() throws javax.swing.undo.CannotRedoException {
		this.set( this.nextGender, this.nextSkinTone, this.nextEyeColor, this.nextOutfit, this.nextHair, this.nextFitnessLevel );
	}
	@Override
	public void undo() throws javax.swing.undo.CannotUndoException {
		this.set( this.prevGender, this.prevSkinTone, this.prevEyeColor, this.prevOutfit, this.prevHair, this.prevFitnessLevel );
	}
	@Override
	public boolean isSignificant() {
		return true;
	}
}
