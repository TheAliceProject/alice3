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
package org.alice.ide.croquet.models.members;

/**
 * @author Dennis Cosgrove
 */
public class MembersTabSelectionState extends edu.cmu.cs.dennisc.croquet.TabSelectionState {
	private static class IndirectCurrentAccessibleTypeIcon implements javax.swing.Icon {
		private javax.swing.Icon getCurrentAccessibleTypeIcon() {
			edu.cmu.cs.dennisc.alice.ast.Accessible accessible = org.alice.ide.croquet.models.ui.AccessibleListSelectionState.getInstance().getSelectedItem();
			edu.cmu.cs.dennisc.alice.ast.AbstractType< ?, ?, ? > type; 
			if( accessible != null ) {
				type = accessible.getValueType();
			} else {
				type = null;
			}
			return org.alice.stageide.gallerybrowser.ResourceManager.getSmallIconForType( type );
		}
		public int getIconHeight() {
			javax.swing.Icon icon = getCurrentAccessibleTypeIcon();
			if( icon != null ) {
				return icon.getIconHeight();
			} else {
				return 0;
			}
		}
		public int getIconWidth() {
			javax.swing.Icon icon = getCurrentAccessibleTypeIcon();
			if( icon != null ) {
				return icon.getIconWidth();
			} else {
				return 0;
			}
		}
		public void paintIcon(java.awt.Component c, java.awt.Graphics g, int x, int y) {
			javax.swing.Icon icon = getCurrentAccessibleTypeIcon();
			if( icon != null ) {
				icon.paintIcon(c, g, x, y);
			}
		}
	}
	
	private static javax.swing.Icon ICON = org.alice.ide.memberseditor.MembersEditor.IS_FOLDER_TABBED_PANE_DESIRED ? null : new IndirectCurrentAccessibleTypeIcon();

	private static class SingletonHolder {
		private static MembersTabSelectionState instance = new MembersTabSelectionState();
	}
	public static MembersTabSelectionState getInstance() {
		return SingletonHolder.instance;
	}
	private MembersTabSelectionState() {
		super( org.alice.ide.IDE.UI_STATE_GROUP, java.util.UUID.fromString( "d8348dfa-35df-441d-b233-0e1bd9ffd68f" ), org.alice.ide.croquet.codecs.SingletonCodec.getInstance( edu.cmu.cs.dennisc.croquet.PredeterminedTab.class ) );
		ProceduresTab proceduresTab = ProceduresTab.getInstance();
		proceduresTab.setTitleIcon( ICON );
		proceduresTab.setTitleText( this.getLocalizedText( "procedures" ) );
		FunctionsTab functionsTab = FunctionsTab.getInstance();
		functionsTab.setTitleIcon( ICON );
		functionsTab.setTitleText( this.getLocalizedText( "functions" ) );
		FieldsTab fieldsTab = FieldsTab.getInstance();
		fieldsTab.setTitleIcon( ICON );
		fieldsTab.setTitleText( this.getLocalizedText( "fields" ) );
		this.setListData( 0, proceduresTab, functionsTab, fieldsTab );
	}
}
