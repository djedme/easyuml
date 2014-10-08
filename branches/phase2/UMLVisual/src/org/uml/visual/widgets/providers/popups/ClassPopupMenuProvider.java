package org.uml.visual.widgets.providers.popups;

import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.windows.WindowManager;
import org.uml.model.ClassComponent;
import org.uml.model.ClassDiagram;
import org.uml.model.Constructor;
import org.uml.model.Field;
import org.uml.model.Member;
import org.uml.model.Method;
import org.uml.model.MethodArgument;
import org.uml.model.RelationComponent;
import org.uml.model.Visibility;
import static org.uml.model.Visibility.PACKAGE;
import static org.uml.model.Visibility.PRIVATE;
import static org.uml.model.Visibility.PROTECTED;
import static org.uml.model.Visibility.PUBLIC;
import org.uml.visual.dialogs.PackageDialog;
import org.uml.visual.widgets.ClassWidget;
import org.uml.visual.widgets.ConstructorWidget;
import org.uml.visual.widgets.FieldWidget;
import org.uml.visual.widgets.MethodWidget;
import org.uml.visual.widgets.actions.LabelTextFieldEditorAction;
import org.uml.visual.widgets.actions.NameEditorAction;
import org.uml.visual.widgets.providers.MouseAdapterZaView;

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
    private JMenuItem addPackage;
    private JMenu visibilitySubmenu;
    private ButtonGroup visibilityGroup;
    private JCheckBoxMenuItem privateItem;
    private JCheckBoxMenuItem publicItem;
    private JCheckBoxMenuItem protectedItem;
    private JCheckBoxMenuItem packageItem;
    private JCheckBoxMenuItem abstractJCBMI;
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

        (addPackage = new JMenuItem("Set Package")).addActionListener(addPackageListener);
        menu.add(addPackage);

        menu.addSeparator();
        (deleteClass = new JMenuItem("Delete Class")).addActionListener(removeWidgetListener);
        menu.add(deleteClass);

        menu.addSeparator();

        visibilityGroup = new ButtonGroup();
        visibilitySubmenu = new JMenu("Visibility");
        visibilitySubmenu.add(publicItem = new JCheckBoxMenuItem("public"));
        publicItem.addActionListener(publicItemListener);
        visibilitySubmenu.add(privateItem = new JCheckBoxMenuItem("private"));
        privateItem.addActionListener(privateItemListener);
        visibilitySubmenu.add(protectedItem = new JCheckBoxMenuItem("protected"));
        protectedItem.addActionListener(protectedItemListener);
        visibilitySubmenu.add(packageItem = new JCheckBoxMenuItem("package"));
        packageItem.addActionListener(packageItemListener);
        visibilityGroup.add(publicItem);
        visibilityGroup.add(privateItem);
        visibilityGroup.add(protectedItem);
        visibilityGroup.add(packageItem);
        menu.add(visibilitySubmenu);

        menu.add(abstractJCBMI = new JCheckBoxMenuItem("abstract"));
        abstractJCBMI.addActionListener(abstractJCBMIListener);
        setSelectedButtons();
    }
    /**
     * Remove Widget Listener
     *
     *
     */
    ActionListener removeWidgetListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            classWidget.getComponent().getParentDiagram().removeComponent(classWidget.getName());
            ClassDiagram classDiagram = classWidget.getComponent().getParentDiagram();

            for (Map.Entry<String, RelationComponent> entry : classDiagram.getRelations().entrySet()) {
                RelationComponent relation = entry.getValue();
                if (relation.getSource().getName().equals(classWidget.getClassName()) || relation.getTarget().getName().equals(classWidget.getClassName())) {
                    classDiagram.removeRelation(relation.getName());
                    classWidget.getClassDiagramScene().removeEdge(relation);
                }
            }

            classWidget.removeFromParent();

        }
    };
    ActionListener addAtributeListener = new ActionListener() {
        int brojac = 1;

        @Override
        public void actionPerformed(ActionEvent e) {
            Field f = new Field("untitledField", null, Visibility.PRIVATE);
            addField(f);
            FieldWidget w = new FieldWidget(classWidget.getClassDiagramScene(), f);
            classWidget.addFieldWidget(w);
            classWidget.getScene().validate();

            WidgetAction nameEditorAction = ActionFactory.createInplaceEditorAction(new NameEditorAction(w));
            ActionFactory.getInplaceEditorController(nameEditorAction).openEditor(w.getNameLabel());
            MouseListener mouseListener = new MouseAdapterZaView(nameEditorAction);
            classWidget.getScene().getView().addMouseListener(mouseListener);
        }
    };
    ActionListener addMethodListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Method m = new Method("untitledMethod", null, new HashMap<String, MethodArgument>());
            addMethod(m);
            MethodWidget w = new MethodWidget(classWidget.getClassDiagramScene(), m);
            classWidget.addMethodWidget(w);
            classWidget.getScene().validate();

            WidgetAction nameEditorAction = ActionFactory.createInplaceEditorAction(new NameEditorAction(w));
            ActionFactory.getInplaceEditorController(nameEditorAction).openEditor(w.getNameLabel());
            MouseListener mouseListener = new MouseAdapterZaView(nameEditorAction);
            classWidget.getScene().getView().addMouseListener(mouseListener);

        }
    };
    ActionListener addConstructorListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Constructor c = new Constructor(classWidget.getName());
            classWidget.getComponent().addConstructor(c);
            ConstructorWidget w = new ConstructorWidget(classWidget.getClassDiagramScene(), c);
            classWidget.addConstructorWidget(w);
            classWidget.getScene().validate();


        }
    };
    ActionListener addPackageListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
