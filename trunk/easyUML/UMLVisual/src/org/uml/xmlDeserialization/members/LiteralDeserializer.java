package org.uml.xmlDeserialization.members;

import org.dom4j.Element;
import org.uml.model.members.Literal;
import org.uml.xmlDeserialization.XmlDeserializer;

/**
 *
 * @author stefanpetrovic
 */
public class LiteralDeserializer implements XmlDeserializer{

    private Literal literal;

    public LiteralDeserializer(Literal literal) {
        this.literal = literal;
    }
    
    /**
     * Fills in attributes of literal object by reading attributes and
     * sub-nodes of the node input element.
     *
     * @param node is XML tree node whose attributes and sub-nodes represent
     * attributes of the literal object.
     */
    @Override
    public void deserialize(Element node) {
        String name = node.attributeValue("name");
        if (name != null) literal.setName(name);
    }
}
