package school.hei.tsinjo.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class UserTest {

  @Test
  void donor_creation_succeeds() {
    var donor = new Donor("d1", "John", "Doe", "john@example.com");

    assertNotNull(donor);
    assertEquals("d1", donor.getId());
    assertEquals("John", donor.getFirstName());
    assertEquals("Doe", donor.getLastName());
    assertEquals("john@example.com", donor.getEmail());
  }

  @Test
  void beneficiary_creation_succeeds() {
    Beneficiary beneficiary = new Beneficiary("b1", "Jane", "Smith", "jane@example.com");

    assertNotNull(beneficiary);
    assertEquals("b1", beneficiary.getId());
    assertEquals("Jane", beneficiary.getFirstName());
    assertEquals("Smith", beneficiary.getLastName());
    assertEquals("jane@example.com", beneficiary.getEmail());
  }

  @Test
  void user_toString_works() {
    var user = new User("u1", "Alice", "Wonder", "alice@example.com");

    var result = user.toString();

    assertNotNull(result);
    assertEquals("User(id=u1, firstName=Alice, lastName=Wonder, email=alice@example.com)", result);
  }

  @Test
  void donor_toString_works() {
    var donor = new Donor("d1", "Bob", "Builder", "bob@example.com");

    var result = donor.toString();

    assertNotNull(result);
    assertEquals("User(id=d1, firstName=Bob, lastName=Builder, email=bob@example.com)", result);
  }
}
