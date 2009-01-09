import java
import edu
import org

from edu.cmu.cs.dennisc import alice

def getType( clsOrType ):
	if isinstance( clsOrType, alice.ast.AbstractType ):
		return clsOrType
	else:
		return alice.ast.TypeDeclaredInJava.get( clsOrType )

class IsInstanceCrawler( edu.cmu.cs.dennisc.pattern.Crawler ):
	def __init__( self, cls ):
		self._cls = cls
		self._references = []
	def visit( self, visitable ):
		if isinstance( visitable, self._cls ):
			self._references.append( visitable )

#todo: add context
def getVariables( method ):
	crawler = IsInstanceCrawler( alice.ast.VariableDeclaredInAlice )
	method.crawl( crawler )
	return crawler._references

#todo: add context
def getConstants( method ):
	crawler = IsInstanceCrawler( alice.ast.ConstantDeclaredInAlice )
	method.crawl( crawler )
	return crawler._references

def getVariableDeclarationStatements( method ):
	crawler = IsInstanceCrawler( alice.ast.VariableDeclarationStatement )
	method.crawl( crawler )
	return crawler._references

def wrapInArguments( parameters, expressions ):
	N = len( parameters )
	assert N == len( expressions )
	rv = [ None ] * N
	i = 0
	while i < N:
		rv[ i ] = alice.ast.Argument( parameters[ i ], expressions[ i ].getExpression() )
		i += 1
	return rv

def lookupMethod( cls, methodName, paramClses ):
	mthd = cls.getMethod( methodName, paramClses )
	try:
		return alice.ast.TypeDeclaredInJava.getMethod( mthd )
	except:
		print "TODO: lookupMethod", mthd
		return alice.ast.MethodDeclaredInJava( mthd );


def lookupField( cls, gttrName, sttrName, paramCls ):
	return alice.ast.TypeDeclaredInJava.getField( cls.getMethod( gttrName, [] ), cls.getMethod( sttrName, [ paramCls ] ) )

def createFieldAccess( astInstanceExpression, astField ):
	return alice.ast.FieldAccess( astInstanceExpression, astField )

def _createArguments( astMethod, astExpressions ):
	N = len( astExpressions )
	arguments = [ None ] * N
	i = 0
	while i<N:
		arguments[ i ] = alice.ast.Argument( astMethod.parameters.get( i ), astExpressions[ i ] )
		i += 1
	return arguments
	
def createMethodInvocation( astInstanceExpression, astMethod, astArgumentExpressions ):
	return alice.ast.MethodInvocation( astInstanceExpression, astMethod, _createArguments( astMethod, astArgumentExpressions ) )

def createMethodInvocationStatement( astInstanceExpression, astMethod, astArgumentExpressions ):
	return alice.ast.ExpressionStatement( createMethodInvocation( astInstanceExpression, astMethod, astArgumentExpressions ) )



def createString( s ):
	return alice.ast.StringLiteral( s )

def createTuple3( cls, t ):
	cnstrctr = alice.ast.TypeDeclaredInJava.getConstructor( cls.getConstructor( [java.lang.Double.TYPE]*3 ) )
	rv = alice.ast.InstanceCreation( 
		cnstrctr, [ 
			alice.ast.Argument( cnstrctr.parameters.get( 0 ), alice.ast.DoubleLiteral( t.x ) ),
			alice.ast.Argument( cnstrctr.parameters.get( 1 ), alice.ast.DoubleLiteral( t.y ) ),
			alice.ast.Argument( cnstrctr.parameters.get( 2 ), alice.ast.DoubleLiteral( t.z ) )
		]
	)
	return rv

def createVector3( v ):
	return createTuple3( edu.cmu.cs.dennisc.math.Vector3, v )
def createPoint3( p ):
	return createTuple3( edu.cmu.cs.dennisc.math.Point3, p )

def createOrthogonalMatrix3x3( m ):
	cls = edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3
	cnstrctr = alice.ast.TypeDeclaredInJava.getConstructor( cls.getConstructor( [edu.cmu.cs.dennisc.math.Vector3]*3 ) )
	rv = alice.ast.InstanceCreation( 
		cnstrctr, [ 
			alice.ast.Argument( cnstrctr.parameters.get( 0 ), createVector3( m.right ) ),
			alice.ast.Argument( cnstrctr.parameters.get( 1 ), createVector3( m.up ) ),
			alice.ast.Argument( cnstrctr.parameters.get( 2 ), createVector3( m.backward ) ),
		]
	)
	return rv

