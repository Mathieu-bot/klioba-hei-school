package school.hei.tsinjo.endpoint.rest.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import school.hei.tsinjo.model.Event;

@RestController
public class TsinjoController {

  @GetMapping("/")
  public List<Event> getEvents() {
    throw new RuntimeException("TODO");
  }
}
