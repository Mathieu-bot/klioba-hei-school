package school.hei.tsinjo.endpoint.http;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import school.hei.tsinjo.endpoint.http.model.ThEvent;
import school.hei.tsinjo.endpoint.http.model.ThFund;
import school.hei.tsinjo.service.EventService;

@Controller
@AllArgsConstructor
public class KliobaController {

  private final EventService eventService;

  @GetMapping("/history/{clubId}")
  public String historyByClub(@PathVariable String clubId, Model model) {
    var events = eventService.findAllByClubIdWithPaymentResolution(clubId);
    var thEvents = events.stream().map(ThEvent::new).toList();
    model.addAttribute("events", thEvents);
    model.addAttribute("fund", new ThFund(events));
    model.addAttribute("currentPage", 0);
    model.addAttribute("totalPages", 1);
    return "history";
  }
}
