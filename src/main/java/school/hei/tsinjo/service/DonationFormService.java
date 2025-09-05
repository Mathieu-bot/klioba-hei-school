package school.hei.tsinjo.service;

import org.springframework.stereotype.Service;
import school.hei.tsinjo.endpoint.http.model.DonationCreationForm;
import school.hei.tsinjo.endpoint.http.model.ThEvent;

@Service
public class DonationFormService {

  private final EventService eventService;

  public DonationFormService(EventService eventService) {
    this.eventService = eventService;
  }

  public DonationCreationForm getPrefilledDonationForm(String email) {
    var events = eventService.findAllWithPaymentResolution();
    var thEvents = events.stream().map(ThEvent::new).toList();

    var lastEvent =
        thEvents.stream()
            .filter(e -> e.event().getUser().getEmail().equals(email))
            .reduce((first, second) -> second) // dernier event de l'utilisateur
            .orElse(null);

    if (lastEvent != null) {
      return new DonationCreationForm(
          lastEvent.event().getUser().getFirstName(),
          lastEvent.event().getUser().getLastName(),
          "" // pspId toujours vide
          );
    } else {
      return new DonationCreationForm("", "", "");
    }
  }
}
