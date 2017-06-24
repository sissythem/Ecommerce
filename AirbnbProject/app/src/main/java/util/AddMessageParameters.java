package util;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import fromRESTful.Conversations;
import fromRESTful.Users;

public class AddMessageParameters {
    private Map<String, Object> AddMessageParams;

    public AddMessageParameters(Users user, Conversations conversation, String body) {
        AddMessageParams = new HashMap<>();
        AddMessageParams.put("userId", user.toJSON());
        AddMessageParams.put("conversationId", conversation.toJSON());
        AddMessageParams.put("body", body);
    }

    public String getAddMessageParameters() {
        JSONObject jsonparams = new JSONObject(AddMessageParams);
        System.out.println(jsonparams);
        return jsonparams.toString();
    }
}
