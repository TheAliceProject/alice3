#print "-->", __name__

from ExpressionFillerInner import *

class PortionFillerInner( ExpressionFillerInner ):
	def __init__( self ):
		ExpressionFillerInner.__init__( self, ecc.dennisc.alice.ast.getType( org.alice.apis.moveandturn.Portion ), alice.ast.InstanceCreation )
	def addFillIns( self, blank ):
		cls = org.alice.apis.moveandturn.Portion
		cnstrctr = alice.ast.TypeDeclaredInJava.getConstructor( cls.getConstructor( [java.lang.Number] ) )
		param = cnstrctr.parameters.get( 0 )
		self._addExpressionFillIn( blank, cnstrctr, [ alice.ast.Argument( param, alice.ast.DoubleLiteral( 0.0 ) ) ] ) 
		self._addExpressionFillIn( blank, cnstrctr, [ alice.ast.Argument( param, alice.ast.DoubleLiteral( 0.1 ) ) ] ) 
		self._addExpressionFillIn( blank, cnstrctr, [ alice.ast.Argument( param, alice.ast.DoubleLiteral( 0.2 ) ) ] ) 
		self._addExpressionFillIn( blank, cnstrctr, [ alice.ast.Argument( param, alice.ast.DoubleLiteral( 0.3 ) ) ] ) 
		self._addExpressionFillIn( blank, cnstrctr, [ alice.ast.Argument( param, alice.ast.DoubleLiteral( 0.4 ) ) ] ) 
		self._addExpressionFillIn( blank, cnstrctr, [ alice.ast.Argument( param, alice.ast.DoubleLiteral( 0.5 ) ) ] ) 
		self._addExpressionFillIn( blank, cnstrctr, [ alice.ast.Argument( param, alice.ast.DoubleLiteral( 0.6 ) ) ] ) 
		self._addExpressionFillIn( blank, cnstrctr, [ alice.ast.Argument( param, alice.ast.DoubleLiteral( 0.7 ) ) ] ) 
		self._addExpressionFillIn( blank, cnstrctr, [ alice.ast.Argument( param, alice.ast.DoubleLiteral( 0.8 ) ) ] ) 
		self._addExpressionFillIn( blank, cnstrctr, [ alice.ast.Argument( param, alice.ast.DoubleLiteral( 0.9 ) ) ] ) 
		self._addExpressionFillIn( blank, cnstrctr, [ alice.ast.Argument( param, alice.ast.DoubleLiteral( 1.0 ) ) ] ) 
		blank.addSeparator()
		blank.addChild( ecc.dennisc.alice.ide.cascade.CustomPortionFillIn() )

#print "<--", __name__
