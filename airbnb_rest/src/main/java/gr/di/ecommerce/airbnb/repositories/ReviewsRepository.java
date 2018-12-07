package gr.di.ecommerce.airbnb.repositories;

import gr.di.ecommerce.airbnb.entities.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewsRepository extends JpaRepository<Reviews, String> {

    @Query("SELECT r FROM Reviews r WHERE r.residenceId=(:residenceId)")
    List<Reviews> findAllByResidenceId(@Param("residenceId") Integer residenceId);

    @Query("SELECT r FROM Reviews r WHERE r.tenantId=(:tenantId)")
    List<Reviews> findAllByTenantId(@Param("tenantId") Integer tenantId);
}
