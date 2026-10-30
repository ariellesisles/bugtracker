package com.project.bugtracker.controller;

import com.project.bugtracker.dto.Bug;
import com.project.bugtracker.dto.BugTrackerConfiguration;
import com.project.bugtracker.service.BugTrackerService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import static utils.CommonUtils.prettyPrintToken;

@Controller
@RequiredArgsConstructor
@RequestMapping("/bugtracker/ui")
public class BugTruckerController {

    private final BugTrackerService service;



    @GetMapping("")
    public ModelAndView index(OAuth2AuthenticationToken token,
                        @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient client) {

        OidcUser principal = (OidcUser) token.getPrincipal();
        ModelAndView mv = generateDefaultModel(token);

        mv.setViewName("home");
        mv.addObject("accessToken", prettyPrintToken(client.getAccessToken().getTokenValue()));
        mv.addObject("refreshToken", prettyPrintToken(client.getRefreshToken().getTokenValue()));
        mv.addObject("idToken", prettyPrintToken(principal.getIdToken().getTokenValue()));

        return mv;

    }

    @GetMapping("/show-bugs")
    public ModelAndView showBugs(OAuth2AuthenticationToken token)
    {
        ModelAndView mv = generateDefaultModel(token);
        mv.setViewName("show-bugs");
        mv.addObject("bugs", service.findAllBugs());
        return mv;
    }

    @GetMapping("/create")
    private ModelAndView createBug(OAuth2AuthenticationToken token)
    {
        ModelAndView mv = generateDefaultModel(token);

        BugTrackerConfiguration config = service.getConfiguration();

        mv.setViewName("create-bug");
        mv.addObject("bug", Bug.emptyBug(token.getName()));
        mv.addObject("projects", config.projects());

        return mv;

    }

    @PostMapping("/save-form")
    public String saveBug(@ModelAttribute Bug bug){

        if(bug.id() == null){
            service.createBug(bug);
        }else{
            service.updateBug(bug);
        }

        return "redirect:/bugtracker/ui/show-bugs";
    }

    @GetMapping("/update-form")
    public ModelAndView updateBug(
            OAuth2AuthenticationToken token,
            @RequestParam @NotNull Long bugId){

        ModelAndView mv = generateDefaultModel(token);

        Bug bug = service.getBug(bugId);
        BugTrackerConfiguration config = service.getConfiguration();

       mv.setViewName("update-form");
       mv.addObject("bug", bug);
       mv.addObject("projects", config.projects());
       return mv;

    }

    @GetMapping("/delete-bug")
    public String deleteBug(@RequestParam @NotNull Long bugId){

        service.deleteBug(bugId);
        return "redirect:/bugtracker/ui/show-bugs";
    }

    private ModelAndView generateDefaultModel(OAuth2AuthenticationToken token) {

        OidcUser principal = (OidcUser) token.getPrincipal();
        ModelAndView model = new ModelAndView();
        model.addObject("user", principal);
        return model;
    }


}