def createAffineMatrix4x4( m ):
	cls = edu.cmu.cs.dennisc.math.AffineMatrix4x4
	cnstrctr = alice.ast.TypeDeclaredInJava.getConstructor( cls.getConstructor( [edu.cmu.cs.dennisc.math.Orientation, edu.cmu.cs.dennisc.math.Point3] ) )
	rv = alice.ast.InstanceCreation( 
		cnstrctr, [ 
			alice.ast.Argument( cnstrctr.parameters.get( 0 ), createOrthogonalMatrix3x3( m.orientation ) ),
			alice.ast.Argument( cnstrctr.parameters.get( 1 ), createPoint3( m.translation ) )
		]
	)
	return rv

def createColor( c ):
	rv = None
	cls = org.alice.apis.moveandturn.Color
	for fld in edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getPublicStaticFinalFields( cls, cls ):
		if fld.get( None ) == c:
			astExpression = alice.ast.TypeExpression( cls )
			type = astExpression.value.getValue()
			astField = type.getField( fld )
			if astField:
				rv = alice.ast.FieldAccess( astExpression, astField )
			else:
				raise fld
	
	if rv:
		pass
	else:
		cnstrctr = alice.ast.TypeDeclaredInJava.getConstructor( cls.getConstructor( [java.lang.Number]*3 ) )
		rv = alice.ast.InstanceCreation( 
			cnstrctr, [ 
				alice.ast.Argument( cnstrctr.parameters.get( 0 ), alice.ast.DoubleLiteral( c.getRed() ) ),
				alice.ast.Argument( cnstrctr.parameters.get( 1 ), alice.ast.DoubleLiteral( c.getGreen() ) ),
				alice.ast.Argument( cnstrctr.parameters.get( 2 ), alice.ast.DoubleLiteral( c.getBlue() ) ),
			]
		)

	return rv


def createFont( font ):
	cls = org.alice.apis.moveandturn.Font
	attibuteArrayCls = edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getArrayClass( org.alice.apis.moveandturn.font.Attribute )
	selectedConstructor = cls.getDeclaredConstructor( [attibuteArrayCls] )
#	contructors = cls.getConstructors()
#	for contructor in contructors:
#		paramClses = contructor.getParameterTypes()
#		if len( paramClses ) == 1:
#			if paramClses[ 0 ].isArray():
#				if paramClses[ 0 ].getComponentType() == edu.cmu.cs.dennisc.font.Attribute:
#					selectedConstructor = contructor
#					selectedParamCls = paramClses[ 0 ]

#	value = alice.ast.ArrayInstanceCreation( attibuteArrayCls, [ 3 ], [
#		createEnumConstant( font.getFamily() ), 
#		createEnumConstant( font.getWeight() ),
#		createEnumConstant( font.getPosture() )																					
#	] )
	cnstrctr = alice.ast.TypeDeclaredInJava.getConstructor( selectedConstructor )
	param = cnstrctr.parameters.get( 0 )
	rv = alice.ast.InstanceCreation( 
		cnstrctr, [ 
			alice.ast.Argument( param, createEnumConstant( font.getFamily() ) ),
			alice.ast.Argument( param, createEnumConstant( font.getWeight() ) ),
			alice.ast.Argument( param, createEnumConstant( font.getPosture() ) ),
		]
	)
	return rv

#def createFont( font ):
#	cls = edu.cmu.cs.dennisc.font.Font
#	cnstrctr = alice.ast.TypeDeclaredInJava.getConstructor( cls.getConstructor( [] ) )
#	rv = alice.ast.InstanceCreation( 
#		cnstrctr, [ 
#		]
#	)
#	return rv


def createPortion( portion ):
	cls = org.alice.apis.moveandturn.Portion
	cnstrctr = alice.ast.TypeDeclaredInJava.getConstructor( cls.getConstructor( [java.lang.Number] ) )
	rv = alice.ast.InstanceCreation( 
		cnstrctr, [ 
			alice.ast.Argument( cnstrctr.parameters.get( 0 ), alice.ast.DoubleLiteral( portion.getValue() ) ),
		]
	)
	return rv
	
def createAngleInRevolutions( angle ):
	cls = org.alice.apis.moveandturn.AngleInRevolutions
	cnstrctr = alice.ast.TypeDeclaredInJava.getConstructor( cls.getConstructor( [java.lang.Number] ) )
	rv = alice.ast.InstanceCreation( 
		cnstrctr, [ 
			alice.ast.Argument( cnstrctr.parameters.get( 0 ), alice.ast.DoubleLiteral( angle.getAsRevolutions() ) ),
		]
	)
	return rv

def createEnumConstant( enumConstant ):
	typeDeclaredInJava = alice.ast.TypeDeclaredInJava.get( enumConstant.__class__ )
	astExpression = alice.ast.TypeExpression( typeDeclaredInJava )
	astField = typeDeclaredInJava.getDeclaredField( typeDeclaredInJava, enumConstant.name() )
	
	#for field in typeDeclaredInJava.getDeclaredFields():
	#	if field.getName() == enumConstant.name():
	#		astField = field
	#		break
	return alice.ast.FieldAccess( astExpression, astField )
	
