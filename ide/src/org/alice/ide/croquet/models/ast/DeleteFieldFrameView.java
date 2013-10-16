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
package org.alice.ide.croquet.models.ast;

import javax.swing.Icon;

import org.alice.stageide.icons.IconFactoryManager;
import org.lgna.croquet.components.BorderPanel;
import org.lgna.croquet.components.GridPanel;
import org.lgna.croquet.components.ImmutableTextArea;
import org.lgna.croquet.components.Label;
import org.lgna.croquet.icon.IconFactory;

import edu.cmu.cs.dennisc.math.GoldenRatio;

/**
 * @author Matt May
 */
public class DeleteFieldFrameView extends BorderPanel {

	private static int WIDTH = 550;

	public DeleteFieldFrameView( DeleteFieldFrameComposite composite ) {
		this.setMinimumPreferredHeight( GoldenRatio.getShorterSideLength( WIDTH ) );
		this.setMinimumPreferredWidth( WIDTH );
		//		System.out.println( composite.getField().getValueType() );
		//		System.out.println( composite.getField().getValueType().getFirstEncounteredJavaType().getClassReflectionProxy() );
		//		BufferedImage image = AliceResourceUtilties.getThumbnail( composite.getField().getValueType().getFirstEncounteredJavaType().getClassReflectionProxy().getReification() );
		ImmutableTextArea textArea = composite.getBleh().createImmutableTextArea();
		//		System.out.println(image);
		//		Icon icon = new ScaledImageIcon( image, 120, 90 );
		//		System.out.println( composite.getField() );
		IconFactory iconFactory = IconFactoryManager.getIconFactoryForField( composite.getField() );
		Icon icon = iconFactory.getIcon( org.alice.ide.Theme.DEFAULT_LARGE_ICON_SIZE );
		//		System.out.println( "icon factory: " + iconFactory );
		//		System.out.println( "icon: " + icon );
		Label label = new Label( icon );
		this.addComponent( label, Constraint.LINE_START );
		GridPanel panel = GridPanel.createGridPane( 3, 1 );
		panel.addComponent( new Label() );
		panel.addComponent( textArea );
		panel.addComponent( new Label() );
		this.addComponent( panel, Constraint.CENTER );
		//		FindFieldsFrameComposite searchFrame = FindFieldsFrameComposite.getFrameFor( composite.getField() );
		//		this.addComponent( composite.getSearchFrame().getBooleanState().createToggleButton(), Constraint.PAGE_END );
	}
}
