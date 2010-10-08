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
package edu.cmu.cs.dennisc.alice.ui;

/**
 * @author Dennis Cosgrove
 */
public class StringPropertyView extends edu.cmu.cs.dennisc.alan.TextLabel {
	class StringPropertyChangeAdapter implements edu.cmu.cs.dennisc.property.event.PropertyListener {
		public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
		}
		public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			StringPropertyView.this.updateText();
		}
	}
	private edu.cmu.cs.dennisc.property.StringProperty m_stringProperty;
	private StringPropertyChangeAdapter m_propertyChangeAdapter;
//	public StringPropertyView() {
//	}
	public StringPropertyView( edu.cmu.cs.dennisc.property.StringProperty stringProperty ) {
		m_stringProperty = stringProperty;
		this.addHierarchyListener( new edu.cmu.cs.dennisc.alan.event.HierarchyListener() {
			public void hierarchyChanging( edu.cmu.cs.dennisc.alan.event.HierarchyEvent e ) {
			}
			public void hierarchyChanged( edu.cmu.cs.dennisc.alan.event.HierarchyEvent e ) {
				//todo: remove listener when taken out of the tree
				if( StringPropertyView.this.m_propertyChangeAdapter != null ) {
					//pass
				} else {
					StringPropertyView.this.m_propertyChangeAdapter = new StringPropertyChangeAdapter();
					StringPropertyView.this.m_stringProperty.addPropertyListener( m_propertyChangeAdapter );
				}
			}			
		} );
		updateText();
	}
	private void updateText() {
		setText( m_stringProperty.getValue() );
	}
}
