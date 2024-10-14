package com.project.bankassetor.filter.response;

import lombok.Getter;

@Getter
public class LocationResponse {
    private String country;
    private String city;

    public LocationResponse(String country, String city) {
        this.country = country;
        this.city = city;
    }
}
