package gr.di.ecommerce.airbnb.services;

import gr.di.ecommerce.airbnb.entities.Users;
import gr.di.ecommerce.airbnb.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class UserService {

    @Autowired
    private UsersRepository usersRepository;

    public String createUser(Users users) {
        List<Users> usersByEmail = usersRepository.findAllByEmail(users.getEmail());
        List<Users> usersByUsername = usersRepository.findAllByUsername(users.getUsername());

        if(CollectionUtils.isEmpty(usersByEmail) && CollectionUtils.isEmpty(usersByUsername)) {
            usersRepository.save(users);
        }
        if(!CollectionUtils.isEmpty(usersByUsername) && usersByUsername.size()!=0) {
            return "username exists";
        }
        if(!CollectionUtils.isEmpty(usersByEmail) && usersByEmail.size()!=0) {
            return "email exists";
        }
        return "success";
    }

    public String login(String username, String password) {
        List<Users> users = usersRepository.findAllByUsernameAndPassword(username, password);
        if(CollectionUtils.isEmpty(users)) {
            return "fail";
        }
        return "success";
    }

    public void editUser(Users users) {
        usersRepository.save(users);
    }

    public void deleteUser(Users users) {
        usersRepository.delete(users);
    }

    public void deleteUser(Integer id) {
        usersRepository.delete(id.toString());
    }

    public Users getUser(Integer id) {
        return usersRepository.getOne(id.toString());
    }

    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    public void deleteUserPhoto(Integer id) {
        usersRepository.deleteUserPhoto(id);
    }

    public void updateUserPhoto(String name, Integer id) {
        usersRepository.updateUserPhoto(name, id);
    }

    public Users getUserByUsername(String username) {
        List<Users> users = usersRepository.findAllByUsername(username);
        if(!CollectionUtils.isEmpty(users)) {
            return users.get(0);
        }
        return null;
    }

    public Users getUserByEmail(String email) {
        List<Users> users = usersRepository.findAllByEmail(email);
        if(!CollectionUtils.isEmpty(users)) {
            return users.get(0);
        }
        return null;
    }
}
