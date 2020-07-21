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
  private List<String> comments = new ArrayList<>();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String json = convertToJson(comments);
    
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  private String convertToJson(List<String> arr) {
    StringBuilder json = new StringBuilder("[\n");
    int len = arr.size();
    String field = "comment";

    for (int i = 0; i < len - 1; i++) {
      json.append(toJsonBlock(field, arr.get(i))).append(",\n");
    }
    json.append(toJsonBlock(field, arr.get(len - 1))).append("\n]");
    return json.toString();
  }


  private String toJsonBlock(String field, String comment) {
    return "{\"" + field + "\": " + "\"" + comment + "\"}";
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String comment = getParameter(request, "comment-input", "");
    response.setContentType("text/html;");
    
    if (!comment.isEmpty()) {
      comments.add(comment);
    }
    
    response.sendRedirect("/index.html");
  }

  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    } else {
      return value;
    }
  }

}
