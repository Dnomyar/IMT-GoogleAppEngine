package com.zenika.zencontact.resource.auth;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class AuthenticationService {
    private static AuthenticationService INSTANCE = new AuthenticationService();

    private UserService userService = UserServiceFactory.getUserService();

    public static AuthenticationService getInstance(){
        return INSTANCE;
    }

    public boolean isAuthenticated(){
        return userService.isUserLoggedIn();
    }

    public String getLoginURL(String url){
        return userService.createLoginURL(url);
    }

    public String getLogoutURL(String url){
        return userService.createLogoutURL(url);
    }

    public String getUsername(){
        return userService.getCurrentUser().getNickname();
    }

    public Boolean isAdmin(){
        return userService.isUserAdmin();
    }


}
