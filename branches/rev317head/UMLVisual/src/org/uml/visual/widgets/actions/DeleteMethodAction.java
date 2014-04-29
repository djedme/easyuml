/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.actions;

import java.awt.Point;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.widget.Widget;
import org.uml.visual.widgets.ClassWidget;

/**
 *
 * @author "NUGS"
 */
public class DeleteMethodAction implements SelectProvider {
    
        ClassWidget classWidget;
        public DeleteMethodAction(ClassWidget classWidget) {
           this.classWidget=classWidget;
        }

        @Override
        public boolean isAimingAllowed(Widget widget, Point point, boolean bln) {
            return true;
        }

        @Override
        public boolean isSelectionAllowed(Widget widget, Point point, boolean bln) {
            return true;
        }

        @Override
        public void select(Widget widget, Point point, boolean bln) {
            classWidget.removeMethod(widget.getParentWidget());
        }
    }
