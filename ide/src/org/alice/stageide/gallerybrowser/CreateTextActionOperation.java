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
package org.alice.stageide.gallerybrowser;

/**
 * @author Dennis Cosgrove
 */
class CreateTextPane extends edu.cmu.cs.dennisc.croquet.RowsSpringPanel {
	class FamilyList extends edu.cmu.cs.dennisc.croquet.List< String > {
		public FamilyList() {
			this.setListData( "Serif", "SansSerif" );
			this.setSelectedIndex( 0 );
		}
		public org.alice.apis.moveandturn.font.FamilyAttribute getFamilyAttribute() {
			Object value = this.getSelectedValue();
			org.alice.apis.moveandturn.font.FamilyAttribute rv;
			if( value.equals( "Serif" ) ) {
				rv = org.alice.apis.moveandturn.font.FamilyConstant.SERIF;
			} else {
				rv = org.alice.apis.moveandturn.font.FamilyConstant.SANS_SERIF;
			}
			return rv;
		}
		public void setFamilyAttribute( org.alice.apis.moveandturn.font.FamilyAttribute familyAttribute ) {
			if( familyAttribute == org.alice.apis.moveandturn.font.FamilyConstant.SERIF ) {
				this.setSelectedValue( "Serif", true );
			} else {
				this.setSelectedValue( "SansSerif", true );
			}
		}
	}

	class StyleList extends edu.cmu.cs.dennisc.croquet.List< String > {
		public StyleList() {
			this.setListData( "Regular", "Bold", "Italic", "Bold Italic" );
			this.setSelectedIndex( 0 );
		}
		public org.alice.apis.moveandturn.font.WeightAttribute getWeightAttribute() {
			Object value = this.getSelectedValue();
			org.alice.apis.moveandturn.font.WeightAttribute rv;
			if( value != null && (value.equals( "Bold" ) || value.equals( "Bold Italic" )) ) {
				rv = org.alice.apis.moveandturn.font.WeightConstant.BOLD;
			} else {
				rv = org.alice.apis.moveandturn.font.WeightConstant.REGULAR;
			}
			return rv;
		}
		public org.alice.apis.moveandturn.font.PostureAttribute getPostureAttribute() {
			Object value = this.getSelectedValue();
			org.alice.apis.moveandturn.font.PostureAttribute rv;
			if( value != null && (value.equals( "Italic" ) || value.equals( "Bold Italic" )) ) {
				rv = org.alice.apis.moveandturn.font.PostureConstant.OBLIQUE;
			} else {
				rv = org.alice.apis.moveandturn.font.PostureConstant.REGULAR;
			}
			return rv;
		}
		public void setStyleAttributes( org.alice.apis.moveandturn.font.WeightAttribute weight, org.alice.apis.moveandturn.font.PostureAttribute posture ) {
			boolean isBold = (weight == org.alice.apis.moveandturn.font.WeightConstant.BOLD);
			boolean isItalic = (posture == org.alice.apis.moveandturn.font.PostureConstant.OBLIQUE);
			String selectedValue;
			if( isBold ) {
				if( isItalic ) {
					selectedValue = "Bold Italic";
				} else {
					selectedValue = "Bold";
				}
			} else {
				if( isItalic ) {
					selectedValue = "Italic";
				} else {
					selectedValue = "Regular";
				}
			}
			this.setSelectedValue( selectedValue, true );
		}
	}

//	private edu.cmu.cs.dennisc.croquet.StringStateOperation textState = new edu.cmu.cs.dennisc.croquet.StringStateOperation( edu.cmu.cs.dennisc.zoot.ZManager.UNKNOWN_GROUP, java.util.UUID.fromString( "e1f5fa35-6ddb-4d9a-8a76-8ff9421cf4b4" ), "" );
//	private edu.cmu.cs.dennisc.croquet.StringStateOperation instanceNameState = new edu.cmu.cs.dennisc.croquet.StringStateOperation( edu.cmu.cs.dennisc.zoot.ZManager.UNKNOWN_GROUP, java.util.UUID.fromString( "1c29999d-003b-4f90-83da-67d21f93789a" ), "" );

