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
import school.hei.klioba.endpoint.http.model.MembershipFeeCreationForm;
import school.hei.klioba.endpoint.http.model.ThEvent;
import school.hei.klioba.endpoint.http.model.ThFund;
import school.hei.klioba.service.ClubService;
import school.hei.klioba.service.EventService;
import school.hei.klioba.service.MembershipFeeCreationFormConsumer;
import school.hei.klioba.service.MembershipFormService;

@Controller
@AllArgsConstructor
public class KliobaController {

  private final EventService eventService;
  private final ClubService clubService;
  private final MembershipFeeCreationFormConsumer membershipFeeCreationFormConsumer;
  private final MembershipFormService membershipFormService;

  @GetMapping("/")
  public String home(Model model) {
    var clubs = clubService.findAll();
    model.addAttribute("clubs", clubs);
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

  @GetMapping("/clubs/{clubId}")
  public String showClubDetail(@PathVariable String clubId, Model model) {
    var club = clubService.findById(clubId);
    var events = eventService.findAllByClubIdWithPaymentResolution(clubId);
    var thEvents = events.stream().map(ThEvent::new).toList();
    model.addAttribute("club", club);
    model.addAttribute("events", thEvents);
    model.addAttribute("fund", new ThFund(events));
    return "clubDetail";
  }

  @PostMapping("/club/{clubId}/membershipFee")
  public String membershipFee(
      @PathVariable String clubId,
      Authentication authentication,
      MembershipFeeCreationForm membershipFeeCreationForm) {
    var defaultOAuth2User = ((DefaultOAuth2User) authentication.getPrincipal());
    var email = defaultOAuth2User.getAttributes().get("email").toString();
    membershipFeeCreationFormConsumer.accept(membershipFeeCreationForm, email, clubId);
    return "redirect:/history";
  }

  @GetMapping("/club/{clubId}/membershipFee")
  public String showMembershipFeeForm(
      @PathVariable String clubId, Authentication authentication, Model model) {
    var defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
    var email = defaultOAuth2User.getAttributes().get("email").toString();
    var club = clubService.findById(clubId);
    var form = membershipFormService.getPrefilledDonationForm(email);
    model.addAttribute("club", club);
    model.addAttribute("membershipForm", form);
    return "membership-fee";
  }

  @GetMapping("/logout")
  public String showLogoutConfirmation() {
    return "logout-confirm";
  }
}
