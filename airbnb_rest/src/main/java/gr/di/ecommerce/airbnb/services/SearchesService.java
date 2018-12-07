package gr.di.ecommerce.airbnb.services;

import gr.di.ecommerce.airbnb.entities.Searches;
import gr.di.ecommerce.airbnb.entities.Users;
import gr.di.ecommerce.airbnb.repositories.SearchesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchesService {

    @Autowired
    private SearchesRepository searchesRepository;

    @Autowired
    private UserService userService;

    public void createSearch(Searches searches) {
        searchesRepository.save(searches);
    }

    public void editSearch(Searches searches) {
        searchesRepository.save(searches);
    }

    public void deleteSearch(Searches searches) {
        searchesRepository.delete(searches);
    }

    public void deleteSearch(Integer id) {
        searchesRepository.delete(id.toString());
    }

    public Searches getSearch(Integer id) {
        return searchesRepository.getOne(id.toString());
    }

    public List<Searches> getAllSearches() {
        return searchesRepository.findAll();
    }

    public void addUserSearch(String username, String city) {
        Users user = userService.getUserByUsername(username);
        Searches searches = new Searches();
        searches.setUserId(user);
        searches.setCity(city);
        createSearch(searches);
    }

    public List<Searches> getSearchesByUser(Integer userId) {
        return searchesRepository.findAllByUserId(userId);
    }
}
