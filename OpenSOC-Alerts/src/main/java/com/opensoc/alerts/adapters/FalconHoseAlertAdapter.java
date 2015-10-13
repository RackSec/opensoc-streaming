package com.opensoc.alerts.adapters;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.simple.JSONObject;
import org.apache.log4j.Logger;
import com.opensoc.alerts.interfaces.AlertsAdapter;

public class FalconHoseAlertAdapter implements AlertsAdapter, Serializable {

	protected static final Logger LOG = Logger
			.getLogger(FalconHoseAlertAdapter.class);

    public FalconHoseAlertAdapter(Map<String, String> config) {
    }

    @Override
    public boolean initialize() {
        return true;
    }

    @Override
    public boolean refresh() throws Exception {
        return false;
    }

    @Override
    public Map<String, JSONObject> alert(JSONObject raw_message) {
        Map<String, JSONObject> alerts = new HashMap<String, JSONObject>();

        JSONObject content = (JSONObject)raw_message.get("message");

        String dest = "unknown";

        if (content.containsKey("ip_dst_addr")) {
            dest = content.get("ip_dst_addr").toString();
        }

        String alert_id = generateAlertId();

        JSONObject alert = new JSONObject();

        alert.put("alert_id", alert_id);
        alert.put("designated_host", dest);
        alert.put("description", content.get("original_string").toString());

        if (content.containsKey("SeverityName")) {
            alert.put("priority", content.get("SeverityName").toString());
        } else {
            alert.put("priority", "MED");
        }

        alerts.put(alert_id, alert);

        return alerts;
    }

    @Override
    public boolean containsAlertId(String alert) {
        return false;
    }

	protected String generateAlertId() {
		String new_UUID = System.currentTimeMillis() + "-" + UUID.randomUUID();
		return new_UUID;

	}
}
