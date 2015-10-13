package com.opensoc.parsing.parsers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensoc.tldextractor.BasicTldExtractor;

@SuppressWarnings("serial")
public class BasicFalconHoseParser extends AbstractParser {

    protected static final Logger _LOG = LoggerFactory.getLogger(BasicFalconHoseParser.class);
    private JSONCleaner cleaner = new JSONCleaner();

    @SuppressWarnings("unchecked")
    public JSONObject parse(byte[] msg) {
        _LOG.trace("[OpenSOC] Starting to parse incoming message");

        String raw_message = null;

        try {

            raw_message = new String(msg, "UTF-8");
            _LOG.trace("[OpenSOC] Received message: " + raw_message);

            JSONObject cleaned_message = cleaner.Clean(raw_message);
            _LOG.debug("[OpenSOC] Cleaned message: " + raw_message);

            if (cleaned_message == null || cleaned_message.isEmpty()) {
                throw new Exception("Unable to clean message: " + raw_message);
            }

            JSONObject payload = (JSONObject)cleaned_message.get("event");

            if (payload == null) {
                throw new Exception("Unable to retrieve payload for message: "
                        + raw_message);
            }

            String originalString = "";
            for (Object k : payload.keySet()) {
                originalString += " " + k.toString() + ":" + payload.get(k).toString();
            }
            payload.put("original_string", originalString);

            if (payload.containsKey("LoginTime")) {
                Long ts = Long.parseLong(payload.remove("LoginTime").toString());
                payload.put("timestamp", ts * 1000);
                _LOG.trace("[OpenSOC] Added ts to: " + payload);
            } else if (payload.containsKey("ProcessStartTime")) {
                Long ts = Long.parseLong(payload.remove("ProcessStartTime").toString());
                payload.put("timestamp", ts);
                _LOG.trace("[OpenSOC] Added ts to: " + payload);
            } else {
                payload.put("timestamp", System.currentTimeMillis());
            }

            if (payload.containsKey("UserIp")) {
                String ip = payload.remove("UserIp").toString();
                payload.put("ip_src_addr", ip);
                payload.put("ip_dst_addr", ip);
                payload.put("ip_src_port", 0);
                payload.put("ip_dst_port", 0);
            } else if (payload.containsKey("ComputerName")) {
                String name = payload.remove("ComputerName").toString();
                payload.put("ip_src_addr", name);
                payload.put("ip_dst_addr", name);
                payload.put("ip_src_port", 0);
                payload.put("ip_dst_port", 0);
            }

            _LOG.trace("[OpenSOC] Inner message: " + payload);

            payload.put("protocol", "http");
            _LOG.debug("[OpenSOC] Returning parsed message: " + payload);

            return payload;
        } catch (Exception e) {
            _LOG.error("Unable to Parse Message: " + raw_message);
            e.printStackTrace();
            return null;
        }

    }


}
