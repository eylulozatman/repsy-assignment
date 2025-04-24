package com.eylul.repsy.repsy_assignment.dto;

import lombok.Data;
import java.util.List;

@Data
public class MetaDataDTO {
    private String name;
    private String version;
    private String author;
    private List<Dependency> dependencies;
}
