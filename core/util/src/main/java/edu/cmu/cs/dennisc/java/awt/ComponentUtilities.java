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
package edu.cmu.cs.dennisc.java.awt;

/**
 * @author Dennis Cosgrove
 */
public class ComponentUtilities {
	public static void makeStandOut( java.awt.Component component ) {
		assert component != null;
		if( component instanceof javax.swing.JComponent ) {
			javax.swing.JComponent jComponent = (javax.swing.JComponent)component;
			jComponent.setBorder( javax.swing.BorderFactory.createLineBorder( java.awt.Color.RED, 4 ) );
			jComponent.setOpaque( true );
		}
		component.setBackground( java.awt.Color.GREEN );
	}

	private static java.awt.Point getLocation( java.awt.Point rv, java.awt.Component c, java.awt.Component ancestor ) {
		assert c != null;
		if( c == ancestor ) {
			return rv;
		} else {
			rv.x += c.getX();
			rv.y += c.getY();
			return getLocation( rv, c.getParent(), ancestor );
		}
	}

	private static java.awt.Point getLocation( java.awt.Component c, java.awt.Component ancestor ) {
		return getLocation( new java.awt.Point(), c, ancestor );
	}

	public static java.awt.Point convertPoint( java.awt.Component src, int srcX, int srcY, java.awt.Component dst ) {
		java.awt.Component srcRoot = javax.swing.SwingUtilities.getRoot( src );
		java.awt.Component dstRoot = javax.swing.SwingUtilities.getRoot( dst );
		//avoid tree lock, if possible
		if( ( srcRoot != null ) && ( srcRoot == dstRoot ) ) {
			java.awt.Point srcPt = getLocation( src, srcRoot );
			java.awt.Point dstPt = getLocation( dst, dstRoot );
			java.awt.Point rv = srcPt;
			rv.x -= dstPt.x;
			rv.y -= dstPt.y;
			rv.x += srcX;
			rv.y += srcY;
			return rv;
		} else {
			return javax.swing.SwingUtilities.convertPoint( src, srcX, srcY, dst );
		}
	}

	public static java.awt.Point convertPoint( java.awt.Component src, java.awt.Point srcPt, java.awt.Component dst ) {
		return convertPoint( src, srcPt.x, srcPt.y, dst );
	}

	public static java.awt.Rectangle convertRectangle( java.awt.Component src, java.awt.Rectangle srcRect, java.awt.Component dst ) {
		java.awt.Point pt = convertPoint( src, srcRect.x, srcRect.y, dst );
		return new java.awt.Rectangle( pt.x, pt.y, srcRect.width, srcRect.height );
	}

	public static final edu.cmu.cs.dennisc.pattern.HowMuch DEFAULT_HOW_MUCH = edu.cmu.cs.dennisc.pattern.HowMuch.COMPONENT_AND_DESCENDANTS;

