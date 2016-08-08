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
package org.lgna.croquet.color;

import org.lgna.croquet.PrepModel;

/**
 * @author Dennis Cosgrove
 */
public abstract class ColorState extends org.lgna.croquet.ItemState<java.awt.Color> {

	public class SwingModel {
		private java.awt.Color value;

		private final java.util.List<javax.swing.event.ChangeListener> changeListeners = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();

		public SwingModel( java.awt.Color initialValue ) {
			this.value = initialValue;
		}

		public java.awt.Color getValue() {
			return this.value;
		}

		public void setValue( java.awt.Color nextValue, java.awt.event.MouseEvent e ) {
			if( this.value.equals( nextValue ) ) {
				//pass
			} else {
				this.value = nextValue;
				IsAdjusting isAdjusting;
				if( e != null ) {
					isAdjusting = e.getID() != java.awt.event.MouseEvent.MOUSE_RELEASED ? IsAdjusting.TRUE : IsAdjusting.FALSE;
				} else {
					isAdjusting = IsAdjusting.FALSE;
				}

				//todo
				isAdjusting = IsAdjusting.FALSE;

				org.lgna.croquet.triggers.Trigger trigger;
				if( e != null ) {
					trigger = org.lgna.croquet.triggers.MouseEventTrigger.createUserInstance( e );
				} else {
					trigger = org.lgna.croquet.triggers.NullTrigger.createUserInstance();
				}
				changeValueFromSwing( this.value, isAdjusting, trigger );
				if( this.changeListeners.size() > 0 ) {
					Object source = e != null ? e.getSource() : this;
					javax.swing.event.ChangeEvent changeEvent = new javax.swing.event.ChangeEvent( source );
					for( javax.swing.event.ChangeListener changeListener : this.changeListeners ) {
						changeListener.stateChanged( changeEvent );
					}
				}
			}
		}

		public void addChangeListener( javax.swing.event.ChangeListener changeListener ) {
			this.changeListeners.add( changeListener );
		}

		public void removeChangeListener( javax.swing.event.ChangeListener changeListener ) {
			this.changeListeners.remove( changeListener );
		}
	}

	private final SwingModel swingModel;

	private final ColorChooserDialogCoreComposite chooserDialogCoreComposite;

	public ColorState( org.lgna.croquet.Group group, java.util.UUID id, java.awt.Color initialValue ) {
		super( group, id, initialValue, org.lgna.croquet.codecs.ColorCodec.SINGLETON );
		this.swingModel = new SwingModel( initialValue );
		this.chooserDialogCoreComposite = new ColorChooserDialogCoreComposite( this );
	}

	public SwingModel getSwingModel() {
		return this.swingModel;
	}

	@Override
	protected void initialize() {
		super.initialize();
		this.chooserDialogCoreComposite.initializeIfNecessary();
	}

	@Override
	protected void localize() {
	}

	@Override
	protected java.awt.Color getSwingValue() {
		return this.swingModel.value;
	}

	@Override
	protected void setSwingValue( java.awt.Color nextValue ) {
		this.swingModel.value = nextValue;
		if( this.swingModel.changeListeners.size() > 0 ) {
			Object source = this;
			javax.swing.event.ChangeEvent changeEvent = new javax.swing.event.ChangeEvent( source );
			for( javax.swing.event.ChangeListener changeListener : this.swingModel.changeListeners ) {
				changeListener.stateChanged( changeEvent );
			}
		}
	}

	@Override
	public java.util.List<java.util.List<PrepModel>> getPotentialPrepModelPaths( org.lgna.croquet.edits.Edit edit ) {
		return java.util.Collections.emptyList();
	}

	public ColorChooserDialogCoreComposite getChooserDialogCoreComposite() {
		return this.chooserDialogCoreComposite;
	}
}
