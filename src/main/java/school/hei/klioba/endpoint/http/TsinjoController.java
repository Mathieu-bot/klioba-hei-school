package school.hei.klioba.endpoint.http;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import school.hei.klioba.endpoint.http.model.MembershipCreationForm;
import school.hei.klioba.endpoint.http.model.ThEvent;
import school.hei.klioba.endpoint.http.model.ThFund;
import school.hei.klioba.service.EventService;
import school.hei.klioba.service.MembershipCreationFormConsumer;
import school.hei.klioba.service.MembershipFormService;

@Controller
@AllArgsConstructor
public class TsinjoController {

  private final EventService eventService;
  private final MembershipCreationFormConsumer membershipCreationFormConsumer;
  private final MembershipFormService membershipFormService;

  @GetMapping("/")
  public String home() {
    return "home";
  }

  @GetMapping("/history")
  public String history(
      Model model,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "50") int size) {
    var events = eventService.findAllWithPaymentResolution();
    var thEvents = events.stream().map(ThEvent::new).toList();
    int total = thEvents.size();
    int fromIndex = Math.min(page * size, total);
    int toIndex = Math.min(fromIndex + size, total);
    var pagedEvents = thEvents.subList(fromIndex, toIndex);
    model.addAttribute("events", pagedEvents);
    model.addAttribute("fund", new ThFund(events));
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", (int) Math.ceil((double) total / size));

    return "history";
  }

  @PostMapping("/club/{clubId}/membershipFee")
  public String membershipFee(
      @RequestParam String clubId,
      Authentication authentication,
      MembershipCreationForm membershipCreationForm) {
    var defaultOAuth2User = ((DefaultOAuth2User) authentication.getPrincipal());
    var email = defaultOAuth2User.getAttributes().get("email").toString();
    membershipCreationFormConsumer.accept(membershipCreationForm, email, clubId);
    return "redirect:/history";
  }

  @GetMapping("/club/{clubId}/membershipFee")
  public String membershipFee(
      @PathVariable String clubId, Authentication authentication, Model model) {
    var defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
    var email = defaultOAuth2User.getAttributes().get("email").toString();
    MembershipCreationForm membershipForm = membershipFormService.getPrefilledDonationForm(email);
    model.addAttribute("membershipForm", membershipForm);
    return "membership-fee";
  }

  @GetMapping("/logout")
  public String showLogoutConfirmation() {
    return "logout-confirm";
  }
}
