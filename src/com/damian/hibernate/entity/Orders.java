package com.damian.hibernate.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@RequiredArgsConstructor
public class Orders extends SuperEntity{
    @Id
    @NonNull
    private String oId;
    @NonNull
    private Date date;

    @ManyToOne
    @NonNull
    private Customer customer;

    @ManyToMany
    private List<Item>itemList;

}
