/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.xmlSerialization;

import org.dom4j.Element;
import org.uml.model.ComponentBase;
import org.uml.model.relations.ImplementsRelationComponent;
import org.uml.model.relations.RelationComponent;

/**
 *
 * @author Stefan
 */
public class ImplementsRelationSerializer implements RelationSerializer{

    private ImplementsRelationComponent implementsRelationComponent;
    
    /**
     * Sets the implementsRelationComponent object that is going to be serialized.
     * @param component represents implementsRelationComponent object to be serialized.
     */
    @Override
    public void addRelationComponent(RelationComponent relation) {
        try{
            implementsRelationComponent = (ImplementsRelationComponent) relation;
        }catch (ClassCastException e) {
            System.out.println("You have tried to cast invalid type to ImplementsRelationComponent!");
            e.printStackTrace();
        }
    }

    /**
     * Serializes implementsRelationComponent object to XML by translating its fields into parameter node's attributes and subelements.
     * @param node represents the node that will contain serialized ImplementsRelationComponent object.
     */
    @Override
    public void serialize(Element node) {
        if (implementsRelationComponent.getName() != null) node.addAttribute("name", implementsRelationComponent.getName());
        ComponentBase sourceComponent = implementsRelationComponent.getSource();
        ComponentBase targetComponent = implementsRelationComponent.getTarget();
        if (sourceComponent != null) { 
            node.addAttribute("source", sourceComponent.getName());
        }else {
            node.addAttribute("source", "");
        }
        if (targetComponent != null) {
            node.addAttribute("target", targetComponent.getName());
        }else {
            node.addAttribute("target", "");
        }
    }
    
}
