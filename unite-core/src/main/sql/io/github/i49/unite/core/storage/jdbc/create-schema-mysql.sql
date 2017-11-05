-- 
-- Copyright 2017 the original author or authors.
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
-- 
--     http://www.apache.org/licenses/LICENSE-2.0
-- 
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

CREATE TABLE workflow (
    workflow_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    workflow_name MEDIUMTEXT NOT NULL,
    workflow_status VARCHAR(10) NOT NULL
);

CREATE TABLE job (
    job_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    job_name MEDIUMTEXT NOT NULL,
    job_status VARCHAR(10) NOT NULL,
    job_input BLOB,
    job_output BLOB,
    standard_output MEDIUMTEXT, 
    workflow_id BIGINT NOT NULL,

    FOREIGN KEY (workflow_id) REFERENCES workflow (workflow_id)
);

CREATE TABLE job_link (
    source_job_id BIGINT,
    target_job_id BIGINT,
    mapper_class MEDIUMTEXT NOT NULL,
    mapper_object BLOB,
    
    PRIMARY KEY (source_job_id, target_job_id),
    FOREIGN KEY (source_job_id) REFERENCES job (job_id),
    FOREIGN KEY (target_job_id) REFERENCES job (job_id)
);

CREATE TABLE task (
    job_id BIGINT,
    sequence_number INTEGER NOT NULL,

    class_name VARCHAR(1000) NOT NULL,
    parameters VARCHAR(10000),

    PRIMARY KEY (job_id, sequence_number),
    FOREIGN KEY (job_id) REFERENCES job (job_id)
);