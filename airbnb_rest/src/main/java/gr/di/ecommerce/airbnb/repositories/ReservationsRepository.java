package gr.di.ecommerce.airbnb.repositories;

import gr.di.ecommerce.airbnb.entities.Reservations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationsRepository extends JpaRepository<Reservations, String> {

    @Query("SELECT r FROM Reservations r WHERE r.tenantId=(:tenantId)")
    List<Reservations> findAllByTenantId(@Param("tenantId") Integer tenantId);

    @Query("SELECT r FROM Reservations r WHERE r.residenceId=(:residenceId)")
    List<Reservations> findAllByResidenceId(@Param("residenceId") Integer residenceId);

    @Query("SELECT r FROM Reservations r WHERE r.tenantId=(:tenantId) AND r.residenceId=(:residenceId)")
    List<Reservations> allowComment(@Param("tenantId") Integer tenantId, @Param("residenceId") Integer residenceId);

    @Query("SELECT r FROM Reservations r WHERE r.residenceId=(:residenceId) AND r.startDate=(:startDate) AND r.endDate=(:endDate)")
    List<Reservations> findAllByResidenceIdAndStartDateAndEndDate(@Param("residenceId") Integer residenceId, @Param("startDate")LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT (r.guests) FROM Reservations r WHERE r.residenceId=(:residenceId) AND r.startDate=(:startDate) AND r.endDate=(:endDate)")
    Integer countGuestsByResidenceIdAndStartDateAndEndDate(@Param("residenceId") Integer residenceId, @Param("startDate")LocalDate startDate, @Param("endDate") LocalDate endDate);
}
