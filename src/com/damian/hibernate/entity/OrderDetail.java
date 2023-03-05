package com.damian.hibernate.entity;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class OrderDetail extends SuperEntity{
    @Id
    private String orderId;
    private String itemCode;

    private int qty;

    private double unitPrice;





}
