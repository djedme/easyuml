/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.providers.unused;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;
import org.openide.windows.WindowManager;
import org.uml.jung.JUNGEngine;
import org.uml.model.ClassComponent;
import org.uml.model.EnumComponent;
import org.uml.model.InterfaceComponent;
import org.uml.visual.dialogs.AddRelationDialog;
import org.uml.visual.dialogs.GenerateCodeDialog;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.ClassWidget;
import org.uml.visual.widgets.EnumWidget;
import org.uml.visual.widgets.InterfaceWidget;
import org.uml.visual.widgets.actions.NameEditorAction;
import org.uml.visual.widgets.providers.MouseAdapterZaView;

/**
 *
 * @author hrza
 */
public class ScenePopupMenuProvider implements PopupMenuProvider {

    private final JPopupMenu sceneMenu;
    private JMenuItem createClassItem;
    private JMenuItem createInterfaceItem;
    private JMenuItem createEnumItem;
    private JMenuItem createRelationshipItem;
    private JMenuItem generateCode;
    private JMenuItem exportAsImage;
    private JMenuItem applyJUNGLayout;
    private ClassDiagramScene scene;
    private Point popupPoint;

    public ScenePopupMenuProvider(ClassDiagramScene scene) {
        this.scene = scene;
        sceneMenu = new JPopupMenu("Diagram menu!");
        generateMenu();
    }

    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        popupPoint = point;
        return sceneMenu;
    }

    public final void generateMenu() {

        createClassItem = new JMenuItem("Add Class");
        createClassItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // add neww class component and widget to scene
                // here we should just add new class component to model, and the scene should be updated elsewhere
                ClassWidget widget = (ClassWidget) scene.addNode(new ClassComponent());
                widget.setPreferredLocation(popupPoint);
                scene.validate();
                WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new NameEditorAction(widget));

                // open editor for class name
                ActionFactory.getInplaceEditorController(editorAction).openEditor(widget.getNameLabel());
                scene.getView().addMouseListener(new MouseAdapterZaView(editorAction));
            }
        });
        createInterfaceItem = new JMenuItem("Add Interface");
        createInterfaceItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InterfaceWidget widget = (InterfaceWidget) scene.addNode(new InterfaceComponent());
                widget.setPreferredLocation(popupPoint);
                scene.validate();
                WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new NameEditorAction(widget));

                ActionFactory.getInplaceEditorController(editorAction).openEditor(widget.getNameLabel());
                scene.getView().addMouseListener(new MouseAdapterZaView(editorAction));
            }
        });
        createEnumItem = new JMenuItem("Add Enum");
        createEnumItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EnumWidget widget = (EnumWidget) scene.addNode(new EnumComponent());
                widget.setPreferredLocation(popupPoint);
                scene.validate();
                WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new NameEditorAction(widget));

                ActionFactory.getInplaceEditorController(editorAction).openEditor(widget.getNameLabel());
                scene.getView().addMouseListener(new MouseAdapterZaView(editorAction));
            }
        });

        createRelationshipItem = new JMenuItem("Add Relationship");
        createRelationshipItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddRelationDialog dialog = new AddRelationDialog(null, scene, true);
                dialog.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
                dialog.setVisible(true);
            }
        });

        generateCode = new JMenuItem("Generate code");
        generateCode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GenerateCodeDialog dialog = new GenerateCodeDialog(null, true, scene.getClassDiagram());
                dialog.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
                dialog.setTitle("Generate code");
                dialog.setVisible(true);
            }
        });

        exportAsImage = new JMenuItem("Export As Image");

        exportAsImage.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        BufferedImage img = new BufferedImage(
                                scene.getView().getWidth(),
                                scene.getView().getHeight(),
                                BufferedImage.TYPE_4BYTE_ABGR);
                        Graphics2D graphics = img.createGraphics();
                        scene.paint(graphics);
                        graphics.dispose();
                        JFileChooser chooser = new JFileChooser();
                        chooser.setFileFilter(new FileNameExtensionFilter(
                                        "Portable Network Graphics (.png)", "png"));
                        if (chooser.showSaveDialog(scene.getView()) == JFileChooser.APPROVE_OPTION) {
                            File f = chooser.getSelectedFile();
                            if (!f.getName().toLowerCase().endsWith(".png")) {
                                f = new File(f.getParentFile(), f.getName() + ".png");

                                try {
                                    ImageIO.write(img, "png", f);
                                } catch (IOException ex) {
                                    //Logger.getLogger(getName()).warning(ex.toString());
                                }
                            }
                        }

                    }
                });

        applyJUNGLayout = new JMenuItem("Arrange diagram");
        applyJUNGLayout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JUNGEngine je = new JUNGEngine(scene);
                je.applyJUNGLayout();
            }
        });

        sceneMenu.add(createClassItem);

        sceneMenu.add(createInterfaceItem);

        sceneMenu.add(createEnumItem);

        sceneMenu.add(createRelationshipItem);

        sceneMenu.add(generateCode);

        sceneMenu.add(exportAsImage);

        sceneMenu.add(applyJUNGLayout);
    }
}
