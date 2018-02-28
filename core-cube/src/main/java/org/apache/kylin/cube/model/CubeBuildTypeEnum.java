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

package org.apache.kylin.cube.model;

/**
 * cube构建类型枚举类 3种
 *
 * @author xduo
 */
public enum CubeBuildTypeEnum {
    /**
     * 普通构建，包括增量构建和全量构建
     * rebuild a segment or incremental build
     */
    BUILD,
    /**
     * 分段合并
     * merge segments
     */
    MERGE,

    /**
     * cube刷新
     * refresh segments
     */
    REFRESH
}
