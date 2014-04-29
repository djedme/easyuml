/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.xmlSerialization;

import java.util.HashMap;
import org.dom4j.Element;
import org.uml.model.Literal;

/**
 *
 * @author stefanpetrovic
 */
public class LiteralSerializer implements XmlSerializer{

    private HashMap<String, Literal> literal;

    public LiteralSerializer(HashMap<String, Literal> literal) {
        this.literal = literal;
    }
    
    /**
     * Serializes literal object to XML by translating its fields into parameter node's attributes and subelements.
     * @param node represents the node that will contain serialized literal object.
     */
    @Override
    public void serialize(Element node) {
        for (Literal l : literal.values()) {
            Element literalNode = node.addElement("Literal");
            if (literalNode != null) literalNode.addAttribute("name", l.getName());
        }
    }
    
}
