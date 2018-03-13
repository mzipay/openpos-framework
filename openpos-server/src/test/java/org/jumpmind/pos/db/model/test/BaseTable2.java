package org.jumpmind.pos.db.model.test;

/**
 * Licensed to JumpMind Inc under one or more contributor
 * license agreements.  See the NOTICE file distributed
 * with this work for additional information regarding
 * copyright ownership.  JumpMind Inc licenses this file
 * to you under the GNU General Public License, version 3.0 (GPLv3)
 * (the "License"); you may not use this file except in compliance
 * with the License.
 *
 * You should have received a copy of the GNU General Public License,
 * version 3.0 (GPLv3) along with this library; if not, see
 * <http://www.gnu.org/licenses/>.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.sql.JDBCType;

import org.jumpmind.pos.db.Column;
import org.jumpmind.pos.db.Table;

@Table(name="base_table_2",description="Description for table base_table_2.")
public class BaseTable2 {

    @Column(name="id",primaryKey=true, required=true, type=JDBCType.VARCHAR, size="16",
            description="Description for column id.")
    private String id;
    
    @Column(name="fld1", required=true, type=JDBCType.VARCHAR, size="50",
            description="Description for column fld_1.")
    private String fld1;
    
    @Column(name="fld2", required=true, type=JDBCType.VARCHAR, size="50",
            description="Description for column fld_2.")
    private String fld2;

}
