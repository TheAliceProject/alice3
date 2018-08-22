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

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.imp.booleanstate.BooleanStateImp;
import org.lgna.croquet.triggers.ItemEventTrigger;
import org.lgna.croquet.views.CheckBox;
import org.lgna.croquet.views.OperationButton;
import org.lgna.croquet.views.Panel;
import org.lgna.croquet.views.PushButton;
import org.lgna.croquet.views.RadioButton;
import org.lgna.croquet.views.ToggleButton;

import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import java.awt.LayoutManager;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class BooleanState extends State<Boolean> {
	protected BooleanState( Group group, UUID id, boolean initialValue, ButtonModel buttonModel ) {
		super( group, id, initialValue );
		this.imp = new BooleanStateImp( this, initialValue, buttonModel );
		this.imp.getSwingModel().getButtonModel().addItemListener( this.itemListener );
	}

	public BooleanState( Group group, UUID id, boolean initialValue ) {
		this( group, id, initialValue, new JToggleButton.ToggleButtonModel() );
	}

	public BooleanStateImp getImp() {
		return this.imp;
	}

	@Override
	public List<List<PrepModel>> getPotentialPrepModelPaths( Edit edit ) {
		return this.imp.getPotentialPrepModelPaths( edit );
	}

	@Override
	public Boolean decodeValue( BinaryDecoder binaryDecoder ) {
		return binaryDecoder.decodeBoolean();
	}

	@Override
	public void encodeValue( BinaryEncoder binaryEncoder, Boolean value ) {
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

	protected void handleItemStateChanged( ItemEvent e ) {
		if( this.isItemStateChangedToBeIgnored ) {
			//pass
		} else {
			boolean nextValue = e.getStateChange() == ItemEvent.SELECTED;
			this.changeValueFromSwing( nextValue, IsAdjusting.FALSE, ItemEventTrigger.createUserInstance( e ) );
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
		ButtonModel buttonModel = this.imp.getSwingModel().getButtonModel();
		return buttonModel.isSelected();
	}

	@Override
	protected void setSwingValue( Boolean nextValue ) {
		ButtonModel buttonModel = this.imp.getSwingModel().getButtonModel();
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

	public final Icon getIconFor( boolean value ) {
		return value ? this.getTrueIcon() : this.getFalseIcon();
	}

	public Icon getTrueIcon() {
		return this.trueIcon;
	}

	public Icon getFalseIcon() {
		return this.falseIcon;
	}

	public void setIconForBothTrueAndFalse( Icon icon ) {
		this.setIconForTrueAndIconForFalse( icon, icon );
	}

	public void setIconForTrueAndIconForFalse( Icon trueIcon, Icon falseIcon ) {
		this.trueIcon = trueIcon;
		this.falseIcon = falseIcon;
		this.updateNameAndIcon();
	}

	//public javax.swing.KeyStroke getAcceleratorKey() {
	//	return javax.swing.KeyStroke.class.cast( this.swingModel.action.getValue( javax.swing.Action.ACCELERATOR_KEY ) );
	//}
	private void setAcceleratorKey( KeyStroke acceleratorKey ) {
		this.imp.getSwingModel().getAction().putValue( Action.ACCELERATOR_KEY, acceleratorKey );
	}

	private void setShortDescription( String shortDescription ) {
		this.imp.getSwingModel().getAction().putValue( Action.SHORT_DESCRIPTION, shortDescription );
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

	public RadioButton createRadioButton() {
		return new RadioButton( this );
	}

	public CheckBox createCheckBox() {
		return new CheckBox( this );
	}

	public ToggleButton createToggleButton() {
		return new ToggleButton( this );
	}

	@Deprecated
	public PushButton createPushButton() {
		return new PushButton( this );
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

	private static final class InternalRadioButton extends OperationButton<JRadioButton, Operation> {
		public InternalRadioButton( Operation operation ) {
			super( operation );
		}

		@Override
		protected JRadioButton createAwtComponent() {
			return new JRadioButton();
		}
	}

	private static final class InternalToggleButton extends OperationButton<JToggleButton, Operation> {
		public InternalToggleButton( Operation operation ) {
			super( operation );
		}

		@Override
		protected JToggleButton createAwtComponent() {
			return new JToggleButton();
		}
	}

	private abstract class AbstractToggleButtonsPanel extends Panel {
		private final ButtonGroup buttonGroup = new ButtonGroup();
		private final OperationButton<? extends JToggleButton, Operation> trueButton;
		private final OperationButton<? extends JToggleButton, Operation> falseButton;
		private final int axis;
		private final org.lgna.croquet.event.ValueListener<Boolean> valueListener =
						e -> handleChanged( e.getNextValue() );

		public AbstractToggleButtonsPanel( boolean isVertical, boolean isTrueFirst, OperationButton<? extends JToggleButton, Operation> trueButton, OperationButton<? extends JToggleButton, Operation> falseButton ) {
			this.axis = isVertical ? BoxLayout.PAGE_AXIS : BoxLayout.LINE_AXIS;
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
		protected LayoutManager createLayoutManager( JPanel jPanel ) {
			return new BoxLayout( jPanel, axis );
		}

		private void handleChanged( Boolean nextValue ) {
			OperationButton<? extends JToggleButton, Operation> selected = nextValue ? this.trueButton : this.falseButton;
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

	public Panel createVerticalRadioButtons( boolean isTrueFirst ) {
		return new RadioButtonsPanel( true, isTrueFirst );
	}

	public Panel createHorizontalToggleButtons( boolean isTrueFirst ) {
		return new ToggleButtonsPanel( false, isTrueFirst );
	}

	public Panel createHorizontalToggleButtons() {
		return this.createHorizontalToggleButtons( true );
	}

	private final BooleanStateImp imp;
	private String trueText;
	private String falseText;
	private Icon trueIcon;
	private Icon falseIcon;

	private boolean isItemStateChangedToBeIgnored = false;

	private final ItemListener itemListener = new ItemListener() {
		@Override
		public void itemStateChanged( ItemEvent e ) {
			BooleanState.this.handleItemStateChanged( e );
		}
	};
}
