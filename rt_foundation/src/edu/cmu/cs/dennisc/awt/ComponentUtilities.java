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
package edu.cmu.cs.dennisc.awt;


/**
 * @author Dennis Cosgrove
 */
public class ComponentUtilities {
	public static final edu.cmu.cs.dennisc.pattern.HowMuch DEFAULT_HOW_MUCH = edu.cmu.cs.dennisc.pattern.HowMuch.COMPONENT_AND_DESCENDANTS;
	private static <E extends java.awt.Component> E getFirstToAccept( boolean isComponentACandidate, boolean isChildACandidate, boolean isGrandchildAndBeyondACandidate, java.awt.Component component, Class< E > cls, edu.cmu.cs.dennisc.pattern.Criterion< ? >... criterions ) {
		assert component != null;
		E rv = null;
		boolean isAcceptedByAll;
		if( isComponentACandidate ) {
			if( cls == null || cls.isAssignableFrom( component.getClass() ) ) {
				isAcceptedByAll = true;
				if( criterions == null ) {
					//pass
				} else {
					for( edu.cmu.cs.dennisc.pattern.Criterion criterion : criterions ) {
						if( criterion.accept( component ) ) {
							//pass
						} else {
							isAcceptedByAll = false;
							break;
						}
					}
				}
			} else {
				isAcceptedByAll = false;
			}
		} else {
			isAcceptedByAll = false;
		}
		if( isAcceptedByAll ) {
			rv = (E)component;
		} else {
			if( isChildACandidate ) {
				if( component instanceof java.awt.Container ) {
					for( java.awt.Component componentI : ((java.awt.Container)component).getComponents() ) {
						rv = getFirstToAccept( isChildACandidate, isGrandchildAndBeyondACandidate, isGrandchildAndBeyondACandidate, componentI, cls, criterions );
						if( rv != null ) {
							break;
						}
					}
				}
			}
		}
		return rv;
	}

	private static <E extends java.awt.Component> void updateAllToAccept( boolean isComponentACandidate, boolean isChildACandidate, boolean isGrandchildAndBeyondACandidate, java.util.List< E > list, java.awt.Component component, Class< E > cls, edu.cmu.cs.dennisc.pattern.Criterion< ? >... criterions ) {
		assert component != null;

		if( isComponentACandidate ) {
			if( cls == null || cls.isAssignableFrom( component.getClass() ) ) {
				boolean isAcceptedByAll = true;
				if( criterions == null ) {
					//pass
				} else {
					for( edu.cmu.cs.dennisc.pattern.Criterion criterion : criterions ) {
						if( criterion.accept( component ) ) {
							//pass
						} else {
							isAcceptedByAll = false;
							break;
						}
					}
				}
				if( isAcceptedByAll ) {
					list.add( (E)component );
				}
			}
		}

		if( isChildACandidate ) {
			if( component instanceof java.awt.Container ) {
				for( java.awt.Component componentI : ((java.awt.Container)component).getComponents() ) {
					updateAllToAccept( isChildACandidate, isGrandchildAndBeyondACandidate, isGrandchildAndBeyondACandidate, list, componentI, cls, criterions );
				}
			}
		}
	}
	private static <E extends java.awt.Component> E getFirstToAccept( edu.cmu.cs.dennisc.pattern.HowMuch candidateMask, java.awt.Component component, Class< E > cls, edu.cmu.cs.dennisc.pattern.Criterion< ? >... criterions ) {
		return getFirstToAccept( candidateMask.isComponentACandidate(), candidateMask.isChildACandidate(), candidateMask.isGrandchildAndBeyondACandidate(), component, cls, criterions );
	}

	private static <E extends java.awt.Component> void updateAllToAccept( edu.cmu.cs.dennisc.pattern.HowMuch candidateMask, java.util.List< E > list, java.awt.Component component, Class< E > cls, edu.cmu.cs.dennisc.pattern.Criterion< ? >... criterions ) {
		updateAllToAccept( candidateMask.isComponentACandidate(), candidateMask.isChildACandidate(), candidateMask.isGrandchildAndBeyondACandidate(), list, component, cls, criterions );
	}
	
