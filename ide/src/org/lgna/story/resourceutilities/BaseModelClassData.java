package org.lgna.story.resourceutilities;

public class BaseModelClassData
{
	public final Class implementationFactoryClass;
	public final Class implementationClass;
	public final Class abstractionClass;
	
	public BaseModelClassData(BaseModelClassData other)
	{
		this(other.abstractionClass, other.implementationClass, other.implementationFactoryClass);
	}
	
	public BaseModelClassData(Class absCls, Class implCls, Class implFactory )
	{
		this.implementationFactoryClass = implFactory;
		this.implementationClass = implCls;
		this.abstractionClass = absCls;
	}
}