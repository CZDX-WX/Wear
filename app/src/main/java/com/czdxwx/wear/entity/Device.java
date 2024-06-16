package com.czdxwx.wear.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {
    private int id;
    private String name;
    private String owner;
    private Integer isOnline;
}
