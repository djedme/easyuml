/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.providers;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;
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
           classWidget.removeFromParent();
        }
    };
    ActionListener addAtributeListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Widget w = classWidget.createFieldWidget("Field");
            classWidget.createFieldAction(w);
            classWidget.getScene().validate();
            
            ActionFactory.getInplaceEditorController(editorAction).openEditor(w.getChildren().get(0));
            w.getScene().getView().addMouseListener(mouseListener);
//            w.getScene().getView().addMouseListener(new MouseAdapter() {
//
//                @Override
//                public void mousePressed(MouseEvent e) {
//                    super.mousePressed(e);
//                    ActionFactory.getInplaceEditorController(editorAction).closeEditor(true);
//                }
//            });
        }
    };
    ActionListener addMethodListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            
            Widget w = classWidget.createMethodWidget("Method()");
            classWidget.createMethodAction(w);
            classWidget.getScene().validate();
            
            ActionFactory.getInplaceEditorController(editorAction).openEditor(w.getChildren().get(0));
            w.getScene().getView().addMouseListener(mouseListener);
        }
    };
    ActionListener addConstructorListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            classWidget.createMethodAction(classWidget.createMethodWidget(classWidget.getClassName()+"()"));
            classWidget.getScene().validate();
            addConstructor.setEnabled(false);
        }
    };

    // TODO Dodati jos listenera za ClassWidgetMeni
    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        return menu;
    }
}
