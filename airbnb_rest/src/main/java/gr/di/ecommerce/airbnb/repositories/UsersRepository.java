package gr.di.ecommerce.airbnb.repositories;

import gr.di.ecommerce.airbnb.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users, String> {

    @Query("UPDATE Users u SET u.photo =NULL WHERE u.id =(:id)")
    void deleteUserPhoto(@Param("id") Integer id);

    @Query("UPDATE Users u SET u.photo =(:path) WHERE u.id=(:id)")
    void updateUserPhoto(@Param("filePath") String path, @Param("id") Integer id);

    @Query("SELECT u FROM Users u WHERE u.username=(:username)")
    List<Users> findAllByUsername(@Param("username") String username);

    @Query("SELECT u FROM Users u WHERE u.email=(:email)")
    List<Users> findAllByEmail(@Param("email") String email);

    @Query("SELECT u FROM Users u WHERE u.username = (:username) AND u.password = (:password)")
    List<Users> findAllByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
}
