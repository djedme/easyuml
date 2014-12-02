package org.uml.xmlDeserialization;

import java.util.List;
import org.dom4j.Element;
import org.uml.model.relations.CardinalityEnum;
import org.uml.model.components.ComponentBase;
import org.uml.model.relations.HasBaseRelation;

/**
 *
 * @author stefanpetrovic
 */
public class HasRelationDeserializer implements XmlDeserializer{

    private HasBaseRelation relation;
    private List<ComponentBase> components;
    
    public HasRelationDeserializer(HasBaseRelation relation, List<ComponentBase> components) {
        this.relation = relation;
        this.components = components;
    }
    
    /**
     * Fills in attributes of relation object by reading attributes and
     * sub-nodes of the node input element.
     *
     * @param node is XML tree node whose attributes and sub-nodes represent
     * attributes of the relation object.
     */
    @Override
    public void deserialize(Element node) {
        String name = node.attributeValue("name");
        String source = node.attributeValue("source");
        String target = node.attributeValue("target");
        if (name != null && !name.equals("")) {
            relation.setName(name);
        }
        for (int i = 0; i < components.size(); i++) {
            if (source != null && components.get(i).getName().equals(source)) {
                relation.setSource(components.get(i));
            }else if (target != null && components.get(i).getName().equals(target)) {
                relation.setTarget(components.get(i));
            }
        }
        String sourceCardinality = node.attributeValue("sourceCardinality");
        String targetCaridnality = node.attributeValue("targetCardinality");
        relation.setCardinalitySource(CardinalityEnum.parseString(sourceCardinality));
        relation.setCardinalityTarget(CardinalityEnum.parseString(targetCaridnality));
        String collectionType = node.attributeValue("collectionType");
        if (collectionType != null) {
            relation.setCollectionType(collectionType);
        }
    }
    
}
