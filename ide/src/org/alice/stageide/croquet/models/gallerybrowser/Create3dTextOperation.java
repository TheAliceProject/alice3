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
package org.alice.stageide.croquet.models.gallerybrowser;

/**
 * @author Dennis Cosgrove
 */
//class CreateTextPane extends org.alice.ide.declarationpanes.CreateLargelyPredeterminedFieldPane {
class CreateTextPane extends org.lgna.croquet.components.RowsSpringPanel {
	private static abstract class TextAttributeSelectionOperation extends org.lgna.croquet.DefaultListSelectionState<String> {
		public TextAttributeSelectionOperation( java.util.UUID individualId, int selectionIndex, String... items ) {
			super( org.lgna.croquet.Application.INHERIT_GROUP, individualId, org.alice.ide.croquet.codecs.StringCodec.SINGLETON, selectionIndex, items );
		}
	}
	private static class FamilySelectionOperation extends TextAttributeSelectionOperation {
		public FamilySelectionOperation() {
			super( java.util.UUID.fromString( "a5678cc6-78b0-4cc1-adbb-e90969d42823" ), 0, "Serif", "SansSerif" );
		}
		public org.alice.apis.moveandturn.font.FamilyAttribute getFamilyAttribute() {
			Object value = this.getSelectedItem();
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
				this.setSelectedItem( "Serif" );
			} else {
				this.setSelectedItem( "SansSerif" );
			}
		}
	}

	private static class StyleSelectionOperation extends TextAttributeSelectionOperation {
		public StyleSelectionOperation() {
			super( java.util.UUID.fromString( "a5678cc6-78b0-4cc1-adbb-e90969d42823" ), 0, "Regular", "Bold", "Italic", "Bold Italic" );
		}
		public org.alice.apis.moveandturn.font.WeightAttribute getWeightAttribute() {
			Object value = this.getSelectedItem();
			org.alice.apis.moveandturn.font.WeightAttribute rv;
			if( value != null && (value.equals( "Bold" ) || value.equals( "Bold Italic" )) ) {
				rv = org.alice.apis.moveandturn.font.WeightConstant.BOLD;
			} else {
				rv = org.alice.apis.moveandturn.font.WeightConstant.REGULAR;
			}
			return rv;
		}
		public org.alice.apis.moveandturn.font.PostureAttribute getPostureAttribute() {
			Object value = this.getSelectedItem();
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
			this.setSelectedItem( selectedValue );
		}
	}

//	private org.lgna.croquet.StringStateOperation textState = new org.lgna.croquet.StringStateOperation( edu.cmu.cs.dennisc.zoot.ZManager.UNKNOWN_GROUP, java.util.UUID.fromString( "e1f5fa35-6ddb-4d9a-8a76-8ff9421cf4b4" ), "" );
//	private org.lgna.croquet.StringStateOperation instanceNameState = new org.lgna.croquet.StringStateOperation( edu.cmu.cs.dennisc.zoot.ZManager.UNKNOWN_GROUP, java.util.UUID.fromString( "1c29999d-003b-4f90-83da-67d21f93789a" ), "" );

	private javax.swing.JTextField textVC;
	private javax.swing.JTextField instanceNameVC;
	private org.lgna.croquet.components.CheckBox constrainInstanceNameToTextVC;

	private javax.swing.JTextField heightTextField;
	private FamilySelectionOperation familySelection;
	private StyleSelectionOperation styleSelection;

	private org.lgna.croquet.components.Label sample;

