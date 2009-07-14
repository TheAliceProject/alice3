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
class IngredientsPane extends swing.BorderPane {
	private zoot.ZButton randomizeButton = new zoot.ZButton( new RandomPersonActionOperation() );
	private LifeStageList lifeStageList = new LifeStageList();
	private GenderList genderList = new GenderList();
	private FitnessLevelPane fitnessLevelPane = new FitnessLevelPane();
	private BaseSkinToneList baseSkinToneList = new BaseSkinToneList();
	private BaseEyeColorList baseEyeColorList = new BaseEyeColorList();
	private HairColorList hairColorList = new HairColorList();
	private HairList hairList = new HairList();
	private FullBodyOutfitList fullBodyOutfitList = new FullBodyOutfitList();
	
	private static java.awt.Component createLabel( String text ) {
		zoot.ZLabel rv = zoot.ZLabel.acquire( text );
		rv.setHorizontalAlignment( javax.swing.SwingConstants.TRAILING );
		return rv;
	}
	
	private static final java.awt.Color BACKGROUND_COLOR = new java.awt.Color( 220, 220, 255 );
	public IngredientsPane() {
		this.hairList.setOpaque( false );
		this.fullBodyOutfitList.setOpaque( false );

		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
		this.setBackground( BACKGROUND_COLOR );
		this.setOpaque( true );

		this.lifeStageList.setLayoutOrientation( javax.swing.JList.HORIZONTAL_WRAP );
		this.lifeStageList.setVisibleRowCount( 1 );
		this.lifeStageList.setOpaque( false );
		this.genderList.setLayoutOrientation( javax.swing.JList.HORIZONTAL_WRAP );
		this.genderList.setVisibleRowCount( 1 );
		this.genderList.setOpaque( false );
		this.baseSkinToneList.setLayoutOrientation( javax.swing.JList.HORIZONTAL_WRAP );
		this.baseSkinToneList.setVisibleRowCount( 1 );
		this.baseSkinToneList.setOpaque( false );
		this.hairColorList.setLayoutOrientation( javax.swing.JList.HORIZONTAL_WRAP );
		this.hairColorList.setVisibleRowCount( 1 );
		this.hairColorList.setOpaque( false );
		this.baseEyeColorList.setLayoutOrientation( javax.swing.JList.HORIZONTAL_WRAP );
		this.baseEyeColorList.setVisibleRowCount( 1 );
		this.baseEyeColorList.setOpaque( false );
		
		final javax.swing.border.Border border = javax.swing.BorderFactory.createEmptyBorder( 2, 8, 2, 8 );
		class ListCellRenderer extends javax.swing.DefaultListCellRenderer {
			@Override
			public java.awt.Component getListCellRendererComponent( javax.swing.JList list, java.lang.Object value, int index, boolean isSelected, boolean cellHasFocus ) {
				java.awt.Component rv = super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
				javax.swing.JLabel label = edu.cmu.cs.dennisc.lang.ClassUtilities.getInstance( rv, javax.swing.JLabel.class );
				if( label != null ) {
					label.setBorder( border );
					label.setHorizontalAlignment( javax.swing.SwingUtilities.CENTER );
					if( isSelected ) {
						label.setBackground( edu.cmu.cs.dennisc.awt.ColorUtilities.scaleHSB( java.awt.Color.GREEN, 1.0, 0.3, 2.0 ) );
					} else {
						label.setBackground( java.awt.Color.LIGHT_GRAY );
					}
					//label.setOpaque( isSelected );
				}
				return rv;
			}
		}
		
//		class ListCellRenderer implements javax.swing.ListCellRenderer {
//			public ListCellRenderer() {
//			}
//			public java.awt.Component getListCellRendererComponent( javax.swing.JList list, java.lang.Object value, int index, boolean isSelected, boolean cellHasFocus ) {
//				java.awt.Component rv = super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
//				javax.swing.JLabel label = edu.cmu.cs.dennisc.lang.ClassUtilities.getInstance( rv, javax.swing.JLabel.class );
//				if( label != null ) {
//					label.setBorder( border );
//					label.setHorizontalAlignment( javax.swing.SwingUtilities.CENTER );
//					if( isSelected ) {
//						label.setBackground( edu.cmu.cs.dennisc.awt.ColorUtilities.scaleHSB( java.awt.Color.GREEN, 1.0, 0.3, 2.0 ) );
//					} else {
//						label.setBackground( java.awt.Color.LIGHT_GRAY );
//					}
//					//label.setOpaque( isSelected );
//				}
//				return rv;
//			}
//		}
		
		this.genderList.setCellRenderer( new ListCellRenderer() );
		this.lifeStageList.setCellRenderer( new ListCellRenderer() );
		this.baseSkinToneList.setCellRenderer( new ListCellRenderer() );
		this.hairColorList.setCellRenderer( new ListCellRenderer() );
		this.baseEyeColorList.setCellRenderer( new ListCellRenderer() );

		swing.BorderPane northPane = new swing.BorderPane();
		northPane.add( this.randomizeButton, java.awt.BorderLayout.NORTH );
		
		swing.RowsSpringPane ubiquitousPane = new swing.RowsSpringPane( 8, 8 ) {
			@Override
			protected java.util.List< java.awt.Component[] > addComponentRows( java.util.List< java.awt.Component[] > rv ) {
				rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( "gender:" ), genderList ) );
				rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( "skin tone:" ), baseSkinToneList ) );
				return rv;
			}
		};
		northPane.add( ubiquitousPane, java.awt.BorderLayout.CENTER );
		
