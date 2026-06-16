package school.hei.klioba.endpoint.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.ui.Model;
import school.hei.klioba.endpoint.http.model.MembershipCreationForm;
import school.hei.klioba.model.Donation;
import school.hei.klioba.model.Payment;
import school.hei.klioba.model.PaymentStatus;
import school.hei.klioba.model.User;
import school.hei.klioba.model.psp.PspType;
import school.hei.klioba.service.EventService;
import school.hei.klioba.service.MembershipCreationFormConsumer;
import school.hei.klioba.service.MembershipFormService;

class TsinjoControllerTest {

  private TsinjoController controller;
  private EventService eventService;
  private MembershipCreationFormConsumer membershipCreationFormConsumer;
  private MembershipFormService membershipFormService;
  private Model model;
  private Authentication authentication;

  @BeforeEach
  void setUp() {
    eventService = mock(EventService.class);
    membershipCreationFormConsumer = mock(MembershipCreationFormConsumer.class);
    membershipFormService = mock(MembershipFormService.class);
    model = mock(Model.class);
    authentication = mock(Authentication.class);

    controller =
        new TsinjoController(eventService, membershipCreationFormConsumer, membershipFormService);
  }

  @Test
  void home_returnsHomeView() {
    String result = controller.home();
    assertEquals("home", result);
  }

  @Test
  void history_withDefaultPagination_returnsHistoryView() {
    var user = new User("1", "John", "Doe", "john@example.com");
    var payment =
        new Payment(
            "p1",
            1000,
            PspType.ORANGE_MONEY,
            "PSP123",
            PaymentStatus.CONFIRMED,
            Instant.now(),
            Instant.now());
    Donation donation = new Donation("d1", payment, user, Instant.now());

    when(eventService.findAllWithPaymentResolution()).thenReturn(List.of(donation));

    String result = controller.history(model, 0, 50);

    assertEquals("history", result);
    verify(model).addAttribute(eq("events"), anyList());
    verify(model).addAttribute(eq("fund"), any());
    verify(model).addAttribute("currentPage", 0);
    verify(model).addAttribute("totalPages", 1);
  }

  @Test
  void history_withCustomPagination_returnsPagedEvents() {
    var user = new User("1", "John", "Doe", "john@example.com");
    var payment =
        new Payment(
            "p1",
            1000,
            PspType.ORANGE_MONEY,
            "PSP123",
            PaymentStatus.CONFIRMED,
            Instant.now(),
            Instant.now());

    List<school.hei.klioba.model.Event> events =
        List.of(
            new Donation("d1", payment, user, Instant.now()),
            new Donation("d2", payment, user, Instant.now()),
            new Donation("d3", payment, user, Instant.now()),
            new Donation("d4", payment, user, Instant.now()),
            new Donation("d5", payment, user, Instant.now()));

    when(eventService.findAllWithPaymentResolution()).thenReturn(events);

    var result = controller.history(model, 1, 2);

    assertEquals("history", result);
    verify(model).addAttribute(eq("events"), anyList());
    verify(model).addAttribute("currentPage", 1);
    verify(model).addAttribute("totalPages", 3);
  }

  @Test
  void history_withEmptyEvents_returnsEmptyHistory() {
    when(eventService.findAllWithPaymentResolution()).thenReturn(List.of());

    String result = controller.history(model, 0, 50);

    assertEquals("history", result);
    verify(model).addAttribute(eq("events"), anyList());
    verify(model).addAttribute("currentPage", 0);
    verify(model).addAttribute("totalPages", 0);
  }

  @Test
  void donate_get_returnsPrefilledDonationForm() {
    var email = "test@example.com";
    Map<String, Object> attributes = new HashMap<>();
    attributes.put("email", email);

    DefaultOAuth2User oAuth2User = mock(DefaultOAuth2User.class);
    when(oAuth2User.getAttributes()).thenReturn(attributes);
    when(authentication.getPrincipal()).thenReturn(oAuth2User);

    var prefilledForm = new MembershipCreationForm("John", "Doe", "");
    when(membershipFormService.getPrefilledDonationForm(email)).thenReturn(prefilledForm);

    var result = controller.membershipFee(authentication, model);

    assertEquals("donate", result);
    verify(membershipFormService).getPrefilledDonationForm(email);
    verify(model).addAttribute("donationForm", prefilledForm);
  }

  @Test
  void donate_post_processesFormAndRedirects() {
    var email = "test@example.com";
    Map<String, Object> attributes = new HashMap<>();
    attributes.put("email", email);

    DefaultOAuth2User oAuth2User = mock(DefaultOAuth2User.class);
    when(oAuth2User.getAttributes()).thenReturn(attributes);
    when(authentication.getPrincipal()).thenReturn(oAuth2User);

    var form = new MembershipCreationForm("John", "Doe", "PSP123");

    var result = controller.donate(authentication, form);

    assertEquals("redirect:/history", result);
    verify(membershipCreationFormConsumer).accept(form, email);
  }

  @Test
  void logout_showsLogoutConfirmation() {
    String result = controller.showLogoutConfirmation();
    assertEquals("logout-confirm", result);
  }
}
