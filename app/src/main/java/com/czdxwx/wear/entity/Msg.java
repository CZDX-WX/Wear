package com.czdxwx.wear.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Msg {
    private String location;
    private String time;
    private String message;
}
