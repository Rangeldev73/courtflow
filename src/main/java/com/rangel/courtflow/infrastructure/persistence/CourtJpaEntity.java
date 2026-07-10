package com.rangel.courtflow.infrastructure.persistence;

import com.rangel.courtflow.domain.model.CourtStatus;
import com.rangel.courtflow.domain.model.Sport;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "courts")
@Getter
@Setter
@NoArgsConstructor
public class CourtJpaEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CourtStatus status;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "court_sports",
            joinColumns = @JoinColumn(name = "court_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "sport", nullable = false)
    private Set<Sport> sports;
}
