package gr.di.ecommerce.airbnb.controllers;

import gr.di.ecommerce.airbnb.entities.Reservations;
import gr.di.ecommerce.airbnb.services.ReservationsService;
import gr.di.ecommerce.airbnb.utils.Constants;
import gr.di.ecommerce.airbnb.utils.KeyHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations/")
public class ReservationsController {

    private static String className = ReservationsController.class.getSimpleName();

    @Autowired
    private ReservationsService reservationsService;

    @RequestMapping(value = "makereservation", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String createReservation(@RequestHeader(Constants.AUTHORIZATION) String token, Reservations entity) {
        if (KeyHolder.checkToken(token, className)) {
            reservationsService.createReservation(entity);
            return token;
        }
        return "";
    }

    @RequestMapping(value = "comment", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<Reservations> review(@RequestHeader(Constants.AUTHORIZATION) String token,
                                                   @RequestParam("tenantId") Integer tenantId,
                                                   @RequestParam("residenceId") Integer residenceId) {
        if (KeyHolder.checkToken(token, className)) {
            return reservationsService.allowComment(tenantId, residenceId);
        }
        return null;
    }

    @RequestMapping(value = "residence", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<Reservations> getReservationsByResidence(@RequestHeader(Constants.AUTHORIZATION) String token,
                                                                       @RequestParam("residenceId") Integer residenceId) {
        if (KeyHolder.checkToken(token, className)) {
            return reservationsService.getReservationsByResidence(residenceId);
        }
        return null;
    }

}
