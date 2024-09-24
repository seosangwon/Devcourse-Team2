package com.example.devcoursed.domain.member.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Setter
@Getter
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@ToString
@EqualsAndHashCode
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(unique = true)
    private String mId;

    private String pw;
    private String name;

    @Column(columnDefinition = "TEXT")
    private String mImage;

    @CreatedDate
    private LocalDateTime createdAt;






}
