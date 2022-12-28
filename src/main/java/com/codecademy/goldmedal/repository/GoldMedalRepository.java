package com.codecademy.goldmedal.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.codecademy.goldmedal.model.Country;
import com.codecademy.goldmedal.model.GoldMedal;

public interface GoldMedalRepository extends CrudRepository<GoldMedal, Long> {
    List<GoldMedal> findByCountry(String country);
}
