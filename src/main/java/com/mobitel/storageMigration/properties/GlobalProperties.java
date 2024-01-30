package com.mobitel.storageMigration.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource("classpath:application.properties")
public class GlobalProperties {

    @Value("${file.paths.excel}")
    private String property1;

    @Value("${file.paths.text}")
    private String property2;

    @Value("${com.storage.division}")
    private String division;



}
