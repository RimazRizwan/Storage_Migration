package com.mobitel.storageMigration.service;

import com.mobitel.storageMigration.properties.GlobalProperties;
import com.mobitel.storageMigration.repository.PrimaryKeyDataRepository;
import com.mobitel.storageMigration.repository.UserDataRepository;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

@Service
public class FileReadServiceImpl implements FileReadService {


    private final ExcelToTxtConverter excelToTxtConverter;

    private final UserDataRepository userDataRepository;

    private final GlobalProperties globalProperties;
    private final PrimaryKeyDataRepository primaryKeyDataRepository;

    // Define uniqueBoxRanges as a class field
    private final Set<String> uniqueBoxRanges = new HashSet<>();



    public FileReadServiceImpl(ExcelToTxtConverter excelToTxtConverter, ExcelToTxtConverter excelToTxtConverter1, UserDataRepository userDataRepository, PrimaryKeyDataRepository primaryKeyDataRepository, GlobalProperties globalProperties){
        this.excelToTxtConverter = excelToTxtConverter1;
        this.userDataRepository = userDataRepository;
        this.primaryKeyDataRepository = primaryKeyDataRepository;
        this.globalProperties = globalProperties;
    }

    @Override
    public String readExcelFile() {
        try {
//            String excelFilePath = "C:/Users/rimas/OneDrive/Desktop/Excel/Book2.xlsx";
//            String txtFilePath = "C:/Users/rimas/OneDrive/Desktop/Excel/Text.txt";
            excelToTxtConverter.convertExcelToTxt(globalProperties.getProperty1(), globalProperties.getProperty2());
            readTxtFile(globalProperties.getProperty2());
            return "Excel file successfully converted to Text file at: " + globalProperties.getProperty2();
        } catch (IOException ex) {
            ex.printStackTrace();
            return "Error converting Excel to Text.";
        }
    }

    private String readTxtFile(String txtFilePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(txtFilePath))) {
            String line;
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] values = line.split("\\|");
                String division = values[0].trim();
                double excelSerialNumber = Double.parseDouble(values[3].trim());
                LocalDate receivedDate = convertExcelDate(excelSerialNumber);

                String expireDate;
                try {
                    double expireDateSerialNumber = Double.parseDouble(values[4].trim());
                    expireDate = convertExcelDate(expireDateSerialNumber).toString();
                } catch (NumberFormatException e) {
                    expireDate = "LIFE TIME";
                }
                String boxRange = values[5].trim();



                //split the number in boxRange
                //for(String number: splitlist){
                //if(uniqueBoxRanges.contains(number)){}
                // }
                if (boxRange ==null) {
                    System.out.println("NULL DATA HERE");
//                    boxRange = boxRange + "A";
//                    primaryKeyDataRepository.addPrimaryKeyDetails(0, boxRange, division, "status");
                } else {
                    if (boxRange.contains(",")) {
                        String[] individualBoxes = boxRange.split(",");
                        for (String individualBox : individualBoxes) {
                            int dataExists = userDataRepository.checkData(individualBox.trim());
                            String newBoxValue = individualBox.trim();

                            for (int i = 1; i <= dataExists; i++) {
                                newBoxValue += "/IS-" + i;
                            }

                            userDataRepository.addEmpDetails("IS", receivedDate.toString(), expireDate, newBoxValue);
                            primaryKeyDataRepository.addPrimaryKeyDetails(individualBox.trim(), newBoxValue, "IS", "Done");
                        }


                    } else if (boxRange.contains("-")) {
                        String[] rangeParts = boxRange.split("-");
                        if (rangeParts.length == 2) {
                            int start = Integer.parseInt(rangeParts[0].trim());
                            int end = Integer.parseInt(rangeParts[1].trim());
                            for (int i = start; i <= end; i++) {
                                int dataExists = userDataRepository.checkData(String.valueOf(i).trim());
                                String newBoxValue = String.valueOf(i).trim();

                                for (int j = 1; j <= dataExists; j++) {
                                    newBoxValue += "/IS-" + j;
                                }

                                userDataRepository.addEmpDetails("IS", receivedDate.toString(), expireDate, newBoxValue);
                                primaryKeyDataRepository.addPrimaryKeyDetails(String.valueOf(i).trim(), newBoxValue, "IS", "Done");
                            }
                        }
                    } else {

                        int dataExists = userDataRepository.checkData(boxRange.trim());

                        for (int i = 1; i <= dataExists; i++) {
                            String suffix = (i == 1) ? "" : "/IS-" + i;
                            String newBoxValue = boxRange.trim() + suffix;
                            System.out.println("New box value ------------------------------>"+newBoxValue);

                            userDataRepository.addEmpDetails("IS", receivedDate.toString(), expireDate, newBoxValue);
                            primaryKeyDataRepository.addPrimaryKeyDetails(boxRange.trim(), newBoxValue, "IS", "Done");
                        }

                    }
                }
            }

            return "Data from Text file successfully added to the database.";
        } catch (IOException ex) {
            ex.printStackTrace();
            return "Error reading and saving data to the database.";
        }
    }

    private LocalDate convertExcelDate(double excelSerialNumber) {
        // Excel base date is January 0, 1900 (yes, January 0)
        Instant instant = Instant.ofEpochMilli((long) ((excelSerialNumber - 25569) * 86400 * 1000));
        return LocalDate.ofInstant(instant, ZoneId.systemDefault());
    }
}
