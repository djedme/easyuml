/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets;

import java.awt.Color;
import java.awt.Font;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.SeparatorWidget;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.InterfaceComponent;
import org.uml.visual.widgets.actions.FieldPopupMenuProvider;
import org.uml.visual.widgets.actions.InterfacePopupMenuProvider;
import org.uml.visual.widgets.actions.LabelTextFieldEditorAction;
import org.uml.visual.widgets.actions.MethodPopupMenuProvider;

/**
 *
 * @author hrza
 */
public class InterfaceWidget extends UMLWidget{

    InterfaceComponent interfaceComponent;
    ClassDiagramScene scene;
    
    private static final Border RESIZE_BORDER = 
        org.netbeans.api.visual.border.BorderFactory.createResizeBorder(4, Color.black, true);
    private static final Border DEFAULT_BORDER = 
        org.netbeans.api.visual.border.BorderFactory.createLineBorder();
    
    private LabelWidget interfaceNameWidget;
    private Widget methodsWidget;
    
    private WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new LabelTextFieldEditorAction());
    
    private static final Border BORDER_4 = BorderFactory.createEmptyBorder(6);
    
    public InterfaceWidget(ClassDiagramScene scene, InterfaceComponent interfaceComponent) {
        super(scene);
        this.interfaceComponent = interfaceComponent;
        this.scene=scene;
        setChildConstraint(getImageWidget(), 1);
        setLayout(LayoutFactory.createVerticalFlowLayout());
        setBorder(org.netbeans.api.visual.border.BorderFactory.createLineBorder());
        setOpaque(true);
        setCheckClipping(true);
        
        Widget interfaceWidget = new Widget(scene);
        interfaceWidget.setLayout(LayoutFactory.createVerticalFlowLayout());
        interfaceWidget.setBorder(BORDER_4);
        
        LabelWidget stereotip = new LabelWidget(scene, "<<interface>>");
        stereotip.setAlignment(LabelWidget.Alignment.CENTER);
        interfaceWidget.addChild(stereotip);
        
        interfaceNameWidget = new LabelWidget(scene);
        interfaceNameWidget.setFont(scene.getDefaultFont().deriveFont(Font.BOLD));
        interfaceNameWidget.setAlignment(LabelWidget.Alignment.CENTER);
        interfaceWidget.addChild(interfaceNameWidget);
        addChild(interfaceWidget);
        interfaceNameWidget.getActions().addAction(editorAction);

        addChild(new SeparatorWidget(scene, SeparatorWidget.Orientation.HORIZONTAL));

        methodsWidget = new Widget(scene);
        methodsWidget.setLayout(LayoutFactory.createVerticalFlowLayout());
        methodsWidget.setOpaque(false);
        methodsWidget.setBorder(BORDER_4);
        LabelWidget operationName = new LabelWidget(scene);
        methodsWidget.addChild(operationName);
        addChild(methodsWidget);
        
        this.interfaceNameWidget.setLabel(interfaceComponent.getName());
        
        getActions().addAction(ActionFactory.createPopupMenuAction(new InterfacePopupMenuProvider(this)));
    }
    
    public void createMethodAction(Widget operationWidget) {
        methodsWidget.addChild(operationWidget);
    }
    
    public Widget createMethodWidget(String methodName) {
        Scene scene = getScene();
        Widget widget = new Widget(scene);
        widget.setLayout(LayoutFactory.createHorizontalFlowLayout());
        
        LabelWidget visibilityLabel = new LabelWidget(scene);
        visibilityLabel.setLabel("+");
        widget.addChild(visibilityLabel);
        
        LabelWidget labelWidget = new LabelWidget(scene);
        labelWidget.setLabel(methodName);
        labelWidget.setFont(scene.getDefaultFont().deriveFont(Font.ITALIC));
        widget.addChild(labelWidget);
        labelWidget.getActions().addAction(editorAction);
        labelWidget.getActions().addAction(ActionFactory.createPopupMenuAction(new MethodPopupMenuProvider(widget)));
        
        return widget;
    }
    
    @Override
    public InterfaceComponent getComponent() {
        return interfaceComponent;
    }

    @Override
    public String toString() {
        return interfaceNameWidget.getLabel();
    }

    public ClassDiagramScene getClassDiagramScene() {
        return scene;
    }

    @Override
    public LabelWidget getNameLabel() {
        return interfaceNameWidget;
    }
    
    
    
}
