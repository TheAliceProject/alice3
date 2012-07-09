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

package org.lgna.cheshire.task;

/**
 * @author Dennis Cosgrove
 */
public class TaskComboBoxModel extends edu.cmu.cs.dennisc.javax.swing.models.AbstractListModel<Task> implements javax.swing.ComboBoxModel {
	private final java.util.List<Task> tasks = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	private int index = -1;
	public TaskComboBoxModel() {
		org.lgna.cheshire.test.TransactionHistoryGeneratorTest test = org.lgna.cheshire.test.TransactionHistoryGeneratorTest.getColorCrazyGenerator();
		test.generate( test.getProject() );
		org.lgna.croquet.history.TransactionHistory history = test.getReuseTransactionHistory();
		for( org.lgna.croquet.history.Transaction transaction : history ) {
			tasks.add( new Task( transaction ) );
		}
		this.index = 0;
//		
//
//		try {
//			org.lgna.project.Project project = org.lgna.project.io.IoUtilities.readProject( org.lgna.cheshire.test.TransactionHistoryGeneratorTest.TEMPORARY_HACK_lastGeneratedProjectFile );
//			org.alice.ide.ProjectStack.pushProject( project );
//			try {
//				org.lgna.croquet.history.TransactionHistory history = edu.cmu.cs.dennisc.codec.CodecUtilities.decodeBinary( org.lgna.cheshire.test.TransactionHistoryGeneratorTest.TEMPORARY_HACK_lastGeneratedTransactionHistoryFile, org.lgna.croquet.history.TransactionHistory.class );
//				for( org.lgna.croquet.history.Transaction transaction : history ) {
//					tasks.add( new Task( transaction ) );
//				}
//				this.index = 0;
//			} finally {
//				org.alice.ide.ProjectStack.popAndCheckProject( project );
//			}
//		} catch( java.io.IOException ioe ) {
//			throw new RuntimeException( ioe );
//		} catch( org.lgna.project.VersionNotSupportedException vnse ) {
//			throw new RuntimeException( vnse );
//		}
	}
	public void insertRecoveryTransaction( org.lgna.croquet.history.Transaction transaction ) {
		Task currentTask = this.getSelectedItem();
		currentTask.insertRecoveryTransaction( transaction );
	}
	
	public Task getElementAt(int index) {
		return this.tasks.get( index );
	}
	public int getSize() {
		return this.tasks.size();
	}

	public Task getSelectedItem() {
		if( this.index >= 0 ) {
			return this.tasks.get( this.index );
		} else {
			return null;
		}
	}
	public void setSelectedItem(Object item) {
		assert item == null || item instanceof Task : item;
		this.index = this.tasks.indexOf( item );
	}
}
