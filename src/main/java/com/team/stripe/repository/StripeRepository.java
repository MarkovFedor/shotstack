package com.team.stripe.repository;

import com.team.stripe.entity.StripeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StripeRepository extends CrudRepository<StripeEntity, Long> {
}
