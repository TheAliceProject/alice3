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

/**
 * @author Dennis Cosgrove
 */
public abstract class DeclarationInfo<D extends org.lgna.project.ast.Declaration> {
	private final ProjectInfo projectInfo;
	private final D declaration;
	private final javax.swing.Action action = new javax.swing.AbstractAction() {
		@Override
		public void actionPerformed( java.awt.event.ActionEvent e ) {
		}
	};
	private final javax.swing.JCheckBox checkBox = new javax.swing.JCheckBox( this.action );
	private final java.awt.event.ItemListener itemListener = new java.awt.event.ItemListener() {
		@Override
		public void itemStateChanged( java.awt.event.ItemEvent e ) {
			handleItemStateChanged( e );
		}
	};

	private boolean isDesired;
	private boolean isRequired;

	public DeclarationInfo( ProjectInfo projectInfo, D declaration ) {
		this.projectInfo = projectInfo;
		this.declaration = declaration;
		this.action.putValue( javax.swing.Action.NAME, this.declaration.getName() );
		this.checkBox.getModel().addItemListener( this.itemListener );
	}

	public ProjectInfo getProjectInfo() {
		return this.projectInfo;
	}

	public D getDeclaration() {
		return this.declaration;
	}

	public javax.swing.JCheckBox getCheckBox() {
		return this.checkBox;
	}

	private void handleItemStateChanged( java.awt.event.ItemEvent e ) {
		if( this.projectInfo.isInTheMidstOfChange() ) {
			//pass
		} else {
			this.isDesired = e.getStateChange() == java.awt.event.ItemEvent.SELECTED;
			this.projectInfo.update();
		}
	}

	public void resetRequired() {
		this.isRequired = false;
	}

	public void appendDesired( java.util.List<DeclarationInfo<?>> desired ) {
		if( this.isDesired ) {
			desired.add( this );
		}
	}

	protected void addRequired( java.util.Set<DeclarationInfo<?>> visited ) {
		visited.add( this );
		this.isRequired = true;
	}

	public final void updateRequired( java.util.Set<DeclarationInfo<?>> visited ) {
		if( visited.contains( this ) ) {
			//pass
		} else {
			this.addRequired( visited );
		}
	}

	public void updateSwing() {
		javax.swing.ButtonModel buttonModel = this.checkBox.getModel();
		buttonModel.setSelected( this.isDesired || this.isRequired );
		buttonModel.setEnabled( this.isDesired || ( this.isRequired == false ) );
	}
}
