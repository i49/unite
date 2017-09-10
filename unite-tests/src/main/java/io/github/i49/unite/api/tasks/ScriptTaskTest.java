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
package io.github.i49.unite.api.tasks;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

/**
 * Unit test of {@link ScriptTask}.
 */
public class ScriptTaskTest extends BaseTaskTest {

    @Test
    public void run_shouldRunScript() {
        // given
        Path dir = Paths.get("target/test-classes");
        Path path = dir.resolve("hello" + getScriptExtension());
        Task task = factory.createShellTaskBuilder(path)
                .arguments("John Smith")
                .build();
        
        // when
        runTask(task);
    }
    
    private static String getScriptExtension() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("windows")) {
            return ".bat";
        } else {
            return ".sh";
        }
    }
}

