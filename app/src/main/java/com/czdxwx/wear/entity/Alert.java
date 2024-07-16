package com.czdxwx.wear.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alert implements Serializable
{

    private Integer id;
    private Integer alertType;
    private String alertTitle;
    private String alertContent;
    private String latitude;
    private String longitude;
    private String province;
    private String city;
    private String district;
    private String temperature;
    private String dateTime;

}
