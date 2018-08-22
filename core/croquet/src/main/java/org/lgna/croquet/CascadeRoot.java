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

import edu.cmu.cs.dennisc.java.util.Lists;
import org.lgna.croquet.history.CompletionStep;
import org.lgna.croquet.history.PopupPrepStep;
import org.lgna.croquet.history.Step;
import org.lgna.croquet.history.Transaction;
import org.lgna.croquet.history.TransactionHistory;
import org.lgna.croquet.history.TransactionManager;
import org.lgna.croquet.imp.cascade.ItemNode;
import org.lgna.croquet.imp.cascade.RtRoot;
import org.lgna.croquet.triggers.CascadeAutomaticDeterminationTrigger;
import org.lgna.croquet.triggers.Trigger;
import org.lgna.croquet.views.FauxComboBoxPopupButton;
import org.lgna.croquet.views.PopupMenu;

import javax.swing.Icon;
import javax.swing.JComponent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class CascadeRoot<T, CM extends CompletionModel> extends CascadeBlankOwner<T[], T> {
	public static final class InternalPopupPrepModel<T> extends PopupPrepModel {
		private final CascadeRoot<T, ?> root;

		private InternalPopupPrepModel( CascadeRoot<T, ?> root ) {
			super( UUID.fromString( "56116a5f-a081-4ce8-9626-9c515c6c5887" ) );
			this.root = root;
		}

		public CascadeRoot<T, ?> getCascadeRoot() {
			return this.root;
		}

		@Override
		public Iterable<? extends Model> getChildren() {
			return Lists.newLinkedList( this.root );
		}

		@Override
		protected Class<? extends Element> getClassUsedForLocalization() {
			return this.root.getClassUsedForLocalization();
		}

		@Override
		protected String getSubKeyForLocalization() {
			return this.root.getSubKeyForLocalization();
		}

		@Override
		protected void prologue( Trigger trigger ) {
			super.prologue( trigger );
			this.root.prologue( trigger );
		}

		@Override
		protected void epilogue() {
			this.root.epilogue();
			super.epilogue();
		}

		protected void handleFinally() {
			this.epilogue();
		}

		@Override
		protected Step<?> perform( Trigger trigger ) {
			this.prologue( trigger );
			final RtRoot<T, ?> rtRoot = new RtRoot( this.root );
			Step<?> rv;
			if( rtRoot.isAutomaticallyDetermined() ) {
				rv = rtRoot.complete( new CascadeAutomaticDeterminationTrigger( trigger ) );
				this.handleFinally();
			} else {
				final PopupPrepStep prepStep = TransactionManager.addPopupPrepStep( this, trigger );
				final PopupMenu popupMenu = new PopupMenu( this );
				popupMenu.addComponentListener( new ComponentListener() {
					@Override
					public void componentShown( ComponentEvent e ) {
					}

					@Override
					public void componentMoved( ComponentEvent e ) {
					}

					@Override
					public void componentResized( ComponentEvent e ) {
						prepStep.firePopupMenuResized();
					}

					@Override
					public void componentHidden( ComponentEvent e ) {
					}
				} );
				popupMenu.addPopupMenuListener( rtRoot.createPopupMenuListener( popupMenu ) );
				trigger.showPopupMenu( popupMenu );
				rv = prepStep;
			}
			return rv;
		}

		@Override
		protected void appendRepr( StringBuilder sb ) {
			super.appendRepr( sb );
			sb.append( this.root );
		}

		public FauxComboBoxPopupButton<T> createFauxComboBoxPopupButton() {
			return new FauxComboBoxPopupButton<T>( this );
		}
	}

	private final InternalPopupPrepModel<T> popupPrepModel = new InternalPopupPrepModel<T>( this );
	private final List<CascadeRejector> cascadeRejectors = Lists.newCopyOnWriteArrayList();
	private String text;

	public CascadeRoot( UUID id ) {
		super( id );
	}

	@Override
	protected void localize() {
		super.localize();
		this.text = this.findDefaultLocalizedText();
	}

	public final int getCascadeRejectorCount() {
		return this.cascadeRejectors.size();
	}

	public void addCascadeRejector( CascadeRejector cascadeRejector ) {
		this.cascadeRejectors.add( cascadeRejector );
	}

	public void removeCascadeRejector( CascadeRejector cascadeRejector ) {
		this.cascadeRejectors.remove( cascadeRejector );
	}

	public void clearCascadeRejectors() {
		this.cascadeRejectors.clear();
	}

	public List<CascadeRejector> getCascadeRejectors() {
		return Collections.unmodifiableList( this.cascadeRejectors );
	}

	public InternalPopupPrepModel<T> getPopupPrepModel() {
		return this.popupPrepModel;
	}

	@Override
	protected Class<? extends Element> getClassUsedForLocalization() {
		return this.getCompletionModel().getClassUsedForLocalization();
	}

	@Override
	protected String getSubKeyForLocalization() {
		return this.getCompletionModel().getSubKeyForLocalization();
	}

	@Override
	protected final JComponent createMenuItemIconProxy( ItemNode<? super T[], T> step ) {
		return null;
	}

	@Override
	public final T[] createValue( ItemNode<? super T[], T> node ) {
		//todo
		//this.cascade.getComponentType();
		//handled elsewhere for now
		throw new AssertionError();
	}

	@Override
	public final T[] getTransientValue( ItemNode<? super T[], T> step ) {
		//todo
		//this.cascade.getComponentType();
		//handled elsewhere for now
		throw new AssertionError();
	}

	@Override
	public final String getMenuItemText( ItemNode<? super T[], T> step ) {
		return this.text;
	}

	@Override
	public final Icon getMenuItemIcon( ItemNode<? super T[], T> step ) {
		return null;
	}

	protected void prologue( Trigger trigger ) {
	}

	protected void epilogue() {
	}

	public abstract AbstractCompletionModel getCompletionModel();

	public abstract Class<T> getComponentType();

	public abstract CompletionStep<CM> createCompletionStep( Transaction transaction, Trigger trigger );

	public abstract CompletionStep<CM> handleCompletion( TransactionHistory transactionHistory, Trigger trigger, RtRoot<T, CM> rtRoot );

	public final void handleCancel( CompletionStep<CM> completionStep, Trigger trigger, CancelException ce ) {
		try {
			if( completionStep != null ) {
				completionStep.cancel();
			} else {
				TransactionManager.addCancelCompletionStep( this.getCompletionModel(), trigger );
			}
		} finally {
			this.getPopupPrepModel().handleFinally();
		}
	}

	@Override
	protected void appendRepr( StringBuilder sb ) {
		super.appendRepr( sb );
		sb.append( this.getCompletionModel() );
	}
}
