
package org.lgna.story.resourceutilities.exporterutils.collada;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for gles_texcombiner_operandAlpha_enums.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="gles_texcombiner_operandAlpha_enums">
 *   &lt;restriction base="{http://www.collada.org/2005/11/COLLADASchema}gl_blend_type">
 *     &lt;enumeration value="SRC_ALPHA"/>
 *     &lt;enumeration value="ONE_MINUS_SRC_ALPHA"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "gles_texcombiner_operandAlpha_enums", namespace = "http://www.collada.org/2005/11/COLLADASchema")
@XmlEnum(GlBlendType.class)
public enum GlesTexcombinerOperandAlphaEnums {

    SRC_ALPHA,
    ONE_MINUS_SRC_ALPHA;

    public String value() {
        return name();
    }

    public static GlesTexcombinerOperandAlphaEnums fromValue(String v) {
        return valueOf(v);
    }

}
