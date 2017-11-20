package com.zenika.zencontact.resource;

import com.google.appengine.api.blobstore.BlobKey;
import com.zenika.zencontact.domain.blob.PhotoService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(name = "PhotoRessource", value = "/api/v0/photo/*")
public class PhotoRessource extends HttpServlet {

    private static final Logger logger = Logger.getLogger(PhotoRessource.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Calling callback");
        String pathInfo = req.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        if(pathParts.length == 0){
            resp.setStatus(404);
            return;
        }
        String blobKey = pathParts[2];
        PhotoService.getInstance().serve(new BlobKey(blobKey), resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Calling callback");
        String pathInfo = req.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        if(pathParts.length == 0){
            resp.setStatus(404);
            return;
        }
        Long id = Long.valueOf(pathParts[1]);
        PhotoService.getInstance().updatePhoto(id, req);
        resp.setContentType("text/plain");
        resp.getWriter().println("");
    }
}