	private javax.swing.JTextField textVC;
	private javax.swing.JTextField instanceNameVC;
	private edu.cmu.cs.dennisc.croquet.CheckBox constrainInstanceNameToTextVC;

	private javax.swing.JTextField heightTextField;
	private FamilyList familyList;
	private StyleList styleList;

	private edu.cmu.cs.dennisc.croquet.Label sample;

	public CreateTextPane() {
		final int INSET = 16;
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( INSET, INSET, INSET, INSET ) );
		this.setBackgroundColor( org.alice.ide.IDE.getSingleton().getFieldColor() );
		this.setOpaque( true );
		class TextField extends edu.cmu.cs.dennisc.javax.swing.components.JSuggestiveTextField {
			public TextField( String text, String textForBlankCondition ) {
				this.setText( text );
				this.setTextForBlankCondition( textForBlankCondition );
			}
			@Override
			public java.awt.Dimension getPreferredSize() {
				return edu.cmu.cs.dennisc.java.awt.DimensionUtilties.constrainToMinimumWidth( super.getPreferredSize(), 160 );
			}
			@Override
			public java.awt.Dimension getMaximumSize() {
				java.awt.Dimension rv = super.getMaximumSize();
				rv.height = this.getPreferredSize().height;
				return rv;
			}
		}
		this.textVC = new TextField( "", " enter text here" );
		this.textVC.getDocument().addDocumentListener( new edu.cmu.cs.dennisc.javax.swing.event.SimplifiedDocumentAdapter() {
			@Override
			protected void updated( javax.swing.event.DocumentEvent e ) {
				CreateTextPane.this.handleTextChange( e );
			}
		} );
		//this.textVC.getDocument().addDocumentListener( ecc.dennisc.swing.event.FilteredDocumentAdapter( this.handleTextChange ) );
		this.instanceNameVC = new TextField( "", " enter instance name here" );
//		this.instanceNameVC.getDocument().addDocumentListener( new edu.cmu.cs.dennisc.javax.swing.event.SimplifiedDocumentAdapter() {
//			@Override
//			protected void updated( javax.swing.event.DocumentEvent e ) {
//				CreateTextPane.this.updateOKButton();
//			}
//		} );
		//this.instanceNameVC.getDocument().addDocumentListener( ecc.dennisc.swing.event.FilteredDocumentAdapter( this.handleInstanceNameChange ) );

		this.constrainInstanceNameToTextVC = new ConstrainInstanceNameToTextBooleanStateOperation().createCheckBox();
		this.constrainInstanceNameToTextVC.getJComponent().setSelected( true );
		this.constrainInstanceNameToTextVC.setOpaque( false );
		this.heightTextField = new TextField( "1.0", " enter height in meters here" );
//		this.heightTextField.getDocument().addDocumentListener( new edu.cmu.cs.dennisc.javax.swing.event.SimplifiedDocumentAdapter() {
//			@Override
//			protected void updated( javax.swing.event.DocumentEvent e ) {
//				CreateTextPane.this.updateOKButton();
//			}
//		} );
		this.familyList = new FamilyList();
		this.styleList = new StyleList();

		this.sample = new edu.cmu.cs.dennisc.croquet.Label( "AaBbYyZz", 1.2f );
		this.updateSample();

		class ListSelectionAdapter implements javax.swing.event.ListSelectionListener {
			public void valueChanged( javax.swing.event.ListSelectionEvent e ) {
				if( e.getValueIsAdjusting() ) {
					//pass
				} else {
					CreateTextPane.this.updateSample();
				}
			}
		}
		ListSelectionAdapter listSelectionAdapter = new ListSelectionAdapter();

