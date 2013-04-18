/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets;

import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.uml.model.Field;
import org.uml.model.Literal;
import org.uml.model.Member;
import org.uml.visual.widgets.actions.NameEditorAction;
import org.uml.visual.widgets.providers.FieldPopupMenuProvider;
import org.uml.visual.widgets.providers.LiteralPopupProvider;

/**
 *
 * @author Jelena
 */
public class LiteralWidget extends MemberWidgetBase{

    Literal literalComponent;
    private WidgetAction nameEditorAction = ActionFactory.createInplaceEditorAction(new NameEditorAction(this));
    //LabelWidget visibilityLabel;
    LabelWidget literalNameWidget;
    
    public LiteralWidget(ClassDiagramScene scene, Literal literal) {
        super(scene);
        this.literalComponent = literal;
        this.setLayout(LayoutFactory.createHorizontalFlowLayout());
//        visibilityLabel = new LabelWidget(getScene());
//        visibilityLabel.setLabel("+");
//        this.addChild(visibilityLabel);

        literalNameWidget = new LabelWidget(getScene());
        literalNameWidget.setLabel(literal.getName());
        this.addChild(literalNameWidget);
        literalNameWidget.getActions().addAction(nameEditorAction);
        literalNameWidget.getActions().addAction(ActionFactory.createPopupMenuAction(new LiteralPopupProvider(this)));

    }
    
    @Override
    public LabelWidget getNameLabel() {
        return literalNameWidget;
    }

    @Override
    public void setName(String newName) {
        if (getName().equals(newName)) {
            return;
        }
        String oldName = literalComponent.getName();
        if (!literalComponent.getDeclaringClass().nameExists(newName)) {
            literalNameWidget.setLabel(newName);
            literalComponent.setName(newName);
            literalComponent.getDeclaringClass().componentNameChanged(literalComponent, oldName);
        }
        else {
            //poruka
        }
    }

    @Override
    public String getName() {
        return literalComponent.getName();
    }

    @Override
    public Member getMember() {
        return literalComponent;
    }
}
