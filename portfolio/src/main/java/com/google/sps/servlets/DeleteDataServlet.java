package com.google.sps.servlets;

import java.io.IOException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/delete-data")
public class DeleteDataServlet extends HttpServlet {
  private final Query COMMENTS_QUERY = new Query("Comment")
      .addSort("text", Query.SortDirection.ASCENDING);
  
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(COMMENTS_QUERY);
    for (Entity comment : results.asIterable()) {
      datastore.delete(comment.getKey());
    }
    response.sendRedirect("/index.html");
  }
}