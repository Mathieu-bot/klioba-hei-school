package school.hei.tsinjo.endpoint.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

class ErrorHandlerTest {

  private ErrorHandler errorHandler;
  private Model model;

  @BeforeEach
  void setUp() {
    errorHandler = new ErrorHandler();
    model = mock(Model.class);
  }

  @Test
  void handleException_setsErrorMessageAndReturnsErrorView() {
    // Arrange
    Exception exception = new RuntimeException("Test error message");

    // Act
    String result = errorHandler.handleException(exception, model);

    // Assert
    assertEquals("error", result);
    verify(model).addAttribute("errorMessage", "Test error message");
  }

  @Test
  void handleException_withNullMessage_setsNullErrorMessage() {
    // Arrange
    Exception exception = new RuntimeException((String) null);

    // Act
    String result = errorHandler.handleException(exception, model);

    // Assert
    assertEquals("error", result);
    verify(model).addAttribute("errorMessage", null);
  }

  @Test
  void handleException_withDifferentExceptionTypes_handlesAll() {
    // Arrange
    IllegalArgumentException illegalArgException = new IllegalArgumentException("Invalid argument");

    // Act
    String result = errorHandler.handleException(illegalArgException, model);

    // Assert
    assertEquals("error", result);
    verify(model).addAttribute("errorMessage", "Invalid argument");
  }
}
