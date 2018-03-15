
package org.lgna.story.resourceutilities.exporterutils.collada;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for gl_blend_equation_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="gl_blend_equation_type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="FUNC_ADD"/>
 *     &lt;enumeration value="FUNC_SUBTRACT"/>
 *     &lt;enumeration value="FUNC_REVERSE_SUBTRACT"/>
 *     &lt;enumeration value="MIN"/>
 *     &lt;enumeration value="MAX"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "gl_blend_equation_type", namespace = "http://www.collada.org/2005/11/COLLADASchema")
@XmlEnum
public enum GlBlendEquationType {

    FUNC_ADD,
    FUNC_SUBTRACT,
    FUNC_REVERSE_SUBTRACT,
    MIN,
    MAX;

    public String value() {
        return name();
    }

    public static GlBlendEquationType fromValue(String v) {
        return valueOf(v);
    }

}
