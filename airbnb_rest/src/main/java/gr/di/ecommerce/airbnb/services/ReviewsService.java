package gr.di.ecommerce.airbnb.services;

import gr.di.ecommerce.airbnb.entities.Reviews;
import gr.di.ecommerce.airbnb.repositories.ReviewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReviewsService {

    @Autowired
    private ReviewsRepository reviewsRepository;

    public void createReview(Reviews reviews) {
        reviewsRepository.save(reviews);
    }

    public void editReview(Reviews reviews) {
        reviewsRepository.save(reviews);
    }

    public void deleteReview(Reviews reviews) {
        reviewsRepository.delete(reviews);
    }

    public void deleteReview(Integer id) {
        reviewsRepository.delete(id.toString());
    }

    public Reviews getReview(Integer id) {
        return reviewsRepository.getOne(id.toString());
    }

    public List<Reviews> getAllReviews() {
        return reviewsRepository.findAll();
    }

    public List<Reviews> getReviewsByResidence(Integer residenceId) {
        return reviewsRepository.findAllByResidenceId(residenceId);
    }

    public List<Reviews> getReviewsByTenant(Integer tenantId) {
        return reviewsRepository.findAllByTenantId(tenantId);
    }

}
