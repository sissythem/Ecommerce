package gr.di.ecommerce.airbnb.repositories;

import gr.di.ecommerce.airbnb.entities.Residences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ResidencesRepository extends JpaRepository<Residences, String> {

    @Query("SELECT r FROM Residences r WHERE r.city=(:city)")
    List<Residences> findAllByCity(@Param("city") String city);

    @Query("SELECT r FROM Residences r WHERE r.hostId=(:hostId)")
    List<Residences> findAllByHostId(@Param("hostId") Integer hostId);

    @Query("SELECT r FROM Residences r WHERE r.availableDateStart=(:startDate) AND r.availableDateEnd=(:endDate)")
    List<Residences> findAllByStartDateAndEndDate(@Param("startDate")LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT r FROM Residences r WHERE r.availableDateStart=(:startDate) AND r.availableDateEnd=(:endDate) AND r.hostId.username<>(:username)")
    List<Residences> findAllByStartDateAndEndDateAndNotSameHost(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("username") String username);

    @Query("SELECT r FROM Residences r WHERE r.availableDateStart=(:startDate) AND r.availableDateEnd=(:endDate) AND r.hostId.username<>(:username) AND r.city=(:city)")
    List<Residences> findAllByStartDateAndEndDateAndNotSameHostAndCity(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("username") String username, @Param("city") String city);

    @Query("SELECT r FROM Residences r WHERE r.availableDateStart=(:startDate) AND r.availableDateEnd=(:endDate) AND r.city=(:city)")
    List<Residences> findAllByStartDateAndEndDateAndCity(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("city") String city);

    @Query("UPDATE Residences r SET r.photos=(:name) WHERE r.id=(:id)")
    void setMainResidencePhoto(@Param("id") Integer id, @Param("name") String name);
}
