/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.lgna.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class BooleanState extends SimpleValueState<Boolean> {
	protected BooleanState( Group group, java.util.UUID id, boolean initialValue, javax.swing.ButtonModel buttonModel ) {
		super( group, id, initialValue );
		this.imp = new org.lgna.croquet.imp.booleanstate.BooleanStateImp( this, initialValue, buttonModel );
		this.imp.getSwingModel().getButtonModel().addItemListener( this.itemListener );
	}

	public BooleanState( Group group, java.util.UUID id, boolean initialValue ) {
		this( group, id, initialValue, new javax.swing.JToggleButton.ToggleButtonModel() );
	}

	public org.lgna.croquet.imp.booleanstate.BooleanStateImp getImp() {
		return this.imp;
	}

	@Override
	public java.util.List<java.util.List<PrepModel>> getPotentialPrepModelPaths( org.lgna.croquet.edits.Edit edit ) {
		return this.imp.getPotentialPrepModelPaths( edit );
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
		return this.imp.isEnabled();
	}

	@Override
	public void setEnabled( boolean isEnabled ) {
		this.imp.setEnabled( isEnabled );
	}

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
		this.setAcceleratorKey( this.getLocalizedAcceleratorKeyStroke() );
	}

	@Override
	protected Boolean getSwingValue() {
		javax.swing.ButtonModel buttonModel = this.imp.getSwingModel().getButtonModel();
		return buttonModel.isSelected();
	}

	@Override
	protected void setSwingValue( Boolean nextValue ) {
		javax.swing.ButtonModel buttonModel = this.imp.getSwingModel().getButtonModel();
		if( buttonModel.isSelected() == nextValue ) {
			//pass
		} else {
			this.isItemStateChangedToBeIgnored = true;
			try {
				buttonModel.setSelected( nextValue );
			} finally {
				this.isItemStateChangedToBeIgnored = false;
			}
		}
	}

	public final String getTextFor( boolean value ) {
		return value ? this.getTrueText() : this.getFalseText();
	}

	public String getTrueText() {
		return this.modifyTextIfNecessary( this.trueText, true );
	}

	public String getFalseText() {
		return this.modifyTextIfNecessary( this.falseText, false );
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

	//public javax.swing.KeyStroke getAcceleratorKey() {
	//	return javax.swing.KeyStroke.class.cast( this.swingModel.action.getValue( javax.swing.Action.ACCELERATOR_KEY ) );
	//}
	private void setAcceleratorKey( javax.swing.KeyStroke acceleratorKey ) {
		this.imp.getSwingModel().getAction().putValue( javax.swing.Action.ACCELERATOR_KEY, acceleratorKey );
	}

	private void setShortDescription( String shortDescription ) {
		this.imp.getSwingModel().getAction().putValue( javax.swing.Action.SHORT_DESCRIPTION, shortDescription );
	}

	public void setToolTipText( String toolTipText ) {
		this.setShortDescription( toolTipText );
	}

	protected String modifyTextIfNecessary( String text, boolean isTrue ) {
		return text;
	}

	public void updateNameAndIcon() {
		String possiblyModifiedTrueText = this.getTrueText();
		String possiblyModifiedFalseText = this.getFalseText();
		this.imp.updateNameAndIcon( this.getValue(), possiblyModifiedTrueText, this.trueIcon, possiblyModifiedFalseText, this.falseIcon );
	}

	public org.lgna.croquet.views.RadioButton createRadioButton() {
		return new org.lgna.croquet.views.RadioButton( this );
	}

	public org.lgna.croquet.views.CheckBox createCheckBox() {
		return new org.lgna.croquet.views.CheckBox( this );
	}

	public org.lgna.croquet.views.ToggleButton createToggleButton() {
		return new org.lgna.croquet.views.ToggleButton( this );
	}

	public org.lgna.croquet.views.ToggleButtonLabelCombo createToggleButtonLabelCombo() {
		return new org.lgna.croquet.views.ToggleButtonLabelCombo( this );
	}

	@Deprecated
	public org.lgna.croquet.views.PushButton createPushButton() {
		return new org.lgna.croquet.views.PushButton( this );
	}

	public Operation getSetToTrueOperation() {
		return this.imp.getSetToTrueOperation();
	}

	public Operation getSetToFalseOperation() {
		return this.imp.getSetToFalseOperation();
	}

	public MenuModel getMenuModel() {
		return this.imp.getMenuModel();
	}

	public StandardMenuItemPrepModel getMenuItemPrepModel() {
		return this.imp.getMenuItemPrepModel();
	}

	private static final class InternalRadioButton extends org.lgna.croquet.views.OperationButton<javax.swing.JRadioButton, Operation> {
		public InternalRadioButton( Operation operation ) {
			super( operation );
		}

		@Override
		protected javax.swing.JRadioButton createAwtComponent() {
			return new javax.swing.JRadioButton();
		}
	}

	private static final class InternalToggleButton extends org.lgna.croquet.views.OperationButton<javax.swing.JToggleButton, Operation> {
		public InternalToggleButton( Operation operation ) {
			super( operation );
		}

		@Override
		protected javax.swing.JToggleButton createAwtComponent() {
			return new javax.swing.JToggleButton();
		}
	}

	private abstract class AbstractToggleButtonsPanel extends org.lgna.croquet.views.Panel {
		private final javax.swing.ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
		private final org.lgna.croquet.views.OperationButton<? extends javax.swing.JToggleButton, Operation> trueButton;
		private final org.lgna.croquet.views.OperationButton<? extends javax.swing.JToggleButton, Operation> falseButton;
		private final int axis;
		private final org.lgna.croquet.event.ValueListener<Boolean> valueListener = new org.lgna.croquet.event.ValueListener<Boolean>() {
			@Override
			public void valueChanged( org.lgna.croquet.event.ValueEvent<java.lang.Boolean> e ) {
				handleChanged( e.getNextValue() );
			}
		};

		public AbstractToggleButtonsPanel( boolean isVertical, boolean isTrueFirst, org.lgna.croquet.views.OperationButton<? extends javax.swing.JToggleButton, Operation> trueButton, org.lgna.croquet.views.OperationButton<? extends javax.swing.JToggleButton, Operation> falseButton ) {
			this.axis = isVertical ? javax.swing.BoxLayout.PAGE_AXIS : javax.swing.BoxLayout.LINE_AXIS;
			this.trueButton = trueButton;
			this.falseButton = falseButton;
			if( isTrueFirst ) {
				this.internalAddComponent( this.trueButton );
				this.internalAddComponent( this.falseButton );
			} else {
				this.internalAddComponent( this.falseButton );
				this.internalAddComponent( this.trueButton );
			}
			this.buttonGroup.add( trueButton.getAwtComponent() );
			this.buttonGroup.add( falseButton.getAwtComponent() );
		}

		@Override
		protected java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
			return new javax.swing.BoxLayout( jPanel, axis );
		}

		private void handleChanged( Boolean nextValue ) {
			org.lgna.croquet.views.OperationButton<? extends javax.swing.JToggleButton, Operation> selected = nextValue ? this.trueButton : this.falseButton;
			this.buttonGroup.setSelected( selected.getAwtComponent().getModel(), true );
		}

		@Override
		protected void handleDisplayable() {
			super.handleDisplayable();
			BooleanState.this.addAndInvokeNewSchoolValueListener( this.valueListener );
		}

		@Override
		protected void handleUndisplayable() {
			BooleanState.this.removeNewSchoolValueListener( this.valueListener );
			super.handleUndisplayable();
		}
	}

	private final class RadioButtonsPanel extends AbstractToggleButtonsPanel {
		public RadioButtonsPanel( boolean isVertical, boolean isTrueFirst ) {
			super( isVertical, isTrueFirst, new InternalRadioButton( getSetToTrueOperation() ), new InternalRadioButton( getSetToFalseOperation() ) );
		}
	}

	private final class ToggleButtonsPanel extends AbstractToggleButtonsPanel {
		public ToggleButtonsPanel( boolean isVertical, boolean isTrueFirst ) {
			super( isVertical, isTrueFirst, new InternalToggleButton( getSetToTrueOperation() ), new InternalToggleButton( getSetToFalseOperation() ) );
		}
	}

	public org.lgna.croquet.views.Panel createVerticalRadioButtons( boolean isTrueFirst ) {
		return new RadioButtonsPanel( true, isTrueFirst );
	}

	public org.lgna.croquet.views.Panel createHorizontalRadioButtons( boolean isTrueFirst ) {
		return new RadioButtonsPanel( false, isTrueFirst );
	}

	public org.lgna.croquet.views.Panel createVerticalRadioButtons() {
		return this.createVerticalRadioButtons( true );
	}

	public org.lgna.croquet.views.Panel createHorizontalRadioButtons() {
		return this.createHorizontalRadioButtons( true );
	}

	public org.lgna.croquet.views.Panel createVerticalToggleButtons( boolean isTrueFirst ) {
		return new ToggleButtonsPanel( true, isTrueFirst );
	}

	public org.lgna.croquet.views.Panel createHorizontalToggleButtons( boolean isTrueFirst ) {
		return new ToggleButtonsPanel( false, isTrueFirst );
	}

	public org.lgna.croquet.views.Panel createVerticalToggleButtons() {
		return this.createVerticalToggleButtons( true );
	}

	public org.lgna.croquet.views.Panel createHorizontalToggleButtons() {
		return this.createHorizontalToggleButtons( true );
	}

	private final org.lgna.croquet.imp.booleanstate.BooleanStateImp imp;
	private String trueText;
	private String falseText;
	private javax.swing.Icon trueIcon;
	private javax.swing.Icon falseIcon;

	private boolean isItemStateChangedToBeIgnored = false;

	private final java.awt.event.ItemListener itemListener = new java.awt.event.ItemListener() {
		@Override
		public void itemStateChanged( java.awt.event.ItemEvent e ) {
			BooleanState.this.handleItemStateChanged( e );
		}
	};
}
