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
import school.hei.klioba.endpoint.http.model.MembershipFeeCreationForm;
import school.hei.klioba.model.Club;
import school.hei.klioba.model.MembershipFee;
import school.hei.klioba.model.Payment;
import school.hei.klioba.model.PaymentStatus;
import school.hei.klioba.model.User;
import school.hei.klioba.model.psp.PspType;
import school.hei.klioba.service.ClubService;
import school.hei.klioba.service.EventService;
import school.hei.klioba.service.MembershipFeeCreationFormConsumer;
import school.hei.klioba.service.MembershipFormService;

class KliobaControllerTest {

  private KliobaController controller;
  private EventService eventService;
  private ClubService clubService;
  private MembershipFeeCreationFormConsumer membershipFeeCreationFormConsumer;
  private MembershipFormService membershipFormService;
  private Model model;
  private Authentication authentication;

  @BeforeEach
  void setUp() {
    eventService = mock(EventService.class);
    clubService = mock(ClubService.class);
    membershipFeeCreationFormConsumer = mock(MembershipFeeCreationFormConsumer.class);
    membershipFormService = mock(MembershipFormService.class);
    model = mock(Model.class);
    authentication = mock(Authentication.class);

    controller =
        new KliobaController(
            eventService, clubService, membershipFeeCreationFormConsumer, membershipFormService);
  }

  @Test
  void home_addsClubsToModel() {
    var clubs = List.of(new Club("c1", "Club 1"), new Club("c2", "Club 2"));
    when(clubService.findAll()).thenReturn(clubs);

    String result = controller.home(model);

    assertEquals("home", result);
    verify(model).addAttribute("clubs", clubs);
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
    MembershipFee donation = new MembershipFee("d1", payment, user, null, Instant.now());

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
            new MembershipFee("d1", payment, user, null, Instant.now()),
            new MembershipFee("d2", payment, user, null, Instant.now()),
            new MembershipFee("d3", payment, user, null, Instant.now()),
            new MembershipFee("d4", payment, user, null, Instant.now()),
            new MembershipFee("d5", payment, user, null, Instant.now()));

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
  void showMembershipFeeForm_returnsPrefilledForm() {
    var email = "test@example.com";
    var club = new Club("c1", "Club 1");
    when(clubService.findById("c1")).thenReturn(club);
    var prefilledForm = new MembershipFeeCreationForm("John", "Doe", "");
    when(membershipFormService.getPrefilledDonationForm(email)).thenReturn(prefilledForm);

    Map<String, Object> attributes = new HashMap<>();
    attributes.put("email", email);
    DefaultOAuth2User oAuth2User = mock(DefaultOAuth2User.class);
    when(oAuth2User.getAttributes()).thenReturn(attributes);
    when(authentication.getPrincipal()).thenReturn(oAuth2User);

    var result = controller.showMembershipFeeForm("c1", authentication, model);

    assertEquals("membership-fee", result);
    verify(model).addAttribute("club", club);
    verify(model).addAttribute("membershipForm", prefilledForm);
  }

  @Test
  void membershipFee_post_processesFormAndRedirects() {
    var email = "test@example.com";
    var clubId = "c1";
    Map<String, Object> attributes = new HashMap<>();
    attributes.put("email", email);

    DefaultOAuth2User oAuth2User = mock(DefaultOAuth2User.class);
    when(oAuth2User.getAttributes()).thenReturn(attributes);
    when(authentication.getPrincipal()).thenReturn(oAuth2User);

    var form = new MembershipFeeCreationForm("John", "Doe", "PSP123");

    var result = controller.membershipFee(clubId, authentication, form);

    assertEquals("redirect:/history", result);
    verify(membershipFeeCreationFormConsumer).accept(form, email, clubId);
  }

  @Test
  void logout_showsLogoutConfirmation() {
    String result = controller.showLogoutConfirmation();
    assertEquals("logout-confirm", result);
  }
}
