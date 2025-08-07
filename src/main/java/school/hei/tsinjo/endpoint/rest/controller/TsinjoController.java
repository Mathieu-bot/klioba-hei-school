package school.hei.tsinjo.endpoint.rest.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import school.hei.tsinjo.model.Event;
import school.hei.tsinjo.repository.EventRepository;

@RestController
@AllArgsConstructor
public class TsinjoController {

  private final EventRepository eventRepository;

  @GetMapping("/")
  public List<Event> getEvents() {
    return eventRepository.findAll();
  }
}
