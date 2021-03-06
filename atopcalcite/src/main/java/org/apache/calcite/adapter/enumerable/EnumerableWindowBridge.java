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

package org.apache.calcite.adapter.enumerable;

import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.Window;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rex.RexLiteral;

import java.util.List;

/**
 * 可迭代的窗口
 * <p>
 * EnumerableWindow cant'be created out of package, here's hack of workaround
 */
public class EnumerableWindowBridge {

    public static EnumerableWindow createEnumerableWindow(RelOptCluster cluster,
                                                          RelTraitSet traits,
                                                          RelNode child,
                                                          List<RexLiteral> constants,
                                                          RelDataType rowType,
                                                          List<Window.Group> groups) {

        return new EnumerableWindow(cluster, traits, child, constants, rowType, groups);

    }

}
