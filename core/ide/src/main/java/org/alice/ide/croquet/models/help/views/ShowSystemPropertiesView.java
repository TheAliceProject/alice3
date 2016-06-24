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
package org.alice.ide.croquet.models.help.views;

/**
 * @author Dennis Cosgrove
 */
public class ShowSystemPropertiesView extends org.lgna.croquet.views.FormPanel {
	public ShowSystemPropertiesView( org.alice.ide.croquet.models.help.ShowSystemPropertiesComposite composite ) {
		super( composite );
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );
	}

	@Override
	protected void appendRows( java.util.List<org.lgna.croquet.views.LabeledFormRow> rows ) {
		String[] propertyNames = { "java.version", "os.name", "os.version", "os.arch", "sun.arch.data.model" };
		for( String propertyName : propertyNames ) {
			String value = System.getProperty( propertyName );
			rows.add( org.lgna.croquet.views.LabeledFormRow.createFromLabel( new org.lgna.croquet.views.Label( propertyName + ":" ), new org.lgna.croquet.views.Label( value ) ) );
		}
		rows.add( new org.lgna.croquet.views.LabeledFormRow( null, org.lgna.croquet.views.BoxUtilities.createVerticalSliver( 8 ) ) );
		org.alice.ide.croquet.models.help.ShowPathPropertyComposite[] showPathPropertyComposites = {
				org.alice.ide.IdeApp.INSTANCE.getShowClassPathPropertyComposite(),
				org.alice.ide.IdeApp.INSTANCE.getShowLibraryPathPropertyComposite()
		};
		for( org.alice.ide.croquet.models.help.ShowPathPropertyComposite showPathPropertyComposite : showPathPropertyComposites ) {
			String propertyName = showPathPropertyComposite.getPropertyName();
			rows.add( org.lgna.croquet.views.LabeledFormRow.createFromLabel(
					new org.lgna.croquet.views.Label( propertyName + ":" ),
					showPathPropertyComposite.getLaunchOperation().createHyperlink()
					) );
		}
		rows.add( new org.lgna.croquet.views.LabeledFormRow( null, org.lgna.croquet.views.BoxUtilities.createVerticalSliver( 8 ) ) );
		rows.add( new org.lgna.croquet.views.LabeledFormRow( null, org.alice.ide.IdeApp.INSTANCE.getShowAllSystemPropertiesComposite().getLaunchOperation().createHyperlink() ) );
	}
}
