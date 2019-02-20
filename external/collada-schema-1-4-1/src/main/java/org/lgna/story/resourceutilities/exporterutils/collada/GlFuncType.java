
package org.lgna.story.resourceutilities.exporterutils.collada;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for gl_func_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="gl_func_type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="NEVER"/>
 *     &lt;enumeration value="LESS"/>
 *     &lt;enumeration value="LEQUAL"/>
 *     &lt;enumeration value="EQUAL"/>
 *     &lt;enumeration value="GREATER"/>
 *     &lt;enumeration value="NOTEQUAL"/>
 *     &lt;enumeration value="GEQUAL"/>
 *     &lt;enumeration value="ALWAYS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "gl_func_type", namespace = "http://www.collada.org/2005/11/COLLADASchema")
@XmlEnum
public enum GlFuncType {

    NEVER,
    LESS,
    LEQUAL,
    EQUAL,
    GREATER,
    NOTEQUAL,
    GEQUAL,
    ALWAYS;

    public String value() {
        return name();
    }

    public static GlFuncType fromValue(String v) {
        return valueOf(v);
    }

}
