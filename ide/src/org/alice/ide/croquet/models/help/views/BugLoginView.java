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
package org.alice.ide.croquet.models.help.views;

import java.util.List;

import org.alice.ide.croquet.models.help.BugLoginComposite;
import org.lgna.croquet.State;
import org.lgna.croquet.State.ValueListener;
import org.lgna.croquet.components.LabeledSpringRow;
import org.lgna.croquet.components.RowSpringPanel;
import org.lgna.croquet.components.SpringRow;

/**
 * @author Matt May
 */
public class BugLoginView extends RowSpringPanel {
	private final ValueListener<Boolean> isPasswordExposedListener = new ValueListener<Boolean>() {
		public void changing( State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
		}

		public void changed( State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			passwordField.setExposed( nextValue );
		}
	};
	private final org.lgna.croquet.components.PasswordField passwordField;

	public BugLoginView( BugLoginComposite bugLoginComposite ) {
		super( bugLoginComposite );
		this.passwordField = bugLoginComposite.getPasswordState().createPasswordField();
		this.setMinimumPreferredHeight( 240 );
	}

	@Override
	protected void appendRows( List<SpringRow> rows ) {
		BugLoginComposite bugLoginComposite = (BugLoginComposite)this.getComposite();
		rows.add( new LabeledSpringRow( bugLoginComposite.getUserNameState().getSidekickLabel(), bugLoginComposite.getUserNameState().createTextField() ) );
		rows.add( new LabeledSpringRow( bugLoginComposite.getPasswordState().getSidekickLabel(), passwordField ) );
		rows.add( new LabeledSpringRow( null, bugLoginComposite.getDisplayPasswordValue().createCheckBox() ) );
	}

	@Override
	protected void handleDisplayable() {
		BugLoginComposite bugLoginComposite = (BugLoginComposite)this.getComposite();
		bugLoginComposite.getDisplayPasswordValue().addAndInvokeValueListener( this.isPasswordExposedListener );
		super.handleDisplayable();
	}

	@Override
	protected void handleUndisplayable() {
		super.handleUndisplayable();
		BugLoginComposite bugLoginComposite = (BugLoginComposite)this.getComposite();
		bugLoginComposite.getDisplayPasswordValue().removeValueListener( this.isPasswordExposedListener );
	}
}
