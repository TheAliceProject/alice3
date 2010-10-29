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
package edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters.pets;

public class BlackCat extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryCharacter { 
	public BlackCat() {
		super( "pets/BlackCat" );
}
	public enum Part {
		RightEye( "head", "rightEye" ),
		LeftEye( "head", "leftEye" ),
		Head( "head" ),
		RearRightLowerLeg( "rearRightUpperLeg", "rearRightLowerLeg" ),
		RearRightUpperLeg( "rearRightUpperLeg" ),
		FrontRightLowerLeg( "frontRightUpperLeg", "frontRightLowerLeg" ),
		FrontRightUpperLeg( "frontRightUpperLeg" ),
		RearLeftLowerLeg( "rearLeftUpperLeg", "rearLeftLowerLeg" ),
		RearLeftUpperLeg( "rearLeftUpperLeg" ),
		FrontLeftLowerLeg( "frontLeftUpperLeg", "frontLeftLowerLeg" ),
		FrontLeftUpperLeg( "frontLeftUpperLeg" ),
		Tail( "tail" );
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

//	public void Purr( ) {
//		org.alice.virtualmachine.DoTogether.invokeAndWait(
////			new Runnable() {
////				public void run() {
////					BlackCat.this.WagTail( 3.0 );
////				}
////			},
//			new Runnable() {
//				public void run() {
//					//BlackCat.this.playSound(sound)
//				}
//			}
//		);
//
//	}

