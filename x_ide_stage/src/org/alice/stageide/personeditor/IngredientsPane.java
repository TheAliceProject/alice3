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
abstract class IngredientsPane extends edu.cmu.cs.dennisc.croquet.swing.BorderPane {
	private RandomPersonActionOperation randomPersonActionOperation = new RandomPersonActionOperation();
	private LifeStageList lifeStageList = new LifeStageList();
	private GenderList genderList = new GenderList();
	private FitnessLevelPane fitnessLevelPane = new FitnessLevelPane();
	private BaseSkinToneList baseSkinToneList = new BaseSkinToneList();
	private BaseEyeColorList baseEyeColorList = new BaseEyeColorList();
	private HairColorList hairColorList = new HairColorList();
	private HairList hairList = new HairList();
	private FullBodyOutfitList fullBodyOutfitList = new FullBodyOutfitList();
	
	private static java.awt.Component createLabel( String text ) {
		javax.swing.JLabel rv = edu.cmu.cs.dennisc.croquet.LabelUtilities.createLabel( text );
		rv.setHorizontalAlignment( javax.swing.SwingConstants.TRAILING );
		return rv;
	}
	
	private static final java.awt.Color BACKGROUND_COLOR = new java.awt.Color( 220, 220, 255 );
	/*package private*/ static final java.awt.Color SELECTED_COLOR = edu.cmu.cs.dennisc.awt.ColorUtilities.scaleHSB( java.awt.Color.YELLOW, 1.0, 0.3, 1.0 );
	private static final java.awt.Color UNSELECTED_COLOR = edu.cmu.cs.dennisc.awt.ColorUtilities.scaleHSB( BACKGROUND_COLOR, 1.0, 0.9, 0.8 );
	
	private edu.cmu.cs.dennisc.zoot.ZTabbedPane tabbedPane = new edu.cmu.cs.dennisc.zoot.ZTabbedPane() {
		@Override
		public java.awt.Color getContentAreaColor() {
			return BACKGROUND_COLOR;
		}
	};
	private javax.swing.event.ChangeListener tabChangeAdapter = new javax.swing.event.ChangeListener() {
		public void stateChanged( javax.swing.event.ChangeEvent e ) {
			int index = tabbedPane.getSelectedIndex();
			handleTabSelection( index );
		}
	};
	
	public IngredientsPane() {
		this.hairList.setOpaque( false );
		this.fullBodyOutfitList.setOpaque( false );

		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
		this.setBackground( BACKGROUND_COLOR );
		this.setOpaque( true );

		this.lifeStageList.setLayoutOrientation( javax.swing.JList.HORIZONTAL_WRAP );
		this.lifeStageList.setVisibleRowCount( 1 );
		this.lifeStageList.setOpaque( false );

		this.lifeStageList.addListSelectionListener( new javax.swing.event.ListSelectionListener() {
			public void valueChanged( javax.swing.event.ListSelectionEvent e ) {
				if( e.getValueIsAdjusting() ) {
					//pass
				} else {
					if( IngredientsPane.this.tabbedPane != null ) {
						javax.swing.SwingUtilities.invokeLater( new Runnable() {
							public void run() {
								IngredientsPane.this.handleLifeStageSelection( IngredientsPane.this.tabbedPane.getSelectedIndex() );
							}
						} );
					}
				}
			}
		} );
		
		
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
		
		class ListCellRenderer implements javax.swing.ListCellRenderer {
			private edu.cmu.cs.dennisc.croquet.swing.BorderPane pane = new edu.cmu.cs.dennisc.croquet.swing.BorderPane();
			private javax.swing.JLabel label = new javax.swing.JLabel();
			public ListCellRenderer() {
				label.setHorizontalAlignment( javax.swing.SwingUtilities.CENTER );
				label.setBorder( javax.swing.BorderFactory.createEmptyBorder( 2, 8, 2, 8 ) );
				label.setOpaque( true );
				pane.setBorder( javax.swing.BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) );
				pane.setOpaque( false );
				pane.add( label, java.awt.BorderLayout.CENTER );
			}
			public java.awt.Component getListCellRendererComponent( javax.swing.JList list, Object value, int index, boolean isSelected, boolean cellHasFocus ) {
				this.label.setText( value.toString() );
				if( isSelected ) {
					this.label.setBackground( SELECTED_COLOR );
				} else {
					this.label.setBackground( UNSELECTED_COLOR );
				}
				return this.pane;
			}
		}
		
