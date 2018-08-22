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

import edu.cmu.cs.dennisc.java.awt.ComponentUtilities;
import edu.cmu.cs.dennisc.javax.swing.IconUtilities;
import org.lgna.croquet.imp.cascade.ItemNode;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class CascadeItem<F, B> extends MenuItemPrepModel implements CascadeBlankChild<F> {
	private transient boolean isDirty = true;
	private transient JComponent menuProxy = null;
	private transient Icon icon = null;

	public CascadeItem( UUID id ) {
		super( id );
	}

	@Override
	public int getItemCount() {
		return 1;
	}

	@Override
	public CascadeItem<F, B> getItemAt( int index ) {
		assert index == 0;
		return this;
	}

	public abstract F getTransientValue( ItemNode<? super F, B> node );

	public abstract F createValue( ItemNode<? super F, B> node );

	protected abstract JComponent createMenuItemIconProxy( ItemNode<? super F, B> node );

	@Override
	protected void localize() {
		this.isDirty = true;
	}

	protected boolean isDirty() {
		return this.isDirty;
	}

	protected void markClean() {
		this.isDirty = false;
	}

	public void markDirty() {
		this.isDirty = true;
	}

	protected JComponent getMenuProxy( ItemNode<? super F, B> node ) {
		if( this.menuProxy != null ) {
			//pass
		} else {
			this.menuProxy = this.createMenuItemIconProxy( node );
		}
		return this.menuProxy;
	}

	//todo:
	private static void setBoxLayoutComponentOrientationTree( Component c, ComponentOrientation componentOrientation ) {
		if( c instanceof JPanel ) {
			JPanel jPanel = (JPanel)c;
			LayoutManager layoutManager = jPanel.getLayout();
			if( layoutManager instanceof BoxLayout ) {
				//javax.swing.BoxLayout boxLayout = (javax.swing.BoxLayout)layoutManager;
				c.setComponentOrientation( componentOrientation );
			}
		}
		if( c instanceof Container ) {
			Container container = (Container)c;
			for( Component component : container.getComponents() ) {
				setBoxLayoutComponentOrientationTree( component, componentOrientation );
			}
		}
	}

	public Icon getMenuItemIcon( ItemNode<? super F, B> node ) {
		if( this.isDirty() ) {
			this.icon = null;
			this.menuProxy = null;
		}
		if( this.icon != null ) {
			//pass
		} else {
			JComponent component = this.getMenuProxy( node );
			if( component != null ) {
				final boolean IS_LEFT_TO_RIGHT_COMPONENT_ORIENTATION_REQUIRED_TO_WORK = true;
				ComponentOrientation componentOrientation = component.getComponentOrientation();
				if( componentOrientation.isLeftToRight() ) {
					//pass
				} else {
					if( IS_LEFT_TO_RIGHT_COMPONENT_ORIENTATION_REQUIRED_TO_WORK ) {
						setBoxLayoutComponentOrientationTree( component, ComponentOrientation.LEFT_TO_RIGHT );
					}
				}

				ComponentUtilities.invalidateTree( component );
				ComponentUtilities.doLayoutTree( component );
				ComponentUtilities.setSizeToPreferredSizeTree( component );

				if( componentOrientation.isLeftToRight() ) {
					//pass
				} else {
					if( IS_LEFT_TO_RIGHT_COMPONENT_ORIENTATION_REQUIRED_TO_WORK ) {
						setBoxLayoutComponentOrientationTree( component, componentOrientation );
					}
				}

				Dimension size = component.getPreferredSize();
				if( ( size.width > 0 ) && ( size.height > 0 ) ) {
					this.icon = IconUtilities.createIcon( component );
				} else {
					this.icon = null;
				}
			} else {
				this.icon = null;
			}
			this.markClean();
		}
		return this.icon;
	}

	public String getMenuItemText( ItemNode<? super F, B> node ) {
		return null;
	}
}