	private static <E extends java.awt.Component> E getFirstToAccept( boolean isComponentACandidate, boolean isChildACandidate, boolean isGrandchildAndBeyondACandidate, java.awt.Component component, Class<E> cls, edu.cmu.cs.dennisc.pattern.Criterion<?>... criterions ) {
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
				if( component instanceof java.awt.Container ) {
					for( java.awt.Component componentI : ( (java.awt.Container)component ).getComponents() ) {
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

	private static <E extends java.awt.Component> void updateAllToAccept( boolean isComponentACandidate, boolean isChildACandidate, boolean isGrandchildAndBeyondACandidate, java.util.List<E> list, java.awt.Component component, Class<E> cls, edu.cmu.cs.dennisc.pattern.Criterion<?>... criterions ) {
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
			if( component instanceof java.awt.Container ) {
				for( java.awt.Component componentI : ( (java.awt.Container)component ).getComponents() ) {
					updateAllToAccept( isChildACandidate, isGrandchildAndBeyondACandidate, isGrandchildAndBeyondACandidate, list, componentI, cls, criterions );
				}
			}
		}
	}

	private static <E extends java.awt.Component> E getFirstToAccept( edu.cmu.cs.dennisc.pattern.HowMuch candidateMask, java.awt.Component component, Class<E> cls, edu.cmu.cs.dennisc.pattern.Criterion<?>... criterions ) {
		return getFirstToAccept( candidateMask.isComponentACandidate(), candidateMask.isChildACandidate(), candidateMask.isGrandchildAndBeyondACandidate(), component, cls, criterions );
	}

	private static <E extends java.awt.Component> void updateAllToAccept( edu.cmu.cs.dennisc.pattern.HowMuch candidateMask, java.util.List<E> list, java.awt.Component component, Class<E> cls, edu.cmu.cs.dennisc.pattern.Criterion<?>... criterions ) {
		updateAllToAccept( candidateMask.isComponentACandidate(), candidateMask.isChildACandidate(), candidateMask.isGrandchildAndBeyondACandidate(), list, component, cls, criterions );
	}

	public static <E extends java.awt.Component> E findFirstMatch( java.awt.Component component, edu.cmu.cs.dennisc.pattern.HowMuch howMuch, Class<E> cls, edu.cmu.cs.dennisc.pattern.Criterion<?>... criterions ) {
		return ComponentUtilities.getFirstToAccept( howMuch, component, cls, criterions );
	}

	public static <E extends java.awt.Component> E findFirstMatch( java.awt.Component component, Class<E> cls, edu.cmu.cs.dennisc.pattern.Criterion<?>... criterions ) {
		return findFirstMatch( component, DEFAULT_HOW_MUCH, cls, criterions );
	}

	public static <E extends java.awt.Component> E findFirstMatch( java.awt.Component component, Class<E> cls ) {
		return findFirstMatch( component, cls, (edu.cmu.cs.dennisc.pattern.Criterion<?>[])null );
	}

	public static java.awt.Component findFirstMatch( java.awt.Component component, edu.cmu.cs.dennisc.pattern.Criterion<?>... criterions ) {
		return findFirstMatch( component, null, criterions );
	}

	public static <E extends java.awt.Component> java.util.List<E> findAllMatches( java.awt.Component component, edu.cmu.cs.dennisc.pattern.HowMuch howMuch, Class<E> cls, edu.cmu.cs.dennisc.pattern.Criterion<?>... criterions ) {
		java.util.List<E> list = new java.util.LinkedList<E>();
		ComponentUtilities.updateAllToAccept( howMuch, list, component, cls, criterions );
		return list;
	}

	public static <E extends java.awt.Component> java.util.List<E> findAllMatches( java.awt.Component component, Class<E> cls, edu.cmu.cs.dennisc.pattern.Criterion<?>... criterions ) {
		return findAllMatches( component, DEFAULT_HOW_MUCH, cls, criterions );
	}

	public static <E extends java.awt.Component> java.util.List<E> findAllMatches( java.awt.Component component, Class<E> cls ) {
		return findAllMatches( component, cls, (edu.cmu.cs.dennisc.pattern.Criterion<?>[])null );
	}

	public static java.util.List<java.awt.Component> findAllMatches( java.awt.Component component, edu.cmu.cs.dennisc.pattern.Criterion<?>... criterions ) {
		return findAllMatches( component, null, criterions );
	}

	public static java.util.List<java.awt.Component> findAllMatches( java.awt.Component component ) {
		return findAllMatches( component, null, (edu.cmu.cs.dennisc.pattern.Criterion<?>[])null );
	}

	public static <E extends java.awt.Component> E findFirstAncestor( java.awt.Component component, boolean isComponentIncludedInSearch, Class<E> cls, edu.cmu.cs.dennisc.pattern.Criterion<?>... criterions ) {
		java.awt.Component c;
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

	public static <E extends java.awt.Component> E findFirstAncestor( java.awt.Component component, boolean isComponentIncludedInSearch, Class<E> cls ) {
		return findFirstAncestor( component, isComponentIncludedInSearch, cls, (edu.cmu.cs.dennisc.pattern.Criterion<?>[])null );
	}

	public static java.awt.Component findFirstAncestor( java.awt.Component component, boolean isComponentIncludedInSearch, edu.cmu.cs.dennisc.pattern.Criterion<?>... criterions ) {
		return findFirstAncestor( component, isComponentIncludedInSearch, null, criterions );
	}

	public static void doLayoutTree( java.awt.Component c ) {
		//c.doLayout();
		if( c instanceof java.awt.Container ) {
			java.awt.Container container = (java.awt.Container)c;
			for( java.awt.Component component : container.getComponents() ) {
				doLayoutTree( component );
			}
		}
		c.doLayout();
	}

	public static void setSizeToPreferredSizeTree( java.awt.Component c ) {
		if( c instanceof java.awt.Container ) {
			java.awt.Container container = (java.awt.Container)c;
			for( java.awt.Component component : container.getComponents() ) {
				setSizeToPreferredSizeTree( component );
			}
		}
		c.setSize( c.getPreferredSize() );
	}

	public static void invalidateTree( java.awt.Component c ) {
		c.invalidate();
		if( c instanceof java.awt.Container ) {
			java.awt.Container container = (java.awt.Container)c;
			for( java.awt.Component component : container.getComponents() ) {
				invalidateTree( component );
			}
		}
	}

	public static void validateTree( java.awt.Component c ) {
		c.validate();
		if( c instanceof java.awt.Container ) {
			java.awt.Container container = (java.awt.Container)c;
			for( java.awt.Component component : container.getComponents() ) {
				validateTree( component );
			}
		}
	}

	public static void revalidateTree( java.awt.Component c ) {
		if( c instanceof javax.swing.JComponent ) {
			javax.swing.JComponent jc = (javax.swing.JComponent)c;
			jc.revalidate();
		}
		if( c instanceof java.awt.Container ) {
			java.awt.Container container = (java.awt.Container)c;
			for( java.awt.Component component : container.getComponents() ) {
				revalidateTree( component );
			}
		}
	}

	public static java.awt.Frame getRootFrame( java.awt.Component c ) {
		java.awt.Component root = javax.swing.SwingUtilities.getRoot( c );
		if( root instanceof java.awt.Frame ) {
			return (java.awt.Frame)root;
		} else {
			return null;
		}
	}

	public static javax.swing.JFrame getRootJFrame( java.awt.Component c ) {
		java.awt.Component root = javax.swing.SwingUtilities.getRoot( c );
		if( root instanceof javax.swing.JFrame ) {
			return (javax.swing.JFrame)root;
		} else {
			return null;
		}
	}

	public static java.awt.Dialog getRootDialog( java.awt.Component c ) {
		java.awt.Component root = javax.swing.SwingUtilities.getRoot( c );
		if( root instanceof java.awt.Dialog ) {
			return (java.awt.Dialog)root;
		} else {
			return null;
		}
	}

	public static javax.swing.JDialog getRootJDialog( java.awt.Component c ) {
		java.awt.Component root = javax.swing.SwingUtilities.getRoot( c );
		if( root instanceof javax.swing.JDialog ) {
			return (javax.swing.JDialog)root;
		} else {
			return null;
		}
	}
}
