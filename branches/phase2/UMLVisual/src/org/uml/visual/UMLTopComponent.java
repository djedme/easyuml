package org.uml.visual;

//import com.thoughtworks.xstream.XStream;
//import com.thoughtworks.xstream.converters.Converter;
//import com.thoughtworks.xstream.converters.MarshallingContext;
//import com.thoughtworks.xstream.converters.UnmarshallingContext;
//import com.thoughtworks.xstream.io.HierarchicalStreamReader;
//import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
//import com.thoughtworks.xstream.io.xml.StaxDriver;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.*;
import javax.swing.JScrollPane;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.netbeans.api.settings.ConvertAsProperties;
import org.netbeans.api.visual.widget.EventProcessingType;
import org.netbeans.spi.actions.AbstractSavable;
import org.openide.*;
import org.openide.NotifyDescriptor.Confirmation;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.filesystems.FileObject;
import org.openide.loaders.SaveAsCapable;
import org.openide.util.*;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.*;
import org.openide.windows.WindowManager;
import org.uml.explorer.ClassDiagramNode;
import org.uml.model.ClassDiagram;
import org.uml.visual.palette.PaletteSupport;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.xmlSerialization.ClassDiagramXmlSerializer;

@ConvertAsProperties(
        dtd = "-//org.uml.visual//UML//EN",
        autostore = false)
@TopComponent.Description(
        preferredID = "UMLTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "org.uml.visual.UMLTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_UMLAction",
        preferredID = "UMLTopComponent")
@Messages({
    "CTL_UMLAction=UML Designer",
    "CTL_UMLTopComponent=UML Class Diagram Window",
    "HINT_UMLTopComponent=This is a UML Class Diagram window"
})
public final class UMLTopComponent extends TopComponent implements ExplorerManager.Provider {

    private final ClassDiagramScene classDiagramScene;
    private final JScrollPane classDiagramPanel;
    private FileObject fileObject;
    private final InstanceContent content = new InstanceContent();
    private final ExplorerManager explorerManager = new ExplorerManager();

    // should never be called, except from Window -> UML Designer
    public UMLTopComponent() {
        this(new ClassDiagram());
    }

    public UMLTopComponent(ClassDiagram classDiagram) {
        initComponents();
        setName(classDiagram.getName());
        setToolTipText(Bundle.HINT_UMLTopComponent());
        setFocusable(true);

        classDiagramScene = new ClassDiagramScene(classDiagram, this);
        classDiagramPanel = new JScrollPane();
        classDiagramPanel.setViewportView(classDiagramScene.createView());
        classDiagramScene.setMaximumBounds(new Rectangle(0, 0, 2000, 2000));
        classDiagramScene.setMaximumSize(new Dimension(2000, 2000));
        classDiagramScene.validate();
        classDiagramScene.setCheckClipping(true);
        classDiagramScene.setKeyEventProcessingType(EventProcessingType.FOCUSED_WIDGET_AND_ITS_CHILDREN);

        add(classDiagramPanel, BorderLayout.CENTER);

        classDiagramScene.validate();

        explorerManager.setRootContext(new ClassDiagramNode(classDiagram));

        Lookup fixedLookup = Lookups.fixed(
                classDiagramScene, // for saving of diagram
                PaletteSupport.getPalette(), // palette
                new UMLNavigatorLookupHint(), // navigator
                new UMLTopComponentSaveAs(this) // SaveAs always enabled    
        );

        AbstractLookup abstrLookup = new AbstractLookup(content);

        ProxyLookup jointLookup = new ProxyLookup(
                fixedLookup,
                abstrLookup, // for modifying (adding UMLTopComponentSavable to Lookup)
                classDiagramScene.getLookup(), // node creation and selection in explorer
                ExplorerUtils.createLookup(explorerManager, getActionMap()) // nodes for properties from UMLTopComponent
        );

        associateLookup(jointLookup);

        //classDiagramScene.getMainLayer().bringToFront();
        // pomereno iz konstruktora class diagram scene
        //GraphLayout graphLayout = GraphLayoutFactory.createOrthogonalGraphLayout(classDiagramScene, true);
        //graphLayout.layoutGraph(classDiagramScene);
//        String SAVE_ACTION = "mySaveAction";
//        getInputMap().put(KeyStroke.getKeyStroke("q"), SAVE_ACTION);
//        getActionMap().put(SAVE_ACTION, new AbstractAction(SAVE_ACTION) {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                saveTopComponent();
//            }
//        });
    }

    @Override
    public void componentOpened() {
        WindowManager.getDefault().findTopComponent("ExplorerTopComponent").open();
        WindowManager.getDefault().findTopComponent("properties").open();
    }

    @Override
    public void componentActivated() {
        super.componentActivated();
        classDiagramScene.selectScene();
    }

    @Override
    protected void componentDeactivated() {
        super.componentDeactivated();
        // When deactivating UMLTopComponent, deselect everything from scene,
        // except when we are accessing linked TCs
        if (!isActivatedLinkedTC()) {
            classDiagramScene.deselectAll();
        }
    }

