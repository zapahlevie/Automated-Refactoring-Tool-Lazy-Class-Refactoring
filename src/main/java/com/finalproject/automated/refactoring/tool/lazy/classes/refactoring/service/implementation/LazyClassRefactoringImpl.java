package com.finalproject.automated.refactoring.tool.lazy.classes.refactoring.service.implementation;

import com.finalproject.automated.refactoring.tool.lazy.classes.refactoring.service.*;
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

    @Autowired
    private BuildClassDeclaration buildClassDeclaration;

    @Autowired
    private MergeField mergeField;

    @Autowired
    private MergeMethod mergeMethod;

    private String LINE_BREAK = "\n";
    private String OPEN_BRACKET_PARENTHESIS = "{";
    private String CLOSE_BRACKET_PARENTHESIS = "}";

    @Override
    public String refactor(ClassModel targetClass, ClassModel lazyClass) {
        return mergeImport.merge(targetClass, lazyClass) + LINE_BREAK +
                buildClassDeclaration.declare(targetClass, lazyClass) + OPEN_BRACKET_PARENTHESIS + LINE_BREAK +
                mergeField.merge(targetClass, lazyClass) + LINE_BREAK +
                mergeMethod.merge(targetClass, lazyClass) + LINE_BREAK + CLOSE_BRACKET_PARENTHESIS;
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
