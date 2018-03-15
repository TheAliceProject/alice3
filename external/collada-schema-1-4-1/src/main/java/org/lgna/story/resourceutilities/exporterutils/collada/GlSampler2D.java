
package org.lgna.story.resourceutilities.exporterutils.collada;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 				A two-dimensional texture sampler for the GLSL profile.
 * 			
 * 
 * <p>Java class for gl_sampler2D complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="gl_sampler2D">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.collada.org/2005/11/COLLADASchema}fx_sampler2D_common">
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "gl_sampler2D", namespace = "http://www.collada.org/2005/11/COLLADASchema")
public class GlSampler2D
    extends FxSampler2DCommon
{


}
