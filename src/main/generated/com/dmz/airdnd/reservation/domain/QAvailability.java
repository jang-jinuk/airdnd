package com.dmz.airdnd.reservation.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAvailability is a Querydsl query type for Availability
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAvailability extends EntityPathBase<Availability> {

    private static final long serialVersionUID = 1606088943L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAvailability availability = new QAvailability("availability");

    public final com.dmz.airdnd.accommodation.domain.QAccommodation accommodation;

    public final DatePath<java.time.LocalDate> date = createDate("date", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QReservation reservation;

    public QAvailability(String variable) {
        this(Availability.class, forVariable(variable), INITS);
    }

    public QAvailability(Path<? extends Availability> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAvailability(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAvailability(PathMetadata metadata, PathInits inits) {
        this(Availability.class, metadata, inits);
    }

    public QAvailability(Class<? extends Availability> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.accommodation = inits.isInitialized("accommodation") ? new com.dmz.airdnd.accommodation.domain.QAccommodation(forProperty("accommodation"), inits.get("accommodation")) : null;
        this.reservation = inits.isInitialized("reservation") ? new QReservation(forProperty("reservation"), inits.get("reservation")) : null;
    }

}

