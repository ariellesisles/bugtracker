package com.project.bugtracker.service;

import com.project.bugtracker.dto.Bug;
import com.project.bugtracker.dto.BugTrackerConfiguration;
import com.project.bugtracker.enums.BugSeverity;
import com.project.bugtracker.enums.BugState;
import jakarta.annotation.PostConstruct;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

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

        Bug b1 = new Bug(id, "johndoe", "Error login", "Unable to login", "App1",
                BugSeverity.MAJOR, BugState.OPEN);
        bugs.add(b1);


        Bug b2 = new Bug(id, "johndoe", "Null on save", "Null On save", "App2",
                BugSeverity.MAJOR, BugState.OPEN);
        bugs.add(b2);

        Bug b3 = new Bug(id, "johndoe", "Button Style", "Button text small", "App2",
                BugSeverity.MINOR, BugState.OPEN);
        bugs.add(b3);
    }

    public List<Bug> findAllBugs() {
        return List.copyOf(this.bugs);
    }

    public Bug createBug(Bug bug) {
        OAuth2AuthenticationToken token =
                (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        OidcUser principal = (OidcUser) token.getPrincipal();

        long id = idGenerator.getAndIncrement();
        Bug newBug = new Bug(id,
                principal.getPreferredUsername(),
                bug.headline(),
                bug.description(),
                bug.project(),
                bug.severity(),
                bug.state());
        bugs.add(newBug);
        return bug;

    }

    public Bug updateBug(Bug bug) {
        int index = IntStream.range(0, this.bugs.size())
                .filter(i -> this.bugs.get(i).id().equals(bug.id()))
                .findFirst()
                .orElseThrow();

        this.bugs.set(index, bug);
        return bug;
    }

    public Bug getBug(long id) {
        return this.bugs.stream()
                .filter(
                        b -> b.id().equals(id))
                .findFirst()
                .orElseThrow();
    }

    public boolean deleteBug(Long bugId) {
        return this.bugs.removeIf(b -> b.id().equals(bugId));
    }


    public BugTrackerConfiguration getConfiguration() {
        return trackerConfiguration.get();

    }



}
