package com.dmz.airdnd.reservation.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFeePolicy is a Querydsl query type for FeePolicy
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFeePolicy extends EntityPathBase<FeePolicy> {

    private static final long serialVersionUID = 973653316L;

    public static final QFeePolicy feePolicy = new QFeePolicy("feePolicy");

    public final NumberPath<java.math.BigDecimal> feePercent = createNumber("feePercent", java.math.BigDecimal.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public QFeePolicy(String variable) {
        super(FeePolicy.class, forVariable(variable));
    }

    public QFeePolicy(Path<? extends FeePolicy> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFeePolicy(PathMetadata metadata) {
        super(FeePolicy.class, metadata);
    }

}

