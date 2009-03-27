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

class LookingGlass extends javax.swing.JPanel {
	public LookingGlass() {
		setBackground( java.awt.Color.BLACK );
	}
	@Override
	public java.awt.Dimension getPreferredSize() {
		return edu.cmu.cs.dennisc.awt.DimensionUtilties.constrainToMinimumWidth( super.getPreferredSize(), 600 );
	}
}

abstract class ConstantsList<E> extends zoot.ZList< E > {
	E[] constants;
	
	@Override
	public void addNotify() {
		if( constants != null ) {
			//pass
		} else {
			this.constants = this.createConstants();
			setListData( this.constants );
		}
		super.addNotify();
	}

	protected abstract E[] createConstants();
	public void randomize() {
		final int N = this.getModel().getSize();
		int i;
		if( N > 0 ) {
			i = org.alice.random.RandomUtilities.nextIntegerFrom0ToNExclusive( N );
		} else {
			i = -1;
		}
		this.setSelectedIndex( i );
	}
	public E getSelectedTypedValue() {
		return (E)this.getSelectedValue();
	}
}

class EnumConstantsList< E extends Enum > extends ConstantsList< E > {
	private Class< E > cls;
	public EnumConstantsList( Class< E > cls ) {
		this.cls = cls;
	}
	@Override
	protected E[] createConstants() {
		return cls.getEnumConstants();
	}
}

abstract class ArrayOfEnumConstantsList<E extends Enum> extends ConstantsList< E > {
	public ArrayOfEnumConstantsList() {
		this.setLayoutOrientation( javax.swing.JList.HORIZONTAL_WRAP );
		this.setVisibleRowCount( -1 );
	}
	protected abstract Class<E>[] createArrayOfClses();
	protected abstract E[] toArray( java.util.List< E > list );
	@Override
	protected E[] createConstants() {
		java.util.List< E > list = new java.util.LinkedList< E >();
		Class<E>[] arrayOfClses = this.createArrayOfClses();
		for( Class<E> cls : arrayOfClses ) {
			for( E e : cls.getEnumConstants() ) {
				list.add( e );
			}
		}
		return toArray( list );
	}
}

abstract class IngredientListCellRenderer< E > extends swing.ListCellRenderer< E > {
	private javax.swing.border.Border border = javax.swing.BorderFactory.createEmptyBorder( 2, 2, 2, 2 );
	protected abstract String getSubPath();
	private String getIngredientPath( org.alice.apis.stage.SkinTone skinTone, String clsName, String enumConstantName ) {
		java.io.File ROOT = org.alice.apis.moveandturn.gallery.GalleryModel.getGalleryRootDirectory();
		String rv = ROOT.getAbsolutePath();
		rv += "/personbuilder/";
		rv += this.getSubPath();
		rv += "/";
		rv += skinTone;
		rv += "/";
		rv += clsName;
		rv += ".";
		rv += enumConstantName;
		rv += ".png";
		return rv;
	}

	@Override
	protected javax.swing.JLabel getListCellRendererComponent( javax.swing.JLabel rv, javax.swing.JList list, E value, int index, boolean isSelected, boolean cellHasFocus ) {
		if( value != null ) {
			String clsName = value.getClass().getSimpleName();
			String enumConstantName = value.toString();
			String pathForIcon = this.getIngredientPath( org.alice.apis.stage.BaseSkinTone.DARK, clsName, enumConstantName );
			rv.setHorizontalTextPosition( javax.swing.SwingConstants.CENTER );
			rv.setVerticalTextPosition( javax.swing.SwingConstants.BOTTOM );

			rv.setText( "" );
			rv.setIcon( new javax.swing.ImageIcon( pathForIcon ) );
			rv.setBorder( this.border );
		} else {
			rv.setText( "null" );
		}
		return rv;
	}
}

class FullBodyOutfitListCellRenderer extends IngredientListCellRenderer {
	@Override
	protected java.lang.String getSubPath() {
		return "fullbodyoutfit_pictures";
	}
}

