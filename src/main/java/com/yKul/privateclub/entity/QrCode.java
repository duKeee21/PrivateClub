package com.yKul.privateclub.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "qr")
@SQLDelete(sql = "UPDATE qr SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class QrCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true)
    private UUID uuid;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_guest", nullable = false)
    private Guest guest;

}
