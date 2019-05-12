package com.finalproject.automated.refactoring.tool.lazy.classes.refactoring.service.implementation;

import com.finalproject.automated.refactoring.tool.lazy.classes.refactoring.service.MergeImport;
import com.finalproject.automated.refactoring.tool.model.ClassModel;

import java.util.List;

/**
 * @author M. Reza Pahlevi
 * @version 1.0.0
 * @since 12 May 2019
 */

public class MergeImportImpl implements MergeImport {
    @Override
    public String mergeImport(ClassModel targetClass, ClassModel lazyClass) {
        StringBuilder refactoredString = new StringBuilder();
        for (String i : targetClass.getImports()){
            refactoredString.append(i).append(";\n");
        }
        for (String i : lazyClass.getImports()){
            //remove duplicated imports
            if(!duplicateInList(i, targetClass.getImports())){
                refactoredString.append(i).append(";\n");
            }
        }
        return refactoredString.toString();
    }

    public boolean duplicateInList(String s, List<String> list){
        for (String l : list){
            if(s.equals(l)){
                return true;
            }
        }
        return false;
    }
}