class HairListCellRenderer extends IngredientListCellRenderer {
	@Override
	protected java.lang.String getSubPath() {
		return "hair_pictures";
	}
}

class LifeStageList extends EnumConstantsList< org.alice.apis.stage.LifeStage > {
	public LifeStageList() {
		super( org.alice.apis.stage.LifeStage.class );
	}
}
class GenderList extends EnumConstantsList< org.alice.apis.stage.Gender > {
	public GenderList() {
		super( org.alice.apis.stage.Gender.class );
	}
}
class BaseSkinToneList extends EnumConstantsList< org.alice.apis.stage.BaseSkinTone > {
	public BaseSkinToneList() {
		super( org.alice.apis.stage.BaseSkinTone.class );
	}
}
class BaseEyeColorList extends EnumConstantsList< org.alice.apis.stage.BaseEyeColor > {
	public BaseEyeColorList() {
		super( org.alice.apis.stage.BaseEyeColor.class );
	}
}

abstract class AbstractLifeStageGenderCardPane extends swing.CardPane {
	private java.util.Map< String, ConstantsList > map = new java.util.HashMap< String, ConstantsList >();
	private static String getKey( org.alice.apis.stage.LifeStage lifeStage, org.alice.apis.stage.Gender gender ) {
		return lifeStage.name() + " " + gender.name();
	}
	public void handleEpicChange( org.alice.apis.stage.LifeStage lifeStage, org.alice.apis.stage.Gender gender ) {
		assert lifeStage != null;
		assert gender != null;
		String key = getKey( lifeStage, gender );
		ConstantsList constantsList = this.map.get( key );
		if( constantsList != null ) {
			//pass
		} else {
			constantsList = this.createList( lifeStage, gender );
			this.map.put( key, constantsList );
			this.add( constantsList, key );
		}
	}
	protected abstract ConstantsList createList( org.alice.apis.stage.LifeStage lifeStage, org.alice.apis.stage.Gender gender );
}

class FullBodyOutfitList extends ArrayOfEnumConstantsList {
	private org.alice.apis.stage.LifeStage lifeStage;
	private org.alice.apis.stage.Gender gender;
	public FullBodyOutfitList( org.alice.apis.stage.LifeStage lifeStage, org.alice.apis.stage.Gender gender ) {
		this.setCellRenderer( new FullBodyOutfitListCellRenderer() );
		this.lifeStage = lifeStage;
		this.gender = gender;
	}
	@Override
	protected Class[] createArrayOfClses() {
		return org.alice.apis.stage.IngredientUtilities.get( this.lifeStage.getFullBodyOutfitInterface( this.gender ) );
	}
	@Override
	protected Enum[] toArray( java.util.List list ) {
		Enum[] rv = new Enum[ list.size() ];
		list.toArray( rv );
		return rv;
	}
}


class FullBodyOutfitPane extends AbstractLifeStageGenderCardPane {
	@Override
	protected ConstantsList createList( org.alice.apis.stage.LifeStage lifeStage, org.alice.apis.stage.Gender gender ) {
		return new FullBodyOutfitList( lifeStage, gender );
	}
}

class HairList extends ArrayOfEnumConstantsList {
	private org.alice.apis.stage.LifeStage lifeStage;
	private org.alice.apis.stage.Gender gender;
	public HairList( org.alice.apis.stage.LifeStage lifeStage, org.alice.apis.stage.Gender gender ) {
		this.setCellRenderer( new HairListCellRenderer() );
		this.lifeStage = lifeStage;
		this.gender = gender;
	}
	@Override
	protected Class[] createArrayOfClses() {
		return org.alice.apis.stage.IngredientUtilities.get( this.lifeStage.getHairInterface( this.gender ) );
	}
	@Override
	protected Enum[] toArray( java.util.List list ) {
		Enum[] rv = new Enum[ list.size() ];
		list.toArray( rv );
		return rv;
	}
}

class HairPane extends AbstractLifeStageGenderCardPane {
	@Override
	protected ConstantsList createList( org.alice.apis.stage.LifeStage lifeStage, org.alice.apis.stage.Gender gender ) {
		return new HairList( lifeStage, gender );
	}
}

