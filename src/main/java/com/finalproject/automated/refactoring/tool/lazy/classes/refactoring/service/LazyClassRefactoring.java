package com.finalproject.automated.refactoring.tool.lazy.classes.refactoring.service;

import com.finalproject.automated.refactoring.tool.model.ClassModel;
import lombok.NonNull;

import java.util.List;

/**
 * @author M. Reza Pahlevi
 * @version 1.0.0
 * @since 29 April 2019
 */

public interface LazyClassRefactoring {
    String refactor(@NonNull ClassModel targetClass, @NonNull ClassModel lazyClass);

    List<String> refactor(@NonNull List<ClassModel> targetClasses, @NonNull List<ClassModel> lazyClasses);
}