//            String pack = "";
            PackageDialog pd = new PackageDialog(null, true, classWidget.getComponent(), classWidget.getClassDiagramScene().getUmlClassDiagram());
            pd.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
            pd.setTitle("Package");
            pd.setVisible(true);

//            classWidget.getComponent().setPack(pack);
//            Constructor c = new Constructor(classWidget.getName());
//            classWidget.getComponent().addConstructor(c);
//            ConstructorWidget w = new ConstructorWidget(classWidget.getClassDiagramScene(), c);
//            classWidget.addConstructorWidget(w);
            classWidget.getScene().validate();

//            w.getActions().addAction(classWidget.getScene().createWidgetHoverAction());
        }
    };
    ActionListener publicItemListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            classWidget.getComponent().setVisibility(Visibility.PUBLIC);

        }
    };
    ActionListener privateItemListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            classWidget.getComponent().setVisibility(Visibility.PRIVATE);
        }
    };
    ActionListener protectedItemListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            classWidget.getComponent().setVisibility(Visibility.PROTECTED);
        }
    };
    ActionListener packageItemListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            classWidget.getComponent().setVisibility(Visibility.PACKAGE);
        }
    };
    ActionListener abstractJCBMIListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Widget classNameWidget = classWidget.getChildren().get(0);
            ClassComponent classComponent = classWidget.getComponent();
            if (classComponent.isIsAbstract() == false) {
                classComponent.setIsAbstract(true);

                LabelWidget abstractLabel = new LabelWidget(classWidget.getScene(), "<<abstract>>");
                abstractLabel.setFont(classWidget.getScene().getDefaultFont().deriveFont(Font.ITALIC));
                abstractLabel.setAlignment(LabelWidget.Alignment.CENTER);

                classNameWidget.addChild(0, abstractLabel);

            } else {
                classComponent.setIsAbstract(false);
                classNameWidget.removeChild(classNameWidget.getChildren().get(0));
            }

        }
    };

    // TODO Dodati jos listenera za ClassWidgetMeni
    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        return menu;
    }

    private void setSelectedButtons() {
        ClassComponent classComponent = classWidget.getComponent();
        publicItem.setSelected(false);
        privateItem.setSelected(false);
        packageItem.setSelected(false);
        protectedItem.setSelected(false);
        abstractJCBMI.setSelected(false);
        switch (classComponent.getVisibility()) {
            case PUBLIC:
                publicItem.setSelected(true);
                break;
            case PRIVATE:
                privateItem.setSelected(true);
                break;
            case PACKAGE:
                packageItem.setSelected(true);
                break;
            case PROTECTED:
                protectedItem.setSelected(true);
                break;
        }
        if (classComponent.isIsAbstract()) {
            abstractJCBMI.setSelected(true);
        }
    }

    private int getCounter(Member member) {
        int brojac = 1;
        String name = member.getName();
        String broj = name.substring(name.length() - 1);
        if (broj.matches("[0-9]")) {
            name = name.substring(0, name.length() - 1);
            member.setName(name);
            brojac = Integer.parseInt(broj) + 1;
        }
        return brojac;
    }

    private void addField(Field f) {
        try {
            classWidget.getComponent().addField(f);
        } catch (RuntimeException ex) {
            int counter = getCounter(f);
            f.setName(f.getName() + counter);
            addField(f);
        }
    }

    private void addMethod(Method method) {
        try {
            classWidget.getComponent().addMethod(method);
        }catch (RuntimeException ex) {
            int counter = getCounter(method);
            method.setName(method.getName() + counter);
            addMethod(method);
        }
    }
}
