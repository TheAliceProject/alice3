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
package edu.cmu.cs.dennisc.croquet;

/**
 * @author Dennis Cosgrove
 */
public /*final*/ class BooleanState extends State<Boolean> {
	public static interface ValueObserver {
		public void changing( boolean nextValue );
		public void changed( boolean nextValue );
	};
	private java.util.List< ValueObserver > valueObservers = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	public void addValueObserver( ValueObserver valueObserver ) {
		this.valueObservers.add( valueObserver );
	}
	public void addAndInvokeValueObserver(ValueObserver valueObserver) {
		this.addValueObserver(valueObserver);
		valueObserver.changed(this.getValue());
	}
	public void removeValueObserver( ValueObserver valueObserver ) {
		this.valueObservers.remove( valueObserver );
	}

	private javax.swing.ButtonModel createButtonModel() {
		return new javax.swing.JToggleButton.ToggleButtonModel();
	}
	private javax.swing.ButtonModel buttonModel = this.createButtonModel();
	private javax.swing.Action action = new javax.swing.AbstractAction() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
		}
	};
	private java.awt.event.ItemListener itemListener = new java.awt.event.ItemListener() {
		public void itemStateChanged(java.awt.event.ItemEvent e) {
			if( ContextManager.isInTheMidstOfUndoOrRedo() ) {
				//pass
			} else {
				if( BooleanState.this.isContextCommitDesired() ) {
					BooleanState.this.commitEdit( new BooleanStateEdit( e.getStateChange() == java.awt.event.ItemEvent.SELECTED ), e );
				}
			}
		}
	};

	private boolean value;
	private String trueText;
	private String falseText;

	public BooleanState(Group group, java.util.UUID id, boolean initialState ) {
		super(group, id);
		this.value = initialState;
		this.buttonModel.setSelected(initialState);
		this.buttonModel.addItemListener(this.itemListener);
		this.localize();
	}
	@Deprecated
	public BooleanState(Group group, java.util.UUID id, boolean initialState, String name ) {
		this(group, id, initialState );
		this.setTextForBothTrueAndFalse( name );
	}
	
	private void commitEdit( BooleanStateEdit booleanStateEdit, java.awt.event.ItemEvent e ) {
		BooleanStateContext childContext = ContextManager.createAndPushBooleanStateContext( BooleanState.this, e, null );
		childContext.commitAndInvokeDo( booleanStateEdit );
		ModelContext< ? > popContext = ContextManager.popContext();
		assert popContext == childContext;
	}
	
	@Override
	public Edit<?> commitTutorialCompletionEdit( Edit<?> originalEdit, edu.cmu.cs.dennisc.croquet.Retargeter retargeter ) {
		assert originalEdit instanceof BooleanStateEdit;
		BooleanStateEdit booleanStateEdit = (BooleanStateEdit)originalEdit;
		this.commitEdit( booleanStateEdit, null );
		return booleanStateEdit;
	}
	
	
	@Override
	protected boolean isOwnerOfEdit() {
		return true;
	}
	
	/*package-private*/boolean isContextCommitDesired() {
		return true;
	}
	
	@Override
	public String getTutorialNoteText( Edit< ? > edit ) {
		BooleanStateEdit booleanStateEdit = (BooleanStateEdit)edit;
		StringBuilder sb = new StringBuilder();
		if( edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( this.trueText, this.falseText ) ) {
			if( booleanStateEdit.getNextValue() ) {
				sb.append( "Select " );
			} else {
				sb.append( "Unselect " );
			}
			sb.append( "<strong>" );
			sb.append( this.getTrueText() );
			sb.append( "</strong>" );
		} else {
			sb.append( "Press " );
			sb.append( "<strong>" );
			if( booleanStateEdit.getNextValue() ) {
				sb.append( this.falseText );
			} else {
				sb.append( this.trueText );
			}
			sb.append( "</strong>" );
		}
		return sb.toString();
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

	/*package-private*/ javax.swing.ButtonModel getButtonModel() {
		return this.buttonModel;
	}
	/*package-private*/ javax.swing.Action getAction() {
		return this.action;
	}
	@Override
	public Boolean getValue() {
		return this.buttonModel.isSelected();
	}
	public void setValue( Boolean value ) {
		if( value != this.value ) {
			//this.buttonModel.removeItemListener(itemListener);

			for( ValueObserver valueObserver : this.valueObservers ) {
				valueObserver.changing( value );
			}
			this.buttonModel.setSelected(value);
			this.value = value;
			for( ValueObserver valueObserver : this.valueObservers ) {
				valueObserver.changed( value );
			}

			//this.buttonModel.addItemListener(itemListener);
			this.updateName();
		}
	}

	public String getTrueText() {
		return this.trueText;
	}
	public String getFalseText() {
		return this.falseText;
	}
	public void setTextForBothTrueAndFalse(String text) {
		this.setTextForTrueAndTextForFalse( text, text );
	}
	public void setTextForTrueAndTextForFalse(String trueText, String falseText) {
		this.trueText = trueText;
		this.falseText = falseText;
		this.updateName();
	}


	private void updateName() {
		String name;
		if (this.getValue()) {
			name = this.trueText;
		} else {
			name = this.falseText;
		}
		this.action.putValue(javax.swing.Action.NAME, name);
	}
	
	public RadioButton createRadioButton() {
		return new RadioButton( this );
	}
	public CheckBox createCheckBox() {
		return new CheckBox( this );
	}
	public CheckBoxMenuItem createCheckBoxMenuItem() {
		return new CheckBoxMenuItem( this );
	}
	public ToolPalette createToolPalette( Component<?> component ) {
		ToolPaletteTitle title = new ToolPaletteTitle( this );
		return new ToolPalette(title, component);
	}
}
