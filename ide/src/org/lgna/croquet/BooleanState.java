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
public abstract class BooleanState extends SimpleValueState<Boolean> {
	public static final class InternalMenuItemPrepModelResolver extends IndirectResolver<InternalMenuItemPrepModel, BooleanState> {
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

	public static final class InternalMenuItemPrepModel extends StandardMenuItemPrepModel {
		private final BooleanState booleanState;

		private InternalMenuItemPrepModel( BooleanState booleanState ) {
			super( java.util.UUID.fromString( "1395490e-a04f-4447-93c5-892a1e1bd899" ) );
			assert booleanState != null;
			this.booleanState = booleanState;
		}

		@Override
		public Iterable<? extends Model> getChildren() {
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
		public org.lgna.croquet.components.MenuItemContainer createMenuItemAndAddTo( org.lgna.croquet.components.MenuItemContainer rv ) {
			rv.addCheckBoxMenuItem( new org.lgna.croquet.components.CheckBoxMenuItem( this.getBooleanState() ) );
			return rv;
		}

		@Override
		protected StringBuilder updateTutorialStepText( StringBuilder rv, org.lgna.croquet.history.Step<?> step, org.lgna.croquet.edits.Edit<?> edit ) {
			return this.booleanState.updateTutorialStepText( rv, step, edit );
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
		private final javax.swing.ButtonModel buttonModel;
		private final javax.swing.Action action = new javax.swing.AbstractAction() {
			@Override
			public Object getValue( String key ) {
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

		private SwingModel( javax.swing.ButtonModel buttonModel ) {
			this.buttonModel = buttonModel;
		}

		public javax.swing.ButtonModel getButtonModel() {
			return this.buttonModel;
		}

		public javax.swing.Action getAction() {
			return this.action;
		}
	}

	private final SwingModel swingModel;

	private String trueText;
	private String falseText;
	private javax.swing.Icon trueIcon;
	private javax.swing.Icon falseIcon;

	private final java.awt.event.ItemListener itemListener = new java.awt.event.ItemListener() {
		public void itemStateChanged( java.awt.event.ItemEvent e ) {
			BooleanState.this.handleItemStateChanged( e );
		}
	};

	protected BooleanState( Group group, java.util.UUID id, boolean initialValue, javax.swing.ButtonModel buttonModel ) {
		super( group, id, initialValue );
		this.swingModel = new SwingModel( buttonModel );
		this.swingModel.buttonModel.setSelected( initialValue );
		this.swingModel.buttonModel.addItemListener( this.itemListener );
	}

	public BooleanState( Group group, java.util.UUID id, boolean initialValue ) {
		this( group, id, initialValue, new javax.swing.JToggleButton.ToggleButtonModel() );
	}

	@Override
	public Iterable<? extends PrepModel> getPotentialRootPrepModels() {
		if( this.menuPrepModel != null ) {
			return edu.cmu.cs.dennisc.java.util.Collections.newArrayList( this.menuPrepModel );
		} else {
			return java.util.Collections.emptyList();
		}
	}

	@Override
	public Class<Boolean> getItemClass() {
		return Boolean.class;
	}

	@Override
	public Boolean decodeValue( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		return binaryDecoder.decodeBoolean();
	}

	@Override
	public void encodeValue( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, Boolean value ) {
		binaryEncoder.encode( value );
	}

	@Override
	public void appendRepresentation( StringBuilder sb, Boolean value ) {
		sb.append( value );
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

	private boolean isItemStateChangedToBeIgnored = false;

	protected void handleItemStateChanged( java.awt.event.ItemEvent e ) {
		if( this.isItemStateChangedToBeIgnored ) {
			//pass
		} else {
			boolean nextValue = e.getStateChange() == java.awt.event.ItemEvent.SELECTED;
			this.changeValueFromSwing( nextValue, IsAdjusting.FALSE, org.lgna.croquet.triggers.ItemEventTrigger.createUserInstance( e ) );
		}
	}

	@Override
	protected void localize() {
		String text = this.findDefaultLocalizedText();
		if( text != null ) {
			this.setTextForBothTrueAndFalse( text );
		} else {
			String trueText = this.findLocalizedText( "true" );
			if( trueText != null ) {
				String falseText = this.findLocalizedText( "false" );
				if( falseText != null ) {
					this.setTextForTrueAndTextForFalse( trueText, falseText );
				} else {
					//todo:
				}
			}
		}
		this.setMnemonicKey( this.getLocalizedMnemonicKey() );
		this.setAcceleratorKey( this.getLocalizedAcceleratorKeyStroke() );
	}

	//	public int getMnemonicKey() {
	//	return Integer.class.cast( this.swingModel.action.getValue( javax.swing.Action.MNEMONIC_KEY ) );
	//}
	private void setMnemonicKey( int mnemonicKey ) {
		this.swingModel.action.putValue( javax.swing.Action.MNEMONIC_KEY, mnemonicKey );
	}

	//public javax.swing.KeyStroke getAcceleratorKey() {
	//	return javax.swing.KeyStroke.class.cast( this.swingModel.action.getValue( javax.swing.Action.ACCELERATOR_KEY ) );
	//}
	private void setAcceleratorKey( javax.swing.KeyStroke acceleratorKey ) {
		this.swingModel.action.putValue( javax.swing.Action.ACCELERATOR_KEY, acceleratorKey );
	}

	@Override
	protected Boolean getSwingValue() {
		return this.swingModel.buttonModel.isSelected();
	}

	@Override
	protected void setSwingValue( Boolean nextValue ) {
		if( this.swingModel.buttonModel.isSelected() != nextValue ) {
			//pass
		} else {
			this.isItemStateChangedToBeIgnored = true;
			try {
				this.swingModel.buttonModel.setSelected( nextValue );
			} finally {
				this.isItemStateChangedToBeIgnored = false;
			}
		}
	}

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
		if( this.trueOperation != null ) {
			this.trueOperation.setName( this.trueText );
			this.trueOperation.setSmallIcon( this.trueIcon );
		}
		if( this.falseOperation != null ) {
			this.falseOperation.setName( this.falseText );
			this.falseOperation.setSmallIcon( this.falseIcon );
		}
	}

	public org.lgna.croquet.components.RadioButton createRadioButton() {
		return new org.lgna.croquet.components.RadioButton( this );
	}

	public org.lgna.croquet.components.CheckBox createCheckBox() {
		return new org.lgna.croquet.components.CheckBox( this );
	}

	public org.lgna.croquet.components.ToggleButton createToggleButton() {
		return new org.lgna.croquet.components.ToggleButton( this );
	}

	@Deprecated
	public org.lgna.croquet.components.PushButton createPushButton() {
		return new org.lgna.croquet.components.PushButton( this );
	}

	private static class InternalSelectValueOperation extends ActionOperation {
		private final BooleanState state;
		private final boolean value;

		private InternalSelectValueOperation( BooleanState state, boolean value ) {
			super( state.getGroup(), java.util.UUID.fromString( "ca23dcf0-e00d-439b-b8a2-6c691be8ab5f" ) );
			assert state != null;
			this.state = state;
			this.value = value;
		}

		@Override
		protected void initialize() {
			this.state.initializeIfNecessary();
			super.initialize();
		}

		@Override
		protected void localize() {
			super.localize();
			this.setName( this.value ? this.state.getTrueText() : this.state.getFalseText() );
			this.setSmallIcon( this.value ? this.state.getTrueIcon() : this.state.getFalseIcon() );
		}

		@Override
		protected final void perform( org.lgna.croquet.history.Transaction transaction, org.lgna.croquet.triggers.Trigger trigger ) {
			org.lgna.croquet.history.CompletionStep<?> step = transaction.createAndSetCompletionStep( this, trigger );
			this.state.setValueTransactionlessly( this.value );
			step.finish();
		}
	}

	private InternalSelectValueOperation trueOperation;
	private InternalSelectValueOperation falseOperation;

	public synchronized Operation getSetToTrueOperation() {
		if( this.trueOperation != null ) {
			//pass
		} else {
			this.trueOperation = new InternalSelectValueOperation( this, true );
		}
		return this.trueOperation;
	}

	public synchronized Operation getSetToFalseOperation() {
		if( this.falseOperation != null ) {
			//pass
		} else {
			this.falseOperation = new InternalSelectValueOperation( this, false );
		}
		return this.falseOperation;
	}

	private final static class InternalRadioButton extends org.lgna.croquet.components.OperationButton<javax.swing.JRadioButton, Operation> {
		public InternalRadioButton( Operation operation ) {
			super( operation );
		}

		@Override
		protected javax.swing.JRadioButton createAwtComponent() {
			return new javax.swing.JRadioButton();
		}
	}

	private final class RadioButtonsPanel extends org.lgna.croquet.components.Panel {
		private final int axis;

		public RadioButtonsPanel( boolean isVertical, boolean isTrueFirst ) {
			this.axis = isVertical ? javax.swing.BoxLayout.PAGE_AXIS : javax.swing.BoxLayout.LINE_AXIS;
			InternalRadioButton trueButton = new InternalRadioButton( getSetToTrueOperation() );
			InternalRadioButton falseButton = new InternalRadioButton( getSetToFalseOperation() );
			if( isTrueFirst ) {
				this.internalAddComponent( trueButton );
				this.internalAddComponent( falseButton );
			} else {
				this.internalAddComponent( falseButton );
				this.internalAddComponent( trueButton );
			}
			javax.swing.ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
			buttonGroup.add( trueButton.getAwtComponent() );
			buttonGroup.add( falseButton.getAwtComponent() );
			InternalRadioButton selected = getValue() ? trueButton : falseButton;
			buttonGroup.setSelected( selected.getAwtComponent().getModel(), true );
		}

		@Override
		protected java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
			return new javax.swing.BoxLayout( jPanel, axis );
		}
	}

	public org.lgna.croquet.components.Panel createVerticalRadioButtons( boolean isTrueFirst ) {
		return new RadioButtonsPanel( true, isTrueFirst );
	}

	public org.lgna.croquet.components.Panel createHorizontalRadioButtons( boolean isTrueFirst ) {
		return new RadioButtonsPanel( false, isTrueFirst );
	}

	public org.lgna.croquet.components.Panel createVerticalRadioButtons() {
		return this.createVerticalRadioButtons( true );
	}

	public org.lgna.croquet.components.Panel createHorizontalRadioButtons() {
		return this.createHorizontalRadioButtons( true );
	}

}