    public boolean isActivatedLinkedTC() {
        TopComponent activatedTC = WindowManager.getDefault().getRegistry().getActivated();
        TopComponent propertiesTC = WindowManager.getDefault().findTopComponent("properties");
        TopComponent explorerTC = WindowManager.getDefault().findTopComponent("ExplorerTopComponent");
        if (activatedTC == propertiesTC || activatedTC == explorerTC || activatedTC == this) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return explorerManager;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        viewPane = new javax.swing.JScrollPane();

        setLayout(new java.awt.BorderLayout());
        add(viewPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane viewPane;
    // End of variables declaration//GEN-END:variables

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    public ClassDiagramScene getClassDiagramScene() {
        return classDiagramScene;
    }

    public void notifyModified() {
        // in other case, when we are doing reverse engineering, modify is called before the lookup is
        // associated with TopComponent, so there is an exception when we associate it later, because it already exists
        if (fileObject != null) {
            if (getLookup().lookup(UMLTopComponentSave.class) != null) {
                // Can do this as UMLTopComponentSave equals method compares TCs, which are the same
                // Could be better to have a private field which holds current UMLTopComponentSave, in order not avoid unnecessary object creation
                content.remove(new UMLTopComponentSave(this, content));
            }
            content.add(new UMLTopComponentSave(this, content));
        }
    }

    public void setFileObject(FileObject fileObject) {
        this.fileObject = fileObject;
    }

    public FileObject getFileObject() {
        return fileObject;
    }

    /**
     * Serialises given UMLTopComponent to .cdg XML file.
     *
     * @param path to save (serialise) to
     */
    void saveTopComponentToPath(String path) {
        FileOutputStream fileOut = null;
        XMLWriter writer = null;
        try {
            fileOut = new FileOutputStream(path);

            ClassDiagramXmlSerializer serializer = ClassDiagramXmlSerializer.getInstance();
            serializer.setClassDiagram(classDiagramScene.getClassDiagram());
            serializer.setClassDiagramScene(classDiagramScene);
//                XStream xstream = new XStream(new StaxDriver());
//                xstream.autodetectAnnotations(true);
//                xstream.registerConverter(new Converter() {
//                    
//                    @Override
//                    public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext mc) {
//                        Point point = (Point) o;
//                        writer.addAttribute("x", Integer.toString(point.x));
//                        writer.addAttribute("y", Integer.toString(point.y));
//                    }
//                    
//                    @Override
//                    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext uc) {
//                        Point point = new Point();
//                        point.x = Integer.parseInt(reader.getAttribute("x"));
//                        point.y = Integer.parseInt(reader.getAttribute("y"));
//                        return point;
//                    }
//                    
//                    @Override
//                    public boolean canConvert(Class type) {
//                        if (type == Point.class) {
//                            return true;
//                        } else {
//                            return false;
//                        }
//                    }
//                });
////                xstream.setMode(XStream.NO_REFERENCES);
//                System.out.println(xstream.toXML(classDiagramScene.getClassDiagram()));

            Document document = DocumentHelper.createDocument();
            // document.setXMLEncoding("UTF-8");
            Element root = document.addElement("ClassDiagram");
            serializer.serialize(root);
            OutputFormat format = OutputFormat.createPrettyPrint();
            writer = new XMLWriter(fileOut, format);
            writer.write(document);
            System.out.println("Diagram file saved to " + path);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            try {
                if (fileOut != null) fileOut.close();
                if (writer != null) writer.close();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    public void saveTopComponent() {
        saveTopComponentToPath(fileObject.getPath());
    }

    class UMLTopComponentSaveAs implements SaveAsCapable {

        UMLTopComponent umlTopComponent;

        public UMLTopComponentSaveAs(UMLTopComponent umlTopComponent) {
            this.umlTopComponent = umlTopComponent;
        }

        @Override
        public void saveAs(FileObject folder, String name) throws IOException {
            String path = folder.getPath() + "/" + name;
            umlTopComponent.saveTopComponentToPath(path);
        }
    }

    class UMLTopComponentSave extends AbstractSavable {

        private final UMLTopComponent umlTopComponent;
        private final InstanceContent ic;
//        private static int maxID = 0;
//        private final int ID = maxID++;

        public UMLTopComponentSave(UMLTopComponent topComponent, InstanceContent instanceContent) {
            this.umlTopComponent = topComponent;
            this.ic = instanceContent;
            register();
        }

        @Override
        protected String findDisplayName() {
            return "Diagram " + umlTopComponent.getName(); // get display name somehow
        }

        @Override
        protected void handleSave() throws IOException {
//            Confirmation msg = new NotifyDescriptor.Confirmation(
//                    "Do you want to save \"" + umlTopComponent.getName() + "\"?",
//                    NotifyDescriptor.OK_CANCEL_OPTION,
//                    NotifyDescriptor.QUESTION_MESSAGE);
//            Object result = DialogDisplayer.getDefault().notify(msg);
//            //When user clicks "Yes", indicating they really want to save,
//            //we need to disable the Save button and Save menu item,
//            //so that it will only be usable when the next change is made
//            // save 'obj' somehow
//            if (NotifyDescriptor.OK_OPTION.equals(result)) {
            umlTopComponent.saveTopComponent();

            ic.remove(this);
//            } else {
//                throw new IOException();
//            }
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof UMLTopComponentSave) {
                return ((UMLTopComponentSave) other).umlTopComponent.equals(umlTopComponent);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return umlTopComponent.hashCode();
//            return ID;
        }
    }

//    Project selectedProject;
//
//    @Override
//    public void resultChanged(LookupEvent le) {
//        Lookup.Result localResult = (Result)le.getSource();
//        Collection<Object> coll = localResult.allInstances();
//        if (!coll.isEmpty()){
//            for (Object selectedItem : coll){
//                if (selectedItem instanceof Project) selectedProject = (Project) selectedItem;
//            }
//        }
//        
//         FileObject folder = selectedProject.getProjectDirectory();
//         String path = folder.getPath();
//    }
}
