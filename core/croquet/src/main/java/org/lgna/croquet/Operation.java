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
public abstract class Operation extends AbstractCompletionModel {
	public class SwingModel {
		private javax.swing.Action action = new javax.swing.AbstractAction() {
			@Override
			public void actionPerformed( java.awt.event.ActionEvent e ) {
				Operation.this.handleActionPerformed( e );
			}
		};

		public javax.swing.Action getAction() {
			return this.action;
		}
	}

	private final SwingModel swingModel = new SwingModel();

	private javax.swing.Icon buttonIcon;

	public Operation( Group group, java.util.UUID id ) {
		super( group, id );
	}

	private void handleActionPerformed( java.awt.event.ActionEvent e ) {
		this.fire( org.lgna.croquet.triggers.ActionEventTrigger.createUserInstance( e ) );
	}

	@Override
	public java.util.List<java.util.List<PrepModel>> getPotentialPrepModelPaths( org.lgna.croquet.edits.Edit edit ) {
		if( this.menuPrepModel != null ) {
			return edu.cmu.cs.dennisc.java.util.Lists.newArrayListOfSingleArrayList( this.menuPrepModel );
		} else {
			return java.util.Collections.emptyList();
		}
	}

	public SwingModel getSwingModel() {
		return this.swingModel;
	}

	protected String modifyNameIfNecessary( String text ) {
		return text;
	}

	@Override
	protected void localize() {
		String name = this.findDefaultLocalizedText();
		if( name != null ) {
			name = modifyNameIfNecessary( name );
			int mnemonicKey = this.getLocalizedMnemonicKey();
			safeSetNameAndMnemonic( this.swingModel.action, name, mnemonicKey );
			this.setAcceleratorKey( this.getLocalizedAcceleratorKeyStroke() );
		}
	}

