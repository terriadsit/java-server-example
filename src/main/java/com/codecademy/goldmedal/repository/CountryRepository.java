package com.codecademy.goldmedal.repository;

import java.util.List;
import java.util.Optional;

//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.codecademy.goldmedal.model.Country;


public interface CountryRepository extends CrudRepository<Country, Long> {
     Optional<Country> getByName(String name);
     List<Country> getAllByOrderByNameAsc();
     List<Country> getAllByOrderByGdpAsc();
     List<Country> getAllByOrderByPopulationAsc();
     List<Country> getAllByOrderByNameDesc();
     List<Country> getAllByOrderByGdpDesc();
     List<Country> getAllByOrderByPopulationDesc();
}
