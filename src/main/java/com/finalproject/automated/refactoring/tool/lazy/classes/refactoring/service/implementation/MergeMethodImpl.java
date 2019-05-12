package com.finalproject.automated.refactoring.tool.lazy.classes.refactoring.service.implementation;

import com.finalproject.automated.refactoring.tool.lazy.classes.refactoring.service.ClassAnalysis;
import com.finalproject.automated.refactoring.tool.lazy.classes.refactoring.service.MergeField;
import com.finalproject.automated.refactoring.tool.lazy.classes.refactoring.service.MergeMethod;
import com.finalproject.automated.refactoring.tool.model.ClassModel;
import com.finalproject.automated.refactoring.tool.model.MethodModel;
import com.finalproject.automated.refactoring.tool.model.PropertyModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author M. Reza Pahlevi
 * @version 1.0.0
 * @since 12 May 2019
 */

@Service
public class MergeMethodImpl implements MergeMethod {

    @Autowired
    private ClassAnalysis classAnalysis;

    @Override
    public String merge(ClassModel targetClass, ClassModel lazyClass) {
        StringBuilder refactoredString = new StringBuilder();
        for (MethodModel prop : lazyClass.getMethodModels()){
            refactoredString.append("\t");
            for(String s : prop.getKeywords()){
                refactoredString.append(s).append(" ");
            }
            //make method static
            if(classAnalysis.isMainClass(targetClass)){
                refactoredString.append("static ");
            }
            //handling constructor return type
            if(prop.getReturnType()==null){
                refactoredString.append("void ");
            }
            else {
                refactoredString.append(prop.getReturnType()).append(" ");
            }
            //handling constructor name
            if(prop.getName().equals(lazyClass.getName())){
                refactoredString.append("method").append(prop.getName()).append(" (");
            }
            else{
                refactoredString.append(prop.getName()).append(" (");
            }
            for(int i = 0; i < prop.getParameters().size(); i++){
                PropertyModel p = prop.getParameters().get(i);
                if(i > 0){
                    refactoredString.append(", ").append(p.getType()).append(" ").append(p.getName());
                }
                else{
                    refactoredString.append(p.getType()).append(" ").append(p.getName());
                }
            }
            refactoredString.append("){");
            refactoredString.append(prop.getBody()).append("\t}\n\n");
        }

        for (MethodModel prop : targetClass.getMethodModels()){
            refactoredString.append("\t");
            for(String s : prop.getKeywords()){
                refactoredString.append(s).append(" ");
            }
            refactoredString.append(prop.getReturnType()).append(" ");
            refactoredString.append(prop.getName()).append(" (");
            for(int i = 0; i < prop.getParameters().size(); i++){
                PropertyModel p = prop.getParameters().get(i);
                if(i > 0){
                    refactoredString.append(", ").append(p.getType()).append(" ").append(p.getName());
                }
                else{
                    refactoredString.append(p.getType()).append(" ").append(p.getName());
                }
            }
            refactoredString.append("){");
            //handling method & field call
            String body = prop.getBody();
            Pattern nameMethodPattern = Pattern.compile(lazyClass.getName()+"\\s+\\w+.*$", Pattern.MULTILINE);
            Matcher nameMethodMatcher = nameMethodPattern.matcher(prop.getBody());
            while(nameMethodMatcher.find()) {
                String classDeclaration = prop.getBody().substring(nameMethodMatcher.start(), nameMethodMatcher.end());
                String[] temp = classDeclaration.trim().split(" ");
                body = body.replaceAll(temp[1]+"\\.", "");
                body = body.replace(classDeclaration, "method"+lazyClass.getName() + classDeclaration.substring(classDeclaration.indexOf("("),classDeclaration.indexOf(")")) +");");
            }
            refactoredString.append(body).append("\t}\n\n");
        }
        return refactoredString.toString();
    }
}
