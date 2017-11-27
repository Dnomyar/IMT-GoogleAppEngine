package com.zenika.zencontact.resource.auth;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = {"api/v0/users/*"})
public class AuthFilter implements Filter {

    private AuthenticationService authenticationService = AuthenticationService.getInstance();;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest)servletRequest;
        HttpServletResponse resp = (HttpServletResponse)servletResponse;

        String pathInfo = req.getPathInfo();
        if(pathInfo != null){ // {id}
            String[] pathParts = pathInfo.split("/");


            // only admin can delete
            if(req.getMethod() == "DELETE" && !authenticationService.isAdmin()){
                resp.setStatus(403);
                return;
            }

            if(authenticationService.isAuthenticated() && authenticationService.getUsername() != null){
                // user is already connected
                resp.setHeader("Username", authenticationService.getUsername());
                resp.setHeader("Logout", authenticationService.getLogoutURL("/#/clear"));
            } else {
                // only authent users can edit
                resp.setHeader("Location", authenticationService.getLoginURL("/#/edit/" + pathParts[0]));
                resp.setHeader("Logout", authenticationService.getLoginURL("/#/clear"));
                resp.setStatus(401);
                return;

            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
