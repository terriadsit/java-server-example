package com.codecademy.goldmedal.repository;

import com.codecademy.goldmedal.model.Country;

import antlr.collections.List;

import org.springframework.data.repository.CrudRepository;

public interface CountryRepository extends CrudRepository<Country, Long> {
    //List<Country> findAll();
}
