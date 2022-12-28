package com.codecademy.goldmedal.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

import javax.swing.SortOrder;

import org.apache.commons.text.WordUtils;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codecademy.goldmedal.model.CountriesResponse;
import com.codecademy.goldmedal.model.CountryDetailsResponse;
import com.codecademy.goldmedal.model.CountryMedalsListResponse;
import com.codecademy.goldmedal.model.Country;
import com.codecademy.goldmedal.model.CountrySummary;
import com.codecademy.goldmedal.model.GoldMedal;
import com.codecademy.goldmedal.repository.CountryRepository;
import com.codecademy.goldmedal.repository.GoldMedalRepository;

@RestController
@RequestMapping("/countries")
public class GoldMedalController {
    private final GoldMedalRepository goldMedalRepository;
    private final CountryRepository countryRepository;

    public GoldMedalController(GoldMedalRepository goldMedalRepository, CountryRepository countryRepository) {
        this.goldMedalRepository = goldMedalRepository;
        this.countryRepository = countryRepository;
    }

    @GetMapping()
    public CountriesResponse getCountries(@RequestParam String sort_by, @RequestParam String ascending) {
        var ascendingOrder = ascending.toLowerCase().equals("y");
        return new CountriesResponse(getCountrySummaries(sort_by.toLowerCase(), ascendingOrder));
    }

   @GetMapping("/{country}")
    public CountryDetailsResponse getCountryDetails(@PathVariable String country) {
        String countryName = WordUtils.capitalizeFully(country);
        return getCountryDetailsResponse(countryName);
    } 

    @GetMapping("/{country}/medals")
    public CountryMedalsListResponse getCountryMedalsList(@PathVariable String country, @RequestParam String sort_by, @RequestParam String ascending) {
        String countryName = WordUtils.capitalizeFully(country);
        var ascendingOrder = ascending.toLowerCase().equals("y");
        return getCountryMedalsListResponse(countryName, sort_by.toLowerCase(), ascendingOrder);
    }

    private CountryMedalsListResponse getCountryMedalsListResponse(String countryName, String sortBy, boolean ascendingOrder) {
        List<GoldMedal> medalsList;
        switch (sortBy) {
            case "year":
                medalsList = ascendingOrder ? this.goldMedalRepository.findByCountryOrderByYearAsc(countryName) : this.goldMedalRepository.findByCountryOrderByYearDesc(countryName);
                break;
            case "season":
                medalsList = ascendingOrder ? this.goldMedalRepository.findByCountryOrderBySeasonAsc(countryName) : this.goldMedalRepository.findByCountryOrderBySeasonDesc(countryName);
                break;
            case "city":
                medalsList = ascendingOrder ? this.goldMedalRepository.findByCountryOrderByCityAsc(countryName) : this.goldMedalRepository.findByCountryOrderByCityDesc(countryName);
                break;
            case "name":
                medalsList = ascendingOrder ? this.goldMedalRepository.findByCountryOrderByNameAsc(countryName) : this.goldMedalRepository.findByCountryOrderByNameDesc(countryName);
                break;
            case "event":
                medalsList = ascendingOrder ? this.goldMedalRepository.findByCountryOrderByEventAsc(countryName) : this.goldMedalRepository.findByCountryOrderByEventDesc(countryName);
                break;
            default:
                medalsList = new ArrayList<>();
                break;
        }

        return new CountryMedalsListResponse(medalsList);
    }

