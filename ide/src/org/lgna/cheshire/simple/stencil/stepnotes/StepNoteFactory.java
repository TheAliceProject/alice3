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

package org.lgna.cheshire.simple.stencil.stepnotes;

/**
 * @author Dennis Cosgrove
 */
public class StepNoteFactory {
	private static final String PACKAGE_NAME = StepNoteFactory.class.getPackage().getName();

	private StepNoteFactory() {
		throw new AssertionError();
	}

	public static StepNote<?> createNote( org.lgna.croquet.history.Step<?> step ) {
		org.lgna.croquet.triggers.Trigger trigger = step.getTrigger();
		if( trigger instanceof org.lgna.croquet.triggers.DropTrigger ) {
			org.lgna.croquet.triggers.DropTrigger dropTrigger = (org.lgna.croquet.triggers.DropTrigger)trigger;
			if( step instanceof org.lgna.croquet.history.PrepStep<?> ) {
				org.lgna.croquet.history.PrepStep<?> prepStep = (org.lgna.croquet.history.PrepStep<?>)step;
				return new DropPrepNote( prepStep );
			} else if( step instanceof org.lgna.croquet.history.CompletionStep<?> ) {
				org.lgna.croquet.history.CompletionStep<?> completionStep = (org.lgna.croquet.history.CompletionStep<?>)step;
				return new DropCompletionNote( completionStep );
			} else {
				throw new RuntimeException();
			}
		} else {
			Class<?> stepCls = step.getClass();
			String stepClsName;
			stepClsName = stepCls.getSimpleName();
			stepClsName = stepClsName.substring( 0, stepClsName.length() - 4 ) + "Note";
			if( step instanceof org.lgna.croquet.history.CompletionStep ) {
				org.lgna.croquet.history.CompletionStep<org.lgna.croquet.CompletionModel> completionStep = (org.lgna.croquet.history.CompletionStep)step;
				//				if( completionStep.isCanceled() ) {
				//					stepClsName = "CancelCompletionNote";
				//				} else {
				org.lgna.croquet.CompletionModel model = completionStep.getModel();
				if( model != null ) {
					if( model instanceof org.lgna.croquet.ActionOperation ) {
						stepClsName = "ActionOperationNote";
					} else if( model instanceof org.lgna.croquet.InputDialogOperation ) {
						stepClsName = "InputDialogOperationNote";
					} else if( model instanceof org.lgna.croquet.PlainDialogOperation ) {
						stepClsName = "PlainDialogOperationNote";
					} else if( model instanceof org.lgna.croquet.PlainDialogOperation.InternalCloseOperation ) {
						stepClsName = "PlainDialogCloseOperationNote";
					} else if( model instanceof org.lgna.croquet.SerialOperation ) {
						stepClsName = "SerialOperationNote";
					} else if( model instanceof org.lgna.croquet.Cascade ) {
						stepClsName = "CascadeCompletionNote";
					} else if( model instanceof org.lgna.croquet.State ) {
						stepClsName = "StateChangeNote";
					} else {
						edu.cmu.cs.dennisc.java.util.logging.Logger.severe( model );
					}
				} else {
					stepClsName = "CancelCompletionNote";
				}
				//				}
			}
			String noteClsName = PACKAGE_NAME + "." + stepClsName;
			try {
				Class<? extends StepNote<?>> noteCls = (Class<? extends StepNote<?>>)edu.cmu.cs.dennisc.java.lang.ClassUtilities.forName( noteClsName );
				return edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newInstance( noteCls, new Class<?>[] { stepCls }, step );
			} catch( ClassNotFoundException cnfe ) {
				throw new RuntimeException( noteClsName, cnfe );
			}
		}
	}
}
