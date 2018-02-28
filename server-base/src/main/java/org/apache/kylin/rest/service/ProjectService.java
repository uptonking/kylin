/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.kylin.rest.service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.kylin.metadata.project.ProjectInstance;
import org.apache.kylin.metadata.project.ProjectManager;
import org.apache.kylin.rest.constant.Constant;
import org.apache.kylin.rest.exception.InternalErrorException;
import org.apache.kylin.rest.request.CreateProjectRequest;
import org.apache.kylin.rest.request.UpdateProjectRequest;
import org.apache.kylin.rest.security.AclPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * project信息管理
 *
 * @author xduo
 */
@Component("projectService")
public class ProjectService extends BasicService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);

    @Autowired
    private AccessService accessService;

    @PreAuthorize(Constant.ACCESS_HAS_ROLE_ADMIN)
    public ProjectInstance createProject(CreateProjectRequest projectRequest) throws IOException {
        String projectName = projectRequest.getName();
        String description = projectRequest.getDescription();
        ProjectInstance currentProject = getProjectManager().getProject(projectName);

        if (currentProject != null) {
            throw new InternalErrorException("The project named " + projectName + " already exists");
        }
        String owner = SecurityContextHolder.getContext().getAuthentication().getName();
        ProjectInstance createdProject = getProjectManager().createProject(projectName, owner, description);
        accessService.init(createdProject, AclPermission.ADMINISTRATION);
        logger.debug("New project created.");

        return createdProject;
    }

    @PreAuthorize(Constant.ACCESS_HAS_ROLE_ADMIN + " or hasPermission(#currentProject, 'ADMINISTRATION') or hasPermission(#currentProject, 'MANAGEMENT')")
    public ProjectInstance updateProject(UpdateProjectRequest projectRequest, ProjectInstance currentProject) throws IOException {
        String formerProjectName = projectRequest.getFormerProjectName();
        String newProjectName = projectRequest.getNewProjectName();
        String newDescription = projectRequest.getNewDescription();

        if (currentProject == null) {
            throw new InternalErrorException("The project named " + formerProjectName + " does not exists");
        }

        ProjectInstance updatedProject = getProjectManager().updateProject(currentProject, newProjectName, newDescription);

        logger.debug("Project updated.");

        return updatedProject;
    }


    @PostFilter(Constant.ACCESS_POST_FILTER_READ)
    public List<ProjectInstance> listProjects(final Integer limit, final Integer offset) {
        List<ProjectInstance> projects = listAllProjects(limit, offset);
        return projects;
    }

    @Deprecated
    public List<ProjectInstance> listAllProjects(final Integer limit, final Integer offset) {
        List<ProjectInstance> projects = getProjectManager().listAllProjects();

        int climit = (null == limit) ? Integer.MAX_VALUE : limit;
        int coffset = (null == offset) ? 0 : offset;

        if (projects.size() <= coffset) {
            return Collections.emptyList();
        }

        if ((projects.size() - coffset) < climit) {
            return projects.subList(coffset, projects.size());
        }

        return projects.subList(coffset, coffset + climit);
    }

    @PreAuthorize(Constant.ACCESS_HAS_ROLE_ADMIN + " or hasPermission(#project, 'ADMINISTRATION') or hasPermission(#cube, 'MANAGEMENT')")
    public void deleteProject(String projectName, ProjectInstance project) throws IOException {
        getProjectManager().dropProject(projectName);

        accessService.clean(project, true);
    }

    public boolean isTableInAnyProject(String tableName) {
        for (ProjectInstance projectInstance : ProjectManager.getInstance(getConfig()).listAllProjects()) {
            if (projectInstance.containsTable(tableName.toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    public boolean isTableInProject(String tableName, String projectName) {
        ProjectInstance projectInstance = ProjectManager.getInstance(getConfig()).getProject(projectName);
        if (projectInstance != null) {
            if (projectInstance.containsTable(tableName.toUpperCase())) {
                return true;
            }
        }
        return false;
    }

}
