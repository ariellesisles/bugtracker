package com.project.bugtracker.dto;

import com.project.bugtracker.enums.BugSeverity;
import com.project.bugtracker.enums.BugState;
import jakarta.validation.constraints.NotNull;


public record Bug(Long id, String submitter, @NotNull String headline, @NotNull
String description, @NotNull String project, BugSeverity severity, BugState state) {


    public static Bug emptyBug(String submitter){
        return new Bug(
                null,
                submitter,
                null,
                null,
                null,
                BugSeverity.LOW,
                BugState.OPEN );
    }
}
