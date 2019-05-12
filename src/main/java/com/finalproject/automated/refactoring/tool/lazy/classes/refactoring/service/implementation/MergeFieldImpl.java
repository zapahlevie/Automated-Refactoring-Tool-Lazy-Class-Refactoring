package com.finalproject.automated.refactoring.tool.lazy.classes.refactoring.service.implementation;

import com.finalproject.automated.refactoring.tool.lazy.classes.refactoring.service.ClassAnalysis;
import com.finalproject.automated.refactoring.tool.lazy.classes.refactoring.service.MergeField;
import com.finalproject.automated.refactoring.tool.lazy.classes.refactoring.service.MergeImport;
import com.finalproject.automated.refactoring.tool.model.ClassModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author M. Reza Pahlevi
 * @version 1.0.0
 * @since 12 May 2019
 */

@Service
public class MergeFieldImpl implements MergeField {

    @Autowired
    private ClassAnalysis classAnalysis;

    @Override
    public String merge(ClassModel targetClass, ClassModel lazyClass) {
        StringBuilder refactoredString = new StringBuilder();
        for (String attr : lazyClass.getAttributes()){
            //make field static
            if(classAnalysis.isMainClass(targetClass)){
                String[] arr = attr.split(" ");
                if(arr[0].matches("public|private|protected")){
                    refactoredString.append("\t").append(arr[0]).append(" static");
                    for(int i = 1; i < arr.length; i++){
                        refactoredString.append(" ").append(arr[i]);
                    }
                    refactoredString.append(";\n");
                }
                else{
                    refactoredString.append("\t").append("static ").append(attr).append(";\n");
                }
            }
            else{
                refactoredString.append("\t").append(attr).append(";\n");
            }
        }
        for (String attr : targetClass.getAttributes()){
            refactoredString.append("\t").append(attr).append(";\n");
        }
        return refactoredString.toString();
    }
}
