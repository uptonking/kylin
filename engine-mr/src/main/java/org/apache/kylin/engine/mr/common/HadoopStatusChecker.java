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

package org.apache.kylin.engine.mr.common;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.hadoop.yarn.api.records.FinalApplicationStatus;
import org.apache.hadoop.yarn.server.resourcemanager.rmapp.RMAppState;
import org.apache.kylin.job.constant.JobStepStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * hadoop分步作业状态检测器
 * @author xduo
 *
 */
public class HadoopStatusChecker {
    protected static final Logger logger = LoggerFactory.getLogger(HadoopStatusChecker.class);

    private final String yarnUrl;
    private final String mrJobID;
    private final StringBuilder output;
    private final boolean useKerberosAuth;

    public HadoopStatusChecker(String yarnUrl, String mrJobID, StringBuilder output, boolean useKerberosAuth) {
        this.yarnUrl = yarnUrl;
        this.mrJobID = mrJobID;
        this.output = output;
        this.useKerberosAuth = useKerberosAuth;
    }

    public JobStepStatusEnum checkStatus() {
        if (null == mrJobID) {
            this.output.append("Skip status check with empty job id..\n");
            return JobStepStatusEnum.WAITING;
        }
        JobStepStatusEnum status = null;
        try {
            final Pair<RMAppState, FinalApplicationStatus> result = new HadoopStatusGetter(yarnUrl, mrJobID).get(useKerberosAuth);
            logger.debug("State of Hadoop job: " + mrJobID + ":" + result.getLeft() + "-" + result.getRight());
            output.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date()) + " - State of Hadoop job: " + mrJobID + ":" + result.getLeft() + " - " + result.getRight() + "\n");

            switch (result.getRight()) {
            case SUCCEEDED:
                status = JobStepStatusEnum.FINISHED;
                break;
            case FAILED:
                status = JobStepStatusEnum.ERROR;
                break;
            case KILLED:
                status = JobStepStatusEnum.KILLED;
                break;
            case UNDEFINED:
                switch (result.getLeft()) {
                case NEW:
                case NEW_SAVING:
                case SUBMITTED:
                case ACCEPTED:
                    status = JobStepStatusEnum.WAITING;
                    break;
                case RUNNING:
                    status = JobStepStatusEnum.RUNNING;
                    break;
                case FINAL_SAVING:
                case FINISHING:
                case FINISHED:
                case FAILED:
                case KILLING:
                case KILLED:
                    break;
                default:
                    throw new IllegalStateException();
                }
                break;
            default:
                throw new IllegalStateException();
            }
        } catch (Exception e) {
            logger.error("error check status", e);
            output.append("Exception: " + e.getLocalizedMessage() + "\n");
            status = JobStepStatusEnum.ERROR;
        }

        return status;
    }

}
