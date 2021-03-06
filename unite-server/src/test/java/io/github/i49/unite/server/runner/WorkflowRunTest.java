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
package io.github.i49.unite.server.runner;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Map;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import io.github.i49.unite.api.repository.WorkflowRepository;
import io.github.i49.unite.api.tasks.Task;
import io.github.i49.unite.api.tasks.TaskFactory;
import io.github.i49.unite.api.workflow.Job;
import io.github.i49.unite.api.workflow.JobStatus;
import io.github.i49.unite.api.workflow.Workflow;
import io.github.i49.unite.api.workflow.WorkflowFactory;
import io.github.i49.unite.server.RepositoryResource;
import io.github.i49.unite.server.RunnerResource;

public class WorkflowRunTest {

    @ClassRule
    public static RepositoryResource repositoryResource = new RepositoryResource();
    @ClassRule
    public static RunnerResource runnerResource = new RunnerResource();
    
    private WorkflowRepository repository;
    private WorkflowFactory workflowFactory;
    private TaskFactory taskFactory;
    
    private WorkflowRunner runner;

    @Before
    public void setUp() {
        repository = repositoryResource.getRepository();
        repository.clear();
        
        workflowFactory = WorkflowFactory.newInstance();
        taskFactory = TaskFactory.newInstance();

        runner = runnerResource.getRunner();
    }

    @Test
    public void runSingle_shouldRunSingleJob() {
        Task task1 = taskFactory.createEchoTask("Hello");
        Task task2 = taskFactory.createEchoTask("World");
        Job job1 = workflowFactory.createJobBuilder("job1").tasks(task1, task2).build();
        Workflow workflow = workflowFactory.createWorkflowBuilder("workflow1").jobs(job1).build();
        repository.addWorkflow(workflow);

        assertThat(repository.countJobsWithStatus(JobStatus.READY)).isEqualTo(1);
        runner.runSingle();
        assertThat(repository.countJobsWithStatus(JobStatus.READY)).isEqualTo(0);
    }
    
    @Test
    public void runSingle_shouldRunJobWithDependencies() {
        Job job1 = workflowFactory.createJobBuilder("job1")
                .tasks(taskFactory.createEchoTask("Running job1"))
                .build();
        Job job2 = workflowFactory.createJobBuilder("job2")
                .tasks(taskFactory.createEchoTask("Running job2"))
                .build();
        
        Workflow workflow1 = workflowFactory.createWorkflowBuilder("workflow1")
                .link(job1, job2)
                .build();
        
        repository.addWorkflow(workflow1);
        
        assertThat(repository.getJobStatus(job1.getId())).isSameAs(JobStatus.READY);
        assertThat(repository.getJobStatus(job2.getId())).isSameAs(JobStatus.WAITING);

        runner.runSingle();
        assertThat(repository.getJobStatus(job1.getId())).isSameAs(JobStatus.COMPLETED);
        assertThat(repository.getJobStatus(job2.getId())).isSameAs(JobStatus.READY);
       
        runner.runSingle();
        assertThat(repository.getJobStatus(job1.getId())).isSameAs(JobStatus.COMPLETED);
        assertThat(repository.getJobStatus(job2.getId())).isSameAs(JobStatus.COMPLETED);
    }

