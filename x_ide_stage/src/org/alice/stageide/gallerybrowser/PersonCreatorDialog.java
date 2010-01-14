/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.stageide.gallerybrowser;

/**
 * @author Dennis Cosgrove
 */
class PersonCreatorDialog extends edu.cmu.cs.dennisc.progress.ProgressDialog {
	private org.alice.apis.stage.Person person;

	public PersonCreatorDialog( javax.swing.JDialog owner ) {
		super( owner );
	}

	public PersonCreatorDialog( javax.swing.JFrame owner ) {
		super( owner );
	}

	@Override
	protected edu.cmu.cs.dennisc.progress.ProgressDialog.Worker createWorker() {
		class PersonCreatorWorker extends edu.cmu.cs.dennisc.progress.ProgressDialog.Worker {
			@Override
			protected Boolean doInBackground() throws Exception {
				this.publish( "opening person creator..." );
				org.alice.stageide.personeditor.PersonEditorInputPane personEditorInputPane = new org.alice.stageide.personeditor.PersonEditorInputPane( null ) {
					@Override
					public void addNotify() {
						super.addNotify();
						PersonCreatorDialog.this.setVisible( false );
					}
				};

				org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
				PersonCreatorDialog.this.person = personEditorInputPane.showInJDialog( ide );
				return true;
			}
		}
		return new PersonCreatorWorker();
	}

	@Override
	protected void handleDone( Boolean result ) {
		if( result && this.person != null ) {
			CreatePersonActionOperation createPersonActionOperation = new CreatePersonActionOperation( this.person );
			edu.cmu.cs.dennisc.zoot.ZManager.performIfAppropriate( createPersonActionOperation, null, true );
			this.setVisible( false );
		}
	}

	@Override
	protected boolean isProgressBarDesired() {
		return false;
	}
}
