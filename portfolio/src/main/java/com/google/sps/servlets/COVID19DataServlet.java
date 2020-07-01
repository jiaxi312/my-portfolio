package com.google.sps.servlets;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Scanner;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** When requested, returns the covid-19 active cases in each country as a JSON object */
@WebServlet("/covid-19-data")
public class COVID19DataServlet extends HttpServlet {

  @Override
  /** Reads the data from the local file */
  public void init() {
    Scanner scanner = new Scanner(getServletContext().getResourceAsStream(
      "/WEB-INF/country_wise_latest.csv"));
    scanner.nextLine();
    while (scanner.hasNextLine()) {
      String[] cells = scanner.nextLine().split(",");
      // Read the country name in 0th position and the active case in 9th position
      try {
        covid19Data.put(cells[0], Integer.valueOf(cells[4]));
      } catch (NumberFormatException e) {
        // the data is not avaliable for that country
        covid19Data.put(cells[0], DATA_MISSING);
      }
    }
    scanner.close();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    response.getWriter().println(ServletHelper.GSON.toJson(covid19Data));
  }

  private final LinkedHashMap<String, Integer> covid19Data = new LinkedHashMap<>();

  private static final int DATA_MISSING = -1;

}