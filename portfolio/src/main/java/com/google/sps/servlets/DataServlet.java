package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/data")
public class DataServlet extends HttpServlet {
  private final Query COMMENTS_QUERY = new Query("Comment")
          .addSort("text", Query.SortDirection.ASCENDING);

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    PreparedQuery results = DatastoreServiceFactory.getDatastoreService().prepare(COMMENTS_QUERY);

    List<Comment> comments = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
      String text = (String) entity.getProperty("text");
      comments.add(new Comment(text));
    }

    response.setContentType("application/json;");
    response.getWriter().println(convertToJson(comments));
  }

  private String convertToJson(List<Comment> comments) {
    return new Gson().toJson(comments);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String comment = getParameter(request, "comment-input", "");
    if (!comment.isEmpty()) {
      addComment(comment);
    }
    response.sendRedirect("/index.html");
  }

  private void addComment(String text) {
    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("text", text);
    DatastoreServiceFactory.getDatastoreService().put(commentEntity);
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
