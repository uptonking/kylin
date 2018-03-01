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

package org.apache.kylin.engine;

import org.apache.kylin.cube.CubeSegment;
import org.apache.kylin.cube.model.CubeDesc;
import org.apache.kylin.job.execution.DefaultChainedExecutable;
import org.apache.kylin.metadata.model.IJoinedFlatTableDesc;

/**
 * 计算引擎接口
 */
public interface IBatchCubingEngine {

    /**
     * 返回一个工作流计划，用以构建指定的 CubeSegment
     * Build a new cube segment, typically its time range appends to the end of current cube.
     */
    DefaultChainedExecutable createBatchCubingJob(CubeSegment newSegment, String submitter);

    /**
     * 返回一个工作流计划，用以合并制定的 CubeSegment
     * Merge multiple small segments into a big one.
     */
    DefaultChainedExecutable createBatchMergeJob(CubeSegment mergeSegment, String submitter);

    /**
     * 指定该计算引擎的 IN 接口
     */
    Class<?> getSourceInterface();

    /**
     * 指定该计算引擎的 OUT 接口
     */
    Class<?> getStorageInterface();

    /**
     * Mark deprecated to indicate for test purpose only
     */
    @Deprecated
    IJoinedFlatTableDesc getJoinedFlatTableDesc(CubeDesc cubeDesc);

    IJoinedFlatTableDesc getJoinedFlatTableDesc(CubeSegment newSegment);
}
