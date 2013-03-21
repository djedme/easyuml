/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.providers;

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
import org.uml.visual.widgets.providers.MouseAdapterZaView;
import org.uml.visual.widgets.InterfaceWidget;
import org.uml.visual.widgets.actions.LabelTextFieldEditorAction;

/**
 *
 * @author Jelena
 */
public class InterfacePopupMenuProvider implements PopupMenuProvider{
    private InterfaceWidget interfaceWidget;
    private JPopupMenu menu;
    private JMenuItem deleteInterface;
    private JMenuItem addMethod;
    WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new LabelTextFieldEditorAction());
    MouseListener mouseListener = new MouseAdapterZaView(editorAction);

    public InterfacePopupMenuProvider(InterfaceWidget interfaceWidget) {
        this.interfaceWidget = interfaceWidget;
        menu = new JPopupMenu("Interface Menu");       
        
        (addMethod = new JMenuItem("Add Method")).addActionListener(addMethodListener);
        menu.add(addMethod);
        menu.addSeparator();
        (deleteInterface= new JMenuItem("Delete Interface")).addActionListener(removeWidgetListener);
        menu.add(deleteInterface);
    }
    
    ActionListener addMethodListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            
            Widget w = interfaceWidget.createMethodWidget("Method()");
            interfaceWidget.createMethodAction(w);
            interfaceWidget.getScene().validate();
            
            ActionFactory.getInplaceEditorController (editorAction).openEditor(w.getChildren().get(1));
            w.getScene().getView().addMouseListener(mouseListener);
        }
    };
    
    ActionListener removeWidgetListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            interfaceWidget.removeFromParent();
            interfaceWidget.getScene().validate();
        }
    };

    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        return menu;
    }
    
}
