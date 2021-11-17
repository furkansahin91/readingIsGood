package com.getir.entity;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class CustomerOrderAudPK implements Serializable {
    private Integer id;
    private Long rev;
}
