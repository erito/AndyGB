package com.erikbuttram.andygb.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by erikb on 4/17/15.
 */
public class Venue {

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    @JsonProperty("city")
    private String city;
    @JsonProperty("state")
    private String state;
}
