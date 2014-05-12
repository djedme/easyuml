/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.code;

import java.util.HashMap;
import java.util.Map;
import org.uml.model.ClassDiagramComponent;
import org.uml.model.Method;
import org.uml.model.MethodArgument;

/**
 *
 * @author Uros
 */
public class MethodCodeGenerator implements CodeGenerator {

    HashMap<String, Method> methods = new HashMap<String, Method>();

    public MethodCodeGenerator(HashMap<String, Method> methods) {

        this.methods = methods;
    }

    @Override
    public String generateCode() {
        String methodsString = "";
        for (Map.Entry<String, Method> entry : methods.entrySet()) {
            Method method = entry.getValue();
            methodsString += method.getSignature();
        }
        return methodsString;
    }

    @Override
    public void setClassDiagramComponent(ClassDiagramComponent component) {
    }

    String generateCodeMethodsForInterfaces() {
        String methodsString = "";
        for (Map.Entry<String, Method> entry : methods.entrySet()) {
            Method method = entry.getValue();
            methodsString += method.getSignatureForInterfaces();
        }
        return methodsString;
    }
}
