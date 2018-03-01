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

package org.apache.kylin.metadata.datatype;

import java.nio.ByteBuffer;

/**
 * double序列化
 */
public class DoubleSerializer extends DataTypeSerializer<DoubleMutable> {

    // be thread-safe and avoid repeated obj creation
    private ThreadLocal<DoubleMutable> current = new ThreadLocal<DoubleMutable>();

    public DoubleSerializer(DataType type) {
    }

    @Override
    public void serialize(DoubleMutable value, ByteBuffer out) {
        out.putDouble(value.get());
    }

    private DoubleMutable current() {
        DoubleMutable d = current.get();
        if (d == null) {
            d = new DoubleMutable();
            current.set(d);
        }
        return d;
    }

    @Override
    public DoubleMutable deserialize(ByteBuffer in) {
        DoubleMutable d = current();
        d.set(in.getDouble());
        return d;
    }

    @Override
    public int peekLength(ByteBuffer in) {
        return 8;
    }

    @Override
    public int maxLength() {
        return 8;
    }

    @Override
    public int getStorageBytesEstimate() {
        return 8;
    }

    @Override
    public DoubleMutable valueOf(String str) {
        return new DoubleMutable(Double.parseDouble(str));
    }
}
