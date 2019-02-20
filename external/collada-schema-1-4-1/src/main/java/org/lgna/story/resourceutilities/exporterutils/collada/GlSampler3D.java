
package org.lgna.story.resourceutilities.exporterutils.collada;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 				A three-dimensional texture sampler for the GLSL profile.
 * 			
 * 
 * <p>Java class for gl_sampler3D complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="gl_sampler3D">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.collada.org/2005/11/COLLADASchema}fx_sampler3D_common">
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "gl_sampler3D", namespace = "http://www.collada.org/2005/11/COLLADASchema")
public class GlSampler3D
    extends FxSampler3DCommon
{


}
