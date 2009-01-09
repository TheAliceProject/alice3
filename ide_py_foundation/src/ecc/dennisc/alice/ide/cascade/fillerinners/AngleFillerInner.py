#print "-->", __name__

from ExpressionFillerInner import *

class AngleFillerInner( ExpressionFillerInner ):
	def __init__( self ):
		ExpressionFillerInner.__init__( self, ecc.dennisc.alice.ast.getType( org.alice.apis.moveandturn.AngleInRevolutions ), alice.ast.InstanceCreation )
	def addFillIns( self, blank ):
		cls = org.alice.apis.moveandturn.AngleInRevolutions
		cnstrctr = alice.ast.TypeDeclaredInJava.getConstructor( cls.getConstructor( [java.lang.Number] ) )
		param = cnstrctr.parameters.get( 0 )
		self._addExpressionFillIn( blank, cnstrctr, [ alice.ast.Argument( param, alice.ast.DoubleLiteral( 0.125 ) ) ] ) 
		self._addExpressionFillIn( blank, cnstrctr, [ alice.ast.Argument( param, alice.ast.DoubleLiteral( 0.25 ) ) ] ) 
		self._addExpressionFillIn( blank, cnstrctr, [ alice.ast.Argument( param, alice.ast.DoubleLiteral( 0.5 ) ) ] ) 
		self._addExpressionFillIn( blank, cnstrctr, [ alice.ast.Argument( param, alice.ast.DoubleLiteral( 1.0 ) ) ] ) 
		self._addExpressionFillIn( blank, cnstrctr, [ alice.ast.Argument( param, alice.ast.DoubleLiteral( 2.0 ) ) ] ) 
		self._addExpressionFillIn( blank, cnstrctr, [ alice.ast.Argument( param, alice.ast.DoubleLiteral( 4.0 ) ) ] )
		blank.addSeparator()
		blank.addChild( ecc.dennisc.alice.ide.cascade.CustomAngleFillIn() )
		 

#print "<--", __name__
