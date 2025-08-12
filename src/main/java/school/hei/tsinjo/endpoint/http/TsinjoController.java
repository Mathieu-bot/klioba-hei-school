package school.hei.tsinjo.endpoint.http;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import school.hei.tsinjo.endpoint.http.model.DonationCreationForm;
import school.hei.tsinjo.endpoint.http.model.ThEvent;
import school.hei.tsinjo.endpoint.http.model.ThFund;
import school.hei.tsinjo.service.DonationCreationFormConsumer;
import school.hei.tsinjo.service.EventService;

@Controller
@AllArgsConstructor
public class TsinjoController {

  private final EventService eventService;
  private final DonationCreationFormConsumer donationCreationFormConsumer;

  @GetMapping("/")
  public String home() {
    return "home";
  }

  @GetMapping("/history")
  public String history(Model model) {
    var events = eventService.findAllWithPaymentResolution();
    var thEvents = events.stream().map(ThEvent::new).toList();
    model.addAttribute("events", thEvents);
    model.addAttribute("fund", new ThFund(events));
    return "history";
  }

  @GetMapping("/donate")
  public String donate() {
    return "donate";
  }

  @PostMapping("/donate")
  public String donate(Authentication authentication, DonationCreationForm donationCreationForm) {
    var defaultOAuth2User = ((DefaultOAuth2User) authentication.getPrincipal());
    var email = defaultOAuth2User.getAttributes().get("email").toString();
    donationCreationFormConsumer.accept(donationCreationForm, email);
    return "redirect:/history";
  }
}
