package school.hei.tsinjo.endpoint.http;

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
import school.hei.tsinjo.endpoint.http.model.DonationCreationForm;
import school.hei.tsinjo.model.Donation;
import school.hei.tsinjo.model.Payment;
import school.hei.tsinjo.model.PaymentStatus;
import school.hei.tsinjo.model.User;
import school.hei.tsinjo.model.psp.PspType;
import school.hei.tsinjo.service.DonationCreationFormConsumer;
import school.hei.tsinjo.service.DonationFormService;
import school.hei.tsinjo.service.EventService;

class TsinjoControllerTest {

  private TsinjoController controller;
  private EventService eventService;
  private DonationCreationFormConsumer donationCreationFormConsumer;
  private DonationFormService donationFormService;
  private Model model;
  private Authentication authentication;

  @BeforeEach
  void setUp() {
    eventService = mock(EventService.class);
    donationCreationFormConsumer = mock(DonationCreationFormConsumer.class);
    donationFormService = mock(DonationFormService.class);
    model = mock(Model.class);
    authentication = mock(Authentication.class);

    controller =
        new TsinjoController(eventService, donationCreationFormConsumer, donationFormService);
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

    List<school.hei.tsinjo.model.Event> events =
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

    var prefilledForm = new DonationCreationForm("John", "Doe", "");
    when(donationFormService.getPrefilledDonationForm(email)).thenReturn(prefilledForm);

    var result = controller.donate(authentication, model);

    assertEquals("donate", result);
    verify(donationFormService).getPrefilledDonationForm(email);
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

    var form = new DonationCreationForm("John", "Doe", "PSP123");

    var result = controller.donate(authentication, form);

    assertEquals("redirect:/history", result);
    verify(donationCreationFormConsumer).accept(form, email);
  }

  @Test
  void logout_showsLogoutConfirmation() {
    String result = controller.showLogoutConfirmation();
    assertEquals("logout-confirm", result);
  }
}
