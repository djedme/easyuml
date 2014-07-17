/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.reveng;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import java.util.LinkedList;
import java.util.List;
import javax.annotation.processing.AbstractProcessor;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;

import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import org.apache.commons.io.FileUtils;
import org.uml.reveng.classProcessing.ClassProcessing;

/**
 * Compilation process that processes all required files starts here.
 *
 * @author Milan Djoric
 */
public class Compilation {

    /**
     * Starts the compilation process and cleans compiled files afterwards.
     *
     * @param projectSource where the selected project is located
     * @param separator default file system separator character
     */
    public static void initiateCompilation(String projectSource, String separator) {
        //Creating an instance of Java compiler
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        //Creating an instance of Standard Java file manager
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        //Setting the root folder location and extension of files to search for
        String source = projectSource + separator + "src";
        String extension = "java";
        //Generating an array of filepaths corresponding to found files
        List<String> listOfFoundFiles = FileSearch.searchTheDirectory(new File(source), extension);
        String[] listaStr = new String[listOfFoundFiles.size()];
        for (int i = 0; i < listOfFoundFiles.size(); i++) {
            listaStr[i] = listOfFoundFiles.get(i).toString();
        }
        //Creating a list of files to be compiled
        Iterable<? extends JavaFileObject> compilationUnit = fileManager.getJavaFileObjects(listaStr);
        //Creating tempClasses file and emptying one, if it already exists
        String temporaryClassFolderName = "UMLTemp";
        String path = listOfFoundFiles.get(0).split("src")[0] + temporaryClassFolderName;
        File f = new File(path);
        f.mkdirs();
        try {
            FileUtils.cleanDirectory(f);
        } catch (IOException ex) {
            System.out.println("Could not erase folder's contents!");
        }
        //Setting the compilation options
        String encodingType = "UTF-8";
        Iterable<String> compOptions = Arrays.asList("-g", "-encoding", encodingType, "-d", path);
        //Creating a compilation task that will compile found files
        CompilationTask task = compiler.getTask(null, fileManager, null, compOptions, null, compilationUnit);
        //Creating a list that will hold annotation processors
        LinkedList<AbstractProcessor> processors = new LinkedList<AbstractProcessor>();
        //Adding a annotation processor(s) to the the above mentioned list
        processors.add(new CompilationProcessor());
        //Setting annotation prosessor(s) to the compilation task
        task.setProcessors(processors);
        //Staritng the compilation
        task.call();
        //Searching for .class files that have been made
        List<String> listOfFoundClasses = FileSearch.searchTheDirectory(new File(projectSource + separator
                + temporaryClassFolderName), "class");
        //Fill in data about Implements and Extends relations that the compiler couldn't get
        ClassProcessing.ClassProcessor(listOfFoundClasses, separator, temporaryClassFolderName);
        //Debug diagram
        GeneratedDiagramManager genDiag = GeneratedDiagramManager.getDefault();
        //Raises the flag if there are no .class files generated during th compilation process -
        //if it has failed (usually if the project selected has errors)
        if (f.listFiles().length == 0) {
            GeneratedDiagramManager.getDefault().setZeroClassesGenerated(true);
        } else {
            GeneratedDiagramManager.getDefault().setZeroClassesGenerated(false);
        }
        //Delete the folder containing compiled .class files
        try {
            FileUtils.deleteDirectory(f);
        } catch (IOException ex) {
            System.out.println("Directory for deletion not found!");
        }
    }
}
