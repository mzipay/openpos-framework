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
package org.jumpmind.pos.db.model;

import java.sql.JDBCType;

import org.jumpmind.pos.db.Column;
import org.jumpmind.pos.db.Table;

@Table(name="operator",description="Security Identifier granting and denying access to the systems of the retail enterprise, and recorded upon the transaction originating from those systems.")
public class Operator extends AbstractObject {

    @Column(name="id",primaryKey=true, required=true, type=JDBCType.VARCHAR, size="16",
            description="A unique, automatically assigned key used to identify an operator.")
    private String id;
    
    @Column(name="user_name", required=true, type=JDBCType.VARCHAR, size="50",
            description="The short human readable name used as an alternate ID for Operator.")
    private String userName;
    
    @Column(name="password", required=true, type=JDBCType.VARCHAR, size="50",
            description="A hashed security password entered by an operator and used to verify his/her identity when signing on to a Workstation.")
    private String password;

}
