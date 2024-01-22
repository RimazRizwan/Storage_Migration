package com.mobitel.storageMigration.dto;


import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserDto {
    private String devision;
    private String receivedDate;
    private String expireDate;
    private String boxRange;


}
