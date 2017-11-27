package com.zenika.zencontact.resource;

import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.zenika.zencontact.domain.User;
import com.google.gson.Gson;
import com.zenika.zencontact.persistence.UserDao;
import com.zenika.zencontact.persistence.datastore.UserDaoDatastore;
import com.zenika.zencontact.persistence.objectify.UserDaoObjectify;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// With @WebServlet annotation the webapp/WEB-INF/web.xml is no longer required.
@WebServlet(name = "UserResource", value = "/api/v0/users")
public class UserResource extends HttpServlet {

    //private UserDao userDao = UserDaoDatastore.getInstance();
    private UserDao userDao = UserDaoObjectify.getInstance();

    private static final java.util.logging.Logger logger = Logger.getLogger(UserResource.class.getName());

    public static final String CONTACT_CACHE_KEY = "com.zenika.zencontact.resource.UserResource.getAll";
    public static final MemcacheService cache = MemcacheServiceFactory.getMemcacheService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        List<User> contacts = (List<User>) cache.get(CONTACT_CACHE_KEY);
        if(contacts == null){
            contacts = userDao.getAll();
            cache.put(CONTACT_CACHE_KEY, contacts, Expiration.byDeltaMillis(240), MemcacheService.SetPolicy.ADD_ONLY_IF_NOT_PRESENT);
            logger.info("Caching");
        }else {
            logger.info("Retrieving from cache");
        }
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().println(new Gson().toJsonTree(contacts).getAsJsonArray());
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        User user = new Gson().fromJson(request.getReader(), User.class);
        user.id(userDao.save(user));
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(201);
        response.getWriter().println(new Gson().toJson(user));
    }
}