	public void RubAgainst( final edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel What) {
		this.WalkTo( What );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
//			new Runnable() {
//				public void run() {
//					BlackCat.this.Purr(  );
//				}
//			},
			new Runnable() {
				public void run() {
					for (int index1 = 0; index1 < 4; index1 = index1 + 1) {
				// DoInOrder { 
					BlackCat.this.getPart(BlackCat.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
					BlackCat.this.getPart(BlackCat.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
//				}

			}
				}
			}
		);

	}

	public void Swat( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					// DoInOrder { 
				BlackCat.this.delay(0.1);
				//BlackCat.this.playSound(sound)
//			}
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				BlackCat.this.getPart(BlackCat.Part.FrontRightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.1 );
				BlackCat.this.getPart(BlackCat.Part.FrontRightLowerLeg).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.1 );
				BlackCat.this.getPart(BlackCat.Part.FrontRightLowerLeg).roll( org.alice.apis.moveandturn.RollDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.1 );
				BlackCat.this.getPart(BlackCat.Part.FrontRightLowerLeg).roll( org.alice.apis.moveandturn.RollDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.1), 0.1 );
				BlackCat.this.getPart(BlackCat.Part.FrontRightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.2), 0.1 );
//			}
				}
			}
		);

	}

	public void Walk( final Number Distance) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					BlackCat.this.move( org.alice.apis.moveandturn.MoveDirection.FORWARD, Distance.doubleValue(), Distance.doubleValue() );
				}
			},
			new Runnable() {
				public void run() {
					for (int index2 = 0; index2 < Distance.intValue(); index2 = index2 + 1) {
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							// DoInOrder { 
						org.alice.virtualmachine.DoTogether.invokeAndWait(
							new Runnable() {
								public void run() {
									BlackCat.this.getPart(BlackCat.Part.RearRightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.35 );
								}
							},
							new Runnable() {
								public void run() {
									// DoInOrder { 
								BlackCat.this.delay(0.1);
								BlackCat.this.getPart(BlackCat.Part.RearRightLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
//							}
								}
							}
						);

						org.alice.virtualmachine.DoTogether.invokeAndWait(
							new Runnable() {
								public void run() {
									BlackCat.this.getPart(BlackCat.Part.RearRightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.35 );
								}
							},
							new Runnable() {
								public void run() {
									// DoInOrder { 
								BlackCat.this.delay(0.1);
								BlackCat.this.getPart(BlackCat.Part.RearRightLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
//							}
								}
							}
						);

//					}
						}
					},
					new Runnable() {
						public void run() {
							// DoInOrder { 
						org.alice.virtualmachine.DoTogether.invokeAndWait(
							new Runnable() {
								public void run() {
									BlackCat.this.getPart(BlackCat.Part.FrontLeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.35 );
								}
							},
							new Runnable() {
								public void run() {
									// DoInOrder { 
								BlackCat.this.delay(0.1);
								BlackCat.this.getPart(BlackCat.Part.FrontLeftLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
//							}
								}
							}
						);

						org.alice.virtualmachine.DoTogether.invokeAndWait(
							new Runnable() {
								public void run() {
									BlackCat.this.getPart(BlackCat.Part.FrontLeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.35 );
								}
							},
							new Runnable() {
								public void run() {
									// DoInOrder { 
								BlackCat.this.delay(0.1);
								BlackCat.this.getPart(BlackCat.Part.FrontLeftLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
//							}
								}
							}
						);

//					}
						}
					}
				);

			}
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				BlackCat.this.delay(0.4);
				for (int index3 = 0; index3 < Distance.intValue(); index3 = index3 + 1) {
					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								// DoInOrder { 
							org.alice.virtualmachine.DoTogether.invokeAndWait(
								new Runnable() {
									public void run() {
										BlackCat.this.getPart(BlackCat.Part.FrontRightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.35 );
									}
								},
								new Runnable() {
									public void run() {
										// DoInOrder { 
									BlackCat.this.delay(0.1);
									BlackCat.this.getPart(BlackCat.Part.FrontRightLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
//								}
									}
								}
							);

							org.alice.virtualmachine.DoTogether.invokeAndWait(
								new Runnable() {
									public void run() {
										BlackCat.this.getPart(BlackCat.Part.FrontRightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.35 );
									}
								},
								new Runnable() {
									public void run() {
										// DoInOrder { 
									BlackCat.this.delay(0.1);
									BlackCat.this.getPart(BlackCat.Part.FrontRightLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
//								}
									}
								}
							);

//						}
							}
						},
						new Runnable() {
							public void run() {
								// DoInOrder { 
							org.alice.virtualmachine.DoTogether.invokeAndWait(
								new Runnable() {
									public void run() {
										BlackCat.this.getPart(BlackCat.Part.RearLeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.35 );
									}
								},
								new Runnable() {
									public void run() {
										// DoInOrder { 
									BlackCat.this.delay(0.1);
									BlackCat.this.getPart(BlackCat.Part.RearLeftLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
//								}
									}
								}
							);

							org.alice.virtualmachine.DoTogether.invokeAndWait(
								new Runnable() {
									public void run() {
										BlackCat.this.getPart(BlackCat.Part.RearLeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.35 );
									}
								},
								new Runnable() {
									public void run() {
										// DoInOrder { 
									BlackCat.this.delay(0.1);
									BlackCat.this.getPart(BlackCat.Part.RearLeftLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
//								}
									}
								}
							);

//						}
							}
						}
					);

				}

//			}
				}
			}
		);

	}

	public void WalkTo( final edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel What) {
		 final edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean> Walking= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Boolean>( true );
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					// DoInOrder { 
				BlackCat.this.moveTo( What, edu.wustl.cse.lookingglass.apis.walkandtouch.SpatialRelation.IN_FRONT_OF, 1.0, (BlackCat.this.getDistanceTo(What) + 0.4) );
				Walking.value = false;
//			}
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				BlackCat.this.delay(0.4);
				while ( Walking.value) {
					org.alice.virtualmachine.DoTogether.invokeAndWait(
						new Runnable() {
							public void run() {
								// DoInOrder { 
							org.alice.virtualmachine.DoTogether.invokeAndWait(
								new Runnable() {
									public void run() {
										BlackCat.this.getPart(BlackCat.Part.FrontRightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.35 );
									}
								},
								new Runnable() {
									public void run() {
										// DoInOrder { 
									BlackCat.this.delay(0.1);
									BlackCat.this.getPart(BlackCat.Part.FrontRightLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
//								}
									}
								}
							);

							org.alice.virtualmachine.DoTogether.invokeAndWait(
								new Runnable() {
									public void run() {
										BlackCat.this.getPart(BlackCat.Part.FrontRightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.35 );
									}
								},
								new Runnable() {
									public void run() {
										// DoInOrder { 
									BlackCat.this.delay(0.1);
									BlackCat.this.getPart(BlackCat.Part.FrontRightLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
//								}
									}
								}
							);

//						}
							}
						},
						new Runnable() {
							public void run() {
								// DoInOrder { 
							org.alice.virtualmachine.DoTogether.invokeAndWait(
								new Runnable() {
									public void run() {
										BlackCat.this.getPart(BlackCat.Part.RearLeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.35 );
									}
								},
								new Runnable() {
									public void run() {
										// DoInOrder { 
									BlackCat.this.delay(0.1);
									BlackCat.this.getPart(BlackCat.Part.RearLeftLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
//								}
									}
								}
							);

							org.alice.virtualmachine.DoTogether.invokeAndWait(
								new Runnable() {
									public void run() {
										BlackCat.this.getPart(BlackCat.Part.RearLeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.35 );
									}
								},
								new Runnable() {
									public void run() {
										// DoInOrder { 
									BlackCat.this.delay(0.1);
									BlackCat.this.getPart(BlackCat.Part.RearLeftLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
//								}
									}
								}
							);

//						}
							}
						}
					);

				}

//			}
				}
			},
			new Runnable() {
				public void run() {
					while ( Walking.value) {
				org.alice.virtualmachine.DoTogether.invokeAndWait(
					new Runnable() {
						public void run() {
							// DoInOrder { 
						org.alice.virtualmachine.DoTogether.invokeAndWait(
							new Runnable() {
								public void run() {
									BlackCat.this.getPart(BlackCat.Part.RearRightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.35 );
								}
							},
							new Runnable() {
								public void run() {
									// DoInOrder { 
								BlackCat.this.delay(0.1);
								BlackCat.this.getPart(BlackCat.Part.RearRightLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
//							}
								}
							}
						);

						org.alice.virtualmachine.DoTogether.invokeAndWait(
							new Runnable() {
								public void run() {
									BlackCat.this.getPart(BlackCat.Part.RearRightUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.35 );
								}
							},
							new Runnable() {
								public void run() {
									// DoInOrder { 
								BlackCat.this.delay(0.1);
								BlackCat.this.getPart(BlackCat.Part.RearRightLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
//							}
								}
							}
						);

//					}
						}
					},
					new Runnable() {
						public void run() {
							// DoInOrder { 
						org.alice.virtualmachine.DoTogether.invokeAndWait(
							new Runnable() {
								public void run() {
									BlackCat.this.getPart(BlackCat.Part.FrontLeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.35 );
								}
							},
							new Runnable() {
								public void run() {
									// DoInOrder { 
								BlackCat.this.delay(0.1);
								BlackCat.this.getPart(BlackCat.Part.FrontLeftLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
//							}
								}
							}
						);

						org.alice.virtualmachine.DoTogether.invokeAndWait(
							new Runnable() {
								public void run() {
									BlackCat.this.getPart(BlackCat.Part.FrontLeftUpperLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.35 );
								}
							},
							new Runnable() {
								public void run() {
									// DoInOrder { 
								BlackCat.this.delay(0.1);
								BlackCat.this.getPart(BlackCat.Part.FrontLeftLowerLeg).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.25 );
//							}
								}
							}
						);

//					}
						}
					}
				);

			}
				}
			}
		);

	}

	public void Meow( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					//BlackCat.this.playSound(sound)
				}
			},
			new Runnable() {
				public void run() {
					// DoInOrder { 
				BlackCat.this.getPart(BlackCat.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
				BlackCat.this.getPart(BlackCat.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05), 0.5 );
//			}
				}
			}
		);

	}

	public void Growl( ) {
		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					BlackCat.this.getPart(BlackCat.Part.Tail).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					BlackCat.this.getPart(BlackCat.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
				}
			},
			new Runnable() {
				public void run() {
					//BlackCat.this.playSound(sound)
				}
			}
		);

		org.alice.virtualmachine.DoTogether.invokeAndWait(
			new Runnable() {
				public void run() {
					BlackCat.this.getPart(BlackCat.Part.Tail).turn( org.alice.apis.moveandturn.TurnDirection.BACKWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.1) );
				}
			},
			new Runnable() {
				public void run() {
					BlackCat.this.getPart(BlackCat.Part.Head).turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions(0.05) );
				}
			}
		);

	}
}
