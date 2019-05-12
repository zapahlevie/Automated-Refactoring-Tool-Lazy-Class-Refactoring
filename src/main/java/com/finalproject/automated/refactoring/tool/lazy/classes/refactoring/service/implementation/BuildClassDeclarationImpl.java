package com.finalproject.automated.refactoring.tool.lazy.classes.refactoring.service.implementation;

import com.finalproject.automated.refactoring.tool.lazy.classes.refactoring.service.BuildClassDeclaration;
import com.finalproject.automated.refactoring.tool.model.ClassModel;
import org.springframework.stereotype.Service;

/**
 * @author M. Reza Pahlevi
 * @version 1.0.0
 * @since 12 May 2019
 */

@Service
public class BuildClassDeclarationImpl implements BuildClassDeclaration {
    @Override
    public String declare(ClassModel targetClass, ClassModel lazyClass) {
        StringBuilder refactoredString = new StringBuilder();
        for (String key : targetClass.getKeywords()){
            refactoredString.append(key).append(" ");
        }
        refactoredString.append("class ").append(targetClass.getName()).append(" ");
        if(targetClass.getExtend()!=null){
            refactoredString.append(targetClass.getExtend()).append(" ");
        }
        if(targetClass.getImplement()!=null){
            refactoredString.append(targetClass.getImplement()).append(" ");
        }
        return refactoredString.toString();
    }
}
