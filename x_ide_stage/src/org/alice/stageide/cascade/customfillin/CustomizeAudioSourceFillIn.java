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
package org.alice.stageide.cascade.customfillin;

/**
 * @author Dennis Cosgrove
 */
public class CustomizeAudioSourceFillIn extends org.alice.ide.cascade.customfillin.CustomFillIn<edu.cmu.cs.dennisc.alice.ast.InstanceCreation, org.alice.apis.moveandturn.AudioSource> {
	@Override
	protected String getMenuProxyText() {
		return "Customize Audio Source...";
	}
	@Override
	protected org.alice.ide.choosers.ValueChooser createCustomPane() {
		return new org.alice.stageide.choosers.AudioSourceChooser();
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.InstanceCreation createExpression( org.alice.apis.moveandturn.AudioSource value ) {
		org.alice.virtualmachine.resources.AudioResource audioResource = value.getAudioResource();

		org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
		edu.cmu.cs.dennisc.alice.Project project = ide.getProject();
		if( project != null ) {
			project.addResource( audioResource );
		}

		edu.cmu.cs.dennisc.alice.ast.ResourceExpression resourceExpression = new edu.cmu.cs.dennisc.alice.ast.ResourceExpression( org.alice.virtualmachine.resources.AudioResource.class, audioResource );

		double volume = value.getVolume();
		boolean isNotDefaultVolume = edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( volume, org.alice.apis.moveandturn.AudioSource.DEFAULT_VOLUME ) == false;
		double startTime = value.getStartTime();
		boolean isNotDefaultStartTime = edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( startTime, org.alice.apis.moveandturn.AudioSource.DEFAULT_START_TIME ) == false;
		double stopTime = value.getStopTime();
		boolean isNotDefaultStopTime = Double.isNaN( stopTime ) == false;

		if( isNotDefaultVolume || isNotDefaultStartTime || isNotDefaultStopTime ) {
			edu.cmu.cs.dennisc.alice.ast.DoubleLiteral volumeLiteral = new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( volume );
			if( isNotDefaultStartTime || isNotDefaultStopTime ) {
				edu.cmu.cs.dennisc.alice.ast.DoubleLiteral startTimeLiteral = new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( startTime );
				if( isNotDefaultStopTime ) {
					edu.cmu.cs.dennisc.alice.ast.DoubleLiteral stopTimeLiteral = new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( stopTime );

					edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava constructor = edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava.get( 
							org.alice.apis.moveandturn.AudioSource.class, 
							org.alice.virtualmachine.resources.AudioResource.class,
							Number.class, 
							Number.class, 
							Number.class );
					return org.alice.ide.ast.NodeUtilities.createInstanceCreation( constructor, resourceExpression, volumeLiteral, startTimeLiteral, stopTimeLiteral );
				} else {
					edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava constructor = edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava.get( 
							org.alice.apis.moveandturn.AudioSource.class, 
							org.alice.virtualmachine.resources.AudioResource.class,
							Number.class, 
							Number.class );
					return org.alice.ide.ast.NodeUtilities.createInstanceCreation( constructor, resourceExpression, volumeLiteral, startTimeLiteral );
				}
			} else {
				edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava constructor = edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava.get( 
						org.alice.apis.moveandturn.AudioSource.class, 
						org.alice.virtualmachine.resources.AudioResource.class,
						Number.class );
				return org.alice.ide.ast.NodeUtilities.createInstanceCreation( constructor, resourceExpression, volumeLiteral );
			}
		} else {
			edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava constructor = edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava.get( 
					org.alice.apis.moveandturn.AudioSource.class, 
					org.alice.virtualmachine.resources.AudioResource.class );
			return org.alice.ide.ast.NodeUtilities.createInstanceCreation( constructor, resourceExpression );
		}
	}
}
