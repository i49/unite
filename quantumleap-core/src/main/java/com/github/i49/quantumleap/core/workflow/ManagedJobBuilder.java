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
package com.github.i49.quantumleap.core.workflow;

import java.util.Map;

import com.github.i49.quantumleap.api.workflow.JobBuilder;
import com.github.i49.quantumleap.api.workflow.JobStatus;

/**
 * Builder interface for building an instance of {@link ManagedJob}.
 * This type is for internal use.
 */
public interface ManagedJobBuilder extends JobBuilder {
    
    @Override
    ManagedJob get();
    
    ManagedJobBuilder jobId(long id);
    
    ManagedJobBuilder jobOutput(Map<String, Object> jobOutput);
    
    ManagedJobBuilder status(JobStatus status);

    ManagedJobBuilder standardOutput(String[] lines);
}