		this.familyList.addListSelectionListener( listSelectionAdapter );
		this.styleList.addListSelectionListener( listSelectionAdapter );

//		edu.cmu.cs.dennisc.javax.swing.components.JRowsSpringPane pane = new edu.cmu.cs.dennisc.javax.swing.components.JRowsSpringPane( 16, 4 ) {
//			@Override
//			protected java.util.List< java.awt.Component[] > addComponentRows( java.util.List< java.awt.Component[] > rv ) {
//				CreateTextPane.this.updateRows( rv );
//				return rv;
//			}
//		};
//
//		this.addComponent( pane, java.awt.BorderLayout.CENTER );
	}

	class ConstrainInstanceNameToTextBooleanStateOperation extends org.alice.ide.operations.AbstractBooleanStateOperation {
		public ConstrainInstanceNameToTextBooleanStateOperation() {
			super( org.alice.app.ProjectApplication.IDE_GROUP, java.util.UUID.fromString( "74c18933-e5d7-4c48-ad88-46a7a83ff12d" ), false, "constrain to text" );
		}
		@Override
		protected void handleStateChange( boolean value ) {
			CreateTextPane.this.instanceNameVC.setEditable( value == false );
		}
	}
	@Override
	protected java.util.List<edu.cmu.cs.dennisc.croquet.Component<?>[]> updateComponentRows(java.util.List<edu.cmu.cs.dennisc.croquet.Component<?>[]> rv) {
		rv.add( edu.cmu.cs.dennisc.croquet.SpringUtilities.createRow( 
				edu.cmu.cs.dennisc.croquet.SpringUtilities.createTrailingTopLabel( "text:" ), 
				new edu.cmu.cs.dennisc.croquet.SwingAdapter( this.textVC ), 
				null 
		) );
		rv.add( edu.cmu.cs.dennisc.croquet.SpringUtilities.createRow( 
				edu.cmu.cs.dennisc.croquet.SpringUtilities.createTrailingTopLabel( "instance:" ), 
				new edu.cmu.cs.dennisc.croquet.SwingAdapter( this.instanceNameVC ), 
				this.constrainInstanceNameToTextVC 
		) );
		rv.add( edu.cmu.cs.dennisc.croquet.SpringUtilities.createRow( 
				edu.cmu.cs.dennisc.croquet.BoxUtilities.createVerticalSliver( 24 ),
				null,
				null 
		) );
		rv.add( edu.cmu.cs.dennisc.croquet.SpringUtilities.createRow( 
				edu.cmu.cs.dennisc.croquet.SpringUtilities.createTrailingTopLabel( "letter height:" ), 
				new edu.cmu.cs.dennisc.croquet.SwingAdapter( this.heightTextField ), 
				new edu.cmu.cs.dennisc.croquet.Label( "(meters)" ) 
		) );
		rv.add( edu.cmu.cs.dennisc.croquet.SpringUtilities.createRow( 
				edu.cmu.cs.dennisc.croquet.BoxUtilities.createVerticalSliver( 4 ), 
				null, 
				null 
		) );
		rv.add( edu.cmu.cs.dennisc.croquet.SpringUtilities.createRow( 
				edu.cmu.cs.dennisc.croquet.SpringUtilities.createTrailingTopLabel( "family:" ), 
				this.familyList, 
				null 
		) );
		rv.add( edu.cmu.cs.dennisc.croquet.SpringUtilities.createRow( 
				edu.cmu.cs.dennisc.croquet.SpringUtilities.createTrailingTopLabel( "style:" ), 
				this.styleList, 
				null 
		) );
		rv.add( edu.cmu.cs.dennisc.croquet.SpringUtilities.createRow( 
				edu.cmu.cs.dennisc.croquet.SpringUtilities.createTrailingTopLabel( "sample:" ), 
				this.sample, 
				null 
		) );
		rv.add( edu.cmu.cs.dennisc.croquet.SpringUtilities.createRow( null, null, null ) );

		return rv;
	}

	private void updateSample() {
		org.alice.apis.moveandturn.font.FamilyAttribute familyAttribute = this.familyList.getFamilyAttribute();
		org.alice.apis.moveandturn.font.WeightAttribute weightAttribute = this.styleList.getWeightAttribute();
		org.alice.apis.moveandturn.font.PostureAttribute postureAttribute = this.styleList.getPostureAttribute();
		edu.cmu.cs.dennisc.java.awt.font.FontUtilities.setFontToDerivedFont( this.sample.getJComponent(), familyAttribute.getKey(), familyAttribute.getValue(), weightAttribute.getKey(), weightAttribute.getValue(), postureAttribute.getKey(), postureAttribute.getValue() );
	}
