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

/**
 * @author Dennis Cosgrove
 */
public abstract class ToolPaletteCoreComposite<V extends org.lgna.croquet.views.CompositeView<?, ?>> extends AbstractComposite<V> {
	private static class InternalIsExpandedState extends BooleanState {
		private final ToolPaletteCoreComposite<?> coreComposite;

		private InternalIsExpandedState( Group group, boolean initialValue, ToolPaletteCoreComposite<?> coreComposite ) {
			super( group, java.util.UUID.fromString( "470a871b-61ec-495b-8007-06a573a7a126" ), initialValue );
			this.coreComposite = coreComposite;
		}

		@Override
		protected Class<? extends Element> getClassUsedForLocalization() {
			return this.coreComposite.getClassUsedForLocalization();
		}

		@Override
		protected String modifyTextIfNecessary( String text, boolean isTrue ) {
			return this.coreComposite.modifyTextIfNecessary( super.modifyTextIfNecessary( text, isTrue ), isTrue );
		}
	}

	public static final class OuterComposite extends AbstractComposite<org.lgna.croquet.views.ToolPaletteView> {
		private final BooleanState isExpandedState;
		private final ToolPaletteCoreComposite<?> coreComposite;

		private OuterComposite( BooleanState isExpandedState, ToolPaletteCoreComposite<?> coreComposite ) {
			super( java.util.UUID.fromString( "92df5e68-7aa6-4bc7-9ab1-da5cf0a448c0" ) );
			this.isExpandedState = isExpandedState;
			this.coreComposite = coreComposite;
		}

		public BooleanState getIsExpandedState() {
			return this.isExpandedState;
		}

		public ToolPaletteCoreComposite<?> getCoreComposite() {
			return this.coreComposite;
		}

		@Override
		protected org.lgna.croquet.views.ScrollPane createScrollPaneIfDesired() {
			return null;
		}

		@Override
		protected org.lgna.croquet.views.ToolPaletteView createView() {
			return new org.lgna.croquet.views.ToolPaletteView( this );
		}

		@Override
		public void handlePreActivation() {
			this.coreComposite.handlePreActivation();
			super.handlePreActivation();
		}

		@Override
		public void handlePostDeactivation() {
			super.handlePostDeactivation();
			this.coreComposite.handlePostDeactivation();
		}
	}

	private final OuterComposite outerComposite;

	public ToolPaletteCoreComposite( java.util.UUID migrationId, Group group, boolean initialValue ) {
		super( migrationId );
		InternalIsExpandedState isExpandedState = new InternalIsExpandedState( group, initialValue, this );
		this.outerComposite = new OuterComposite( isExpandedState, this );
	}

	protected String modifyTextIfNecessary( String text, boolean isExpanded ) {
		return text;
	}

	public OuterComposite getOuterComposite() {
		return this.outerComposite;
	}
}