		ListCellRenderer listCellRenderer = new ListCellRenderer();
		this.genderList.setCellRenderer( listCellRenderer );
		this.lifeStageList.setCellRenderer( listCellRenderer );
		this.baseSkinToneList.setCellRenderer( listCellRenderer );
		this.hairColorList.setCellRenderer( listCellRenderer );
		this.baseEyeColorList.setCellRenderer( listCellRenderer );

		edu.cmu.cs.dennisc.croquet.swing.BorderPane northPane = new edu.cmu.cs.dennisc.croquet.swing.BorderPane();
		northPane.add( edu.cmu.cs.dennisc.zoot.ZManager.createButton( this.randomPersonActionOperation ), java.awt.BorderLayout.NORTH );
		
		edu.cmu.cs.dennisc.croquet.swing.RowsSpringPane ubiquitousPane = new edu.cmu.cs.dennisc.croquet.swing.RowsSpringPane( 8, 8 ) {
			@Override
			protected java.util.List< java.awt.Component[] > addComponentRows( java.util.List< java.awt.Component[] > rv ) {
				rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( "life stage:" ), lifeStageList ) );
				rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( "gender:" ), genderList ) );
				rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( "skin tone:" ), baseSkinToneList ) );
				return rv;
			}
		};
		northPane.add( ubiquitousPane, java.awt.BorderLayout.CENTER );
		
//		final swing.BorderPane hairPane = new swing.BorderPane();
//		hairPane.add( this.hairColorList, java.awt.BorderLayout.NORTH );
//		hairPane.add( this.hairList, java.awt.BorderLayout.CENTER );
		
		edu.cmu.cs.dennisc.croquet.swing.RowsSpringPane headPane = new edu.cmu.cs.dennisc.croquet.swing.RowsSpringPane( 8, 8 ) {
			@Override
			protected java.util.List< java.awt.Component[] > addComponentRows( java.util.List< java.awt.Component[] > rv ) {
				rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( "hair:" ), hairColorList ) );
				rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( null, hairList ) );
				rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( "eye color:" ), baseEyeColorList ) );
				rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( null, javax.swing.Box.createGlue() ) );
				return rv;
			}
		};
		

		javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane( this.fullBodyOutfitList );
		scrollPane.getVerticalScrollBar().setUnitIncrement( 66 );
		//scrollPane.getVerticalScrollBar().setBlockIncrement( 10 );
		
		scrollPane.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
		scrollPane.setOpaque( false );
		scrollPane.getViewport().setOpaque( false );

		edu.cmu.cs.dennisc.croquet.swing.BorderPane bodyPane = new edu.cmu.cs.dennisc.croquet.swing.BorderPane( 8, 8 );
		bodyPane.add( scrollPane, java.awt.BorderLayout.CENTER );
		bodyPane.add( this.fitnessLevelPane, java.awt.BorderLayout.SOUTH );
		
		//java.awt.Color color = edu.cmu.cs.dennisc.awt.ColorUtilities.scaleHSB( BACKGROUND_COLOR, 1.0, 0.9, 0.8 );
		java.awt.Color color = BACKGROUND_COLOR;
		
		bodyPane.setBackground( color );
		headPane.setBackground( color );
		bodyPane.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8,8,8,8  ) );
		headPane.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8,8,8,8  ) );
		
		this.tabbedPane.add( "Body", bodyPane );
		this.tabbedPane.add( "Head", headPane );
		this.tabbedPane.setOpaque( true );

		java.awt.Font font = tabbedPane.getFont();
		this.tabbedPane.setFont( font.deriveFont( font.getSize2D() * 1.5f ) );

		this.add( northPane, java.awt.BorderLayout.NORTH );
		this.add( tabbedPane, java.awt.BorderLayout.CENTER );
		
	}
	
	protected abstract void handleTabSelection( int tabIndex );
	protected abstract void handleLifeStageSelection( int tabIndex );

	@Override
	public void addNotify() {
		super.addNotify();
		tabbedPane.addChangeListener( this.tabChangeAdapter );
	}
	@Override
	public void removeNotify() {
		tabbedPane.removeChangeListener( this.tabChangeAdapter );
		super.removeNotify();
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
