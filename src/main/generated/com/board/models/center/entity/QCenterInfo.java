package com.board.models.center.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCenterInfo is a Querydsl query type for CenterInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCenterInfo extends EntityPathBase<CenterInfo> {

    private static final long serialVersionUID = -357886556L;

    public static final QCenterInfo centerInfo = new QCenterInfo("centerInfo");

    public final com.board.entities.QBaseMember _super = new com.board.entities.QBaseMember(this);

    public final StringPath address = createString("address");

    public final StringPath addressSub = createString("addressSub");

    public final StringPath bookAvl = createString("bookAvl");

    public final NumberPath<Integer> bookBlock = createNumber("bookBlock", Integer.class);

    public final NumberPath<Integer> bookCapacity = createNumber("bookCapacity", Integer.class);

    public final BooleanPath bookHday = createBoolean("bookHday");

    public final StringPath bookNotAvl = createString("bookNotAvl");

    public final StringPath bookYoil = createString("bookYoil");

    public final NumberPath<Long> cCode = createNumber("cCode", Long.class);

    public final StringPath centerType = createString("centerType");

    public final StringPath cName = createString("cName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final StringPath location = createString("location");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    //inherited
    public final StringPath modifiedBy = _super.modifiedBy;

    public final StringPath operHour = createString("operHour");

    public final StringPath telNum = createString("telNum");

    public final StringPath zonecode = createString("zonecode");

    public QCenterInfo(String variable) {
        super(CenterInfo.class, forVariable(variable));
    }

    public QCenterInfo(Path<? extends CenterInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCenterInfo(PathMetadata metadata) {
        super(CenterInfo.class, metadata);
    }

}

