package com.board.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBaseMember is a Querydsl query type for BaseMember
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QBaseMember extends EntityPathBase<BaseMember> {

    private static final long serialVersionUID = 572523523L;

    public static final QBaseMember baseMember = new QBaseMember("baseMember");

    public final QBase _super = new QBase(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final StringPath createBy = createString("createBy");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath modifiedBy = createString("modifiedBy");

    public QBaseMember(String variable) {
        super(BaseMember.class, forVariable(variable));
    }

    public QBaseMember(Path<? extends BaseMember> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBaseMember(PathMetadata metadata) {
        super(BaseMember.class, metadata);
    }

}

