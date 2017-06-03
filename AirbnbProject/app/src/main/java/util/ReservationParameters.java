package util;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import fromRESTful.Residences;
import fromRESTful.Users;

/**
 * Created by sissy on 3/6/2017.
 */

public class ReservationParameters
{
    private Map<String, Object> ReservationParams;
    public ReservationParameters (Users tenant, Residences residence, String startDate, String endDate, String guests){

        ReservationParams = new HashMap<>();
        JSONObject jsonObjectTenant = tenant.toJSON();
        JSONObject jsonObjectResidence = residence.toJSON();
        ReservationParams.put("tenantId", jsonObjectTenant);
        ReservationParams.put("residenceId", jsonObjectResidence);
        ReservationParams.put("startDate", startDate);
        ReservationParams.put("endDate", endDate);
        ReservationParams.put("guests", guests);
    }


    public String getReservationParameters() {
        JSONObject jsonparams = new JSONObject(ReservationParams);
        return jsonparams.toString();
    }
}
