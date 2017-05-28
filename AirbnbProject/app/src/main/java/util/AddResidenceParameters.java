package util;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import fromRESTful.Users;

/**
 * Created by sissy on 27/5/2017.
 */

public class AddResidenceParameters {

    private Map<String, Object> AddResidenceParams;
    public AddResidenceParameters (Users host, String type, String about, String address, String city, String country, String amenities, String floor, String rooms, String baths,
                                   String view, String spaceArea, String guests, String minPrice, String additionalCostPerPerson, String availableDateStart, String availableDateEnd,
                                   String cancellationPolicy, String rules, String photo, String kitchen, String livingRoom){

        AddResidenceParams = new HashMap<>();
        JSONObject jsonObjectHost = host.toJSON();
        AddResidenceParams.put("hostId", jsonObjectHost);
        AddResidenceParams.put("type", type);
        AddResidenceParams.put("about", about);
        AddResidenceParams.put("address", address);
        AddResidenceParams.put("city", city);
        AddResidenceParams.put("country", country);
        AddResidenceParams.put("amenities", amenities);
        AddResidenceParams.put("floor", floor);
        AddResidenceParams.put("rooms", rooms);
        AddResidenceParams.put("baths", baths);
        AddResidenceParams.put("view", view);
        AddResidenceParams.put("spaceArea", spaceArea);
        AddResidenceParams.put("guests", guests);
        AddResidenceParams.put("minPrice", minPrice);
        AddResidenceParams.put("additionalCostPerPerson", additionalCostPerPerson);
        AddResidenceParams.put("availableDateStart", availableDateStart);
        AddResidenceParams.put("availableDateEnd", availableDateEnd);
        AddResidenceParams.put("cancellationPolicy", cancellationPolicy);
        AddResidenceParams.put("rules", rules);
        AddResidenceParams.put("photos", photo);
        AddResidenceParams.put("kitchen", kitchen);
        AddResidenceParams.put("livingRoom", livingRoom);
    }


    public String getAddResidenceParameters() {
        JSONObject jsonparams = new JSONObject(AddResidenceParams);
        return jsonparams.toString();
    }

}
