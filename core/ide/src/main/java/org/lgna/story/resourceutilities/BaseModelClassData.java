package org.lgna.story.resourceutilities;

public class BaseModelClassData
{
	public final Class implementationClass;
	public final Class abstractionClass;

	public BaseModelClassData( BaseModelClassData other )
	{
		this( other.abstractionClass, other.implementationClass );
	}

	public BaseModelClassData( Class absCls, Class implCls )
	{
		this.implementationClass = implCls;
		this.abstractionClass = absCls;
	}
}
