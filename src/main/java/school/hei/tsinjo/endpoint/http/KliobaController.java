package school.hei.tsinjo.endpoint.http;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import school.hei.tsinjo.endpoint.http.model.ThEvent;
import school.hei.tsinjo.endpoint.http.model.ThFund;
import school.hei.tsinjo.service.EventService;

@Controller
@AllArgsConstructor
public class KliobaController {

  private final EventService eventService;

  @GetMapping("/history/{clubId}")
  public String historyByClub(
      @PathVariable String clubId,
      Model model,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "50") int size) {
    var events = eventService.findAllByClubIdWithPaymentResolution(clubId);
    var thEvents = events.stream().map(ThEvent::new).toList();
    int total = thEvents.size();
    int fromIndex = Math.min(page * size, total);
    int toIndex = Math.min(fromIndex + size, total);
    var pagedEvents = thEvents.subList(fromIndex, toIndex);

    model.addAttribute("events", pagedEvents);
    model.addAttribute("fund", new ThFund(events));
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", (int) Math.ceil((double) total / size));
    model.addAttribute("size", size);
    model.addAttribute("clubId", clubId);
    return "history";
  }
}
