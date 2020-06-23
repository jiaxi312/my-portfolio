package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** BlobstoreServlet that responses an URL for uploading an image when requested*/
@WebServlet("/image-upload-url")
public class BlobstoreUploadUrlServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String uploadUrl = ServletHelper.DEFAULT_BLOBSTORE_SERVICE.createUploadUrl("/data");

    response.setContentType("text/html");
    response.getWriter().println(uploadUrl);
  }
}