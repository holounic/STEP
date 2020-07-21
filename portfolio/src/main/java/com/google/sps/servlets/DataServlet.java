package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

@WebServlet("/data")
public class DataServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    List<String> numbers = new ArrayList<>(
      Arrays.asList("one", "two", "five"));
    String json = convertToJson(numbers);
    
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  private String convertToJson(List<String> arr) {
    if (arr.isEmpty()) {
      return "";
    }
    StringBuilder json = new StringBuilder("[\n");
    int len = arr.size();

    for (int i = 0; i < len - 1; i++) {
      json.append(toJsonBlock(arr.get(i))).append(",\n");
    }
    json.append(toJsonBlock(arr.get(len - 1))).append("\n]");
    return json.toString();
  }

  private String toJsonBlock(String number) {
    return "{\"number\": " + "\"" + number + "\"}";
  }

}
