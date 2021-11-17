package com.getir.entity;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Entity
@Table(name="revinfo")
@RevisionEntity (MyRevisionListener.class)
@NamedQuery(name="RevisionInfo.findAll", query="SELECT r FROM RevisionInfo r")
public class RevisionInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Date revisionTimestamp;

    private static final ZoneId ZONE_ID_UTC = ZoneId.of("UTC");

    public void fixTimezone() {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(revisionTimestamp.toInstant(), ZONE_ID_UTC);
        revisionTimestamp = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    public RevisionInfo() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @RevisionNumber
    @Column(name = "rev")
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @RevisionTimestamp
    @Temporal (TemporalType.TIMESTAMP)
    @Column (nullable = false, name = "REVTSTMP")
    public Date getRevisionTimestamp() {
        return this.revisionTimestamp;
    }

    public void setRevisionTimestamp(Date revisionTimestamp) {
        this.revisionTimestamp = revisionTimestamp;
    }

}