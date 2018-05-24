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
package org.alice.ide.ast.export;

import org.lgna.project.ast.Declaration;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonModel;
import javax.swing.JCheckBox;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.Set;

/**
 * @author Dennis Cosgrove
 */
public abstract class DeclarationInfo<D extends Declaration> {
	private final ProjectInfo projectInfo;
	private final D declaration;
	private final Action action = new AbstractAction() {
		@Override
		public void actionPerformed( ActionEvent e ) {
		}
	};
	private final JCheckBox checkBox = new JCheckBox( this.action );
	private final ItemListener itemListener = new ItemListener() {
		@Override
		public void itemStateChanged( ItemEvent e ) {
			handleItemStateChanged( e );
		}
	};

	private boolean isDesired;
	private boolean isRequired;

	public DeclarationInfo( ProjectInfo projectInfo, D declaration ) {
		this.projectInfo = projectInfo;
		this.declaration = declaration;
		this.action.putValue( Action.NAME, this.declaration.getName() );
		this.checkBox.getModel().addItemListener( this.itemListener );
	}

	public ProjectInfo getProjectInfo() {
		return this.projectInfo;
	}

	public D getDeclaration() {
		return this.declaration;
	}

	public JCheckBox getCheckBox() {
		return this.checkBox;
	}

	private void handleItemStateChanged( ItemEvent e ) {
		if( this.projectInfo.isInTheMidstOfChange() ) {
			//pass
		} else {
			this.isDesired = e.getStateChange() == ItemEvent.SELECTED;
			this.projectInfo.update();
		}
	}

	public void resetRequired() {
		this.isRequired = false;
	}

	public void appendDesired( List<DeclarationInfo<?>> desired ) {
		if( this.isDesired ) {
			desired.add( this );
		}
	}

	protected void addRequired( Set<DeclarationInfo<?>> visited ) {
		visited.add( this );
		this.isRequired = true;
	}

	public final void updateRequired( Set<DeclarationInfo<?>> visited ) {
		if( visited.contains( this ) ) {
			//pass
		} else {
			this.addRequired( visited );
		}
	}

	public void updateSwing() {
		ButtonModel buttonModel = this.checkBox.getModel();
		buttonModel.setSelected( this.isDesired || this.isRequired );
		buttonModel.setEnabled( this.isDesired || ( this.isRequired == false ) );
	}
}
