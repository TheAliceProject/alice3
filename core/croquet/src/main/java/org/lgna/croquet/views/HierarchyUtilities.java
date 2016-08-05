/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.lgna.croquet.views;

/**
 * @author Dennis Cosgrove
 */
public class HierarchyUtilities {
	private HierarchyUtilities() {
		throw new AssertionError();
	}

	public static final edu.cmu.cs.dennisc.pattern.HowMuch DEFAULT_HOW_MUCH = edu.cmu.cs.dennisc.pattern.HowMuch.COMPONENT_AND_DESCENDANTS;

	private static <E extends AwtComponentView<?>> E getFirstToAccept( boolean isComponentACandidate, boolean isChildACandidate, boolean isGrandchildAndBeyondACandidate, AwtComponentView<?> component, Class<E> cls, edu.cmu.cs.dennisc.pattern.Criterion<?>... criterions ) {
		assert component != null;
		E rv = null;
		boolean isAcceptedByAll;
		if( isComponentACandidate ) {
			if( ( cls == null ) || cls.isAssignableFrom( component.getClass() ) ) {
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
				if( component instanceof AwtContainerView<?> ) {
					AwtContainerView<?> container = (AwtContainerView<?>)component;
					for( AwtComponentView<?> componentI : container.getComponents() ) {
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

	private static <E extends AwtComponentView<?>> void updateAllToAccept( boolean isComponentACandidate, boolean isChildACandidate, boolean isGrandchildAndBeyondACandidate, java.util.List<E> list, AwtComponentView<?> component, Class<E> cls, edu.cmu.cs.dennisc.pattern.Criterion<?>... criterions ) {
		assert component != null;

		if( isComponentACandidate ) {
			if( ( cls == null ) || cls.isAssignableFrom( component.getClass() ) ) {
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
			if( component instanceof AwtContainerView<?> ) {
				AwtContainerView<?> container = (AwtContainerView<?>)component;
				for( AwtComponentView<?> componentI : container.getComponents() ) {
					updateAllToAccept( isChildACandidate, isGrandchildAndBeyondACandidate, isGrandchildAndBeyondACandidate, list, componentI, cls, criterions );
				}
			}
		}
	}

	private static <E extends AwtComponentView<?>> E getFirstToAccept( edu.cmu.cs.dennisc.pattern.HowMuch candidateMask, AwtComponentView<?> component, Class<E> cls, edu.cmu.cs.dennisc.pattern.Criterion<?>... criterions ) {
		return getFirstToAccept( candidateMask.isComponentACandidate(), candidateMask.isChildACandidate(), candidateMask.isGrandchildAndBeyondACandidate(), component, cls, criterions );
	}

	private static <E extends AwtComponentView<?>> void updateAllToAccept( edu.cmu.cs.dennisc.pattern.HowMuch candidateMask, java.util.List<E> list, AwtComponentView<?> component, Class<E> cls, edu.cmu.cs.dennisc.pattern.Criterion<?>... criterions ) {
		updateAllToAccept( candidateMask.isComponentACandidate(), candidateMask.isChildACandidate(), candidateMask.isGrandchildAndBeyondACandidate(), list, component, cls, criterions );
	}

	public static <E extends AwtComponentView<?>> E findFirstMatch( AwtComponentView<?> component, edu.cmu.cs.dennisc.pattern.HowMuch howMuch, Class<E> cls, edu.cmu.cs.dennisc.pattern.Criterion<?>... criterions ) {
		return HierarchyUtilities.getFirstToAccept( howMuch, component, cls, criterions );
	}

	public static <E extends AwtComponentView<?>> E findFirstMatch( AwtComponentView<?> component, Class<E> cls, edu.cmu.cs.dennisc.pattern.Criterion<?>... criterions ) {
		return findFirstMatch( component, DEFAULT_HOW_MUCH, cls, criterions );
	}

	public static <E extends AwtComponentView<?>> E findFirstMatch( AwtComponentView<?> component, Class<E> cls ) {
		return findFirstMatch( component, cls, (edu.cmu.cs.dennisc.pattern.Criterion<?>[])null );
	}

	public static AwtComponentView<?> findFirstMatch( AwtComponentView<?> component, edu.cmu.cs.dennisc.pattern.Criterion<?>... criterions ) {
		return findFirstMatch( component, null, criterions );
	}

	public static <E extends AwtComponentView<?>> java.util.List<E> findAllMatches( AwtComponentView<?> component, edu.cmu.cs.dennisc.pattern.HowMuch howMuch, Class<E> cls, edu.cmu.cs.dennisc.pattern.Criterion<?>... criterions ) {
		java.util.List<E> list = new java.util.LinkedList<E>();
		HierarchyUtilities.updateAllToAccept( howMuch, list, component, cls, criterions );
		return list;
	}

	public static <E extends AwtComponentView<?>> java.util.List<E> findAllMatches( AwtComponentView<?> component, Class<E> cls, edu.cmu.cs.dennisc.pattern.Criterion<?>... criterions ) {
		return findAllMatches( component, DEFAULT_HOW_MUCH, cls, criterions );
	}

	public static <E extends AwtComponentView<?>> java.util.List<E> findAllMatches( AwtComponentView<?> component, Class<E> cls ) {
		return findAllMatches( component, cls, (edu.cmu.cs.dennisc.pattern.Criterion<?>[])null );
	}

	public static java.util.List<AwtComponentView<?>> findAllMatches( AwtComponentView<?> component, edu.cmu.cs.dennisc.pattern.Criterion<?>... criterions ) {
		return findAllMatches( component, null, criterions );
	}

	public static java.util.List<AwtComponentView<?>> findAllMatches( AwtComponentView<?> component ) {
		return findAllMatches( component, null, (edu.cmu.cs.dennisc.pattern.Criterion<?>[])null );
	}

	public static <E extends AwtComponentView<?>> E findFirstAncestor( AwtComponentView<?> component, boolean isComponentIncludedInSearch, Class<E> cls, edu.cmu.cs.dennisc.pattern.Criterion<?>... criterions ) {
		AwtComponentView<?> c;
		if( isComponentIncludedInSearch ) {
			c = component;
		} else {
			c = component.getParent();
		}
		while( c != null ) {
			boolean isAcceptedByAll;
			if( ( cls == null ) || cls.isAssignableFrom( c.getClass() ) ) {
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

	public static <E extends AwtComponentView<?>> E findFirstAncestor( AwtComponentView<?> component, boolean isComponentIncludedInSearch, Class<E> cls ) {
		return findFirstAncestor( component, isComponentIncludedInSearch, cls, (edu.cmu.cs.dennisc.pattern.Criterion<?>[])null );
	}

	public static AwtComponentView<?> findFirstAncestor( AwtComponentView<?> component, boolean isComponentIncludedInSearch, edu.cmu.cs.dennisc.pattern.Criterion<?>... criterions ) {
		return findFirstAncestor( component, isComponentIncludedInSearch, null, criterions );
	}
}
