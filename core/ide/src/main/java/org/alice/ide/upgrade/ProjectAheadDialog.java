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
package org.alice.ide.upgrade;

import edu.cmu.cs.dennisc.javax.swing.UIManagerUtilities;
import org.alice.ide.upgrade.views.ProjectAheadView;
import org.lgna.croquet.Application;
import org.lgna.croquet.SimpleOperationInputDialogCoreComposite;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.CompletionStep;
import org.lgna.croquet.simple.SimpleApplication;
import org.lgna.croquet.views.Panel;
import org.lgna.project.ProjectVersion;
import org.lgna.project.Version;

import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class ProjectAheadDialog extends SimpleOperationInputDialogCoreComposite<Panel> {
	private final Version projectVersion;

	public ProjectAheadDialog( Version projectVersion ) {
		super( UUID.fromString( "51405f1e-777c-4622-aa47-a5d65e87ea90" ), Application.INFORMATION_GROUP );
		this.projectVersion = projectVersion;
	}

	public Version getProjectVersion() {
		return this.projectVersion;
	}

	public Version getApplicationVersion() {
		return ProjectVersion.getCurrentVersion();
	}

	@Override
	protected Status getStatusPreRejectorCheck( CompletionStep<?> step ) {
		return IS_GOOD_TO_GO_STATUS;
	}

	@Override
	protected Edit createEdit( CompletionStep<?> completionStep ) {
		return null;
	}

	@Override
	protected Panel createView() {
		return new ProjectAheadView( this );
	}

	public static void main( String[] args ) throws Exception {
		UIManagerUtilities.setLookAndFeel( "Nimbus" );
		new SimpleApplication();
		new ProjectAheadDialog( new Version( "3.1.112358.0.0" ) ).getLaunchOperation().fire();
		System.exit( 0 );
	}
}
