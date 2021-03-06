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
package io.github.i49.unite.core.tasks;

import java.io.PrintStream;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;

import io.github.i49.unite.api.tasks.EchoTask;
import io.github.i49.unite.api.tasks.TaskContext;

/**
 * An implementation of {@link EchoTask}.
 */
public class EchoTaskImpl implements EchoTask {

    private final String message;

    @JsonbCreator
    public EchoTaskImpl(@JsonbProperty("message") String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void run(TaskContext context) {
        PrintStream stream = context.getStandardStream();
        stream.println(getMessage());
    }
}
