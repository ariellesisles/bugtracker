package com.project.bugtracker.service;

import com.project.bugtracker.dto.Bug;
import com.project.bugtracker.dto.BugTrackerConfiguration;
import com.project.bugtracker.enums.BugSeverity;
import com.project.bugtracker.enums.BugState;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class BugTrackerService {

    private static final List<String> INITIAL_APPS = List.of("App1", "App2", "App3", "App4", "App5");

    private final AtomicReference<BugTrackerConfiguration> trackerConfiguration =
            new AtomicReference<>(new BugTrackerConfiguration(INITIAL_APPS));

    private final AtomicLong idGenerator = new AtomicLong();

    private final List<Bug> bugs = Collections.synchronizedList(new ArrayList<>());


    @PostConstruct
    public void init() {
        long id = idGenerator.getAndIncrement();

        Bug b1 = new Bug(id, "johndoe", "Error login", "Unable to login","App1",
                BugSeverity.MAJOR, BugState.OPEN);
        bugs.add(b1);



        Bug b2 = new Bug(id, "johndoe", "Null on save", "Null On save","App2",
                BugSeverity.MAJOR, BugState.OPEN);
        bugs.add(b2);

        Bug b3 = new Bug(id, "johndoe", "Button Style", "Button text small","App2",
                BugSeverity.MINOR, BugState.OPEN);
        bugs.add(b3);
    }

}