    @Test
    public void runSingle_shouldRunJobInDiamondDependencies() {
        Job job1 = workflowFactory.createJobBuilder("job1")
                .tasks(taskFactory.createEchoTask("Running job1"))
                .build();
        Job job2 = workflowFactory.createJobBuilder("job2")
                .tasks(taskFactory.createEchoTask("Running job2"))
                .build();
        Job job3 = workflowFactory.createJobBuilder("job3")
                .tasks(taskFactory.createEchoTask("Running job3"))
                .build();
        Job job4 = workflowFactory.createJobBuilder("job4")
                .tasks(taskFactory.createEchoTask("Running job4"))
                .build();
        
        Workflow workflow1 = workflowFactory.createWorkflowBuilder("workflow1")
                .link(job1, job2)
                .link(job1, job3)
                .link(job2, job4)
                .link(job3, job4)
                .build();
        
        repository.addWorkflow(workflow1);
        
        assertThat(repository.getJobStatus(job1.getId())).isSameAs(JobStatus.READY);
        assertThat(repository.getJobStatus(job2.getId())).isSameAs(JobStatus.WAITING);
        assertThat(repository.getJobStatus(job3.getId())).isSameAs(JobStatus.WAITING);
        assertThat(repository.getJobStatus(job4.getId())).isSameAs(JobStatus.WAITING);

        runner.runSingle();
        assertThat(repository.getJobStatus(job1.getId())).isSameAs(JobStatus.COMPLETED);
        assertThat(repository.getJobStatus(job2.getId())).isSameAs(JobStatus.READY);
        assertThat(repository.getJobStatus(job3.getId())).isSameAs(JobStatus.READY);
        assertThat(repository.getJobStatus(job4.getId())).isSameAs(JobStatus.WAITING);
       
        runner.runSingle();
        runner.runSingle();
        assertThat(repository.getJobStatus(job1.getId())).isSameAs(JobStatus.COMPLETED);
        assertThat(repository.getJobStatus(job2.getId())).isSameAs(JobStatus.COMPLETED);
        assertThat(repository.getJobStatus(job3.getId())).isSameAs(JobStatus.COMPLETED);
        assertThat(repository.getJobStatus(job4.getId())).isSameAs(JobStatus.READY);

        runner.runSingle();
        assertThat(repository.getJobStatus(job1.getId())).isSameAs(JobStatus.COMPLETED);
        assertThat(repository.getJobStatus(job2.getId())).isSameAs(JobStatus.COMPLETED);
        assertThat(repository.getJobStatus(job3.getId())).isSameAs(JobStatus.COMPLETED);
        assertThat(repository.getJobStatus(job4.getId())).isSameAs(JobStatus.COMPLETED);
    }
    
    @Test
    public void runSingle_shouldRunSummingJob() {
        Job job1 = workflowFactory.createJobBuilder("job1")
                .tasks(new SummingTask())
                .input("numbers", Arrays.asList(1, 2, 3))
                .build();
        Workflow workflow1 = workflowFactory.createWorkflowBuilder("workflow1").jobs(job1).build();
        repository.addWorkflow(workflow1);
        runner.runSingle();
        
        job1 = repository.findJobById(job1.getId());
        Map<String, Object> jobInput = job1.getInputParameters();
        assertThat(jobInput).containsKey("numbers");
        Map<String, Object> jobOutput = job1.getOutputParameters();
        assertThat(jobOutput.get("sum")).isEqualTo(6);
    }

    @Test
    public void runSingle_shouldRunScalingJob() {
        Job job1 = workflowFactory.createJobBuilder("job1")
                .tasks(new ScalingTask())
                .input("multiplicand", 2)
                .input("multiplier", 4)
                .build();
        Workflow workflow1 = workflowFactory.createWorkflowBuilder("workflow1").jobs(job1).build();
        repository.addWorkflow(workflow1);
        runner.runSingle();
        
        job1 = repository.findJobById(job1.getId());
        Map<String, Object> jobInput = job1.getInputParameters();
        assertThat(jobInput).containsKey("multiplicand");
        assertThat(jobInput).containsKey("multiplier");
        Map<String, Object> jobOutput = job1.getOutputParameters();
        assertThat(jobOutput.get("answer")).isEqualTo(8);
    }
    
    @Test
    public void runSingle_shouldRunJobsPassingParameters() {
        Job job1 = workflowFactory.createJobBuilder("job1")
                .tasks(new SummingTask())
                .input("numbers", Arrays.asList(1, 2, 3))
                .build();
        Job job2 = workflowFactory.createJobBuilder("job2")
                .tasks(new ScalingTask())
                .input("multiplier", 4)
                .build();
        
        Workflow workflow1 = workflowFactory.createWorkflowBuilder("workflow1")
                .link(job1, job2, workflowFactory.createKeyMapper("sum", "multiplicand"))
                .build();

        repository.addWorkflow(workflow1);

        runner.runSingle();
        runner.runSingle();

        job2 = repository.findJobById(job2.getId());
        Map<String, Object> out = job2.getOutputParameters();
        assertThat(out.get("answer")).isEqualTo(24);
   }
}
