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
package org.alice.stageide.personeditor;

/**
 * @author Dennis Cosgrove
 */
class IngredientsPane extends swing.GridBagPane {
	private LifeStageList lifeStageList = new LifeStageList();
	private GenderList genderList = new GenderList();
	private FitnessLevelPane fitnessLevelPane = new FitnessLevelPane();
	private BaseSkinToneList baseSkinToneList = new BaseSkinToneList();
	private BaseEyeColorList baseEyeColorList = new BaseEyeColorList();
	private HairColorList hairColorList = new HairColorList();
	private HairList hairList = new HairList();
	private FullBodyOutfitList fullBodyOutfitList = new FullBodyOutfitList();
	private zoot.ZButton randomizeButton = new zoot.ZButton( new RandomPersonActionOperation() );
	public IngredientsPane() {
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
		this.setBackground( new java.awt.Color( 220, 220, 255 ) );
		this.setOpaque( true );

		java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
		gbc.fill = java.awt.GridBagConstraints.BOTH;
		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		
		final int INSET_TOP = 12;
		final int INSET_LEFT = 2;

//		this.add( new zoot.ZLabel( "life stage" ), gbc );
//		this.add( this.lifeStageList, gbc );
		
		this.add( this.randomizeButton, gbc );

		gbc.insets.top = INSET_TOP;
		this.add( new zoot.ZLabel( "gender" ), gbc );
		gbc.insets.top = 0;
		this.add( this.genderList, gbc );

		gbc.insets.top = INSET_TOP;
		this.add( new zoot.ZLabel( "skin tone" ), gbc );
		gbc.insets.top = 0;
		this.add( this.baseSkinToneList, gbc );

		gbc.insets.top = INSET_TOP;
		this.add( new zoot.ZLabel( "fitness level" ), gbc );
		gbc.insets.top = 0;
		this.add( this.fitnessLevelPane, gbc );

		gbc.weightx = 0.0;
		gbc.insets.top = INSET_TOP;
		gbc.gridwidth = 1;
		this.add( new zoot.ZLabel( "eye color" ), gbc );
		gbc.insets.left = INSET_LEFT*8;
		gbc.gridwidth = java.awt.GridBagConstraints.RELATIVE;
		this.add( new zoot.ZLabel( "hair" ), gbc );
		gbc.insets.left = INSET_LEFT;
		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		this.add( new zoot.ZLabel( "" ), gbc );
		gbc.insets.left = 0;
		gbc.weightx = 0.0;

		
		gbc.weighty = 1.0;
		gbc.insets.top = 0;
		gbc.gridwidth = 1;
		this.add( this.baseEyeColorList, gbc );
		gbc.gridwidth = java.awt.GridBagConstraints.RELATIVE;
		gbc.insets.left = INSET_LEFT*8;
		this.add( this.hairColorList, gbc );
		gbc.insets.left = INSET_LEFT;
		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		javax.swing.JScrollPane hairScrollPane = new javax.swing.JScrollPane( this.hairList );
		hairScrollPane.setBorder( null );
		this.add( hairScrollPane, gbc ); 
		gbc.insets.left = 0;

		gbc.weighty = 0.0;
		gbc.insets.top = INSET_TOP;
		this.add( new zoot.ZLabel( "full body outfit" ), gbc );
		gbc.insets.top = 0;
		gbc.weighty = 6.0;
		this.add( new javax.swing.JScrollPane( this.fullBodyOutfitList ), gbc ); 
	}
	
	public void refresh() {
		final boolean shouldScroll = true;
		final PersonViewer personViewer = PersonViewer.getSingleton();
		org.alice.apis.stage.LifeStage lifeStage = personViewer.getLifeStage();
		org.alice.apis.stage.Gender gender = personViewer.getGender();
		org.alice.apis.stage.Hair hair = personViewer.getHair();
		if( hair != null ) {
			String hairColor = hair.toString();
			this.lifeStageList.setSelectedValue( lifeStage, shouldScroll );
			this.genderList.setSelectedValue( gender, shouldScroll );
			this.fullBodyOutfitList.handleEpicChange( lifeStage, gender, hairColor );
			this.hairList.handleEpicChange( lifeStage, gender, hairColor );
			this.hairList.setSelectedValue( hair, shouldScroll );
			this.hairColorList.setSelectedValue( hairColor, shouldScroll );
			
			this.fullBodyOutfitList.setSelectedValue( personViewer.getFullBodyOutfit(), shouldScroll );
			this.baseSkinToneList.setSelectedValue( personViewer.getBaseSkinTone(), shouldScroll );
			this.baseEyeColorList.setSelectedValue( personViewer.getBaseEyeColor(), shouldScroll );
			
//			javax.swing.SwingUtilities.invokeLater( new Runnable() {
//				public void run() {
//				}
//			} );
		} else {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "hair is null" );
		}
	}
//	public void randomize() {
//		this.genderList.randomize();
//		this.baseSkinToneList.randomize();
//		this.baseEyeColorList.randomize();
//		//this.fitnessLevelPane.randomize();
//	}
}
