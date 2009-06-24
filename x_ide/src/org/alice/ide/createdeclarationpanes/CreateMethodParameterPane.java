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
package org.alice.ide.createdeclarationpanes;


/**
 * @author Dennis Cosgrove
 */
public class CreateMethodParameterPane extends CreateParameterPane {
	class UnderstandingConfirmationOperation extends org.alice.ide.operations.AbstractBooleanStateOperation {
		public UnderstandingConfirmationOperation( String codeText ) {
			super( false );
			this.putValue( javax.swing.Action.NAME, "I understand that I need to update the invocations to this " + codeText + "." );
		}
		public void performStateChange( zoot.BooleanStateContext booleanStateContext ) {
			CreateMethodParameterPane.this.updateOKButton();
			booleanStateContext.put( org.alice.ide.IDE.IS_PROJECT_CHANGED_KEY, false );
			booleanStateContext.commit();
		}
	}

	private javax.swing.JCheckBox checkBox;
	private java.util.List< edu.cmu.cs.dennisc.alice.ast.MethodInvocation > methodInvocations;
	public CreateMethodParameterPane( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method, java.util.List< edu.cmu.cs.dennisc.alice.ast.MethodInvocation > methodInvocations ) {
		super( method );
		this.methodInvocations = methodInvocations;
		
	}
	@Override
	protected java.awt.Component[] createWarningRow() {
		final int N = this.methodInvocations.size();
		if( N > 0 ) {

			String codeText;
//			if( code instanceof edu.cmu.cs.dennisc.alice.ast.AbstractMethod ) {
				edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = (edu.cmu.cs.dennisc.alice.ast.AbstractMethod)this.getCode();
				if( method.isProcedure() ) {
					codeText = "procedure";
				} else {
					codeText = "function";
				}
//			} else {
//				codeText = "constructor";
//			}
			this.checkBox = new zoot.ZCheckBox( new UnderstandingConfirmationOperation( codeText ) );
			this.checkBox.setOpaque( false );
			
			StringBuffer sb = new StringBuffer();
			sb.append( "<html><body>There " );
			if( N == 1 ) {
				sb.append( "is 1 invocation" );
			} else {
				sb.append( "are " );
				sb.append( N );
				sb.append( " invocations" );
			}
			sb.append( " to this " );
			sb.append( codeText );
			sb.append( " in your program.<br>You will need to fill in " );
			if( N == 1 ) {
				sb.append( "a value" );
			} else {
				sb.append( "values" );
			}
			sb.append( " for the new " );
			if( N == 1 ) {
				sb.append( "argument at the" );
			} else {
				sb.append( "arguments at each" );
			}
			sb.append( " invocation.</body></html>" );

			swing.PageAxisPane pane = new swing.PageAxisPane();
			pane.add( zoot.ZLabel.acquire( sb.toString() ) );
			pane.add( javax.swing.Box.createVerticalStrut( 8 ) );
			pane.add( new swing.LineAxisPane( zoot.ZLabel.acquire( "Tip: look for " ), org.alice.ide.IDE.getSingleton().getPreviewFactory().createExpressionPane( new edu.cmu.cs.dennisc.alice.ast.NullLiteral() ) ) );
			pane.add( javax.swing.Box.createVerticalStrut( 8 ) );
			pane.add( this.checkBox );
			return new java.awt.Component[] { zoot.ZLabel.acquire( "WARNING:" ), pane };
		} else {
			this.checkBox = null;
			return null;
		}
	}
	@Override
	public boolean isOKButtonValid() {
		return super.isOKButtonValid() && ( this.checkBox == null || this.checkBox.isSelected() );
	}
}
