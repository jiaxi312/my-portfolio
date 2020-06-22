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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.gson.Gson;
import java.io.IOException;
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
    final int maxComments = Integer.parseInt(
                getParameterWithDefault(request, "comments-to-show", DEFALUT_COMMENTS_TO_SHOW));

    // Fetch the max number of comments from the datastore
    PreparedQuery commentEntities = DEFAULT_DATASTORE_SERVICE.prepare(new Query("Comment"));
    
    // Fetch the max number of comments
    response.setContentType("application/json");
    response.getWriter().println(
            GSON.toJson(commentEntities.asList(FetchOptions.Builder.withLimit(maxComments))));
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the information from the post
    String comment = getParameterWithDefault(request, "comment-input", "");
    if (comment.isEmpty()) {
        // Do nothing and redirect to the homepage for emtpy input
        response.sendRedirect("/index.html");
        return;
    }

    String name = getParameterWithDefault(request, "name-input", ANONYMOUS);
        
    // Check if the user want to submit the comment anonymously
    boolean isAnonymous = Boolean.parseBoolean(
                getParameterWithDefault(request, "anonymous", "false"));
    if (isAnonymous) {
      name = ANONYMOUS;
    }

    // Store the comment and name as entities into the datastore
    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("name", name);
    commentEntity.setProperty("content", comment);

    DEFAULT_DATASTORE_SERVICE.put(commentEntity);

    response.sendRedirect("/index.html");
  }

  /**
   * @return the request parameter, or the default value if the parameter
   *         was not specified by the client
   */
  private String getParameterWithDefault(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null || value.isEmpty()) {
        return defaultValue;
    }
    return value;
  }

  private static final String ANONYMOUS = "Anonymous";
  private static final DatastoreService DEFAULT_DATASTORE_SERVICE 
                          = DatastoreServiceFactory.getDatastoreService();
  private static final Gson GSON = new Gson();
  private static final String DEFALUT_COMMENTS_TO_SHOW = "0";
}
