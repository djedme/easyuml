/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.providers;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.Map;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.ClassDiagram;
import org.uml.model.RelationComponent;
import org.uml.visual.widgets.ClassWidget;
import org.uml.visual.widgets.actions.LabelTextFieldEditorAction;

/**
 *
 * @author hrza
 */
public class ClassPopupMenuProvider implements PopupMenuProvider {

    private ClassWidget classWidget;
    private JPopupMenu menu;
    private JMenuItem deleteClass;
    private JMenuItem addField;
    private JMenuItem addMethod;
    private JMenuItem addConstructor;
    WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new LabelTextFieldEditorAction());
    MouseListener mouseListener = new MouseAdapterZaView(editorAction);

    public ClassPopupMenuProvider(ClassWidget classWidget) {
        this.classWidget = classWidget;
        menu = new JPopupMenu("Class Menu");

        (addConstructor = new JMenuItem("Add Constructor")).addActionListener(addConstructorListener);
        menu.add(addConstructor);        
        
        (addField = new JMenuItem("Add Field")).addActionListener(addAtributeListener);
        menu.add(addField);
        (addMethod = new JMenuItem("Add Method")).addActionListener(addMethodListener);
        menu.add(addMethod);
        menu.addSeparator();
        (deleteClass = new JMenuItem("Delete Class")).addActionListener(removeWidgetListener);
        menu.add(deleteClass);        
        
    }
    /**
     * Remove Widget Listener
     *
     *
     */
    ActionListener removeWidgetListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
           classWidget.getComponent().getParentDiagram().removeComponent(classWidget.getClassName());
           ClassDiagram classDiagram = classWidget.getComponent().getParentDiagram();
           
           for(Map.Entry<String,RelationComponent> entry : classDiagram.getRelations().entrySet()) {
                RelationComponent relation = entry.getValue();
                if (relation.getSource().getName().equals(classWidget.getClassName())||relation.getTarget().getName().equals(classWidget.getClassName())) {
                   classDiagram.removeRelation(relation.getName());
                   classWidget.getClassDiagramScene().removeEdge(relation);
               }
           }
            
           classWidget.removeFromParent();
           
        }
    };
    ActionListener addAtributeListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Widget w = classWidget.createFieldWidget("Field");
            classWidget.createFieldAction(w);
            classWidget.getScene().validate();
            
            ActionFactory.getInplaceEditorController(editorAction).openEditor(w.getChildren().get(1));
            w.getScene().getView().addMouseListener(mouseListener);
         
        }
    };
    ActionListener addMethodListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            
            Widget w = classWidget.createMethodWidget("Method()");
            classWidget.createMethodAction(w);
            classWidget.getScene().validate();
            
            ActionFactory.getInplaceEditorController(editorAction).openEditor(w.getChildren().get(1));
            w.getScene().getView().addMouseListener(mouseListener);
        }
    };
    ActionListener addConstructorListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            classWidget.createMethodAction(classWidget.createMethodWidget(classWidget.getClassName()+"()"));
            classWidget.getScene().validate();
        }
    };

    // TODO Dodati jos listenera za ClassWidgetMeni
    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        return menu;
    }
}
