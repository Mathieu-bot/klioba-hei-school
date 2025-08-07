package school.hei.tsinjo.endpoint.rest.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import school.hei.tsinjo.repository.jpa.JEventRepository;
import school.hei.tsinjo.repository.jpa.model.JEvent;

@RestController
@AllArgsConstructor
public class TsinjoController {

  private final JEventRepository jEventRepository;

  @GetMapping("/")
  public List<JEvent> getEvents() {
    return jEventRepository.findAll();
  }
}
