package org.uml.visual.widgets.components;

import org.uml.visual.widgets.members.MemberWidgetBase;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JOptionPane;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.WeakListeners;
import org.uml.model.components.ComponentBase;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.IUMLWidget;
import org.uml.visual.widgets.TranslucentCompositeBorder;
import org.uml.visual.widgets.actions.ComponentWidgetKeyboardAction;
import org.uml.visual.widgets.actions.NameEditor;
import org.uml.visual.widgets.providers.ComponentConnectProvider;

/**
 *
 * @author "NUGS"
 */
// doesn't have to be ImageWidget
abstract public class ComponentWidgetBase extends Widget implements IUMLWidget, PropertyChangeListener {

    protected ComponentBase component;
    protected LabelWidget nameWidget;

    // attribute name
    protected static final Dimension MIN_DIMENSION = new Dimension(120, 120);
    protected static final Dimension CONTAINER_MIN_DIMENSION = new Dimension(112, 50);

    public static final int RESIZE_SIZE = 5;

    // Gray theme
    public static final Color DEFAULT_COLOR = new Color(0xFBFBFB);
    public static final Color HOVER_COLOR = new Color(0xF2F2F2);
    public static final Color HOVER_SELECTED_COLOR = new Color(0xF7F7F7);
    public static final Color SELECTED_COLOR = new Color(0xFAFAFA);

    // Sand theme
//    public static final Color DEFAULT_COLOR = new Color(0xF2D8A0);
//    public static final Color HOVER_COLOR = new Color(0xF27272);
//    public static final Color HOVER_SELECTED_COLOR = new Color(0x70F27272, true);
//    public static final Color SELECTED_COLOR = new Color(0x7FF27272, true);
    
    // Blue theme
//    public static final Color DEFAULT_COLOR = new Color(0x7FCCCCCC, true);
//    public static final Color HOVER_COLOR = new Color(0x7F003366, true);
//    public static final Color HOVER_SELECTED_COLOR = new Color(0x7003366, true);
//    public static final Color SELECTED_COLOR = new Color(0x7F03366, true);

    
//    public static final Border DEFAULT_BORDER = new TranslucentCompositeBorder(BorderFactory.createRoundedBorder(RESIZE_SIZE, RESIZE_SIZE, DEFAULT_COLOR, Color.BLACK));
//    public static final Border HOVER_BORDER = new TranslucentCompositeBorder(BorderFactory.createRoundedBorder(RESIZE_SIZE, RESIZE_SIZE, HOVER_COLOR, new Color(0x000047)));
    
    public static final Border DEFAULT_BORDER = new TranslucentCompositeBorder(BorderFactory.createEmptyBorder(RESIZE_SIZE), BorderFactory.createLineBorder());
    public static final Border HOVER_BORDER = new TranslucentCompositeBorder(BorderFactory.createEmptyBorder(RESIZE_SIZE), BorderFactory.createLineBorder(1, new Color(0x0000BB)));
    // +1, because dashed line falls into the widget (thickness of resize border is 
    public static final Border RESIZE_BORDER = new TranslucentCompositeBorder(BorderFactory.createResizeBorder(RESIZE_SIZE, new Color(0x0000BB), false), BorderFactory.createEmptyBorder(1));

    public static final Border EMPTY_BORDER_4 = BorderFactory.createEmptyBorder(4);

//    @Override
//    public Lookup getLookup() {
//        return Lookups.fixed(this, this.getScene());
//    }
    public ComponentWidgetBase(final ClassDiagramScene scene, ComponentBase component) {
        super(scene);
        this.component = component;
        this.component.addPropertyChangeListener(WeakListeners.propertyChange(this, this.component));
        this.component.setParentDiagram(scene.getClassDiagram());

        // Layout
        setBorder(DEFAULT_BORDER);
        setMinimumSize(MIN_DIMENSION);
        setLayout(LayoutFactory.createVerticalFlowLayout());
        setOpaque(true);
        //setCheckClipping(true);
        setBackground(DEFAULT_COLOR);

        nameWidget = new LabelWidget(scene);
        nameWidget.setFont(scene.getDefaultFont().deriveFont(Font.BOLD));
        nameWidget.setAlignment(LabelWidget.Alignment.CENTER);
        nameWidget.getActions().addAction(ActionFactory.createInplaceEditorAction(new NameEditor(this)));

        // **** Actions ****
        // Connect action - CTRL + click
        getActions().addAction(ActionFactory.createExtendedConnectAction(scene.getInterractionLayer(), new ComponentConnectProvider()));
        // Keybord actions over components
        getActions().addAction(new ComponentWidgetKeyboardAction());
        // Hover, select and resize
        getActions().addAction(scene.createWidgetHoverAction());
        getActions().addAction(scene.createSelectAction());
        getActions().addAction(ActionFactory.createAlignWithResizeAction(scene.getMainLayer(), scene.getInterractionLayer(), null, false));
        getActions().addAction(ActionFactory.createAlignWithMoveAction(scene.getMainLayer(), scene.getInterractionLayer(), null, false));

//        // TODO: Change detection - check how this works
//        // Too slow, should find another solution
//        addDependency(new Dependency() {
//            @Override
//            public void revalidateDependency() {
//                Point widgetBounds = getPreferredLocation();
//                int x = widgetBounds.x;
//                int y = widgetBounds.y;
//                        
//                Rectangle bounds = scene.getBounds();
//                int leftBound = bounds.x;
//                int topBound = bounds.y;
//                int rightBound = bounds.x + bounds.width;
//                int bottomBound = bounds.y + bounds.height;
//                
//                if(x <= leftBound) x = leftBound;
//                if(y <= topBound) y = topBound;
//                if(x >= rightBound - 10) x = rightBound - 10;
//                if(y >= bottomBound - 10) y = bottomBound - 10;
//                
//                if(x != widgetBounds.x || y != widgetBounds.y) setPreferredLocation(new Point(x, y));
//            }
//        });
    }

