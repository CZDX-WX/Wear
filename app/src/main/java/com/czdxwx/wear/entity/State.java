package com.czdxwx.wear.entity;

import java.util.Date;

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
    private Date time;
    private String device_name;
}
