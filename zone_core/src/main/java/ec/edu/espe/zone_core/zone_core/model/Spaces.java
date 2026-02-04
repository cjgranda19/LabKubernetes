package ec.edu.espe.zone_core.zone_core.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ec.edu.espe.zone_core.zone_core.model.SapaceStatus;

import java.util.UUID;

@Entity
@Table(name = "spaces")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Spaces {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;


    @Column (length=20, nullable=false, unique=true)
    private String name;

    @Column (length=20, nullable=false, unique=true)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column (nullable=false)
    private SapaceStatus status;

    @Column (length=100)
    private String description;


    @Column(nullable = false)
    private Integer priority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "zone_id")
    private Zone zone;

}
