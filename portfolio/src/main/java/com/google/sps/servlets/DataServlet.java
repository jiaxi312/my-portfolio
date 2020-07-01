// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.cloud.vision.v1.EntityAnnotation;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Decide the max number of comments to display
    final int maxComments = Integer.parseInt(ServletHelper.getParameterWithDefault
                                (request, "comments-to-show", DEFALUT_COMMENTS_TO_SHOW));

    // Fetch the max number of comments from the datastore
    PreparedQuery commentEntities =
                        ServletHelper.DEFAULT_DATASTORE_SERVICE.prepare(new Query("Comment"));

    // Fetch the max number of comments
    response.setContentType("application/json");
    response.getWriter().println(
            ServletHelper.GSON.toJson(
                commentEntities.asList(FetchOptions.Builder.withLimit(maxComments))));
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the information from the post
    String comment = ServletHelper.getParameterWithDefault(request, "comment-input", "");
    if (comment.isEmpty()) {
        // Do nothing and redirect to the homepage for emtpy input
        response.sendRedirect("/index.html");
        return;
    }

    String imageUrl = ServletHelper.getUploadedFileUrl(request, "image");

    // Store the comment and name as entities into the datastore
    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("content", comment);
    commentEntity.setProperty("imageUrl", imageUrl);
    ServletHelper.DEFAULT_DATASTORE_SERVICE.put(commentEntity);

    showUploadedComment(response.getWriter(), comment, imageUrl);
    showImageLabels(request);
  }

  /** Shows the comment uploaded and the image if any*/
  private static void showUploadedComment(PrintWriter out, String comment, String imageUrl) {
    out.println("<p>Here's the comment you left: </p>");
    out.println(comment);
    out.println("<p>Here's the image you uploaded:</p>");
    out.println("<a href=\"" + imageUrl + "\">");
    out.println("<img src=\"" + imageUrl + "\" />");
    out.println("</a><br><br>");
    out.println("<a href=\"/index.html\"> click here to return </a>");
  }

  /** Shows the labels extracted from the uploaded impage*/
  private static void showImageLabels(HttpServletRequest request) throws IOException {
    BlobKey blobKey = ServletHelper.getBlobKey(request, "image");
    byte[] blobBytes = ServletHelper.getBlobBytes(blobKey);
    List<EntityAnnotation> imageLabels = ServletHelper.getImageLabels(blobBytes);

    // Display the labels to the console
    for (EntityAnnotation label : imageLabels) {
      System.out.println(label.getDescription() + " " + label.getScore());
    }
  }

  private static final String ANONYMOUS = "Anonymous";
  private static final String DEFALUT_COMMENTS_TO_SHOW = "0";
}
