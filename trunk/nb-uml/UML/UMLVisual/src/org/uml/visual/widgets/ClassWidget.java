package org.uml.visual.widgets;

import java.awt.Color;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import javax.sound.sampled.FloatControl;
import javax.swing.JOptionPane;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.SeparatorWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.uml.model.ClassComponent;
import org.uml.model.Visibility;
import org.uml.visual.widgets.actions.NameEditorAction;
import org.uml.visual.widgets.providers.ClassPopupMenuProvider;
import org.uml.visual.widgets.providers.ClassWidgetAcceptProvider;

/**
 *
 * @author NUGS
 */
public class ClassWidget extends ComponentWidgetBase implements NameableWidget {

    //TODO Zoki da li si razmisljao da napravimo domen neki UmlElement pa da ovi nasledjuju to? 
    ClassComponent classComponent;
    private static final Border RESIZE_BORDER =
            BorderFactory.createResizeBorder(4, Color.black, true);
    private static final Border DEFAULT_BORDER =
            BorderFactory.createLineBorder();
    private Widget fieldsWidget;
    private Widget methodsWidget;
    //private WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new LabelTextFieldEditorAction());
    private WidgetAction nameEditorAction = ActionFactory.createInplaceEditorAction(new NameEditorAction(this));
    private static final Border BORDER_4 = BorderFactory.createEmptyBorder(6);
    private final Lookup lookup;

    public ClassWidget(ClassDiagramScene scene, ClassComponent classComponent) {
        super(scene);
        this.classComponent = classComponent;
        lookup = Lookups.fixed(classComponent, this);
        //lookup = Lookups.singleton(classComponent);
        this.scene = scene;
        setChildConstraint(getImageWidget(), 1);
        setLayout(LayoutFactory.createVerticalFlowLayout());
        setBorder(BorderFactory.createLineBorder());
        setOpaque(true);
        setCheckClipping(true);


        Widget classWidget = new Widget(scene); // mora ovako zbog layouta ne moze this 
        classWidget.setLayout(LayoutFactory.createVerticalFlowLayout());
        classWidget.setBorder(BORDER_4);

        //ImageWidget classImage= new ImageWidget(scene);
        //classImage.setImage(this.classComponent.getImage());


        classWidget.addChild(nameWidget);
        addChild(classWidget);

        nameWidget.getActions().addAction(nameEditorAction);

        addChild(new SeparatorWidget(scene, SeparatorWidget.Orientation.HORIZONTAL));

        fieldsWidget = new Widget(scene);
        fieldsWidget.setLayout(LayoutFactory.createVerticalFlowLayout());
        fieldsWidget.setOpaque(false);
        fieldsWidget.setBorder(BORDER_4);
        LabelWidget memberName = new LabelWidget(scene);
        fieldsWidget.addChild(memberName);
        addChild(fieldsWidget);

        addChild(new SeparatorWidget(scene, SeparatorWidget.Orientation.HORIZONTAL));

        methodsWidget = new Widget(scene);
        methodsWidget.setLayout(LayoutFactory.createVerticalFlowLayout());
        methodsWidget.setOpaque(false);
        methodsWidget.setBorder(BORDER_4);
        LabelWidget operationName = new LabelWidget(scene);
        methodsWidget.addChild(operationName);
        addChild(methodsWidget);

        this.nameWidget.setLabel(classComponent.getName());

        getActions().addAction(ActionFactory.createAcceptAction(new ClassWidgetAcceptProvider()));
        getActions().addAction(ActionFactory.createPopupMenuAction(new ClassPopupMenuProvider(this)));
        //getActions().addAction(ActionFactory.createResizeAction());
        //getActions().addAction(ActionFactory.createHoverAction(new ClassHoverProvider()));
    }

    @Override
    public String getName() {
        return nameWidget.getLabel();
    }

    public String getClassName() {
        return nameWidget.getLabel();
    }

    public void setClassName(String className) {
        this.nameWidget.setLabel(className);
    }

