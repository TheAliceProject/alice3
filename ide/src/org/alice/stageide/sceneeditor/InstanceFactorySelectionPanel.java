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

package org.alice.stageide.sceneeditor;

/**
 * @author Dennis Cosgrove
 */
public class InstanceFactorySelectionPanel extends org.lgna.croquet.components.ViewController< javax.swing.JPanel, org.alice.ide.instancefactory.InstanceFactoryState > {
	private static final class InternalButton extends org.lgna.croquet.components.JComponent< javax.swing.AbstractButton > {
		private final org.alice.ide.instancefactory.InstanceFactory instanceFactory;
		public InternalButton( org.alice.ide.instancefactory.InstanceFactory instanceFactory ) {
			this.instanceFactory = instanceFactory;
		}
		@Override
		protected javax.swing.AbstractButton createAwtComponent() {
			javax.swing.JRadioButton rv = new javax.swing.JRadioButton( this.instanceFactory.getRepr() );
			if( this.instanceFactory instanceof org.alice.ide.instancefactory.ThisInstanceFactory ) {
				//pass
			} else {
				rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( 0, 16, 0, 0 ) );
			}
			return rv;
		}
	}
	private static final class InternalPanel extends org.alice.ide.croquet.components.RefreshPanel {
		private final javax.swing.ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
		private final java.util.Map< org.alice.ide.instancefactory.InstanceFactory, InternalButton > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
		public InternalPanel() {
		}
		@Override
		protected java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
			return new javax.swing.BoxLayout( jPanel, javax.swing.BoxLayout.PAGE_AXIS );
		}
		
		private InternalButton getButtonFor( org.alice.ide.instancefactory.InstanceFactory instanceFactory ) {
			InternalButton rv = map.get( instanceFactory );
			if( rv != null ) {
				//pass
			} else {
				rv = new InternalButton( instanceFactory );
			}
			return rv;
		}
		
		@Override
		protected void internalRefresh() {
			this.removeAllComponents();
			java.util.List< InternalButton > buttons = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			buttons.add( getButtonFor( org.alice.ide.instancefactory.ThisInstanceFactory.SINGLETON ) );
			for( org.lgna.project.ast.UserField field : org.alice.ide.IDE.getActiveInstance().getSceneType().fields ) {
				buttons.add( getButtonFor( org.alice.ide.instancefactory.ThisFieldAccessFactory.getInstance( field ) ) );
			}
			for( InternalButton button : buttons ) {
				this.internalAddComponent( button );
				this.buttonGroup.add( button.getAwtComponent() );
			}
			this.revalidateAndRepaint();
		}
	}
	
	private final InternalPanel internalPanel = new InternalPanel();
	public InstanceFactorySelectionPanel() {
		super( org.alice.ide.instancefactory.InstanceFactoryState.getInstance() );
	}
	@Override
	protected javax.swing.JPanel createAwtComponent() {
		javax.swing.JPanel rv = new javax.swing.JPanel();
		rv.setLayout( new java.awt.BorderLayout() );
		rv.setOpaque( false );
		rv.add( this.internalPanel.getAwtComponent(), java.awt.BorderLayout.CENTER );
		return rv;
	}
}
