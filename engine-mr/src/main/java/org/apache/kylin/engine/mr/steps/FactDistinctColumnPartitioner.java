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

package org.apache.kylin.engine.mr.steps;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.kylin.common.util.BytesUtil;

/**
 * 计算事实表列的分区函数
 */
public class FactDistinctColumnPartitioner extends Partitioner<Text, Text> {

    private Configuration conf;

    @Override
    public int getPartition(Text key, Text value, int numReduceTasks) {

        if (key.getBytes()[0] == FactDistinctHiveColumnsMapper.MARK_FOR_HLL) {
            // the last reducer is for merging hll
            return numReduceTasks - 1;
        } else if (key.getBytes()[0] == FactDistinctHiveColumnsMapper.MARK_FOR_PARTITION_COL) {
            // the last reducer is for merging hll
            return numReduceTasks - 2;
        } else {
            int colIndex = BytesUtil.readUnsigned(key.getBytes(), 0, 1);
            return colIndex;
        }

    }

}
