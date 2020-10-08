package com.maynar;

import com.maynar.model.BaseMessage;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class AppTest {
  @Test
  public void successfulResponse() {
    App app = new App();
    String result = app.handleRequest(new BaseMessage(), null);
    assertNotNull(result);
  }
}
