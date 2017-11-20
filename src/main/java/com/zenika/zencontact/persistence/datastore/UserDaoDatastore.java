package com.zenika.zencontact.persistence.datastore;

import com.google.appengine.api.datastore.*;
import com.zenika.zencontact.domain.User;
import com.zenika.zencontact.persistence.UserDao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserDaoDatastore implements UserDao {

    private static UserDaoDatastore INSTANCE = new UserDaoDatastore();
    private final String kind = "User";
    public DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    public static UserDaoDatastore getInstance(){
        return INSTANCE;
    }

    private UserDaoDatastore(){

    }

    @Override
    public long save(User contact) {
        Entity user = new Entity(kind);
        if (contact.id != null){
            Key userKey = KeyFactory.createKey(kind, contact.id);
            try {
                user = datastore.get(userKey);
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
            }
        }
        user.setProperty("firstname", contact.firstName);
        user.setProperty("lastname", contact.lastName);
        user.setProperty("email", contact.email);
        if(contact.birthdate != null){
            user.setProperty("birthdate", contact.birthdate);
        }
        user.setProperty("notes", contact.notes);

        return
                datastore
                        .put(user)
                        .getId();
    }

    @Override
    public void delete(Long id) {
        Key userKey = KeyFactory.createKey(kind, id);
        datastore.delete(userKey);
    }

    @Override
    public User get(Long id) {
        Entity user;
        try {
            user = datastore.get(KeyFactory.createKey(kind, id));
        } catch (EntityNotFoundException e) {
            return null;
        }
        return createUserFromEntity(user);
    }

    private User createUserFromEntity(Entity user) {
        return User.create()
                .id(user.getKey().getId())
                .firstName((String) user.getProperty("firstname"))
                .lastName((String) user.getProperty("lastname"))
                .email((String) user.getProperty("email"))
                .birthdate((Date) user.getProperty("birthday"))
                .notes((String) user.getProperty("notes"));
    }

    @Override
    public List<User> getAll() {
        List<User> contacts = new ArrayList<>();

        Query q = new Query(kind)
                .addProjection(new PropertyProjection("firstname", String.class))
                .addProjection(new PropertyProjection("lastname", String.class))
                .addProjection(new PropertyProjection("email", String.class))
                .addProjection(new PropertyProjection("notes", String.class));

        PreparedQuery pq = datastore.prepare(q);
        for (Entity contact : pq.asIterable()) {
            contacts.add(createUserFromEntity(contact));
        }
        return contacts;
    }
}
