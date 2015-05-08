/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.alice.stageide.sceneeditor.side;

import org.alice.interact.handle.HandleStyle;

/**
 * @author Dennis Cosgrove
 */
public class SideComposite extends org.lgna.croquet.SimpleComposite<org.alice.stageide.sceneeditor.side.views.SideView> {
	//todo
	@Deprecated
	public static SideComposite getInstance() {
		return (SideComposite)org.alice.stageide.perspectives.scenesetup.SetupScenePerspectiveComposite.getInstance().getSceneLayoutComposite().getTrailingComposite();
	}

	//todo: remove and migrate handle.properties to croquet.properties
	private static enum HandleStyleCodec implements org.lgna.croquet.ItemCodec<HandleStyle> {
		INSTANCE;
		@Override
		public void encodeValue( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, org.alice.interact.handle.HandleStyle value ) {
			binaryEncoder.encode( value );
		}

		@Override
		public org.alice.interact.handle.HandleStyle decodeValue( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			return binaryDecoder.decodeEnum();
		}

		@Override
		public Class<org.alice.interact.handle.HandleStyle> getValueClass() {
			return HandleStyle.class;
		}

		@Override
		public void appendRepresentation( StringBuilder sb, org.alice.interact.handle.HandleStyle value ) {
			String bundleName = org.alice.interact.handle.HandleStyle.class.getPackage().getName() + ".handle";
			String key = value.name().toLowerCase( java.util.Locale.ENGLISH );
			try {
				java.util.Locale locale = javax.swing.JComponent.getDefaultLocale();
				java.util.ResourceBundle resourceBundle = edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities.getUtf8Bundle( bundleName, locale );
				edu.cmu.cs.dennisc.java.util.logging.Logger.errln( bundleName, key );
				sb.append( resourceBundle.getString( key ) );
			} catch( java.util.MissingResourceException mre ) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.throwable( mre, bundleName, key );
				sb.append( key );
			}
		}
	}

	private final SnapDetailsToolPaletteCoreComposite snapDetailsToolPaletteCoreComposite = new SnapDetailsToolPaletteCoreComposite();
	private final ObjectPropertiesToolPalette objectPropertiesTab = new ObjectPropertiesToolPalette();
	private final ObjectMarkersToolPalette objectMarkersTab = new ObjectMarkersToolPalette();
	private final CameraMarkersToolPalette cameraMarkersTab = new CameraMarkersToolPalette();
	private final org.lgna.croquet.ImmutableDataSingleSelectListState<HandleStyle> handleStyleState = this.createImmutableListState( "handleStyleState", HandleStyle.class, HandleStyleCodec.INSTANCE, 0, HandleStyle.values() );

	private final org.lgna.croquet.BooleanState isSnapEnabledState = this.createBooleanState( "isSnapEnabledState", false );

	private final org.lgna.croquet.BooleanState areJointsShowingState = this.createBooleanState( "areJointsShowingState", false );

	public SideComposite() {
		super( java.util.UUID.fromString( "3adc7b8a-f317-467d-8c8a-807086fffaea" ) );
	}

	@Override
	protected void localize() {
		super.localize();
		for( HandleStyle handleStyle : HandleStyle.values() ) {
			org.lgna.croquet.BooleanState booleanState = this.handleStyleState.getItemSelectedState( handleStyle );
			booleanState.setIconForBothTrueAndFalse( handleStyle.getIcon() );
			booleanState.setToolTipText( handleStyle.getToolTipText() );
		}
		this.registerSubComposite( snapDetailsToolPaletteCoreComposite.getOuterComposite() );

		this.registerSubComposite( this.objectPropertiesTab );
		this.registerSubComposite( this.objectMarkersTab );
		this.registerSubComposite( this.cameraMarkersTab );
	}

	public SnapDetailsToolPaletteCoreComposite getSnapDetailsToolPaletteCoreComposite() {
		return this.snapDetailsToolPaletteCoreComposite;
	}

	public ObjectPropertiesToolPalette getObjectPropertiesTab() {
		return this.objectPropertiesTab;
	}

	public CameraMarkersToolPalette getCameraMarkersTab() {
		return this.cameraMarkersTab;
	}

	public ObjectMarkersToolPalette getObjectMarkersTab() {
		return this.objectMarkersTab;
	}

	public org.lgna.croquet.BooleanState getAreJointsShowingState() {
		return this.areJointsShowingState;
	}

	public org.lgna.croquet.BooleanState getIsSnapEnabledState() {
		return this.isSnapEnabledState;
	}

	public org.lgna.croquet.ImmutableDataSingleSelectListState<HandleStyle> getHandleStyleState() {
		return this.handleStyleState;
	}

	@Override
	protected org.alice.stageide.sceneeditor.side.views.SideView createView() {
		return new org.alice.stageide.sceneeditor.side.views.SideView( this );
	}
}
