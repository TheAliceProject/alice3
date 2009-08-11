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
package org.alice.ide.operations.help;

/**
 * @author Dennis Cosgrove
 */
public abstract class BrowserOperation extends org.alice.ide.operations.AbstractActionOperation {
	private String url;
	public BrowserOperation( String url ) {
		this.url = url;
	}
	protected abstract String getTitle(); 
	protected abstract int getMessageType();
//	protected abstract StringBuffer getMessage( StringBuffer sb ); 
	public final void perform( zoot.ActionContext actionContext ) {
//		edu.cmu.cs.dennisc.ui.html.HTMLPane htmlPane = new edu.cmu.cs.dennisc.ui.html.HTMLPane();
//		StringBuffer sb = new StringBuffer();
//		sb.append( "<html>" );
//		getMessage( sb );
//		sb.append( "</html>" );
//		htmlPane.setText( sb.toString() );
//		htmlPane.setOpaque( false );
//		javax.swing.JOptionPane.showMessageDialog( this.getIDE(), htmlPane, this.getTitle(), this.getMessageType() );
		edu.cmu.cs.dennisc.browser.BrowserProgressDialog dialog = new edu.cmu.cs.dennisc.browser.BrowserProgressDialog( this.getIDE(), this.url);
		dialog.createAndExecuteWorker();
		dialog.pack();
		dialog.setVisible( true );
		
		actionContext.put( org.alice.ide.IDE.IS_PROJECT_CHANGED_KEY, false );
		actionContext.commit();
	}
}