	public boolean isToolBarTextClobbered() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return this.swingModel.action.isEnabled();
	}

	@Override
	public void setEnabled( boolean isEnabled ) {
		this.swingModel.action.setEnabled( isEnabled );
	}

	public final String getName() {
		return String.class.cast( this.swingModel.action.getValue( javax.swing.Action.NAME ) );
	}

	public final void setName( String name ) {
		this.swingModel.action.putValue( javax.swing.Action.NAME, name );
	}

	//	public String getShortDescription() {
	//		return String.class.cast( this.swingModel.action.getValue( javax.swing.Action.SHORT_DESCRIPTION ) );
	//	}
	public void setShortDescription( String shortDescription ) {
		this.swingModel.action.putValue( javax.swing.Action.SHORT_DESCRIPTION, shortDescription );
	}

	public void setToolTipText( String toolTipText ) {
		this.setShortDescription( toolTipText );
	}

	//	public String getLongDescription() {
	//		return String.class.cast( this.swingModel.action.getValue( javax.swing.Action.LONG_DESCRIPTION ) );
	//	}
	public void setLongDescription( String longDescription ) {
		this.swingModel.action.putValue( javax.swing.Action.LONG_DESCRIPTION, longDescription );
	}

	public javax.swing.Icon getSmallIcon() {
		return javax.swing.Icon.class.cast( this.swingModel.action.getValue( javax.swing.Action.SMALL_ICON ) );
	}

	public void setSmallIcon( javax.swing.Icon icon ) {
		this.swingModel.action.putValue( javax.swing.Action.SMALL_ICON, icon );
	}

	public javax.swing.Icon getButtonIcon() {
		return this.buttonIcon;
	}

	public void setButtonIcon( javax.swing.Icon icon ) {
		this.buttonIcon = icon;
	}

	private void setMnemonicKey( int mnemonicKey ) {
		this.swingModel.action.putValue( javax.swing.Action.MNEMONIC_KEY, mnemonicKey );
	}

	//	public javax.swing.KeyStroke getAcceleratorKey() {
	//		return javax.swing.KeyStroke.class.cast( this.swingModel.action.getValue( javax.swing.Action.ACCELERATOR_KEY ) );
	//	}
	protected void setAcceleratorKey( javax.swing.KeyStroke acceleratorKey ) {
		this.swingModel.action.putValue( javax.swing.Action.ACCELERATOR_KEY, acceleratorKey );
	}

	private final static class InternalMenuItemPrepModel extends StandardMenuItemPrepModel {
		private final Operation operation;

		private InternalMenuItemPrepModel( Operation operation ) {
			super( java.util.UUID.fromString( "652a76ce-4c05-4c31-901c-ff14548e50aa" ) );
			assert operation != null;
			this.operation = operation;
		}

		@Override
		public Iterable<? extends Model> getChildren() {
			return edu.cmu.cs.dennisc.java.util.Lists.newArrayList( this.operation );
		}

		@Override
		protected void localize() {
		}

		public Operation getOperation() {
			return this.operation;
		}

		@Override
		public boolean isEnabled() {
			return this.operation.isEnabled();
		}

		@Override
		public void setEnabled( boolean isEnabled ) {
			this.operation.setEnabled( isEnabled );
		}

		@Override
		public org.lgna.croquet.views.MenuItem createMenuItemAndAddTo( org.lgna.croquet.views.MenuItemContainer menuItemContainer ) {
			org.lgna.croquet.views.MenuItem menuItem = new org.lgna.croquet.views.MenuItem( this.getOperation() );
			menuItemContainer.addMenuItem( menuItem );
			return menuItem;
		}

		@Override
		protected void appendRepr( StringBuilder sb ) {
			super.appendRepr( sb );
			sb.append( "operation=" );
			sb.append( this.getOperation() );
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

	private class FauxCascadeItem<F, B> extends CascadeFillIn<F, B> {
		public FauxCascadeItem() {
			super( java.util.UUID.fromString( "68f2167d-0763-4a43-8ce3-592e1562877c" ) );
		}

		@Override
		public java.util.List<? extends org.lgna.croquet.CascadeBlank<B>> getBlanks() {
			return java.util.Collections.emptyList();
		}

		@Override
		public F getTransientValue( org.lgna.croquet.imp.cascade.ItemNode<? super F, B> node ) {
			return null;
		}

		@Override
		public F createValue( org.lgna.croquet.imp.cascade.ItemNode<? super F, B> node, org.lgna.croquet.history.TransactionHistory transactionHistory ) {
			Operation.this.fire();
			throw new CancelException();
		}

		@Override
		protected javax.swing.JComponent createMenuItemIconProxy( org.lgna.croquet.imp.cascade.ItemNode<? super F, B> node ) {
			return new javax.swing.JMenuItem( Operation.this.getSwingModel().action );
		}
	}

	private final edu.cmu.cs.dennisc.pattern.Lazy<CascadeItem> fauxCascadeItem = new edu.cmu.cs.dennisc.pattern.Lazy<CascadeItem>() {
		@Override
		protected org.lgna.croquet.CascadeItem<?, ?> create() {
			return new FauxCascadeItem();
		}
	};

	public <F, B> CascadeItem<F, B> getFauxCascadeItem() {
		return this.fauxCascadeItem.get();
	}

	public org.lgna.croquet.views.Button createButton( float fontScalar, edu.cmu.cs.dennisc.java.awt.font.TextAttribute<?>... textAttributes ) {
		return new org.lgna.croquet.views.Button( this, fontScalar, textAttributes );
	}

	public org.lgna.croquet.views.Button createButton( edu.cmu.cs.dennisc.java.awt.font.TextAttribute<?>... textAttributes ) {
		return this.createButton( 1.0f, textAttributes );
	}

	public org.lgna.croquet.views.Hyperlink createHyperlink( float fontScalar, edu.cmu.cs.dennisc.java.awt.font.TextAttribute<?>... textAttributes ) {
		return new org.lgna.croquet.views.Hyperlink( this, fontScalar, textAttributes );
	}

	public org.lgna.croquet.views.Hyperlink createHyperlink( edu.cmu.cs.dennisc.java.awt.font.TextAttribute<?>... textAttributes ) {
		return this.createHyperlink( 1.0f, textAttributes );
	}

	public org.lgna.croquet.views.ButtonWithRightClickCascade createButtonWithRightClickCascade( Cascade<?> cascade ) {
		return new org.lgna.croquet.views.ButtonWithRightClickCascade( this, cascade );
	}
}
