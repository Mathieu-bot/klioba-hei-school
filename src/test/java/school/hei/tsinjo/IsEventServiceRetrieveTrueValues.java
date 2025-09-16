package school.hei.tsinjo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import school.hei.tsinjo.service.EventService;

@SpringBootTest
public class IsEventServiceRetrieveTrueValues {

  @Autowired private EventService eventService;

  @Test
  public void is_everything_has_been_retrieved_correctly() {
    System.out.println("=== Début du test EventService ===");

    var events = eventService.findAllWithPaymentResolution();

    System.out.println("Nombre d'événements trouvés: " + events.size());
    System.out.println("Events retrieved: " + events.toString());

    // Afficher plus de détails si vous voulez
    events.forEach(
        event -> {
          System.out.println("ID de transaction: " + event.getPayment().pspId());
          System.out.println("Event date de don: " + event.getCreationInstant());
          System.out.println("Payment Status: " + event.getPayment().status());
          System.out.println("Event date de transaction: " + event.getPayment().creationInstant());
          System.out.println("---");
        });

    System.out.println("=== Fin du test EventService ===");
  }
}
