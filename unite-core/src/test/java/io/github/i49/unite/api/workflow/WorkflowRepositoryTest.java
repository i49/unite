/* 
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
package io.github.i49.unite.api.workflow;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import io.github.i49.unite.api.TestDataSource;
import io.github.i49.unite.api.repository.WorkflowRepository;
import io.github.i49.unite.api.repository.WorkflowRepositoryBuilder;
import io.github.i49.unite.api.workflow.Job;
import io.github.i49.unite.api.workflow.JobStatus;
import io.github.i49.unite.api.workflow.Workflow;
import io.github.i49.unite.api.workflow.WorkflowEngine;

/**
 * Unit test of {@link WorkflowRepository}.
 */
public class WorkflowRepositoryTest {

    private static final DataSource dataSource = new TestDataSource();
    
    private static WorkflowEngine engine;
    private static WorkflowRepository repository;

    @BeforeClass
    public static void setUpOnce() {
        engine = WorkflowEngine.get();
        repository = WorkflowRepositoryBuilder.newInstance()
                .withDataSource(dataSource).build();
    }

    @AfterClass
    public static void tearDownOnce() {
        if (repository != null) {
            repository.close();
            repository = null;
        }
    }

    @Before
    public void setUp() {
        repository.clear();
    }

    @Test
    public void addWorkflow_shouldAddEmptyWorkflow() {
        Workflow workflow = engine.createWorkflowBuilder("workflow1").build();
        assertThat(repository.countWorkflows()).isEqualTo(0);
        repository.addWorkflow(workflow);
        assertThat(repository.countWorkflows()).isEqualTo(1);
        assertThat(workflow.hasId());
    }

    @Test
    public void addWorkflow_shouldAddJobs() {
        Job job1 = engine.createJobBuilder("job1").build();
        Job job2 = engine.createJobBuilder("job2").build();
        Workflow workflow = engine.createWorkflowBuilder("workflow1").jobs(job1, job2).build();
        repository.addWorkflow(workflow);

        assertThat(repository.countWorkflows()).isEqualTo(1);
        assertThat(repository.countJobs()).isEqualTo(2);
        assertThat(workflow.hasId());
        assertThat(job1.hasId());
        assertThat(job2.hasId());
    }

    @Test
    public void findJobsByStatus_shouldReturnReadyJobs() {
        Job job1 = engine.createJobBuilder("job1").build();
        Job job2 = engine.createJobBuilder("job2").build();
        Workflow workflow = engine.createWorkflowBuilder("workflow1").jobs(job1, job2).build();
        repository.addWorkflow(workflow);

        List<Job> jobs = repository.findJobsByStatus(JobStatus.READY);
        assertThat(jobs).hasSize(2);
        assertThat(jobs).extracting(Job::getName).containsExactly("job1", "job2");
    }

    @Test
    public void findFirstJobByStatus_shouldReturnFirstReadyJob() {
        Job job1 = engine.createJobBuilder("job1").build();
        Job job2 = engine.createJobBuilder("job2").build();
        Workflow workflow = engine.createWorkflowBuilder("workflow1").jobs(job1, job2).build();
        repository.addWorkflow(workflow);

        Optional<Job> job = repository.findFirstJobByStatus(JobStatus.READY);
        assertThat(job).isNotEmpty();
        assertThat(job.get().getName()).isEqualTo("job1");
    }
}