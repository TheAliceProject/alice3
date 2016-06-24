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

package org.alice.ide.properties.uicontroller;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import org.alice.ide.properties.adapter.AbstractPropertyAdapter;
import org.lgna.croquet.views.AwtComponentView;
import org.lgna.croquet.views.BoxUtilities;

public abstract class BasicPropertyController<P> extends AbstractAdapterController<P>
{
	protected static final String BLANK_STRING = "NO VALUE";

	protected AwtComponentView<?> propertyComponent;

	public BasicPropertyController( AbstractPropertyAdapter<P, ?> propertyAdapter )
	{
		super( propertyAdapter );
	}

	protected abstract AwtComponentView<?> createPropertyComponent();

	@Override
	protected void initializeComponents()
	{
		super.initializeComponents();
		this.propertyComponent = createPropertyComponent();
		this.setMinimumPreferredHeight( PropertyAdapterController.MIN_ADAPTER_HEIGHT );
	}

	@Override
	protected void updateUIFromNewAdapter()
	{
		this.removeAllComponents();
		int xIndex = 0;
		this.addComponent( this.propertyComponent, new GridBagConstraints(
				xIndex++, // gridX
				0, // gridY
				1, // gridWidth
				1, // gridHeight
				0.0, // weightX
				0.0, // weightY
				GridBagConstraints.WEST, // anchor
				GridBagConstraints.HORIZONTAL, // fill
				new Insets( 0, 0, 0, 0 ), // insets (top, left, bottom, right)
				0, // ipadX
				0 ) // ipadY
		);
		this.addComponent( BoxUtilities.createHorizontalGlue(), new GridBagConstraints(
				xIndex++, //gridX
				0, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.WEST, //anchor 
				GridBagConstraints.HORIZONTAL, //fill
				new Insets( 0, 0, 0, 0 ), //insets
				0, //ipadX
				0 ) //ipadY
		);
	}
}