    @Override
    public void notifyStateChanged(ObjectState previousState, ObjectState state) {
        // Reaction to hover, focus and selection goes here
        super.notifyStateChanged(previousState, state);

        // resize reaction
        boolean select = state.isFocused();
        boolean wasSelected = previousState.isFocused();

        if (state.isHovered()) {
            if (!select) {
                setBorder(HOVER_BORDER);
                setBackground(HOVER_COLOR);
            } else {
                setBackground(HOVER_SELECTED_COLOR);
            }
        } else {
            if (!select) {
                setBorder(DEFAULT_BORDER);
                setBackground(DEFAULT_COLOR);
            } else {
                setBackground(SELECTED_COLOR);
            }
        }

        if (select && !wasSelected) {
            setBorder(RESIZE_BORDER);
        } else if (!select && wasSelected) {
            setBorder(DEFAULT_BORDER);
        }
    }

    abstract public ComponentBase getComponent();

    public LabelWidget getNameLabel() {
        return nameWidget;
    }

    public final String getName() {
        return nameWidget.getLabel();
    }

    public void setName(String newName) {
        if (!getName().equals(newName)) {
            if (component.getParentDiagram().signatureExists(newName)) {
                JOptionPane.showMessageDialog(getScene().getView(), "Name \"" + newName + "\" already exists!");
            } else {
                component.setName(newName);
            }
        }
    }

    @Override
    public String toString() {
        return nameWidget.getLabel();
    }

    // already has getScene in widget, but this is casted, so it's easier
    public ClassDiagramScene getClassDiagramScene() {
        return (ClassDiagramScene) getScene();
    }

    protected void addMember(Widget container, MemberWidgetBase member) {
        container.addChild(member);
        changedNotify();
    }

    protected void removeMember(Widget container, MemberWidgetBase member) {
        container.removeChild(member);
        changedNotify();
    }

    protected void changedNotify() {
        getClassDiagramScene().getUmlTopComponent().modify();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("name".equals(evt.getPropertyName())) {
            String newName = (String) evt.getNewValue();
            nameWidget.setLabel(newName);
        }
        changedNotify();
        getScene().validate();
    }

    @Override
    public void setSignature(String signature) {
        component.setName(signature);
    }

    @Override
    public String getSignature() {
        return component.getSignature();
    }

    @Override
    // To achieve resize cursors and move cursor
    protected Cursor getCursorAt(Point location) {
        if (getState().isFocused()) {
            Border border = getBorder();
            Rectangle bounds = getBounds();
            Insets insets = border.getInsets();
            int thickness = insets.bottom;

            Rectangle topLeft = new Rectangle(bounds.x, bounds.y, thickness, thickness);
            Rectangle topRight = new Rectangle(bounds.x + bounds.width - thickness, bounds.y, thickness, thickness);
            Rectangle bottomLeft = new Rectangle(bounds.x, bounds.y + bounds.height - thickness, thickness, thickness);
            Rectangle bottomRight = new Rectangle(bounds.x + bounds.width - thickness, bounds.y + bounds.height - thickness, thickness, thickness);

            Point center = new Point(bounds.x + bounds.width / 2, bounds.y + bounds.height / 2);

            Rectangle topCenter = new Rectangle(center.x - thickness / 2, bounds.y, thickness, thickness);
            Rectangle bottomCenter = new Rectangle(center.x - thickness / 2, bounds.y + bounds.height - thickness, thickness, thickness);
            Rectangle leftCenter = new Rectangle(bounds.x, center.y - thickness / 2, thickness, thickness);
            Rectangle rightCenter = new Rectangle(bounds.x + bounds.width - thickness, center.y - thickness / 2, thickness, thickness);

            Rectangle[] rects = new Rectangle[]{
                topLeft,
                topRight,
                bottomLeft,
                bottomRight,
                topCenter,
                bottomCenter,
                leftCenter,
                rightCenter
            };

            Cursor[] cursors = new Cursor[]{
                Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR),
                Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR),
                Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR),
                Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR),
                Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR),
                Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR),
                Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR),
                Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR)
            };

            for (int i = 0; i < rects.length; i++) {
                if (rects[i].contains(location)) {
                    return cursors[i];
                }
            }
            return Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
        } else {
            return Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
        }
    }

}
