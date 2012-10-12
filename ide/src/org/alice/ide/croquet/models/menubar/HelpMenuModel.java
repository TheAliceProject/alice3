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
package org.alice.ide.croquet.models.menubar;

/**
 * @author Dennis Cosgrove
 */
public class HelpMenuModel extends org.lgna.croquet.PredeterminedMenuModel {
	private static org.lgna.croquet.StandardMenuItemPrepModel[] createMenuItemPrepModels() {
		java.util.List<org.lgna.croquet.StandardMenuItemPrepModel> list = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList(
				org.alice.ide.help.HelpComposite.getInstance().getOperation().getMenuItemPrepModel(),
				org.alice.ide.croquet.models.help.GraphicsHelpComposite.getInstance().getOperation().getMenuItemPrepModel(),
				org.lgna.croquet.MenuModel.SEPARATOR,
				org.alice.ide.croquet.models.help.ReportBugComposite.getInstance().getBooleanState().getMenuItemPrepModel(),
				org.alice.ide.croquet.models.help.SuggestImprovementComposite.getInstance().getBooleanState().getMenuItemPrepModel(),
				org.alice.ide.croquet.models.help.RequestNewFeatureComposite.getInstance().getBooleanState().getMenuItemPrepModel(),
				org.lgna.croquet.MenuModel.SEPARATOR,
				org.alice.ide.warning.WarningDialogComposite.getInstance().getOperation().getMenuItemPrepModel(),
				org.alice.ide.croquet.models.help.ShowSystemPropertiesComposite.getInstance().getOperation().getMenuItemPrepModel(),
				org.alice.ide.croquet.models.help.BrowseReleaseNotesOperation.getInstance().getMenuItemPrepModel()
				);
		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isMac() ) {
			//pass
		} else {
			list.add( org.lgna.croquet.MenuModel.SEPARATOR );
			list.add( org.alice.stageide.about.AboutComposite.getInstance().getOperation().getMenuItemPrepModel() );
		}
		return edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( list, org.lgna.croquet.StandardMenuItemPrepModel.class );
	}

	private static class SingletonHolder {
		private static HelpMenuModel instance = new HelpMenuModel();
	}

	public static HelpMenuModel getInstance() {
		return SingletonHolder.instance;
	}

	private HelpMenuModel() {
		super( java.util.UUID.fromString( "435770a7-fb94-49ee-8c4d-b55a80618a09" ), createMenuItemPrepModels() );
	}
}
