/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.actions;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.Widget;
import org.uml.visual.widgets.ClassWidget;
import org.uml.visual.widgets.EnumWidget;

/**
 *
 * @author Jelena
 */
public class EnumPopupMenuProvider implements PopupMenuProvider{
    
    private EnumWidget enumWidget;
    private JPopupMenu menu;
    private JMenuItem deleteClass;
    private JMenuItem addField;
    private JMenuItem addMethod;
    private JMenuItem addConstructor;

    public EnumPopupMenuProvider(EnumWidget enumWidget) {
        this.enumWidget = enumWidget;
        menu = new JPopupMenu("Enum Menu");

        (addField = new JMenuItem("Add Field")).addActionListener(addAtributeListener);
        menu.add(addField);
        (addMethod = new JMenuItem("Add Method")).addActionListener(addMethodListener);
        menu.add(addMethod);
        (deleteClass = new JMenuItem("Delete Enum")).addActionListener(removeWidgetListener);
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
           enumWidget.removeFromParent();
        }
    };
    ActionListener addAtributeListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Widget w = enumWidget.createFieldWidget("Field");
            enumWidget.createFieldAction(w);
            enumWidget.getScene().validate();
        }
    };
    ActionListener addMethodListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            
            enumWidget.createMethodAction(enumWidget.createMethodWidget("Method()"));
            enumWidget.getScene().validate();
        }
    };

    // TODO Dodati jos listenera za ClassWidgetMeni
    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        return menu;
    }
}
