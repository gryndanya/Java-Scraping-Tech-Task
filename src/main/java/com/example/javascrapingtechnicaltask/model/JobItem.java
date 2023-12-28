package com.example.javascrapingtechnicaltask.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobItem {

    private String jobPageUrl;
    private String positionName;
    private String organizationUrl;
    private String linkToLogo;
    private String organizationTitle;
    private String laborFunction;
    private String location;
    private String postedDate;
    private String description;
    private String tagsNames;

}
