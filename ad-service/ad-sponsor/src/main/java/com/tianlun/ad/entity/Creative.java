package com.tianlun.ad.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ad_creative")
public class Creative {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "name", nullable = false)
    private String name;

    /** Type (picture, video, etc) */
    @Basic
    @Column(name = "type", nullable = false)
    private Integer type;

    /** Type of the material(picture: jpg, bmp) */
    @Basic
    @Column(name = "materialType", nullable = false)
    private Integer materialType;

    @Basic
    @Column(name = "height", nullable = false)
    private Integer height;

    @Basic
    @Column(name = "width", nullable = false)
    private Integer width;

    /** Size of the material */
    @Basic
    @Column(name = "size", nullable = false)
    private Long size;

    /** Duration of the video advertisement */
    @Basic
    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Basic
    @Column(name = "audit_status", nullable = false)
    private Integer auditStatus;

    @Basic
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Basic
    @Column(name = "url", nullable = false)
    private String url;

    @Basic
    @Column(name = "create_time", nullable = false)
    private Date createTime;

    @Basic
    @Column(name = "update_time", nullable = false)
    private Date updateTime;


}
