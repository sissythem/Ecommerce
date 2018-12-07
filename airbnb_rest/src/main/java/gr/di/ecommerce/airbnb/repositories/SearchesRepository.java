package gr.di.ecommerce.airbnb.repositories;

import gr.di.ecommerce.airbnb.entities.Searches;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchesRepository extends JpaRepository<Searches, String> {

    @Query("SELECT s FROM Searches s WHERE s.userId=(:userId)")
    List<Searches> findAllByUserId(@Param("userId") Integer userId);
}