    public Widget createFieldWidget(String fieldName) {
        Scene scene = getScene();

        Widget fieldWidget = new Widget(scene);
        fieldWidget.setLayout(LayoutFactory.createHorizontalFlowLayout());

        //fieldWidget.addChild(createAtributeModifierPicker(scene));

        LabelWidget visibilityLabel = new LabelWidget(scene);
        visibilityLabel.setLabel("+");
        fieldWidget.addChild(visibilityLabel);

        LabelWidget labelWidget = new LabelWidget(scene);
        labelWidget.setLabel(fieldName);
        labelWidget.getActions().addAction(nameEditorAction);
        //labelWidget.getActions().addAction(ActionFactory.createPopupMenuAction(new FieldPopupMenuProvider(fieldWidget)));
        //dodato polje u classElement
        fieldWidget.addChild(labelWidget);

        return fieldWidget;
    }

    public Widget createMethodWidget(String methodName) {
        Scene scene = getScene();
        Widget widget = new Widget(scene);
        widget.setLayout(LayoutFactory.createHorizontalFlowLayout());

        //widget.addChild(createMethodModifierPicker(scene));

        LabelWidget visibilityLabel = new LabelWidget(scene);
        visibilityLabel.setLabel("+");
        widget.addChild(visibilityLabel);

        LabelWidget labelWidget = new LabelWidget(scene);
        labelWidget.setLabel(methodName);
        widget.addChild(labelWidget);
        labelWidget.getActions().addAction(nameEditorAction);
        //labelWidget.getActions().addAction(ActionFactory.createPopupMenuAction(new MethodPopupMenuProvider(widget)));

        return widget;
    }

    public void addFieldWidget(FieldWidget fieldWidget) {
        fieldsWidget.addChild(fieldWidget);
    }

    public void removeField(Widget memberWidget) {
        fieldsWidget.removeChild(memberWidget);
    }

    public void addMethodWidget(MethodWidget operationWidget) {
        methodsWidget.addChild(operationWidget);
    }

    public void addConstructorWidget(ConstructorWidget operationWidget) {
        methodsWidget.addChild(operationWidget);
    }

    public void removeMethod(Widget operationWidget) {
        methodsWidget.removeChild(operationWidget);
    }

    @Override
    public String toString() {
        return nameWidget.getLabel();
    }

//     @Override
//    public void notifyStateChanged(ObjectState previousState, ObjectState newState) {
//        super.notifyStateChanged(previousState, newState);
//        setBorder(newState.isSelected() ? (newState.isHovered() ? RESIZE_BORDER : DEFAULT_BORDER) : DEFAULT_BORDER);
//    }
    @Override
    public ClassComponent getComponent() {
        return classComponent;
    }

    @Override
    public LabelWidget getNameLabel() {
        return nameWidget;
    }

    @Override
    public void setName(String newName) {
        if (getNameLabel().getLabel().equals(newName)) {
            return;
        }

        String oldName = classComponent.getName();
        if (!classComponent.getParentDiagram().nameExists(newName)) {
            this.nameWidget.setLabel(newName);
            classComponent.setName(newName);
            classComponent.getParentDiagram().componentNameChanged(classComponent, oldName);
        } else {
            //WidgetAction editor = ActionFactory.createInplaceEditorAction(new LabelTextFieldEditorAction());
            //ActionFactory.getInplaceEditorController(nameEditorAction).openEditor(getNameLabel());
            JOptionPane.showMessageDialog(this.getScene().getView(), "Greska, ime vec postoji.");
        }
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }

    @Override
    public void setAttributes(String attributes) {
        String [] keyWords = attributes.split(" ");
        for (String keyWord: keyWords) {
            if (keyWord.equals("public")) {
                classComponent.setVisibility(Visibility.PUBLIC);
            }
            if (keyWord.equals("protected")) {
                classComponent.setVisibility(Visibility.PUBLIC);
            }
            if (keyWord.equals("private")) {
                classComponent.setVisibility(Visibility.PRIVATE);
            }
            if(keyWord.equals("abstract")) {
                classComponent.setIsAbstract(true);
            }
        }
    }

}
