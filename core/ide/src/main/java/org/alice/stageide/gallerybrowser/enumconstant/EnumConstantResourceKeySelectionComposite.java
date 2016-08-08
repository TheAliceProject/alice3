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
package org.alice.stageide.gallerybrowser.enumconstant;

/**
 * @author Dennis Cosgrove
 */
public class EnumConstantResourceKeySelectionComposite extends org.lgna.croquet.SingleValueCreatorInputDialogCoreComposite<org.alice.stageide.gallerybrowser.enumconstant.views.EnumConstantResourceKeySelectionView, org.alice.stageide.modelresource.EnumConstantResourceKey> {
	private static class SingletonHolder {
		private static EnumConstantResourceKeySelectionComposite instance = new EnumConstantResourceKeySelectionComposite();
	}

	public static EnumConstantResourceKeySelectionComposite getInstance() {
		return SingletonHolder.instance;
	}

	private final org.alice.stageide.gallerybrowser.enumconstant.data.EnumConstantResourceKeyListData listData = new org.alice.stageide.gallerybrowser.enumconstant.data.EnumConstantResourceKeyListData();
	private final org.lgna.croquet.RefreshableDataSingleSelectListState<org.alice.stageide.modelresource.EnumConstantResourceKey> enumConstantResourceKeyState = this.createRefreshableListState( "enumConstantResourceKeyState", this.listData, -1 );

	private final ErrorStatus noSelectionErrorStatus = this.createErrorStatus( "noSelectionErrorStatus" );

	private EnumConstantResourceKeySelectionComposite() {
		super( java.util.UUID.fromString( "2a052ea8-1e92-4408-8ce1-8daec5b3e6ec" ) );
	}

	public org.lgna.croquet.RefreshableDataSingleSelectListState<org.alice.stageide.modelresource.EnumConstantResourceKey> getEnumConstantResourceKeyState() {
		return this.enumConstantResourceKeyState;
	}

	@Override
	protected org.alice.stageide.gallerybrowser.enumconstant.views.EnumConstantResourceKeySelectionView createView() {
		return new org.alice.stageide.gallerybrowser.enumconstant.views.EnumConstantResourceKeySelectionView( this );
	}

	@Override
	protected org.alice.stageide.modelresource.EnumConstantResourceKey createValue() {
		return this.enumConstantResourceKeyState.getValue();
	}

	@Override
	protected Integer getWiderGoldenRatioSizeFromWidth() {
		return 1000;
	}

	@Override
	protected Status getStatusPreRejectorCheck( org.lgna.croquet.history.CompletionStep<?> step ) {
		if( this.enumConstantResourceKeyState.getValue() != null ) {
			return IS_GOOD_TO_GO_STATUS;
		} else {
			return this.noSelectionErrorStatus;
		}
	}

	public void setClassResourceKey( org.alice.stageide.modelresource.ClassResourceKey classResourceKey ) {
		this.enumConstantResourceKeyState.clearSelection();
		this.listData.setClassResourceKey( classResourceKey );
	}

}
