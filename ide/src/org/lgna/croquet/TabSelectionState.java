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
public class TabSelectionState<T extends TabComposite<?>> extends MutableDataListSelectionState<T> {
	public TabSelectionState( Group group, java.util.UUID id, ItemCodec<T> codec, int selectionIndex ) {
		super( group, id, codec, selectionIndex );
	}

	public TabSelectionState( Group group, java.util.UUID id, ItemCodec<T> codec ) {
		super( group, id, codec );
	}

	public TabSelectionState( Group group, java.util.UUID id, ItemCodec<T> codec, int selectionIndex, java.util.Collection<T> data ) {
		super( group, id, codec, selectionIndex, data );
	}

	public TabSelectionState( Group group, java.util.UUID id, ItemCodec<T> codec, int selectionIndex, T... data ) {
		super( group, id, codec, selectionIndex, data );
	}

	public org.lgna.croquet.components.FolderTabbedPane<T> createFolderTabbedPane() {
		return new org.lgna.croquet.components.FolderTabbedPane<T>( this );
	}

	public org.lgna.croquet.components.ToolPaletteTabbedPane<T> createToolPaletteTabbedPane() {
		return new org.lgna.croquet.components.ToolPaletteTabbedPane<T>( this );
	}

	public org.lgna.croquet.components.JComponent<?> getMainComponentFor( T item ) {
		org.lgna.croquet.components.AbstractTabbedPane<T, ?> tabbedPane = org.lgna.croquet.components.ComponentManager.getFirstComponent( this, org.lgna.croquet.components.AbstractTabbedPane.class );
		if( tabbedPane != null ) {
			return tabbedPane.getMainComponentFor( item );
		} else {
			return null;
		}
	}

	public org.lgna.croquet.components.ScrollPane getScrollPaneFor( T item ) {
		org.lgna.croquet.components.AbstractTabbedPane<T, ?> tabbedPane = org.lgna.croquet.components.ComponentManager.getFirstComponent( this, org.lgna.croquet.components.AbstractTabbedPane.class );
		if( tabbedPane != null ) {
			return tabbedPane.getScrollPaneFor( item );
		} else {
			return null;
		}
	}

	public org.lgna.croquet.components.JComponent<?> getRootComponentFor( T item ) {
		org.lgna.croquet.components.AbstractTabbedPane<T, ?> tabbedPane = org.lgna.croquet.components.ComponentManager.getFirstComponent( this, org.lgna.croquet.components.AbstractTabbedPane.class );
		if( tabbedPane != null ) {
			return tabbedPane.getRootComponentFor( item );
		} else {
			return null;
		}
	}

	public void setItemIconForBothTrueAndFalse( T item, javax.swing.Icon icon ) {
		this.getItemSelectedState( item ).setIconForBothTrueAndFalse( icon );
	}

	private boolean isActive;

	public void handlePreActivation() {
		this.initializeIfNecessary();
		TabComposite<?> selected = this.getValue();
		if( selected != null ) {
			selected.handlePreActivation();
		}
		this.isActive = true;
	}

	@Override
	protected void fireChanged( T prevValue, T nextValue, IsAdjusting isAdjusting ) {
		if( isAdjusting.getValue() ) {
			//pass
		} else {
			if( prevValue != nextValue ) {
				if( this.isActive ) {
					if( prevValue != null ) {
						prevValue.handlePostDeactivation();
					}
				}
				super.fireChanged( prevValue, nextValue, isAdjusting );
				if( this.isActive ) {
					if( nextValue != null ) {
						nextValue.handlePreActivation();
					}
				}
			}
		}
	}

	public void handlePostDeactivation() {
		this.isActive = false;
		TabComposite<?> selected = this.getValue();
		if( selected != null ) {
			selected.handlePostDeactivation();
		}
	}
}