//	@Override
//	public boolean isOKButtonValid() {
//		try {
//			Double.parseDouble( this.heightTextField.getText() );
//		} catch( NumberFormatException nfe ) {
//			return false;
//		}
//		boolean isInstanceNameValid = edu.cmu.cs.dennisc.alice.ast.IdentifierUtilities.isValidIdentifier( this.instanceNameVC.getText() );
//		return super.isOKButtonValid() && isInstanceNameValid;
//	}
	private void handleTextChange( javax.swing.event.DocumentEvent e ) {
		if( this.constrainInstanceNameToTextVC.getJComponent().isSelected() ) {
			String text = this.textVC.getText();
			String instanceName = edu.cmu.cs.dennisc.alice.ast.IdentifierUtilities.getConventionalInstanceName( text );
			this.instanceNameVC.setText( instanceName );
		}
	}
	protected org.alice.apis.moveandturn.Text getActualInputValue() {
		org.alice.apis.moveandturn.Text rv = new org.alice.apis.moveandturn.Text();
		org.alice.apis.moveandturn.font.FamilyAttribute familyAttribute = this.familyList.getFamilyAttribute();
		org.alice.apis.moveandturn.font.WeightAttribute weightAttribute = this.styleList.getWeightAttribute();
		org.alice.apis.moveandturn.font.PostureAttribute postureAttribute = this.styleList.getPostureAttribute();

		rv.setName( this.instanceNameVC.getText() );
		rv.setValue( this.textVC.getText() );
		rv.setFont( new org.alice.apis.moveandturn.Font( familyAttribute, weightAttribute, postureAttribute ) );
		rv.setLetterHeight( Double.parseDouble( this.heightTextField.getText() ) );
		return rv;
	}
//	public static void main( String[] args ) {
//		org.alice.ide.IDE ide = new org.alice.ide.FauxIDE();
//		CreateTextPane createTextPane = new CreateTextPane();
//		org.alice.apis.moveandturn.Text text = createTextPane.showInJDialog( ide.getJFrame() );
//		edu.cmu.cs.dennisc.print.PrintUtilities.println( text );
//		System.exit( 0 );
//	}
}

/**
 * @author Dennis Cosgrove
 */
class CreateTextActionOperation extends AbstractGalleryDeclareFieldOperation {
	private CreateTextPane createTextPane = new CreateTextPane();
	public CreateTextActionOperation() {
		super( java.util.UUID.fromString( "37c0a6d6-a21b-4abb-829b-bd3621cada8d" ) );
		this.setName( "Create Text..." );
	}
	@Override
	protected edu.cmu.cs.dennisc.croquet.Component<?> prologue(edu.cmu.cs.dennisc.croquet.Context context) {
		//todo: reset
		return this.createTextPane;
	}
	@Override
	protected edu.cmu.cs.dennisc.pattern.Tuple2< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice, java.lang.Object > createFieldAndInstance( edu.cmu.cs.dennisc.croquet.Context context ) {
		//"Create Text"
		org.alice.apis.moveandturn.Text text = this.createTextPane.getActualInputValue();
		if( text != null ) {
			edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type = this.getIDE().getTypeDeclaredInAliceFor( org.alice.apis.moveandturn.Text.class );
			edu.cmu.cs.dennisc.alice.ast.Expression initializer = org.alice.ide.ast.NodeUtilities.createInstanceCreation( type );
			edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field = new edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice( text.getName(), type, initializer );
			field.finalVolatileOrNeither.setValue( edu.cmu.cs.dennisc.alice.ast.FieldModifierFinalVolatileOrNeither.FINAL );
			field.access.setValue( edu.cmu.cs.dennisc.alice.ast.Access.PRIVATE );
			return new edu.cmu.cs.dennisc.pattern.Tuple2< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice, Object >( field, text );
		} else {
			return null;
		}
	}
}
