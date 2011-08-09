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
package org.lgna.croquet;

import org.lgna.croquet.edits.Edit;

/**
 * @author Dennis Cosgrove
 */
public abstract class BooleanState extends State< Boolean > {
	private boolean value;
	private String trueText;
	private String falseText;
	private javax.swing.Icon trueIcon;
	private javax.swing.Icon falseIcon;

	private javax.swing.ButtonModel buttonModel = this.createButtonModel();
	private javax.swing.Action action = new javax.swing.AbstractAction() {
		public void actionPerformed( java.awt.event.ActionEvent e ) {
		}
	};
	private java.awt.event.ItemListener itemListener = new java.awt.event.ItemListener() {
		public void itemStateChanged( java.awt.event.ItemEvent e ) {
			org.lgna.croquet.history.TransactionManager.handleItemStateChanged( BooleanState.this, e );
		}
	};

	public BooleanState( Group group, java.util.UUID id, boolean initialState ) {
		super( group, id );
		this.value = initialState;
		this.buttonModel.setSelected( initialState );
		this.buttonModel.addItemListener( this.itemListener );
	}

	private javax.swing.ButtonModel createButtonModel() {
		return new javax.swing.JToggleButton.ToggleButtonModel();
	}
	@Override
	protected void localize() {
		String text = this.getDefaultLocalizedText();
		if( text != null ) {
			this.setTextForBothTrueAndFalse( text );
		} else {
			String trueText = this.getLocalizedText( "true" );
			if( trueText != null ) {
				String falseText = this.getLocalizedText( "false" );
				if( falseText != null ) {
					this.setTextForTrueAndTextForFalse( trueText, falseText );
				} else {
					//todo:
				}
			}
		}
	}

	public javax.swing.ButtonModel getButtonModel() {
		return this.buttonModel;
	}
	public javax.swing.Action getAction() {
		return this.action;
	}

//	private void commitEdit( boolean value, org.lgna.croquet.Trigger trigger ) {
//		org.lgna.croquet.steps.BooleanStateChangeStep step = org.lgna.croquet.steps.TransactionManager.addBooleanStateChangeStep( this, trigger );
//		step.commitAndInvokeDo( new org.lgna.croquet.edits.BooleanStateEdit( step, value ) );
//		//		TransactionManager.addBooleanStateChangeStep( this );
//		//		BooleanStateContext childContext = ContextManager.createAndPushBooleanStateContext( BooleanState.this, e, null );
//		//		childContext.commitAndInvokeDo( booleanStateEdit );
//		//		ModelContext< ? > popContext = ContextManager.popContext();
//		//		assert popContext == childContext;
//	}

	@Override
	public Edit< ? > commitTutorialCompletionEdit( org.lgna.croquet.history.CompletionStep< ? > step, Edit< ? > originalEdit, org.lgna.croquet.Retargeter retargeter ) {
		assert originalEdit instanceof org.lgna.croquet.edits.BooleanStateEdit;
		org.lgna.croquet.edits.BooleanStateEdit booleanStateEdit = (org.lgna.croquet.edits.BooleanStateEdit)originalEdit;
		return org.lgna.croquet.history.TransactionManager.commitEdit( this, booleanStateEdit.getNextValue(), new org.lgna.croquet.triggers.SimulatedTrigger() );
	}

//	@Override
//	protected StringBuilder updateTutorialTransactionTitle( StringBuilder rv, org.lgna.croquet.steps.CompletionStep< ? > step, UserInformation userInformation ) {
//		this.updateTutorialStepText( rv, step, step.getEdit(), userInformation );
//		return rv;
//	}

	@Override
	protected StringBuilder updateTutorialStepText( StringBuilder rv, org.lgna.croquet.history.Step< ? > step, Edit< ? > edit, UserInformation userInformation ) {
		if( edit instanceof org.lgna.croquet.edits.BooleanStateEdit ) {
			org.lgna.croquet.edits.BooleanStateEdit booleanStateEdit = (org.lgna.croquet.edits.BooleanStateEdit)edit;
			if( edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( this.trueText, this.falseText ) ) {
				if( booleanStateEdit.getNextValue() ) {
					rv.append( "Select " );
				} else {
					rv.append( "Unselect " );
				}
				rv.append( "<strong>" );
				rv.append( this.getTrueText() );
				rv.append( "</strong>" );
			} else {
				rv.append( "Press " );
				rv.append( "<strong>" );
				if( booleanStateEdit.getNextValue() ) {
					rv.append( this.falseText );
				} else {
					rv.append( this.trueText );
				}
				rv.append( "</strong>" );
			}
		}
		return rv;
	}

	@Override
	public Boolean getValue() {
		return this.buttonModel.isSelected();
	}
	public void setValue( Boolean value ) {
		if( value != this.value ) {
			//this.buttonModel.removeItemListener(itemListener);

			boolean isAdjusting = false;
			
			Boolean prevValue = this.value;
			Boolean nextValue = value;
			this.fireChanging( prevValue, nextValue, isAdjusting );
			this.buttonModel.setSelected( value );
			this.value = value;
			this.fireChanged( prevValue, nextValue, isAdjusting );

			//this.buttonModel.addItemListener(itemListener);
			this.updateNameAndIcon();
		}
	}

	public String getTrueText() {
		return this.trueText;
	}
	public String getFalseText() {
		return this.falseText;
	}
	public void setTextForBothTrueAndFalse( String text ) {
		this.setTextForTrueAndTextForFalse( text, text );
	}
	public void setTextForTrueAndTextForFalse( String trueText, String falseText ) {
		this.trueText = trueText;
		this.falseText = falseText;
		this.updateNameAndIcon();
	}
	public javax.swing.Icon getTrueIcon() {
		return this.trueIcon;
	}
	public javax.swing.Icon getFalseIcon() {
		return this.falseIcon;
	}
	public void setIconForBothTrueAndFalse( javax.swing.Icon icon ) {
		this.setIconForTrueAndIconForFalse( icon, icon );
	}
	public void setIconForTrueAndIconForFalse( javax.swing.Icon trueIcon, javax.swing.Icon falseIcon ) {
		this.trueIcon = trueIcon;
		this.falseIcon = falseIcon;
		this.updateNameAndIcon();
	}

	private void updateNameAndIcon() {
		String name;
		javax.swing.Icon icon;
		if( this.getValue() ) {
			name = this.trueText;
			icon = this.trueIcon;
		} else {
			name = this.falseText;
			icon = this.falseIcon;
		}
		this.action.putValue( javax.swing.Action.NAME, name );
		this.action.putValue( javax.swing.Action.SMALL_ICON, icon );
	}

	private BooleanStateMenuItemPrepModel menuPrepModel;

	public synchronized BooleanStateMenuItemPrepModel getMenuItemPrepModel() {
		if( this.menuPrepModel != null ) {
			//pass
		} else {
			this.menuPrepModel = new BooleanStateMenuItemPrepModel( this );
		}
		return this.menuPrepModel;
	}

	public org.lgna.croquet.components.RadioButton createRadioButton() {
		return new org.lgna.croquet.components.RadioButton( this );
	}
	public org.lgna.croquet.components.CheckBox createCheckBox() {
		return new org.lgna.croquet.components.CheckBox( this );
	}
	public org.lgna.croquet.components.PushButton createPushButton() {
		return new org.lgna.croquet.components.PushButton( this );
	}
	//todo: convert to composite
	public org.lgna.croquet.components.ToolPalette createToolPalette( org.lgna.croquet.components.JComponent< ? > component ) {
		return new org.lgna.croquet.components.ToolPalette( this, component );
	}
}
