package com.yKul.privateclub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "qr")
public class QrCode {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @GeneratedValue(strategy = GenerationType.UUID)
   @Column(name = "uuid", nullable = false, unique = true)
   private UUID uuid;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "id_guest", nullable = false)
   private Guest guest;

}
