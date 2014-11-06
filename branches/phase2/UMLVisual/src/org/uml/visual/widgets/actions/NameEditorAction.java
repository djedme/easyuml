/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.actions;

import java.util.HashMap;
import javax.swing.JOptionPane;
import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.members.Field;
import org.uml.model.members.Member;
import org.uml.model.members.Method;
import org.uml.model.members.MethodArgument;
import org.uml.model.members.Visibility;
import org.uml.visual.parser.WidgetParser;
import org.uml.visual.widgets.members.FieldWidget;
import org.uml.visual.widgets.members.MethodWidget;
import org.uml.visual.widgets.INameableWidget;

/**
 *
 * @author Jelena
 */
public class NameEditorAction implements TextFieldInplaceEditor {

    private INameableWidget nameable;
    private String oldName;

    public NameEditorAction(INameableWidget umlWidget) {
        this.nameable = umlWidget;
    }

    @Override
    public boolean isEnabled(Widget widget) {
        return true;
    }

    @Override
    public String getText(Widget widget) {
        oldName = ((LabelWidget) widget).getLabel();
        return oldName;
    }

    @Override
    public void setText(Widget widget, String string) {
        try {
            nameable.setName(string);
            nameable.setAttributes(string);
        } catch (RuntimeException ex) {
            String input = JOptionPane.showInputDialog("Name you have entered already exists, please enter another one.", getName(nameable));
            //setText(widget, input);
            //JOptionPane.showMessageDialog(null, "Name you have entered already exists, please enter another one.");
        }
        
        //TODO podesavam ime konkretnog elementa, ali ga zove i field editor i method editor, kako da resim to?
        //classWidget.umlClassElement.setName(string);
    }
    
    private String getName(INameableWidget widget) {
        Field f = new Field("", "", Visibility.PRIVATE);
        Method m = new Method("", "", new HashMap<String, MethodArgument>());
        WidgetParser wp = new WidgetParser();
        
        if (widget instanceof FieldWidget) {
            wp.fillFieldComponents(f, widget.getName());
            return f.getName();
        }
        if (widget instanceof MethodWidget) {
            wp.fillMethodComponents(m, widget.getName());
            return m.getName();
        }
        return "";
    }
}
