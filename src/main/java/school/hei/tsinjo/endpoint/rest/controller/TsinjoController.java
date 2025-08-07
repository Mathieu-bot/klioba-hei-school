package school.hei.tsinjo.endpoint.rest.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import school.hei.tsinjo.repository.EventRepository;

@Controller
@AllArgsConstructor
public class TsinjoController {

  private final EventRepository eventRepository;

  @GetMapping("/")
  public String getEvents(Model model) {
    model.addAttribute("events", eventRepository.findAll());
    return "events";
  }
}
