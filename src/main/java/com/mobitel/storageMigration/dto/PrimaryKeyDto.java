package com.mobitel.storageMigration.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PrimaryKeyDto {
    private String boxId;
    private String newBoxId;
    private String division;
    private String status;
}
