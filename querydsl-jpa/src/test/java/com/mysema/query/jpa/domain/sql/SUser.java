/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;


/**
 * SUser is a Querydsl query type for SUser
 */
public class SUser extends RelationalPathBase<SUser> implements RelationalPath<SUser> {

    private static final long serialVersionUID = 501289108;

    public static final SUser user = new SUser("USER_");

    public final NumberPath<Integer> companyId = createNumber("COMPANY_ID", Integer.class);

    public final StringPath firstname = createString("FIRSTNAME");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final StringPath lastname = createString("LASTNAME");

    public final StringPath username = createString("USERNAME");

    public final PrimaryKey<SUser> sql100819184439940 = createPrimaryKey(id);

    public final ForeignKey<SCompany> fk4d495f4555fdbf0 = this.<SCompany>createForeignKey(companyId, "ID");

    public SUser(String variable) {
        super(SUser.class, forVariable(variable), null, "USER");
    }

    public SUser(BeanPath<? extends SUser> entity) {
        super(entity.getType(), entity.getMetadata(), null, "USER");
    }

    public SUser(PathMetadata<?> metadata) {
        super(SUser.class, metadata, null, "USER");
    }

}

