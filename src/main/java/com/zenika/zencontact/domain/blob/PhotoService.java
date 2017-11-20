package com.zenika.zencontact.domain.blob;

import com.google.appengine.api.blobstore.*;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.zenika.zencontact.domain.User;
import com.zenika.zencontact.persistence.objectify.UserDaoObjectify;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PhotoService {

    private static PhotoService INSTANCE = new PhotoService();

    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    private static final Logger logger = Logger.getLogger(PhotoService.class.getName());


    public static PhotoService getInstance() {
        return INSTANCE;
    }

    private PhotoService(){}


    public void preparedUploadURL(User contact){
        String uploadURL = blobstoreService.createUploadUrl("/api/v0/photo/" + contact.id);
        logger.warning("Upload url : " + uploadURL);
        contact.uploadURL(uploadURL);
    }

    public User prepareDownloadURL(User contact){
        BlobKey photoKey = contact.photoKey;
        if(photoKey != null){
            String url = "/api/v0/photo/" + contact.id + "/" + photoKey.getKeyString();
            contact.downloadURL(url);
        }
        return contact;
    }

    public void updatePhoto(Long id, HttpServletRequest req) {
        Map<String, List<BlobKey>> uploads = blobstoreService.getUploads(req);
        if (!uploads.keySet().isEmpty()) {
            // delete old photo from BlobStore to save disk space
            deleteOldBlob(id);
            // update photo BlobKey in Contact entity
            Iterator<String> names = uploads.keySet().iterator();
            String name = names.next();
            List<BlobKey> keys = uploads.get(name);
            User contact = UserDaoObjectify.getInstance().get(id)
                    .photoKey(keys.get(0));
            UserDaoObjectify.getInstance().save(contact);
        }
    }

    private void deleteOldBlob(Long id) {
        BlobKey blogKey = UserDaoObjectify.getInstance().fetchOldBlob(id);
        if(blogKey != null){
            blobstoreService.delete(blogKey);
        }
    }

    public void serve(BlobKey blobKey, HttpServletResponse resp)
            throws IOException {
        BlobInfoFactory blobInfoFactory = new BlobInfoFactory(
                DatastoreServiceFactory.getDatastoreService());
        BlobInfo blobInfo = blobInfoFactory.loadBlobInfo(blobKey);
        logger.log(Level.INFO, "Serving " + blobInfo.getFilename());
        resp.setHeader("Content-Disposition", "attachment; filename="
                + blobInfo.getFilename());
        blobstoreService.serve(blobKey, resp);
    }
}
