/*
 * Copyright (c) 2019-2029, Dreamlu 卢春梦 (596392912@qq.com & www.dreamlu.net).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.dreamlu.mica.activerecord.test;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.PagerUtils;

/**
 * druid 测试
 */
public class DruidTest {

	public static void main(String[] args) {
		String sql = "select * from xxx where 1=2 order by xx";
		String sqlCount = PagerUtils.count(sql, DbType.mysql);
		System.out.println(sqlCount);
		String limit = PagerUtils.limit(sql, DbType.mysql, 2, 20);
		System.out.println(limit);
	}
}
