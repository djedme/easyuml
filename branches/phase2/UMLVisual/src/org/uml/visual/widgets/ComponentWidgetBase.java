package org.uml.visual.widgets;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ResizeControlPointResolver;
import org.netbeans.api.visual.action.ResizeProvider;
import org.netbeans.api.visual.action.TwoStateHoverProvider;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.ImageWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.ResourceTable;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.openide.util.Lookup;
import org.uml.model.ClassDiagramComponent;
import org.uml.visual.widgets.providers.ClassConnectProvider;
import org.uml.visual.widgets.providers.ClassSelectProvider;
import org.uml.visual.widgets.providers.ComponentSelectProvider;

/**
 *
 * @author "NUGS"
 */
abstract public class ComponentWidgetBase extends ImageWidget implements NameableWidget, Lookup.Provider {

    private static final Dimension MINDIMENSION = new Dimension(120, 120);
    private static final Border SELECTED_BORDER = BorderFactory.createResizeBorder(4, Color.black, false);
    private static final Border DEFAULT_BORDER = BorderFactory.createLineBorder();
    //atribut name
    protected LabelWidget nameWidget;
    ClassDiagramScene scene;
    
    public ComponentWidgetBase(ClassDiagramScene scene) {
        super(scene);
        this.scene = scene;
        setBorder(DEFAULT_BORDER);
        getActions().addAction(ActionFactory.createExtendedConnectAction(scene.getInterractionLayer(), new ClassConnectProvider()));
//        getActions().addAction(ActionFactory.createResizeAction(null, ActionFactory.createDefaultResizeProvider())); // /*new MyResizeProvider())*/);                       
        getActions().addAction(ActionFactory.createResizeAction());
        getActions().addAction(scene.createSelectAction());
        getActions().addAction(ActionFactory.createAlignWithMoveAction(scene.getMainLayer(), scene.getInterractionLayer(), null));
        
        getActions().addAction(scene.createObjectHoverAction());
        
        


        setMinimumSize(MINDIMENSION);

        nameWidget = new LabelWidget(scene);
        nameWidget.setFont(scene.getDefaultFont().deriveFont(Font.BOLD));
        nameWidget.setAlignment(LabelWidget.Alignment.CENTER);

        //Delete dugme, za sada ne funkcionise kako bi trebalo
//       InputMap inputMap = new InputMap ();
//       inputMap.put (KeyStroke.getKeyStroke (KeyEvent.VK_DELETE, 0, false), "myAction");        
//       ActionMap actionMap = new ActionMap ();
//       actionMap.put ("myAction", new DeleteClassAction (this));     
//       getActions().addAction(ActionFactory.createActionMapAction(inputMap, actionMap));
    }

    @Override
    public void notifyStateChanged(ObjectState previousState, ObjectState state) {
        // u ovu metodu ubaciti reakcija ne hover, focus, selected itd.
        super.notifyStateChanged(previousState, state);
        
        if (state.isWidgetFocused()) {
            if (state.isSelected() /**&& !previousState.isSelected() */) {
                setBorder(SELECTED_BORDER);
            } else {
                setBorder(DEFAULT_BORDER);
            }
        }else {
            setBorder(DEFAULT_BORDER);
        }
        
        if (state.isHovered()) {
            this.setCursor(new Cursor(Cursor.HAND_CURSOR));

        }else {
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        
        
    }

    abstract public ClassDiagramComponent getComponent();

    abstract public LabelWidget getNameLabel();

    // allready has getScene in widget ..
    public ClassDiagramScene getClassDiagramScene() {
        return scene;
    }
    
//    private class MyResizeProvider implements ResizeProvider {
//
//        @Override
//        public void resizingStarted(Widget widget) {
//            widget.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
//           System.out.println("resize finish");
//        }
//
//        @Override
//        public void resizingFinished(Widget widget) {
//            widget.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
//            System.out.println("resize finish");
//        }
//        
//    }
    
    
    public Graphics2D returnGraphics(){
        return this.getGraphics();
    }
    
    public void setGraphics(Graphics2D newGraphics){
        this.setGraphics(newGraphics);
    }
}
