/* 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.i49.unite.core.message;

import java.util.ResourceBundle;

import io.github.i49.unite.core.common.MessageProvider;

/**
 * Localized messages defined for this API implementation.
 */
public enum Message implements MessageProvider {
 
    PARAMETER_IS_NULL,
    PARAMETER_ITEM_IS_NULL,
    PARAMETER_IS_OF_UNEXPECTED_TYPE,
    PARAMETER_ITEM_IS_OF_UNEXPECTED_TYPE,

    RESOURCE_IS_MISSING,
    RESOURCE_CANNOT_BE_READ,
    
    PLATFORM_CANNOT_BE_DETECTED,

    /* repository */
    
    STATEMENT_IS_UNDEFINED,
    OBJECT_IS_NOT_SERIALIZABLE,
    REPOSITORY_ACCESS_ERROR_OCCURRED,
    REPOSITORY_ACCESS_ERROR_WAS_IGNORED,
    REPOSITORY_PRODUCT_UNSUPPORTED,
    SQL_SCRIPT_FAILED,

    /* others */
    
    INTERNAL_ERROR
    ;

    private static final String BASE_NAME = Message.class.getPackage().getName() + ".messages";
    private static final ResourceBundle bundle = ResourceBundle.getBundle(BASE_NAME);

    @Override
    public String toString() {
        return bundle.getString(name());
    }
}
