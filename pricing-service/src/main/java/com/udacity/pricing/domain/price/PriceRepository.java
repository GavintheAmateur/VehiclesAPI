package com.udacity.pricing.repository;

import com.udacity.pricing.domain.price.Price;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;


@RepositoryRestResource
public interface PriceRepository extends CrudRepository<Price, Long> {

}