class FitnessLevelActionOperation extends org.alice.ide.operations.AbstractActionOperation {
	private org.alice.apis.stage.FitnessLevel fitnessLevel;
	public FitnessLevelActionOperation( org.alice.apis.stage.FitnessLevel fitnessLevel ) {
		this.fitnessLevel = fitnessLevel;
		this.putValue( javax.swing.Action.NAME, fitnessLevel.toString() );
	}
	public void perform( zoot.ActionContext actionContext ) {
	}
}

class FitnessLevelSlider extends javax.swing.JSlider {
	public FitnessLevelSlider() {
		this.setOpaque( false );
	}
}

class FitnessLevelPane extends swing.BorderPane {
	public FitnessLevelPane() {
		this.add( new zoot.ZButton( new FitnessLevelActionOperation( org.alice.apis.stage.FitnessLevel.CUT ) ), java.awt.BorderLayout.WEST );
		this.add( new FitnessLevelSlider(), java.awt.BorderLayout.CENTER );
		this.add( new zoot.ZButton( new FitnessLevelActionOperation( org.alice.apis.stage.FitnessLevel.SOFT ) ), java.awt.BorderLayout.EAST );
	}
}

//class FullBodyOutfitList extends EnumConstantsList< org.alice.apis.stage.FullBodyOutfit > {
//public FullBodyOutfitList() {
//	super( org.alice.apis.stage.FullBodyOutfit.class );
//}
//}

//class IngredientsPane extends swing.Pane {
//	private LifeStageList lifeStageList = new LifeStageList();
//	private GenderList genderList = new GenderList();
//	private BaseSkinToneList baseSkinToneList = new BaseSkinToneList();
//	private BaseEyeColorList baseEyeColorList = new BaseEyeColorList();
//	private FullBodyOutfitPane fullBodyOutfitPane = new FullBodyOutfitPane();
//	private HairPane hairPane = new HairPane();
//	public IngredientsPane() {
//		setBackground( java.awt.Color.RED );
//		int xPad = 4;
//		int yPad = 4;
//		edu.cmu.cs.dennisc.swing.SpringUtilities.springItUpANotch( this, this.createComponentRows(), xPad, yPad );
//	}
//	private static zoot.ZLabel createLabel( String text ) {
//		zoot.ZLabel rv = new zoot.ZLabel( text );
//		rv.setHorizontalAlignment( javax.swing.SwingConstants.TRAILING );
//		return rv;
//	}
//	private java.util.List< java.awt.Component[] > createComponentRows() {
//		java.util.List< java.awt.Component[] > rv = new java.util.LinkedList< java.awt.Component[] >();
//		rv.add( new java.awt.Component[] { createLabel( "life stage:" ), lifeStageList } );
//		rv.add( new java.awt.Component[] { createLabel( "gender:" ), genderList } );
//		rv.add( new java.awt.Component[] { createLabel( "skin tone:" ), baseSkinToneList } );
//		rv.add( new java.awt.Component[] { createLabel( "eye color:" ), baseEyeColorList } );
//		return rv;
//	}
//}

class RandomPersonActionOperation extends org.alice.ide.operations.AbstractActionOperation {
	private IngredientsPane ingredientsPane;
	public RandomPersonActionOperation( IngredientsPane ingredientsPane ) {
		this.ingredientsPane = ingredientsPane;
		this.putValue( javax.swing.Action.NAME, "random" );
	}
	public void perform( zoot.ActionContext actionContext ) {
		this.ingredientsPane.randomize();
		actionContext.commit();
	}
}


