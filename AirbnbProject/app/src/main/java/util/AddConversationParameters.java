package util;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import fromRESTful.Conversations;
import fromRESTful.Residences;
import fromRESTful.Users;

public class AddConversationParameters {
    private Map<String, Object> AddConversationParams;

    public AddConversationParameters(Users sender, Users receiver, Residences residence, String subject) {
        AddConversationParams = new HashMap<>();

        AddConversationParams.put("senderId", sender.toJSON());
        AddConversationParams.put("receiverId", receiver.toJSON());
        AddConversationParams.put("residenceId", residence.toJSON());
        AddConversationParams.put("subject", subject);
    }

    public String getAddConversationParameters() {
        JSONObject jsonparams = new JSONObject(AddConversationParams);
        return jsonparams.toString();
    }
}
