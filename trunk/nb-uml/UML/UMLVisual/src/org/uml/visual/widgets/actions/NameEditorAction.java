/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.actions;

import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;
import org.uml.visual.widgets.Nameable;

/**
 *
 * @author Jelena
 */
public class NameEditorAction implements TextFieldInplaceEditor {

    Nameable nameable;

    public NameEditorAction(Nameable umlWidget) {
        this.nameable = umlWidget;
    }
    private String oldName;

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

        nameable.setName(string);



        //TODO podesavam ime konkretnog elementa, ali ga zove i field editor i method editor, kako da resim to?
        //classWidget.umlClassElement.setName(string);
    }
}
