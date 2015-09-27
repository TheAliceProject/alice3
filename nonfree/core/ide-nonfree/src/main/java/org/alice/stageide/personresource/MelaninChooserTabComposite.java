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
package org.alice.stageide.personresource;

import static org.lgna.story.resources.sims2.BaseSkinTone.DARK;
import static org.lgna.story.resources.sims2.BaseSkinTone.DARKER;
import static org.lgna.story.resources.sims2.BaseSkinTone.LIGHT;
import static org.lgna.story.resources.sims2.BaseSkinTone.LIGHTER;

/**
 * @author Dennis Cosgrove
 */
public final class MelaninChooserTabComposite extends org.lgna.croquet.color.ColorChooserTabComposite<org.alice.stageide.personresource.views.MelaninChooserTabView> {
	private static final java.awt.Color[] MELANIN_CHIP_SHADES = {
			edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( DARKER.getColor(), 1.0, 1.0, 0.7 ),
			DARKER.getColor(),
			edu.cmu.cs.dennisc.java.awt.ColorUtilities.interpolate( DARKER.getColor(), DARK.getColor(), 0.5f ),
			DARK.getColor(),
			edu.cmu.cs.dennisc.java.awt.ColorUtilities.interpolate( DARK.getColor(), LIGHT.getColor(), 0.5f ),
			LIGHT.getColor(),
			edu.cmu.cs.dennisc.java.awt.ColorUtilities.interpolate( LIGHT.getColor(), LIGHTER.getColor(), 0.5f ),
			LIGHTER.getColor(),
			edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( LIGHTER.getColor(), 1.0, 1.0, 1.1 )
	};
	private static final java.awt.Color[] MELANIN_SLIDER_SHADES = {
			MELANIN_CHIP_SHADES[ 0 ],
			DARKER.getColor(),
			DARK.getColor(),
			LIGHT.getColor(),
			LIGHTER.getColor(),
			MELANIN_CHIP_SHADES[ MELANIN_CHIP_SHADES.length - 1 ]
	};

	public MelaninChooserTabComposite() {
		super( java.util.UUID.fromString( "e0d31df0-1775-4fdb-ab00-6ecfe74625bd" ) );
	}

	public java.awt.Color[] getMelaninChipShades() {
		return MELANIN_CHIP_SHADES;
	}

	public java.awt.Color[] getMelaninSliderShades() {
		return MELANIN_SLIDER_SHADES;
	}

	@Override
	protected org.lgna.croquet.views.ScrollPane createScrollPaneIfDesired() {
		return null;
	}

	@Override
	protected org.alice.stageide.personresource.views.MelaninChooserTabView createView() {
		return new org.alice.stageide.personresource.views.MelaninChooserTabView( this );
	}
}
