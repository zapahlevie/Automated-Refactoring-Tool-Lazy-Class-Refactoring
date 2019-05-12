package com.finalproject.automated.refactoring.tool.lazy.classes.refactoring.service;

import com.finalproject.automated.refactoring.tool.model.ClassModel;
import lombok.NonNull;

/**
 * @author M. Reza Pahlevi
 * @version 1.0.0
 * @since 12 May 2019
 */

public interface MergeField {
    String merge(@NonNull ClassModel targetClass, @NonNull ClassModel lazyClass);
}
