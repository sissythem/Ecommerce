package gr.di.ecommerce.airbnb.services;

import gr.di.ecommerce.airbnb.entities.Reservations;
import gr.di.ecommerce.airbnb.entities.Residences;
import gr.di.ecommerce.airbnb.entities.Searches;
import gr.di.ecommerce.airbnb.repositories.ResidencesRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ResidencesService {

    @Autowired
    private ResidencesRepository residencesRepository;

    @Autowired
    private ReservationsService reservationsService;

    @Autowired
    private SearchesService searchesService;

    @Value("${images.folder}")
    private String imagesPath;

    public void createResidence(Residences residences) {
        residencesRepository.save(residences);
    }

    public void editResidence(Residences residences) {
        residencesRepository.save(residences);
    }

    public void deleteResidence(Residences residences) {
        residencesRepository.delete(residences);
    }

    public void deleteResidence(Integer id) throws IOException {
        Residences residences = getResidence(id);
        Path path = Paths.get(imagesPath, residences.getPhotos());
        if(Files.exists(path)) {
            Files.delete(path);
        }
        residencesRepository.delete(id.toString());
    }

    public Residences getResidence(Integer id) {
        return residencesRepository.getOne(id.toString());
    }

    public List<Residences> getAllResidences() {
        return residencesRepository.findAll();
    }

    public List<Residences> getResidencesByCity(String city) {
        return residencesRepository.findAllByCity(city);
    }

    public List<Residences> getResidencesByHost(Integer hostId) {
        return residencesRepository.findAllByHostId(hostId);
    }

    public List<Residences> getRecommendations(String username, String city, LocalDate startDate, LocalDate endDate, Integer guests) {
        if(startDate == null) {
            startDate = LocalDate.now();
        }

        if(endDate == null) {
            endDate = LocalDate.now();
            endDate = endDate.plus(7, ChronoUnit.DAYS);
        }

        if(guests == null) {
            guests = 1;
        }

        List<Residences> residences;
        if(!StringUtils.isBlank(username)) {
            residences = residencesRepository.findAllByStartDateAndEndDateAndNotSameHost(startDate, endDate, username);
        } else {
            residences = residencesRepository.findAllByStartDateAndEndDate(startDate, endDate);
        }
        if(!StringUtils.isBlank(city)) {
            searchesService.addUserSearch(username, city);
            residences = residences.stream().filter(residences1 -> residences1.getCity().equalsIgnoreCase(city)).collect(Collectors.toList());
        }
        List<Residences> finalRecommendations = new ArrayList<>();
        for(Residences residence : residences) {
            Integer alreadyGuests = reservationsService.getAlreadyGuests(residence.getId(), startDate, endDate);
            if((residence.getGuests() - alreadyGuests) >= guests) {
                finalRecommendations.add(residence);
            }
        }
        return finalRecommendations;
    }

    public void setMainResidencePhoto(Integer id, String name) {
        residencesRepository.setMainResidencePhoto(id, name);
    }
}
