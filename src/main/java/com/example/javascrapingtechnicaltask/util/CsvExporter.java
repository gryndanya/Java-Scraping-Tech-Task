package com.example.javascrapingtechnicaltask.util;

import com.example.javascrapingtechnicaltask.model.JobItem;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CsvExporter {
    public static void exportToCsv(List<JobItem> jobs, String jobFunction) {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter("src/main/resources/saves/" + (LocalDate.now()+ "-" + jobFunction) + ".csv"));
            writer.writeNext(new String[]{"Job Page Url",
                    "Position Name",
                    "Url To Organization",
                    "Logo",
                    "Organization Title",
                    "Labor Function",
                    "Location",
                    "Posted Date",
                    "Description",
                    "Tags Names"
            });

            for (JobItem job : jobs) {
                writer.writeNext(new String[]{job.getJobPageUrl(),
                        job.getPositionName(),
                        job.getOrganizationUrl(),
                        job.getLinkToLogo(),
                        job.getOrganizationTitle(),
                        job.getLaborFunction(),
                        job.getLocation(),
                        job.getPostedDate(),
                        job.getDescription(),
                        job.getTagsNames()
                });
            }
            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
