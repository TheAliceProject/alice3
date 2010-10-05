/*
 * Copyright (c) 2008-2010, Washington University in St. Louis. All rights reserved.
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
 * 3. Products derived from the software may not be called "Looking Glass", nor 
 *    may "Looking Glass" appear in their name, without prior written permission
 *    of Washington University in St. Louis.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Washington University in St. Louis"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  ANY AND ALL 
 * EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A PARTICULAR PURPOSE, 
 * TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHORS, 
 * COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 * INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters.heroic;

public class AgentAjunt extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryPerson { 
	public AgentAjunt() {
		super( "heroic/AgentAjunt" );
}
	public enum Part {
		RightFoot( "torso", "rightUpperLeg", "rightLowerLeg", "rightFoot" ),
		RightLowerLeg( "torso", "rightUpperLeg", "rightLowerLeg" ),
		RightUpperLeg( "torso", "rightUpperLeg" ),
		LeftFoot( "torso", "leftUpperLeg", "leftLowerLeg", "leftFoot" ),
		LeftLowerLeg( "torso", "leftUpperLeg", "leftLowerLeg" ),
		LeftUpperLeg( "torso", "leftUpperLeg" ),
		RightHand( "torso", "rightUpperArm", "rightLowerArm", "rightHand" ),
		RightLowerArm( "torso", "rightUpperArm", "rightLowerArm" ),
		RightUpperArm( "torso", "rightUpperArm" ),
		LeftHand( "torso", "leftUpperArm", "leftLowerArm", "leftHand" ),
		LeftLowerArm( "torso", "leftUpperArm", "leftLowerArm" ),
		LeftUpperArm( "torso", "leftUpperArm" ),
		Head( "torso", "head" ),
		Torso( "torso" );
		private String[] m_path;
		Part( String... path ) {
			m_path = path;
		}
		public String[] getPath() {
			return m_path;
		}
	}
	public edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel getPart( Part part ) {
		return getDescendant( edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel.class, part.getPath() );
	}

	public void JudoPunch( final edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel What) {
		this.walkTo( What, edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.IN_FRONT_OF, 0.1 );
		this.PrepareToFight(  );
		this.touch( What, org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.LEFT_HAND, 0.0, 0.25 );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					AgentAjunt.this.keepTouching( What, org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.LEFT_HAND, 0.0, 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					What.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 1.0, 0.5, What );
				}
			}
		);

		this.standUp( false );
	}

	public void JudoChop( final edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel What) {
		this.walkTo( What, edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.IN_FRONT_OF, 0.25 );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					AgentAjunt.this.getPart(AgentAjunt.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					AgentAjunt.this.getPart(AgentAjunt.Part.LeftLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					AgentAjunt.this.getPart(AgentAjunt.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.03), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					AgentAjunt.this.getPart(AgentAjunt.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					AgentAjunt.this.getPart(AgentAjunt.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.3), 0.5 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					AgentAjunt.this.standUp( false, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					AgentAjunt.this.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 0.2, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				AgentAjunt.this.delay(0.1);
				What.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 1.0, 0.5, What );
//			}
				}
			}
		);

	}

	public void JudoKick( final edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel What) {
		this.walkTo( What, edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.IN_FRONT_OF, 0.25 );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					AgentAjunt.this.getPart(AgentAjunt.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.22), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					AgentAjunt.this.getPart(AgentAjunt.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.22), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					AgentAjunt.this.getPart(AgentAjunt.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.07), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					AgentAjunt.this.getPart(AgentAjunt.Part.RightLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.03), 0.5 );
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					AgentAjunt.this.getPart(AgentAjunt.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.3), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					AgentAjunt.this.getPart(AgentAjunt.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.3), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					AgentAjunt.this.touch( What, org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_FOOT, 0.0, 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					AgentAjunt.this.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 0.2, 0.25 );
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				AgentAjunt.this.delay(0.15);
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							AgentAjunt.this.keepTouching( What, org.alice.apis.moveandturn.MoveDirection.FORWARD, edu.wustl.cse.lookingglass.apis.walkandtouch.TouchPart.RIGHT_FOOT, 0.0, 0.5 );
						}
					},
					new Runnable() {
						public void run() {
							What.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, 1.5, 0.5, What );
						}
					}
				);

//			}
				}
			}
		);

		this.standUp( false );
	}

	public void PrepareToFight( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					AgentAjunt.this.getPart(AgentAjunt.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					AgentAjunt.this.getPart(AgentAjunt.Part.RightLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					AgentAjunt.this.getPart(AgentAjunt.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.06), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					AgentAjunt.this.move( org.alice.apis.moveandturn.MoveDirection.DOWN, 0.05, 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					AgentAjunt.this.getPart(AgentAjunt.Part.Torso).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					AgentAjunt.this.getPart(AgentAjunt.Part.RightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					AgentAjunt.this.getPart(AgentAjunt.Part.LeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.03), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					AgentAjunt.this.getPart(AgentAjunt.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					AgentAjunt.this.getPart(AgentAjunt.Part.RightUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					AgentAjunt.this.getPart(AgentAjunt.Part.RightLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.3), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					AgentAjunt.this.getPart(AgentAjunt.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.15), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					AgentAjunt.this.getPart(AgentAjunt.Part.LeftUpperArm).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.06), 0.5 );
				}
			},
			new Runnable() {
				public void run() {
					AgentAjunt.this.getPart(AgentAjunt.Part.LeftLowerArm).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.35), 0.5 );
				}
			}
		);

	}
}
