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
package com.github.i49.quantumleap.core.tasks;

import java.nio.file.Path;

import com.github.i49.quantumleap.api.tasks.ScriptTask;
import com.github.i49.quantumleap.api.tasks.ScriptTaskBuilder;
import com.github.i49.quantumleap.api.tasks.TaskContext;

/**
 * An implementation of {@link ScriptTask}.
 */
public class ScriptTaskImpl implements ScriptTask {
    
    private Path scriptPath;
    
    public ScriptTaskImpl() {
    }
    
    @Override
    public void run(TaskContext context) {
        ShellLauncher launcher = ShellLauncher.get(context.getPlatform());
        launcher.setDirectory(context.getJobDirectory());
        Path scriptPath = getScriptPath().toAbsolutePath();
        launcher.launchScript(scriptPath.toString());
    }

    @Override
    public Path getScriptPath() {
        return scriptPath;
    }

    public void setScriptPath(Path scriptPath) {
        assert(scriptPath != null);
        this.scriptPath = scriptPath;
    }

    /**
     * An implementation of {@link ScriptTaskBuilder}.
     */
    public static class Builder implements ScriptTaskBuilder {
        
        private final Path scriptPath;
        
        Builder(Path scriptPath) {
            assert(scriptPath != null);
            this.scriptPath = scriptPath;
        }
        
        @Override
        public ScriptTask get() {
            ScriptTaskImpl task = new ScriptTaskImpl();
            task.setScriptPath(scriptPath);
            return task;
        }
    }
}