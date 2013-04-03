/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.actions;

import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.ClassDiagram;
import org.uml.model.RelationComponent;

/**
 *
 * @author Uros
 */
public class RelationLabelTextFieldEditorAction implements TextFieldInplaceEditor{

    RelationComponent relationComponent;
    ClassDiagram classDiagram;
    
    public RelationLabelTextFieldEditorAction(RelationComponent relationComponent, ClassDiagram classDiagram) {
        this.relationComponent = relationComponent;
        this.classDiagram = classDiagram;
    }

    
    
    @Override
    public boolean isEnabled(Widget widget) {
        return true;
    }

    @Override
    public String getText(Widget widget) {
        return ((LabelWidget) widget).getLabel();
    }

    @Override
    public void setText(Widget widget, String string) {
        ((LabelWidget) widget).setLabel(string);
        classDiagram.removeRelation(relationComponent.getName());
        relationComponent.setName(string);
        classDiagram.addRelation(relationComponent);
        
            }
    
    
    
}
