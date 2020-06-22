package com.google.sps.servlets;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** BlobstoreServlet that responses an URL for uploading an image when requested*/
@WebServlet("/blobstore-upload-url")
public class BlobstoreUploadUrlServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String uploadUrl = DEFAULT_BLOBSTORE_SERVICE.createUploadUrl("/data");

    response.setContentType("text/html");
    response.getWriter().println(uploadUrl);
  }

  private static final BlobstoreService DEFAULT_BLOBSTORE_SERVICE 
                              = BlobstoreServiceFactory.getBlobstoreService();
}