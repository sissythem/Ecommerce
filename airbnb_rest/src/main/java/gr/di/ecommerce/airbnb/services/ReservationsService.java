package gr.di.ecommerce.airbnb.services;

import gr.di.ecommerce.airbnb.entities.Reservations;
import gr.di.ecommerce.airbnb.repositories.ReservationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ReservationsService {

    @Autowired
    private ReservationsRepository reservationsRepository;

    public void createReservation(Reservations reservations) {
        reservationsRepository.save(reservations);
    }

    public void updateReservation(Reservations reservations) {
        reservationsRepository.save(reservations);
    }

    public void deleteReservation(Reservations reservations) {
        reservationsRepository.delete(reservations);
    }

    public void deleteReservation(Integer id) {
        reservationsRepository.delete(id.toString());
    }

    public Reservations getReservation(Integer id) {
        return reservationsRepository.getOne(id.toString());
    }

    public List<Reservations> getAllReservations() {
        return reservationsRepository.findAll();
    }

    public List<Reservations> getReservationsByTenants(Integer tenantId) {
        return reservationsRepository.findAllByTenantId(tenantId);
    }

    public List<Reservations> getReservationsByResidence(Integer residenceId) {
        return reservationsRepository.findAllByResidenceId(residenceId);
    }

    public List<Reservations> allowComment(Integer tenantId, Integer residenceId) {
        return reservationsRepository.allowComment(tenantId, residenceId);
    }

    public Integer getAlreadyGuests(Integer residenceId, LocalDate startDate, LocalDate endDate) {
        return reservationsRepository.countGuestsByResidenceIdAndStartDateAndEndDate(residenceId, startDate, endDate);
    }
}