	public static <E extends java.awt.Component> E findFirstMatch( java.awt.Component component, edu.cmu.cs.dennisc.pattern.HowMuch howMuch, Class< E > cls, edu.cmu.cs.dennisc.pattern.Criterion< ? >... criterions ) {
		return ComponentUtilities.getFirstToAccept( howMuch, component, cls, criterions );
	}
	public static <E extends java.awt.Component> E findFirstMatch( java.awt.Component component, Class< E > cls, edu.cmu.cs.dennisc.pattern.Criterion< ? >... criterions ) {
		return findFirstMatch( component, DEFAULT_HOW_MUCH, cls, criterions );
	}
	public static <E extends java.awt.Component> E findFirstMatch( java.awt.Component component, Class< E > cls ) {
		return findFirstMatch( component, cls, (edu.cmu.cs.dennisc.pattern.Criterion< ? >[])null );
	}
	public static java.awt.Component findFirstMatch( java.awt.Component component, edu.cmu.cs.dennisc.pattern.Criterion< ? >... criterions ) {
		return findFirstMatch( component, null, criterions );
	}
	
	public static <E extends java.awt.Component> java.util.List< E > findAllMatches( java.awt.Component component, edu.cmu.cs.dennisc.pattern.HowMuch howMuch, Class< E > cls, edu.cmu.cs.dennisc.pattern.Criterion< ? >... criterions ) {
		java.util.List< E > list = new java.util.LinkedList< E >();
		ComponentUtilities.updateAllToAccept( howMuch, list, component, cls, criterions );
		return list;
	}
	public static <E extends java.awt.Component> java.util.List< E > findAllMatches( java.awt.Component component, Class< E > cls, edu.cmu.cs.dennisc.pattern.Criterion< ? >... criterions ) {
		return findAllMatches( component, DEFAULT_HOW_MUCH, cls, criterions );
	}
	public static <E extends java.awt.Component> java.util.List< E > findAllMatches( java.awt.Component component, Class< E > cls ) {
		return findAllMatches( component, cls, (edu.cmu.cs.dennisc.pattern.Criterion< ? >[])null );
	}
	public static java.util.List< java.awt.Component > findAllMatches( java.awt.Component component, edu.cmu.cs.dennisc.pattern.Criterion< ? >... criterions ) {
		return findAllMatches( component, null, criterions );
	}
	public static java.util.List< java.awt.Component > findAllMatches( java.awt.Component component ) {
		return findAllMatches( component, null, (edu.cmu.cs.dennisc.pattern.Criterion< ? >[])null );
	}
	
	
	public static <E extends java.awt.Component> E findFirstAncestor( java.awt.Component component, boolean isComponentIncludedInSearch, Class< E > cls, edu.cmu.cs.dennisc.pattern.Criterion< ? >... criterions ) {
		java.awt.Component c;
		if( isComponentIncludedInSearch ) {
			c = component;
		} else {
			c = component.getParent();
		}
		while( c != null ) {
			boolean isAcceptedByAll;
			if( cls == null || cls.isAssignableFrom( c.getClass() ) ) {
				isAcceptedByAll = true;
				if( criterions == null ) {
					//pass
				} else {
					for( edu.cmu.cs.dennisc.pattern.Criterion criterion : criterions ) {
						if( criterion.accept( component ) ) {
							//pass
						} else {
							isAcceptedByAll = false;
							break;
						}
					}
				}
			} else {
				isAcceptedByAll = false;
			}
			if( isAcceptedByAll ) {
				return (E)c;
			}
			c = c.getParent();
		}
		return null;
	}
	public static <E extends java.awt.Component> E findFirstAncestor( java.awt.Component component, boolean isComponentIncludedInSearch, Class< E > cls ) {
		return findFirstAncestor( component, isComponentIncludedInSearch, cls, (edu.cmu.cs.dennisc.pattern.Criterion< ? >[])null );
	}
	public static java.awt.Component findFirstAncestor( java.awt.Component component, boolean isComponentIncludedInSearch, edu.cmu.cs.dennisc.pattern.Criterion< ? >... criterions ) {
		return findFirstAncestor( component, isComponentIncludedInSearch, null, criterions );
	}
}