//	public CreateTextPane( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType ) {
//		super( declaringType, org.alice.apis.moveandturn.Billboard.class, null );
	public CreateTextPane( final Create3dTextOperation operation ) {
		final int INSET = 16;
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( INSET, INSET, INSET, INSET ) );
		this.setBackgroundColor( org.alice.ide.IDE.getSingleton().getTheme().getFieldColor() );
		class TextField extends edu.cmu.cs.dennisc.javax.swing.components.JSuggestiveTextField {
			public TextField( String text, String textForBlankCondition ) {
				this.setText( text );
				this.setTextForBlankCondition( textForBlankCondition );
			}
			@Override
			public java.awt.Dimension getPreferredSize() {
				return edu.cmu.cs.dennisc.java.awt.DimensionUtilities.constrainToMinimumWidth( super.getPreferredSize(), 160 );
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
//				operation.handleFiredEvent( null );
//			}
//		} );
		//this.instanceNameVC.getDocument().addDocumentListener( ecc.dennisc.swing.event.FilteredDocumentAdapter( this.handleInstanceNameChange ) );

		this.constrainInstanceNameToTextVC = new ConstrainInstanceNameToTextBooleanStateOperation().createCheckBox();
		this.constrainInstanceNameToTextVC.getAwtComponent().setSelected( true );
		this.constrainInstanceNameToTextVC.setBackgroundColor( null );
		this.heightTextField = new TextField( "1.0", " enter height in meters here" );
//		this.heightTextField.getDocument().addDocumentListener( new edu.cmu.cs.dennisc.javax.swing.event.SimplifiedDocumentAdapter() {
//			@Override
//			protected void updated( javax.swing.event.DocumentEvent e ) {
//				CreateTextPane.this.updateOKButton();
//			}
//		} );
		this.familySelection = new FamilySelectionOperation();
		this.styleSelection = new StyleSelectionOperation();

		this.sample = new org.lgna.croquet.components.Label( "AaBbYyZz", 1.2f );
		this.updateSample();

		org.lgna.croquet.ListSelectionState.ValueObserver< String > valueObserver = new org.lgna.croquet.ListSelectionState.ValueObserver< String >() {
			public void changing( org.lgna.croquet.State< String > state, String prevValue, String nextValue, boolean isAdjusting ) {
			}
			public void changed( org.lgna.croquet.State< String > state, String prevValue, String nextValue, boolean isAdjusting ) {
//				if( e.getValueIsAdjusting() ) {
//					//pass
//				} else {
					CreateTextPane.this.updateSample();
//				}
			}
		};
		
		this.familySelection.addValueObserver( valueObserver );
		this.styleSelection.addValueObserver( valueObserver );
//		class ListSelectionAdapter implements javax.swing.event.ListSelectionListener {
//			public void valueChanged( javax.swing.event.ListSelectionEvent e ) {
//				if( e.getValueIsAdjusting() ) {
//					//pass
//				} else {
//					CreateTextPane.this.updateSample();
//				}
//			}
//		}
//		ListSelectionAdapter listSelectionAdapter = new ListSelectionAdapter();
//
//		this.familySelection.addListSelectionListener( listSelectionAdapter );
//		this.styleSelection.addListSelectionListener( listSelectionAdapter );

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

	class ConstrainInstanceNameToTextBooleanStateOperation extends org.lgna.croquet.BooleanState {
		public ConstrainInstanceNameToTextBooleanStateOperation() {
			super( org.alice.ide.ProjectApplication.UI_STATE_GROUP, java.util.UUID.fromString( "74c18933-e5d7-4c48-ad88-46a7a83ff12d" ), false );
			this.setTextForBothTrueAndFalse( "constrain to text" );
			this.addValueObserver( new ValueObserver<Boolean>() {
				public void changing( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
				}
				public void changed( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
					CreateTextPane.this.instanceNameVC.setEditable( nextValue == false );
				}
			} );
		}
	}
	@Override
	protected java.util.List<org.lgna.croquet.components.Component<?>[]> updateComponentRows(java.util.List<org.lgna.croquet.components.Component<?>[]> rv) {
		rv.add( org.lgna.croquet.components.SpringUtilities.createRow( 
				org.lgna.croquet.components.SpringUtilities.createTrailingTopLabel( "text:" ), 
				new org.lgna.croquet.components.SwingAdapter( this.textVC ), 
				null 
		) );
		rv.add( org.lgna.croquet.components.SpringUtilities.createRow( 
				org.lgna.croquet.components.SpringUtilities.createTrailingTopLabel( "instance:" ), 
				new org.lgna.croquet.components.SwingAdapter( this.instanceNameVC ), 
				this.constrainInstanceNameToTextVC 
		) );
		rv.add( org.lgna.croquet.components.SpringUtilities.createRow( 
				org.lgna.croquet.components.BoxUtilities.createVerticalSliver( 24 ),
				null,
				null 
		) );
		rv.add( org.lgna.croquet.components.SpringUtilities.createRow( 
				org.lgna.croquet.components.SpringUtilities.createTrailingTopLabel( "letter height:" ), 
				new org.lgna.croquet.components.SwingAdapter( this.heightTextField ), 
				new org.lgna.croquet.components.Label( "(meters)" ) 
		) );
		rv.add( org.lgna.croquet.components.SpringUtilities.createRow( 
				org.lgna.croquet.components.BoxUtilities.createVerticalSliver( 4 ), 
				null, 
				null 
		) );
		rv.add( org.lgna.croquet.components.SpringUtilities.createRow( 
				org.lgna.croquet.components.SpringUtilities.createTrailingTopLabel( "family:" ), 
				this.familySelection.getPrepModel().createComboBox(), 
				null 
		) );
		rv.add( org.lgna.croquet.components.SpringUtilities.createRow( 
				org.lgna.croquet.components.SpringUtilities.createTrailingTopLabel( "style:" ), 
				this.styleSelection.getPrepModel().createComboBox(), 
				null 
		) );
		rv.add( org.lgna.croquet.components.SpringUtilities.createRow( 
				org.lgna.croquet.components.SpringUtilities.createTrailingTopLabel( "sample:" ), 
				this.sample, 
				null 
		) );
		rv.add( org.lgna.croquet.components.SpringUtilities.createRow( null, null, null ) );

		return rv;
	}

	private void updateSample() {
		org.alice.apis.moveandturn.font.FamilyAttribute familyAttribute = this.familySelection.getFamilyAttribute();
		org.alice.apis.moveandturn.font.WeightAttribute weightAttribute = this.styleSelection.getWeightAttribute();
		org.alice.apis.moveandturn.font.PostureAttribute postureAttribute = this.styleSelection.getPostureAttribute();
		edu.cmu.cs.dennisc.java.awt.font.FontUtilities.setFontToDerivedFont( this.sample.getAwtComponent(), familyAttribute.getKey(), familyAttribute.getValue(), weightAttribute.getKey(), weightAttribute.getValue(), postureAttribute.getKey(), postureAttribute.getValue() );
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
		if( this.constrainInstanceNameToTextVC.getAwtComponent().isSelected() ) {
			String text = this.textVC.getText();
			String instanceName = edu.cmu.cs.dennisc.alice.ast.IdentifierUtilities.getConventionalInstanceName( text );
			this.instanceNameVC.setText( instanceName );
		}
	}

//	/*package-private*/ String getInstanceNameText() {
//		return this.textVC.getText();
//	}
	protected org.alice.apis.moveandturn.Text createText() {
		org.alice.apis.moveandturn.Text rv = new org.alice.apis.moveandturn.Text();
		org.alice.apis.moveandturn.font.FamilyAttribute familyAttribute = this.familySelection.getFamilyAttribute();
		org.alice.apis.moveandturn.font.WeightAttribute weightAttribute = this.styleSelection.getWeightAttribute();
		org.alice.apis.moveandturn.font.PostureAttribute postureAttribute = this.styleSelection.getPostureAttribute();

		rv.setName( this.instanceNameVC.getText() );
		rv.setValue( this.textVC.getText() );
		rv.setFont( new org.alice.apis.moveandturn.Font( familyAttribute, weightAttribute, postureAttribute ) );
		rv.setLetterHeight( Double.parseDouble( this.heightTextField.getText() ) );
		return rv;
	}
	
}

/**
 * @author Dennis Cosgrove
 */
public class Create3dTextOperation extends org.lgna.croquet.InputDialogOperation {
	private static class SingletonHolder {
		private static Create3dTextOperation instance = new Create3dTextOperation();
	}
	public static Create3dTextOperation getInstance() {
		return SingletonHolder.instance;
	}
	private Create3dTextOperation() {
		super( edu.cmu.cs.dennisc.alice.Project.GROUP, java.util.UUID.fromString( "37c0a6d6-a21b-4abb-829b-bd3621cada8d" ) );
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: extend AbstractGalleryDeclareFieldOperation" );
	}
	
//	@Override
//	protected java.lang.String getInternalExplanation( org.lgna.croquet.history.InputDialogOperationStep step ) {
//		CreateTextPane pane = step.getMainPanel();
//		org.alice.ide.name.validators.NodeNameValidator nodeNameValidator = new org.alice.ide.name.validators.FieldNameValidator( org.alice.ide.IDE.getSingleton().getSceneType() );
//		String rv = nodeNameValidator.getExplanationIfOkButtonShouldBeDisabled( pane.getInstanceNameText() );
//		if( rv != null ) {
//			return rv;
//		} else {
//			return super.getInternalExplanation( step );
//		}
//	}

	@Override
	protected org.alice.stageide.croquet.models.gallerybrowser.CreateTextPane prologue( org.lgna.croquet.history.InputDialogOperationStep context ) {
		return new CreateTextPane( this ); 
	}
	
	private edu.cmu.cs.dennisc.pattern.Tuple2< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice, org.alice.apis.moveandturn.Text > createFieldAndInstance( org.lgna.croquet.history.InputDialogOperationStep context ) {
		//"Create Text"
		CreateTextPane createTextPane = context.getMainPanel();
		org.alice.apis.moveandturn.Text text = createTextPane.createText();
		if( text != null ) {
			edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type = org.alice.ide.IDE.getSingleton().getTypeDeclaredInAliceFor( org.alice.apis.moveandturn.Text.class );
			edu.cmu.cs.dennisc.alice.ast.Expression initializer = org.alice.ide.ast.NodeUtilities.createInstanceCreation( type );
			edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field = new edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice( text.getName(), type, initializer );
			field.finalVolatileOrNeither.setValue( edu.cmu.cs.dennisc.alice.ast.FieldModifierFinalVolatileOrNeither.FINAL );
			field.access.setValue( edu.cmu.cs.dennisc.alice.ast.Access.PRIVATE );
			return edu.cmu.cs.dennisc.pattern.Tuple2.createInstance( field, text );
		} else {
			return null;
		}
	}
	
	private final edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice getOwnerType() {
		return org.alice.ide.IDE.getSingleton().getSceneType();
	}
	private boolean isInstanceValid() {
		return true;
	}
	
	@Override
	protected final void epilogue(org.lgna.croquet.history.InputDialogOperationStep step, boolean isOk) {
		if( isOk ) {
			edu.cmu.cs.dennisc.pattern.Tuple2<edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice, org.alice.apis.moveandturn.Text> tuple = this.createFieldAndInstance( step );
			if( tuple != null ) {
				edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field = tuple.getA();
				if( field != null ) {
					edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice<?> ownerType = this.getOwnerType();
					int index = ownerType.fields.size();
					step.commitAndInvokeDo( new org.alice.ide.operations.ast.DeclareFieldEdit( step, ownerType, field, index, tuple.getB(), this.isInstanceValid() ) );
				} else {
					step.cancel();
				}
			} else {
				step.cancel();
			}
		} else {
			step.cancel();
		}
	}
}