   private CountryDetailsResponse getCountryDetailsResponse(String countryName) {
        Optional<Country> countryOptional = this.countryRepository.getByName(countryName);
        // TODO: get the country; this repository method should return a java.util.Optional
        if (countryOptional.isEmpty()) {
            return new CountryDetailsResponse(countryName);
        }

        Country country = countryOptional.get();
        String name = country.getName();
        List<GoldMedal> medals = this.goldMedalRepository.findByCountry(countryName);
        int goldMedalCount = medals.size();
       
        List<GoldMedal> summerWins = this.goldMedalRepository.findByCountryAndSeasonOrderByYearAsc(countryName, "Summer");
        // TODO: get t;he collection of wins at the Summer Olympics, sorted by year in ascending order
        var numberSummerWins = summerWins.size() > 0 ? summerWins.size() : null;
        int totalSummerEvents = this.goldMedalRepository.findDistinctBySeason("Summer").size();
        // TODO: get the total number of events at the Summer Olympics
        var percentageTotalSummerWins = totalSummerEvents != 0 && numberSummerWins != null ? (float) summerWins.size() / totalSummerEvents : null;
        var yearFirstSummerWin = summerWins.size() > 0 ? summerWins.get(0).getYear() : null;

        List<GoldMedal> winterWins = this.goldMedalRepository.findByCountryAndSeasonOrderByYearAsc(countryName, "Summer");
        
         // TODO: get the collection of wins at the Winter Olympics
        var numberWinterWins = winterWins.size() > 0 ? winterWins.size() : null;

        int totalWinterEvents = this.goldMedalRepository.findDistinctBySeason("Winter").size();
         // TODO: get the total number of events at the Winter Olympics, sorted by year in ascending order
        var percentageTotalWinterWins = totalWinterEvents != 0 && numberWinterWins != null ? (float) winterWins.size() / totalWinterEvents : null;
        var yearFirstWinterWin = winterWins.size() > 0 ? winterWins.get(0).getYear() : null;

        int numberEventsWonByFemaleAthletes = this.goldMedalRepository.findDistinctByGender("female").size();
        
        int numberEventsWonByMaleAthletes = this.goldMedalRepository.findDistinctByGender("male").size();// TODO: get the number of wins by male athletes

        return new CountryDetailsResponse(
                countryName,
                country.getGdp(),
                country.getPopulation(),
                goldMedalCount,
                numberSummerWins,
                percentageTotalSummerWins,
                yearFirstSummerWin,
                numberWinterWins,
                percentageTotalWinterWins,
                yearFirstWinterWin,
                numberEventsWonByFemaleAthletes,
                numberEventsWonByMaleAthletes);
    }

    private List<CountrySummary> getCountrySummaries(String sortBy, boolean ascendingOrder) {
        List<Country> countries;
        switch (sortBy) {
            case "name":
                countries = ascendingOrder ? countryRepository.getAllByOrderByNameAsc() : countryRepository.getAllByOrderByNameDesc();
                break;
            case "gdp":
                countries = ascendingOrder ? countryRepository.getAllByOrderByGdpAsc() : countryRepository.getAllByOrderByGdpDesc();
                break;
            case "population":
                countries = ascendingOrder ? countryRepository.getAllByOrderByPopulationAsc() : countryRepository.getAllByOrderByPopulationDesc();
                break;
            case "medals":
            default:
                countries = countryRepository.getAllByOrderByNameAsc();
                break;
        }

        var countrySummaries = getCountrySummariesWithMedalCount(countries);

        if (sortBy.equalsIgnoreCase("medals")) {
            countrySummaries = sortByMedalCount(countrySummaries, ascendingOrder);
        }

        return countrySummaries;
    }

    private List<CountrySummary> sortByMedalCount(List<CountrySummary> countrySummaries, boolean ascendingOrder) {
        return countrySummaries.stream()
                .sorted((t1, t2) -> ascendingOrder ?
                        t1.getMedals() - t2.getMedals() :
                        t2.getMedals() - t1.getMedals())
                .collect(Collectors.toList());
    }

    private List<CountrySummary> getCountrySummariesWithMedalCount(List<Country> countries) {
        List<CountrySummary> countrySummaries = new ArrayList<>();
        for (var country : countries) {
            String countryName = country.getName();
            List<GoldMedal> medals = this.goldMedalRepository.findByCountry(countryName);
            int goldMedalCount = medals.size();
            countrySummaries.add(new CountrySummary(country, goldMedalCount));
        }
        return countrySummaries;
    }
}