//		final swing.BorderPane hairPane = new swing.BorderPane();
//		hairPane.add( this.hairColorList, java.awt.BorderLayout.NORTH );
//		hairPane.add( this.hairList, java.awt.BorderLayout.CENTER );
		
		swing.RowsSpringPane headPane = new swing.RowsSpringPane( 8, 8 ) {
			@Override
			protected java.util.List< java.awt.Component[] > addComponentRows( java.util.List< java.awt.Component[] > rv ) {
				rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( "hair:" ), hairColorList ) );
				rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( null, hairList ) );
				rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( "eye color:" ), baseEyeColorList ) );
				rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( null, javax.swing.Box.createGlue() ) );
				return rv;
			}
		};
		
		zoot.ZTabbedPane tabbedPane = new zoot.ZTabbedPane() {
			@Override
			protected java.awt.Color getContentAreaColor() {
				return BACKGROUND_COLOR;
			}
		};
		
		javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane( this.fullBodyOutfitList );
		scrollPane.getVerticalScrollBar().setUnitIncrement( 66 );
		//scrollPane.getVerticalScrollBar().setBlockIncrement( 10 );
		
		scrollPane.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
		scrollPane.setOpaque( false );
		scrollPane.getViewport().setOpaque( false );

		swing.BorderPane bodyPane = new swing.BorderPane( 8, 8 );
		bodyPane.add( scrollPane, java.awt.BorderLayout.CENTER );
		bodyPane.add( this.fitnessLevelPane, java.awt.BorderLayout.SOUTH );
		
		java.awt.Color color = edu.cmu.cs.dennisc.awt.ColorUtilities.scaleHSB( BACKGROUND_COLOR, 1.0, 0.9, 0.8 );
		
		bodyPane.setBackground( color );
		headPane.setBackground( color );
		bodyPane.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8,8,8,8  ) );
		headPane.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8,8,8,8  ) );
		
		tabbedPane.add( "Body", bodyPane );
		tabbedPane.add( "Head", headPane );
		tabbedPane.setOpaque( true );

		this.add( northPane, java.awt.BorderLayout.NORTH );
		this.add( tabbedPane, java.awt.BorderLayout.CENTER );
		
//		gbc.insets.top = INSET_TOP;
//		this.add( zoot.ZLabel.acquire( "fitness level" ), gbc );
//		gbc.insets.top = 0;
//		this.add( this.fitnessLevelPane, gbc );
//
//		gbc.weightx = 0.0;
//		gbc.insets.top = INSET_TOP;
//		gbc.gridwidth = 1;
//		this.add( zoot.ZLabel.acquire( "hair" ), gbc );
//		gbc.insets.left = INSET_LEFT;
//		gbc.gridwidth = java.awt.GridBagConstraints.RELATIVE;
//		gbc.weightx = 1.0;
//		this.add( zoot.ZLabel.acquire( "" ), gbc );
//		gbc.insets.left = INSET_LEFT*8;
//		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
//		gbc.weightx = 0.0;
//		this.add( zoot.ZLabel.acquire( "eye color" ), gbc );
//		gbc.insets.left = 0;
//
//		
//		gbc.weighty = 1.0;
//		gbc.insets.top = 0;
//		gbc.gridwidth = 1;
//		this.add( this.hairColorList, gbc );
//		gbc.gridwidth = java.awt.GridBagConstraints.RELATIVE;
//		gbc.insets.left = INSET_LEFT;
//		gbc.weightx = 1.0;
//		javax.swing.JScrollPane hairScrollPane = new javax.swing.JScrollPane( this.hairList );
//		hairScrollPane.setBorder( null );
//		this.add( hairScrollPane, gbc );
//		gbc.insets.left = INSET_LEFT*8;
//		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
//		gbc.weightx = 0.0;
//		this.add( this.baseEyeColorList, gbc ); 
//		gbc.insets.left = 0;
//		gbc.weightx = 1.0;
//
//		gbc.weighty = 0.0;
//		gbc.insets.top = INSET_TOP;
//		this.add( zoot.ZLabel.acquire( "full body outfit" ), gbc );
//		gbc.insets.top = 0;
//		gbc.weighty = 6.0;
//		this.add( new javax.swing.JScrollPane( this.fullBodyOutfitList ), gbc ); 
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
			this.fitnessLevelPane.setFitnessLevel( personViewer.getFitnessLevel() );
			
//			javax.swing.SwingUtilities.invokeLater( new Runnable() {
//				public void run() {
//					IngredientsPane.this.fullBodyOutfitList.setSelectedValue( personViewer.getFullBodyOutfit(), shouldScroll );
//					IngredientsPane.this.baseSkinToneList.setSelectedValue( personViewer.getBaseSkinTone(), shouldScroll );
//					IngredientsPane.this.baseEyeColorList.setSelectedValue( personViewer.getBaseEyeColor(), shouldScroll );
//				}
//			} );
		} else {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "hair is null" );
		}
	}
}
