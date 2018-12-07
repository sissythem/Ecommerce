package gr.di.ecommerce.airbnb.repositories;

import gr.di.ecommerce.airbnb.entities.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagesRepository extends JpaRepository<Images, String> {

    @Query("SELECT i FROM Images i WHERE i.name=(:name)")
    List<Images> findAllByName(@Param("name") String name);

    @Query("SELECT i FROM Images i WHERE i.residenceId =(:residenceId)")
    List<Images> findAllImagesByResidenceId(@Param("residenceId") Integer residenceId);
}
