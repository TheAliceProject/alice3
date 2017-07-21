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

package org.alice.ide.perspectives;

/**
 * @author Dennis Cosgrove
 */
public abstract class ProjectPerspective extends org.lgna.croquet.AbstractPerspective {
	public ProjectPerspective( java.util.UUID id, org.alice.ide.ProjectDocumentFrame projectDocumentFrame, org.lgna.croquet.MenuBarComposite menuBar ) {
		super( id );
		this.projectDocumentFrame = projectDocumentFrame;
		this.menuBar = menuBar;
	}

	public org.alice.ide.ProjectDocumentFrame getProjectDocumentFrame() {
		return this.projectDocumentFrame;
	}

	@Override
	public org.lgna.croquet.MenuBarComposite getMenuBarComposite() {
		return this.menuBar;
	}

	public abstract org.lgna.croquet.views.TrackableShape getRenderWindow();

	public abstract org.alice.ide.codedrop.CodePanelWithDropReceptor getCodeDropReceptorInFocus();

	protected abstract void addPotentialDropReceptors( java.util.List<org.lgna.croquet.DropReceptor> out, org.alice.ide.croquet.models.IdeDragModel dragModel );

	public final java.util.List<org.lgna.croquet.DropReceptor> createListOfPotentialDropReceptors( org.alice.ide.croquet.models.IdeDragModel dragModel ) {
		java.util.List<org.lgna.croquet.DropReceptor> rv = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		this.addPotentialDropReceptors( rv, dragModel );
		org.lgna.croquet.DropReceptor recycleBinDropReceptor = org.alice.ide.recyclebin.RecycleBin.SINGLETON.getDropReceptor();
		if( recycleBinDropReceptor.isPotentiallyAcceptingOf( dragModel ) ) {
			rv.add( recycleBinDropReceptor );
		}
		org.lgna.croquet.DropReceptor clipboardDropReceptor = org.alice.ide.clipboard.Clipboard.SINGLETON.getDropReceptor();
		if( clipboardDropReceptor.isPotentiallyAcceptingOf( dragModel ) ) {
			rv.add( clipboardDropReceptor );
		}
		return rv;
	}

	@Override
	protected String createRepr() {
		return this.getName();
	}

	private final org.alice.ide.ProjectDocumentFrame projectDocumentFrame;
	private final org.lgna.croquet.MenuBarComposite menuBar;
}
