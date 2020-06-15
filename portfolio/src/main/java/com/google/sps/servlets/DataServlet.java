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

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import com.google.gson.Gson;


/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String json = getInformationInJson();
        response.setContentType("application/json");
        response.getWriter().println(json);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Get the information from the post
        String comments = getParameter(request, "comment-input", "");
        String name = getParameter(request, "name-input", "Anonymous");
        boolean isAnonymous = Boolean.parseBoolean(
                                getParameter(request, "anonymous", "false"));
        
        // Check if the user want to submit the comment anonymously
        if (isAnonymous) {
            name = "Anonymous";
        }

        // Respond with the result
        response.setContentType("text/html");
        response.getWriter().println(name + " leaves comment: ");
        response.getWriter().println(comments);
    }

    /**
     * @return the request parameter, or the default value if the parameter
     *         was not specified by the client
     */
    private String getParameter(HttpServletRequest request, String name, String defaultValue) {
        String value = request.getParameter(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    /**
     * Creates an ArrayList containing three information, then converts it
     * json.
     * @return a String containing the json format of the those information
     */
    private static String getInformationInJson() {
        // create an ArrayList containing three personal information
        ArrayList<String> information = new ArrayList<>();
        information.add("Jiaxi Chen");
        information.add("Chongqing");

        // get the current time and adds to the list
        information.add(new Date().toString());

        // convert it to json format and returns that json
        return new Gson().toJson(information);
    }
}