class IngredientsPane extends swing.GridBagPane {
	private LifeStageList lifeStageList = new LifeStageList();
	private GenderList genderList = new GenderList();
	private FitnessLevelPane fitnessLevelPane = new FitnessLevelPane();
	private BaseSkinToneList baseSkinToneList = new BaseSkinToneList();
	private BaseEyeColorList baseEyeColorList = new BaseEyeColorList();
	private HairPane hairPane = new HairPane();
	private FullBodyOutfitPane fullBodyOutfitPane = new FullBodyOutfitPane();
	public IngredientsPane() {
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
		this.setBackground( new java.awt.Color( 220, 220, 255 ) );
		this.setOpaque( true );

		
//		lifeStageList.setSelectedValue( org.alice.apis.stage.LifeStage.ADULT, true );
//		this.randomize();
//		this.hairPane.handleEpicChange( lifeStageList.getSelectedTypedValue(), genderList.getSelectedTypedValue() );
//		this.fullBodyOutfitPane.handleEpicChange( lifeStageList.getSelectedTypedValue(), genderList.getSelectedTypedValue() );
//		genderList.setSelectedValue( org.alice.apis.stage.Gender.FEMALE, true );
		this.hairPane.handleEpicChange( org.alice.apis.stage.LifeStage.ADULT, org.alice.apis.stage.Gender.FEMALE );
		this.fullBodyOutfitPane.handleEpicChange( org.alice.apis.stage.LifeStage.ADULT, org.alice.apis.stage.Gender.FEMALE );
		
		java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
		gbc.fill = java.awt.GridBagConstraints.BOTH;
		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		
		final int INSET_TOP = 12;
		final int INSET_LEFT = 8;

		this.add( new zoot.ZLabel( "life stage" ), gbc );
		this.add( this.lifeStageList, gbc );
		
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

		gbc.insets.top = INSET_TOP;
		gbc.gridwidth = java.awt.GridBagConstraints.RELATIVE;
		gbc.weightx = 0.0;
		this.add( new zoot.ZLabel( "eye color" ), gbc );
		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		gbc.insets.left = INSET_LEFT;
		this.add( new zoot.ZLabel( "hair" ), gbc );
		gbc.insets.left = 0;

		
		gbc.weighty = 1.0;
		gbc.insets.top = 0;
		gbc.gridwidth = java.awt.GridBagConstraints.RELATIVE;
		gbc.weightx = 0.0;
		this.add( this.baseEyeColorList, gbc );
		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gbc.weightx = 0.0;
		gbc.insets.left = INSET_LEFT;
		this.add( new javax.swing.JScrollPane( this.hairPane ), gbc ); 
		gbc.insets.left = 0;

		gbc.weighty = 0.0;
		gbc.insets.top = INSET_TOP;
		this.add( new zoot.ZLabel( "full body outfit" ), gbc );
		gbc.insets.top = 0;
		gbc.weighty = 4.0;
		this.add( new javax.swing.JScrollPane( this.fullBodyOutfitPane ), gbc ); 
	}
	
	public void randomize() {
		this.genderList.randomize();
		this.baseSkinToneList.randomize();
		this.baseEyeColorList.randomize();
		//this.fitnessLevelPane.randomize();
	}
}

/**
 * @author Dennis Cosgrove
 */
public class PersonEditor extends org.alice.ide.Editor< edu.cmu.cs.dennisc.alice.ast.AbstractType > {
	private LookingGlass lookingGlass = new LookingGlass();
	private IngredientsPane ingredientsPane = new IngredientsPane();
	public PersonEditor() {
		javax.swing.JSplitPane splitPane = new javax.swing.JSplitPane( javax.swing.JSplitPane.HORIZONTAL_SPLIT, this.lookingGlass, this.ingredientsPane );
		this.setLayout( new java.awt.GridLayout( 1, 1 ) );
		this.add( splitPane );
	}
	public static void main( String[] args ) {
		zoot.ZFrame frame = new zoot.ZFrame() {
			@Override
			protected void handleWindowOpened( java.awt.event.WindowEvent e ) {
			}
			@Override
			protected void handleQuit( java.util.EventObject e ) {
				System.exit( 0 );
			}
		};
		frame.setSize( new java.awt.Dimension( 1024, 768 ) );
		frame.getContentPane().add( new PersonEditor() );
		frame.setVisible( true );
	}
}
