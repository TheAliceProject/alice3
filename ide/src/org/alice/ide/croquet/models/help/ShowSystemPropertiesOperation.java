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
package org.alice.ide.croquet.models.help;

/**
 * @author Dennis Cosgrove
 */
public class ShowSystemPropertiesOperation extends org.alice.ide.operations.InconsequentialActionOperation {
	private static class SingletonHolder {
		private static ShowSystemPropertiesOperation instance = new ShowSystemPropertiesOperation();
	}
	public static ShowSystemPropertiesOperation getInstance() {
		return SingletonHolder.instance;
	}
	private ShowSystemPropertiesOperation() {
		super( java.util.UUID.fromString( "1f1ea35c-0d52-48c3-92fd-fa9f163e48a9" ) );
	}
	@Override
	protected void performInternal( org.lgna.croquet.steps.ActionOperationStep step ) {
		org.lgna.croquet.components.RowsSpringPanel formPane = new org.lgna.croquet.components.RowsSpringPanel( 8, 2 ) {
			private org.lgna.croquet.components.Component< ? >[] createComponentRowForSystemProperty( String name ) {
				String value = System.getProperty( name );
				assert value != null;
				return org.lgna.croquet.components.SpringUtilities.createRow( 
						org.lgna.croquet.components.SpringUtilities.createTrailingLabel( name+":" ),
						new org.lgna.croquet.components.Label( value ) 
				);
			}
			@Override
			protected java.util.List< org.lgna.croquet.components.Component< ? >[] > updateComponentRows( java.util.List< org.lgna.croquet.components.Component< ? >[] > rv ) {
				rv.add( createComponentRowForSystemProperty( "java.version" ) );
				rv.add( createComponentRowForSystemProperty( "os.name" ) );
				rv.add( createComponentRowForSystemProperty( "os.version" ) );
				rv.add( createComponentRowForSystemProperty( "os.arch" ) );
				rv.add( createComponentRowForSystemProperty( "sun.arch.data.model" ) );
				rv.add( org.lgna.croquet.components.SpringUtilities.createRow( org.lgna.croquet.components.BoxUtilities.createVerticalSliver( 8 ), null ) );
				ShowPathPropertyOperation[] showPathPropertyOperations = { 
						ShowClassPathPropertyOperation.getInstance(),
						ShowLibraryPathPropertyOperation.getInstance(),
				};
				for( ShowPathPropertyOperation showPathPropertyOperation : showPathPropertyOperations ) {
					String propertyName = showPathPropertyOperation.getPropertyName();				
					rv.add( org.lgna.croquet.components.SpringUtilities.createRow( 
							org.lgna.croquet.components.SpringUtilities.createTrailingLabel( propertyName+":" ), 
							showPathPropertyOperation.createHyperlink() 
					) );
				}
				rv.add( org.lgna.croquet.components.SpringUtilities.createRow( org.lgna.croquet.components.BoxUtilities.createVerticalSliver( 8 ), null ) );
				rv.add( org.lgna.croquet.components.SpringUtilities.createRow( null, ShowAllSystemPropertiesOperation.getInstance().createHyperlink() ) );
				return rv;
			}
		};
//		edu.cmu.cs.dennisc.croquet.swing.PageAxisPane pane = new edu.cmu.cs.dennisc.croquet.swing.PageAxisPane(
//			formPane,
//			javax.swing.Box.createVerticalStrut( 16 ),
//			new javax.swing.JButton( new AllSystemPropertiesOperation() ),
//			javax.swing.Box.createVerticalStrut( 8 )
//		);
		this.getIDE().showMessageDialog( formPane, "System Properties", org.lgna.croquet.MessageType.INFORMATION ); 
	}
}
