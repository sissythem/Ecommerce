package util;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import fromRESTful.Residences;
import fromRESTful.Users;

/**
 * Created by sissy on 27/5/2017.
 */

public class AddRoomsParameters
{
    private Map<String, Object> AddRoomParams;
    public AddRoomsParameters (Users host, Residences residence, String beds, String bedType, String baths, String spaceArea, String view){

        AddRoomParams = new HashMap<>();
        JSONObject jsonObjectHost = host.toJSON();
        JSONObject jsonObjectResidence = residence.toJSON();
        AddRoomParams.put("hostId", jsonObjectHost);
        AddRoomParams.put("residenceId", jsonObjectResidence);
        AddRoomParams.put("beds", beds);
        AddRoomParams.put("bedType", bedType);
        AddRoomParams.put("bathrooms", baths);
        AddRoomParams.put("view", view);
        AddRoomParams.put("spaceArea", spaceArea);

    }


    public String getAddRoomParameters() {
        JSONObject jsonparams = new JSONObject(AddRoomParams);
        return jsonparams.toString();
    }
}
