package com.team.stripe.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class StripeEntity {
    @Id
    @GeneratedValue
    private Long id;
}
