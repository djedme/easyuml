/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets;

import org.uml.visual.widgets.providers.ConstructorPopupMenuProvider;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.uml.model.ClassDiagramComponent;
import org.uml.model.Constructor;
import org.uml.model.Member;
import org.uml.model.Method;
import org.uml.visual.widgets.actions.NameEditorAction;
import org.uml.visual.widgets.providers.FieldPopupMenuProvider;
import org.uml.visual.widgets.providers.MethodPopupMenuProvider;

/**
 *
 * @author Jelena
 */
public class ConstructorWidget extends MemberWidgetBase{

    Constructor constructorComponent;
    private WidgetAction nameEditorAction = ActionFactory.createInplaceEditorAction(new NameEditorAction(this));
    LabelWidget visibilityLabel;
    LabelWidget construktorNameWidget;
    
    public ConstructorWidget(ClassDiagramScene scene, Constructor constructor) {
        super(scene);
        this.constructorComponent = constructor;
        this.setLayout(LayoutFactory.createHorizontalFlowLayout());
        visibilityLabel = new LabelWidget(getScene());
        visibilityLabel.setLabel("+");
        this.addChild(visibilityLabel);

        construktorNameWidget = new LabelWidget(getScene());
        construktorNameWidget.setLabel(constructorComponent.getDeclaringClass().getName() + "()");
        this.addChild(construktorNameWidget);
        //construktorNameWidget.getActions().addAction(nameEditorAction);
        construktorNameWidget.getActions().addAction(ActionFactory.createPopupMenuAction(new ConstructorPopupMenuProvider(this)));

    }
    
    @Override
    public LabelWidget getNameLabel() {
        return construktorNameWidget;
    }

    @Override
    public void setName(String newName) {
        construktorNameWidget.setLabel(newName);
    }

    @Override
    public String getName() {
        return constructorComponent.getName();
    }

    @Override
    public Member getMember() {
        return constructorComponent;
    }

    @Override
    public void setAttributes(String attributes) {
        
    }
}
