package com.erikbuttram.andygb.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by erikb on 4/17/15.
 *  "data": [
 {
 "startDate": "<date>",
 "endDate": "<date>",
 "name": "<name>",
 "url": "<url>",
 "venue": {"city": "<city>", "state": "<state>"},
 "icon": "<url to png image>"
 },
 … <more objects>
 ]
 }
 */
public class AwesomePojo {

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getIcon() {
        return icon;
    }

    @JsonProperty("startDate")
    private String startDate;
    @JsonProperty("endDate")
    private String endDate;
    @JsonProperty("name")
    private String name;
    @JsonProperty("url")
    private String url;
    @JsonProperty("icon")
    private String icon;

}
