package school.hei.tsinjo.endpoint.http;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import school.hei.tsinjo.endpoint.http.model.DonationCreationForm;
import school.hei.tsinjo.service.DonationCreationFormConsumer;
import school.hei.tsinjo.service.EventService;

@Controller
@AllArgsConstructor
public class TsinjoController {

  private final EventService eventService;
  private final DonationCreationFormConsumer donationCreationFormConsumer;

  @GetMapping("/")
  public String getEvents(Model model) {
    model.addAttribute("events", eventService.findAllWithPaymentResolution());
    return "home";
  }

  @PostMapping("/donate")
  public String donate(DonationCreationForm donationCreationForm) {
    donationCreationFormConsumer.accept(donationCreationForm);
    return "redirect:/";
  }
}
