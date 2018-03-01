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

package org.apache.kylin.metadata.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.google.common.collect.Maps;

/**
 * 表主要信息
 */
public class TableRef {

    final private DataModelDesc model;
    final private String alias;
    final private TableDesc table;
    final private Map<String, TblColRef> columns;

    TableRef(DataModelDesc model, String alias, TableDesc table) {
        this.model = model;
        this.alias = alias;
        this.table = table;
        this.columns = Maps.newLinkedHashMap();

        for (ColumnDesc col : table.getColumns()) {
            columns.put(col.getName(), new TblColRef(this, col));
        }
    }

    public DataModelDesc getModel() {
        return model;
    }

    public String getAlias() {
        return alias;
    }

    public TableDesc getTableDesc() {
        return table;
    }

    public String getTableName() {
        return table.getName();
    }

    public String getTableIdentity() {
        return table.getIdentity();
    }

    public TblColRef getColumn(String name) {
        return columns.get(name);
    }

    public Collection<TblColRef> getColumns() {
        return Collections.unmodifiableCollection(columns.values());
    }

    public TblColRef makeFakeColumn(String name) {
        ColumnDesc colDesc = new ColumnDesc();
        colDesc.setName(name);
        colDesc.setTable(table);
        return new TblColRef(this, colDesc);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        TableRef t = (TableRef) o;

        if (!model.equals(t.model))
            return false;
        if (!alias.equals(t.alias))
            return false;
        if (!table.getIdentity().equals(t.table.getIdentity()))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + model.getName().hashCode();
        result = 31 * result + alias.hashCode();
        result = 31 * result + table.getIdentity().hashCode();
        return result;
    }

    @Override
    public String toString() {
        if (alias.equals(table.getName()))
            return "TableRef[" + table.getName() + "]";
        else
            return "TableRef[" + alias + ":" + table.getName() + "]";
    }
}
