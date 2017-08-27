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
package io.github.i49.unite.core.workflow;

import io.github.i49.unite.api.workflow.ParameterSetMapper;

/**
 * The factory for producing workflow components.
 */
public class WorkflowFactory {
    
    private static final WorkflowFactory singleton = new WorkflowFactory();
    
    /**
     * Returns the singleton of this class.
     * 
     * @return the singleton of this class.
     */
    public static WorkflowFactory getInstance() {
        return singleton;
    }
    
    private WorkflowFactory() {
    }

    public ManagedJobBuilder createJobBuilder(String name) {
        return new ManagedJobBuilder(name);
    }

    public ManagedWorkflowBuilder createWorkflowBuilder(String name) {
        return new ManagedWorkflowBuilder(name);
    }
    
    public JobLink createJobLink(ManagedJob source, ManagedJob target, ParameterSetMapper mapper) {
        return JobLink.of(source, target, mapper);
   }
}