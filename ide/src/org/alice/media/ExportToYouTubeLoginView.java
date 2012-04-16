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
package org.alice.media;

import org.lgna.croquet.StringState;
import org.lgna.croquet.WizardPageComposite;
import org.lgna.croquet.components.BorderPanel;
import org.lgna.croquet.components.BorderPanel.Constraint;
import org.lgna.croquet.components.Button;
import org.lgna.croquet.components.Component;
import org.lgna.croquet.components.GridPanel;
import org.lgna.croquet.components.Label;
import org.lgna.croquet.components.TextField;
import org.lgna.croquet.components.View;

/**
 * @author Matt May
 */
public class ExportToYouTubeLoginView extends WizardPageComposite<View<?,?>> {

	public ExportToYouTubeLoginView() {
		super( java.util.UUID.fromString( "9fb59b54-f9af-4abd-835e-f1e89674e8dd" ) );
	}

	@Override
	protected View<?,?> createView() {
		BorderPanel rv = new BorderPanel();
		rv.addComponent( new UserNameAndPasswordComponent(), Constraint.PAGE_START );
		
		Component<?> meat = new VideoPlayer();
		VideoInfoComponent potatoes = new VideoInfoComponent();
		rv.addComponent( GridPanel.createGridPane( 1, 2, meat, potatoes ), Constraint.CENTER );
		rv.addComponent( new Button( null ), Constraint.PAGE_END );
		return rv;
	}

	private class UserNameAndPasswordComponent extends Component {

		@Override
		protected java.awt.Component createAwtComponent() {
			BorderPanel rv = new BorderPanel();
			rv.addComponent( new Button( null ), Constraint.PAGE_END );
			StringState userNameState = new StringState( null, java.util.UUID.fromString( "2c0d22d6-396d-460c-9afd-8cbb14d661ac" ), "" ) {
			};
			StringState passwordState = new StringState( null, java.util.UUID.fromString( "5d38ea7c-b7ec-4a45-aef3-fa5d9dee55cd" ), "" ) {
			};
			TextField userName = new TextField( userNameState );
			TextField password = new TextField( passwordState );
			GridPanel top = GridPanel.createGridPane( 2, 2 );
			top.addComponent( new Label( "user name" ) );
			top.addComponent( userName );
			top.addComponent( new Label( "password" ) );
			top.addComponent( password );
			rv.addComponent( top, Constraint.CENTER );
			return rv.getAwtComponent();
		}
	}
}
