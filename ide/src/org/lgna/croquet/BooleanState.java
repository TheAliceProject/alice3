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

/**
 * @author Dennis Cosgrove
 */
public abstract class BooleanState extends State< Boolean > {
	public static final class InternalMenuItemPrepModelResolver extends IndirectResolver< InternalMenuItemPrepModel, BooleanState > {
		private InternalMenuItemPrepModelResolver( BooleanState indirect ) {
			super( indirect );
		}
		public InternalMenuItemPrepModelResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}
		@Override
		protected InternalMenuItemPrepModel getDirect( BooleanState indirect ) {
			return indirect.getMenuItemPrepModel();
		}
	}
	private static final class InternalMenuItemPrepModel extends StandardMenuItemPrepModel {
		private final BooleanState booleanState;
		private InternalMenuItemPrepModel( BooleanState booleanState ) {
			super( java.util.UUID.fromString( "1395490e-a04f-4447-93c5-892a1e1bd899" ) );
			assert booleanState != null;
			this.booleanState = booleanState;
		}
		@Override
		public Iterable< ? extends Model > getChildren() {
			return edu.cmu.cs.dennisc.java.util.Collections.newArrayList( this.booleanState );
		}
		@Override
		protected void localize() {
		}
		public BooleanState getBooleanState() {
			return this.booleanState;
		}
		@Override
		public boolean isEnabled() {
			return this.booleanState.isEnabled();
		}
		@Override
		public void setEnabled( boolean isEnabled ) {
			this.booleanState.setEnabled( isEnabled );
		}
		@Override
		protected InternalMenuItemPrepModelResolver createResolver() {
			return new InternalMenuItemPrepModelResolver( this.booleanState );
		}
		@Override
		public org.lgna.croquet.components.JComponent< ? > getFirstComponent() {
			return this.booleanState.getFirstComponent();
		}
		@Override
		public org.lgna.croquet.components.MenuItemContainer createMenuItemAndAddTo( org.lgna.croquet.components.MenuItemContainer rv ) {
			rv.addCheckBoxMenuItem( new org.lgna.croquet.components.CheckBoxMenuItem( this.getBooleanState() ) );
			return rv;
		}
		@Override
		protected StringBuilder updateTutorialStepText(StringBuilder rv, org.lgna.croquet.history.Step<?> step, org.lgna.croquet.edits.Edit<?> edit, UserInformation userInformation) {
			return this.booleanState.updateTutorialStepText( rv, step, edit, userInformation );
		}
	}
	private InternalMenuItemPrepModel menuPrepModel;
	public synchronized InternalMenuItemPrepModel getMenuItemPrepModel() {
		if( this.menuPrepModel != null ) {
			//pass
		} else {
			this.menuPrepModel = new InternalMenuItemPrepModel( this );
		}
		return this.menuPrepModel;
	}
	
	
	public class SwingModel {
		private final javax.swing.ButtonModel buttonModel = new javax.swing.JToggleButton.ToggleButtonModel();
		private final javax.swing.Action action = new javax.swing.AbstractAction() {
			@Override
			public Object getValue(String key) {
				if( NAME.equals( key ) ) {
					return BooleanState.this.getTextFor( buttonModel.isSelected() );
				} else if( SMALL_ICON.equals( key ) ) {
					return BooleanState.this.getIconFor( buttonModel.isSelected() );
				} else {
					return super.getValue( key );
				}
			}
			public void actionPerformed( java.awt.event.ActionEvent e ) {
				boolean isSelected = buttonModel.isSelected();
				if( isTextVariable() ) {
					this.firePropertyChange( NAME, getTextFor( !isSelected ), getTextFor( isSelected ) );
				}
				if( isIconVariable() ) {
					this.firePropertyChange( SMALL_ICON, getIconFor( !isSelected ), getIconFor( isSelected ) );
				}
			}
		};
		private SwingModel() {
		}
		public javax.swing.ButtonModel getButtonModel() {
			return this.buttonModel;
		}
		public javax.swing.Action getAction() {
			return this.action;
		}
	}
	private final SwingModel swingModel = new SwingModel();
	
	private String trueText;
	private String falseText;
	private javax.swing.Icon trueIcon;
	private javax.swing.Icon falseIcon;

	private final java.awt.event.ItemListener itemListener = new java.awt.event.ItemListener() {
		public void itemStateChanged( java.awt.event.ItemEvent e ) {
			BooleanState.this.handleItemStateChanged( e );
		}
	};

	public BooleanState( Group group, java.util.UUID id, boolean initialValue ) {
		super( group, id, initialValue );
		this.swingModel.buttonModel.setSelected( initialValue );
		this.swingModel.buttonModel.addItemListener( this.itemListener );
	}

	@Override
	public boolean isEnabled() {
		return this.swingModel.action.isEnabled();
	}
	@Override
	public void setEnabled( boolean isEnabled ) {
		this.swingModel.action.setEnabled( isEnabled );
	}

	public SwingModel getSwingModel() {
		return this.swingModel;
	}
	private void handleItemStateChanged( java.awt.event.ItemEvent e ) {
		if( this.isAppropriateToComplete() ) {
			boolean nextValue = e.getStateChange() == java.awt.event.ItemEvent.SELECTED;
			this.commitStateEdit( !nextValue, nextValue, false, new org.lgna.croquet.triggers.ItemEventTrigger( e ) );
		}
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
	
	@Override
	protected void commitStateEdit( Boolean prevValue, Boolean nextValue, boolean isAdjusting, org.lgna.croquet.triggers.Trigger trigger ) {
		org.lgna.croquet.history.TransactionManager.commitEdit( this, nextValue, trigger );
	}
	@Override
	public org.lgna.croquet.edits.Edit< ? > commitTutorialCompletionEdit( org.lgna.croquet.history.CompletionStep< ? > step, org.lgna.croquet.edits.Edit< ? > originalEdit, org.lgna.croquet.Retargeter retargeter ) {
		assert originalEdit instanceof org.lgna.croquet.edits.BooleanStateEdit;
		org.lgna.croquet.edits.BooleanStateEdit booleanStateEdit = (org.lgna.croquet.edits.BooleanStateEdit)originalEdit;
		return org.lgna.croquet.history.TransactionManager.commitEdit( this, booleanStateEdit.getNextValue(), new org.lgna.croquet.triggers.SimulatedTrigger() );
	}
	@Override
	protected StringBuilder updateTutorialStepText( StringBuilder rv, org.lgna.croquet.history.Step< ? > step, org.lgna.croquet.edits.Edit< ? > edit, UserInformation userInformation ) {
		if( edit instanceof org.lgna.croquet.edits.BooleanStateEdit ) {
			org.lgna.croquet.edits.BooleanStateEdit booleanStateEdit = (org.lgna.croquet.edits.BooleanStateEdit)edit;
			rv.append( " " );
			if( edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( this.trueText, this.falseText ) ) {
//				if( booleanStateEdit.getNextValue() ) {
//					rv.append( "Select " );
//				} else {
//					rv.append( "Unselect " );
//				}
				rv.append( "<strong>" );
				rv.append( this.trueText );
				rv.append( "</strong>" );
			} else {
//				rv.append( "Press " );
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
	protected Boolean getActualValue() {
		return this.swingModel.buttonModel.isSelected();
	}
	
	@Override
	protected void updateSwingModel( Boolean nextValue ) {
		this.swingModel.buttonModel.removeItemListener( this.itemListener );
		try {
			this.swingModel.buttonModel.setSelected( nextValue );
		} finally {
			this.swingModel.buttonModel.addItemListener( this.itemListener );
		}
	}
//	@Override
//	protected void handleValueChange( Boolean nextValue ) {
//		if( nextValue != this.value ) {
//			//this.buttonModel.removeItemListener(itemListener);
//
//			boolean isAdjusting = false;
//			
//			Boolean prevValue = this.value;
//			this.fireChanging( prevValue, nextValue, isAdjusting );
//			this.swingModel.buttonModel.setSelected( value );
//			this.value = nextValue;
//			this.fireChanged( prevValue, nextValue, isAdjusting );
//
//			//this.buttonModel.addItemListener(itemListener);
//			this.updateNameAndIcon();
//		}
//	}

	private boolean isTextVariable() {
		return edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areNotEquivalent( this.getTrueText(), this.getFalseText() );
	}
	private boolean isIconVariable() {
		return edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areNotEquivalent( this.getTrueIcon(), this.getFalseIcon() );
	}

	public final String getTextFor( boolean value ) {
		return value ? this.getTrueText() : this.getFalseText();
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
	public final javax.swing.Icon getIconFor( boolean value ) {
		return value ? this.getTrueIcon() : this.getFalseIcon();
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
		this.swingModel.action.putValue( javax.swing.Action.NAME, name );
		this.swingModel.action.putValue( javax.swing.Action.SMALL_ICON, icon );
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
