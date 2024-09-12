package com.example.boardapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
public class Image extends BaseTimeEntity{

    @Id
    @Column(name = "image_sid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageSid;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "upload_url")
    private String uploadUrl;

    @ManyToOne
    @JoinColumn(name = "board_sid")
    private Board board;
}
