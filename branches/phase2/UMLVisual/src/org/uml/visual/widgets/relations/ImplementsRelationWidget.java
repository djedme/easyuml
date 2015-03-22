package org.uml.visual.widgets.relations;

import org.uml.model.relations.RelationBase;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.anchors.TriangleEquilateralAnchorShape;

/**
 *
 * @author Boris Perović
 */
public class ImplementsRelationWidget extends RelationBaseWidget {

    public ImplementsRelationWidget(RelationBase relation, ClassDiagramScene scene) {
        super(relation, scene);
        setStroke(DASHED);
        setTargetAnchorShape(new TriangleEquilateralAnchorShape(10, false));

        relation.setName("<<implements>>");
        name.setLabel("<<implements>>");
    }
}
