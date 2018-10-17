/*******************************************************************************
 * Copyright (c) 2018 Carnegie Mellon University. All rights reserved.
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

package org.alice.ide.croquet.components;

import edu.cmu.cs.dennisc.javax.swing.icons.EmptyIcon;
import org.alice.ide.Theme;
import org.alice.stageide.modelresource.ResourceNode;
import org.lgna.croquet.SingleSelectListState;
import org.lgna.croquet.data.MutableListData;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;
import org.lgna.croquet.icon.EmptyIconFactory;
import org.lgna.croquet.icon.IconFactory;
import org.lgna.croquet.views.FauxComboBoxPopupButton;
import org.lgna.croquet.views.Label;

import javax.swing.*;
import java.awt.*;

public class SClassPopupButton  extends FauxComboBoxPopupButton<ResourceNode> implements ValueListener<ResourceNode> {
	private final SingleSelectListState<ResourceNode, MutableListData<ResourceNode>> listState;

	public SClassPopupButton( SingleSelectListState<ResourceNode, MutableListData<ResourceNode>> state ) {
		super(state.getMenuModel().getPopupPrepModel());
		listState = state;
		getAwtComponent().setLayout( new BoxLayout( getAwtComponent(), BoxLayout.LINE_AXIS ) );
	}

	@Override
	protected AbstractButton createSwingButton() {
		return new JFauxComboBoxPopupButton() {
			@Override
			public void invalidate() {
				super.invalidate();
				refreshIfNecessary();
			}
		};
	}

	private ResourceNode nextValue;

	@Override
	public void valueChanged( ValueEvent<ResourceNode> e ) {
		nextValue = e.getNextValue();
		refreshLater();
	}

	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		refreshLater();
		listState.addAndInvokeNewSchoolValueListener( this );
	}

	@Override
	protected void handleUndisplayable() {
		listState.removeNewSchoolValueListener( this );
		super.handleUndisplayable();
	}

	private void refreshLater() {
		isRefreshNecessary = true;
		revalidateAndRepaint();
	}

	private void refreshIfNecessary() {
		if ( isRefreshNecessary && !isInTheMidstOfRefreshing ) {
			isInTheMidstOfRefreshing = true;
			try {
				synchronized (getTreeLock()) {
					internalRefresh();
				}
				isRefreshNecessary = false;
			} finally {
				isInTheMidstOfRefreshing = false;
			}
		}
	}

	protected void internalRefresh() {
		internalForgetAndRemoveAllComponents();
		if( nextValue != null ) {
			IconFactory iconFactory = nextValue.getIconFactory();
			if( ( iconFactory != null ) && ( iconFactory != EmptyIconFactory.getInstance() ) ) {
				Dimension size = iconFactory.getTrimmedSizeForHeight( Theme.DEFAULT_SMALL_ICON_SIZE.height );
				Icon icon = iconFactory.getIcon( size );
				if( icon != null ) {
					internalAddComponent( new Label( icon ) );
				}
			}
			String sClass = nextValue.getSClassName();
			if ( sClass != null ) {
				internalAddComponent( new Label( sClass ) );
			}
		} else {
			internalAddComponent( new Label( new EmptyIcon( 0, 30 )) );
			// TODO Localize this message
			internalAddComponent( new Label("Select parent class") );
		}
		revalidateAndRepaint();
	}

	private boolean isInTheMidstOfRefreshing = false;
	private boolean isRefreshNecessary = true;
}
