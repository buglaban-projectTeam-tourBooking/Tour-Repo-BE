package org.buglaban.travelapi.model;
import jakarta.persistence.*;
import lombok.*;
import org.buglaban.travelapi.util.Gender;
import org.buglaban.travelapi.util.ParticipantType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "order_participants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_detail_id", nullable = false)
    private OrderDetail orderDetail;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "passport_number", length = 50)
    private String passportNumber;

    @Column(name = "nationality", length = 100)
    private String nationality;

    @Enumerated(EnumType.STRING)
    @Column(name = "participant_type", nullable = false)
    private ParticipantType participantType;

    @Column(name = "special_requirements", columnDefinition = "TEXT")
    private String specialRequirements;
}
