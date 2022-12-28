package com.codecademy.goldmedal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.repository.CrudRepository;

import com.codecademy.goldmedal.model.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {
    
    public List<Country> findAllByOrderByNameAsc();
    public List<Country> findAllByOrderByGdpAsc();
    public List<Country> findAllByOrderByPopulationAsc();
    public List<Country> findAllByOrderByNameDesc();
    public List<Country> findAllByOrderByGdpDesc();
    public List<Country> findAllByOrderByPopulationDesc();
}
