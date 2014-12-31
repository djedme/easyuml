package org.uml.newcode;

import java.awt.Frame;
import java.io.File;
import java.util.ArrayList;
import org.netbeans.api.project.ui.OpenProjects;
import org.netbeans.api.project.Project;
import org.uml.model.ClassDiagram;

/**
 *
 * @author Uros
 */
public class GenerateCodeDialog extends javax.swing.JDialog {

    private ClassDiagram classDiagram;
    private ArrayList<Project> projectsList = new ArrayList<>();

    public GenerateCodeDialog(ClassDiagram classDiagram) {
        super((Frame) null, true);
        initComponents();
        buildComboBoxModel();
        this.classDiagram = classDiagram;
    }

    public final void buildComboBoxModel() {
        cbxProjects.removeAllItems();
        Project[] projects = OpenProjects.getDefault().getOpenProjects();

        for (Project project : projects) {
            if (project.getClass().getSimpleName().equals("J2SEProject")) {
                cbxProjects.addItem(project.getProjectDirectory().getName());
                projectsList.add(project);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cbxProjects = new javax.swing.JComboBox<String>();
        lblProject = new javax.swing.JLabel();
        btnGenerateCode = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(GenerateCodeDialog.class, "GenerateCodeDialog.title")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(lblProject, org.openide.util.NbBundle.getMessage(GenerateCodeDialog.class, "GenerateCodeDialog.lblProject.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(btnGenerateCode, org.openide.util.NbBundle.getMessage(GenerateCodeDialog.class, "GenerateCodeDialog.btnGenerateCode.text")); // NOI18N
        btnGenerateCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerateCodeActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(btnCancel, org.openide.util.NbBundle.getMessage(GenerateCodeDialog.class, "GenerateCodeDialog.btnCancel.text")); // NOI18N
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnGenerateCode, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblProject)
                        .addGap(18, 18, 18)
                        .addComponent(cbxProjects, 0, 326, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblProject)
                    .addComponent(cbxProjects, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGenerateCode)
                    .addComponent(btnCancel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGenerateCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerateCodeActionPerformed
        String projectPath = projectsList.get(cbxProjects.getSelectedIndex()).getProjectDirectory().getPath() + File.separator;
        ClassDiagramCodeGenerator.generateOrUpdateCode(classDiagram, projectPath);
        dispose();
    }//GEN-LAST:event_btnGenerateCodeActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnGenerateCode;
    private javax.swing.JComboBox<String> cbxProjects;
    private javax.swing.JLabel lblProject;
    // End of variables declaration//GEN-END:variables
}
