
package org.lgna.story.resourceutilities.exporterutils.collada;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for gl_logic_op_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="gl_logic_op_type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="CLEAR"/>
 *     &lt;enumeration value="AND"/>
 *     &lt;enumeration value="AND_REVERSE"/>
 *     &lt;enumeration value="COPY"/>
 *     &lt;enumeration value="AND_INVERTED"/>
 *     &lt;enumeration value="NOOP"/>
 *     &lt;enumeration value="XOR"/>
 *     &lt;enumeration value="OR"/>
 *     &lt;enumeration value="NOR"/>
 *     &lt;enumeration value="EQUIV"/>
 *     &lt;enumeration value="INVERT"/>
 *     &lt;enumeration value="OR_REVERSE"/>
 *     &lt;enumeration value="COPY_INVERTED"/>
 *     &lt;enumeration value="NAND"/>
 *     &lt;enumeration value="SET"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "gl_logic_op_type", namespace = "http://www.collada.org/2005/11/COLLADASchema")
@XmlEnum
public enum GlLogicOpType {

    CLEAR,
    AND,
    AND_REVERSE,
    COPY,
    AND_INVERTED,
    NOOP,
    XOR,
    OR,
    NOR,
    EQUIV,
    INVERT,
    OR_REVERSE,
    COPY_INVERTED,
    NAND,
    SET;

    public String value() {
        return name();
    }

    public static GlLogicOpType fromValue(String v) {
        return valueOf(v);
    }

}
