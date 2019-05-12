package com.finalproject.automated.refactoring.tool.lazy.classes.refactoring.service.implementation;

import com.finalproject.automated.refactoring.tool.lazy.classes.refactoring.service.LazyClassRefactoring;
import com.finalproject.automated.refactoring.tool.lazy.classes.refactoring.service.MergeImport;
import com.finalproject.automated.refactoring.tool.model.ClassModel;
import com.finalproject.automated.refactoring.tool.model.MethodModel;
import com.finalproject.automated.refactoring.tool.model.PropertyModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author M. Reza Pahlevi
 * @version 1.0.0
 * @since 29 April 2019
 */

@Service
public class LazyClassRefactoringImpl implements LazyClassRefactoring {

    @Autowired
    private MergeImport mergeImport;

    @Override
    public String refactor(ClassModel targetClass, ClassModel lazyClass) {
        boolean isMainClass = false;

        String refactoredString = "";
        refactoredString += mergeImport.mergeImport(targetClass, lazyClass);
        for (String key : targetClass.getKeywords()){
            refactoredString+="\n"+key+" ";
        }
        refactoredString+="class "+targetClass.getName()+" ";
        if(targetClass.getExtend()!=null){
            refactoredString+=targetClass.getExtend()+" ";
        }
        if(targetClass.getExtend()!=null){
            refactoredString+=targetClass.getExtend()+" ";
        }
        refactoredString+="{\n";
        Pattern pattern = Pattern.compile("static\\s+\\w+\\s+main\\(", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(targetClass.getFullContent());
        while(matcher.find()){
            isMainClass = true;
        }
        for (String attr : lazyClass.getAttributes()){
            String[] arr = attr.split(" ");
            if(arr[0].matches("public|private|protected")){
                refactoredString+="\t"+arr[0]+" static";
                for(int i = 1; i < arr.length; i++){
                    refactoredString+=" "+arr[i];
                }
                refactoredString+=";\n";
            }
            else{
                refactoredString+="\t"+attr+";\n";
            }
        }
        for (String attr : targetClass.getAttributes()){
            refactoredString+="\t"+attr+";\n";
        }

        for (MethodModel prop : lazyClass.getMethodModels()){
            refactoredString+="\n\t";
            for(String s : prop.getKeywords()){
                refactoredString+=s+" ";
            }
            if(isMainClass){
                refactoredString += "static ";
            }
            if(prop.getReturnType()==null){
                refactoredString += "void ";
            }
            else {
                refactoredString += prop.getReturnType() + " ";
            }
            if(prop.getName().equals(lazyClass.getName())){
                refactoredString+="method"+prop.getName()+" (";
            }
            else{
                refactoredString+=prop.getName()+" (";
            }
            for(int i = 0; i < prop.getParameters().size(); i++){
                PropertyModel p = prop.getParameters().get(i);
                if(i > 0){
                    refactoredString+=", "+p.getType()+" "+p.getName();
                }
                else{
                    refactoredString+=p.getType()+" "+p.getName();
                }
            }
            refactoredString+="){";
            refactoredString+=prop.getBody()+"\t}\n";
        }

        for (MethodModel prop : targetClass.getMethodModels()){
            refactoredString+="\n\t";
            for(String s : prop.getKeywords()){
                refactoredString+=s+" ";
            }
            refactoredString+=prop.getReturnType()+" ";
            refactoredString+=prop.getName()+" (";
            for(int i = 0; i < prop.getParameters().size(); i++){
                PropertyModel p = prop.getParameters().get(i);
                if(i > 0){
                    refactoredString+=", "+p.getType()+" "+p.getName();
                }
                else{
                    refactoredString+=p.getType()+" "+p.getName();
                }
            }
            refactoredString+="){";
            String body = prop.getBody();
            Pattern nameMethodPattern = Pattern.compile(lazyClass.getName()+"\\s+\\w+.*$", Pattern.MULTILINE);
            Matcher nameMethodMatcher = nameMethodPattern.matcher(prop.getBody());
            while(nameMethodMatcher.find()) {
                String classDeclaration = prop.getBody().substring(nameMethodMatcher.start(), nameMethodMatcher.end());
                String[] temp = classDeclaration.trim().split(" ");
                body = body.replaceAll(temp[1]+"\\.", "");
                body = body.replace(classDeclaration, "method"+lazyClass.getName() + classDeclaration.substring(classDeclaration.indexOf("("),classDeclaration.indexOf(")")) +");");
            }
            refactoredString+=body+"\t}\n";
        }

        refactoredString+="}";
        return refactoredString;
    }

    @Override
    public List<String> refactor(List<ClassModel> targetClasses, List<ClassModel> lazyClasses) {
        List<String> refactoringStrings = new ArrayList<>();
        for(ClassModel source : lazyClasses){
            ClassModel targetClass = targetClasses.get(lazyClasses.indexOf(source));
            refactoringStrings.add(refactor(targetClass, source));
        }
        return refactoringStrings;
    }
}
