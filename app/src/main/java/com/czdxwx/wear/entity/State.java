package com.czdxwx.wear.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class State {
    private Integer id;
    private String latitude;
    private String longitude;
    private String province;
    private String city;
    private String district;
    private String temperature;
    private String dateTime;
    private String deviceName;
    private String owner;
}
