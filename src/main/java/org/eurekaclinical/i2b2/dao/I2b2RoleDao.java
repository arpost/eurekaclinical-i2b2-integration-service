package org.eurekaclinical.i2b2.dao;

/*-
 * #%L
 * i2b2 Eureka Service
 * %%
 * Copyright (C) 2015 - 2016 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.List;
import org.eurekaclinical.i2b2.entity.I2b2RoleEntity;
import org.eurekaclinical.standardapis.dao.DaoWithUniqueName;

/**
 * A data access object interface for working with {@link I2b2RoleEntity} 
 * objects in a data store.
 *
 * @author Andrew Post
 * @param <U> a user type.
 *
 */
public interface I2b2RoleDao<U extends I2b2RoleEntity> extends DaoWithUniqueName<U, Long> {
	List<U> getI2b2Roles(String username, Long projectId);
}
