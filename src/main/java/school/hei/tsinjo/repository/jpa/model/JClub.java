package school.hei.tsinjo.repository.jpa.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "club")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JClub {
    @Id private String id;

    private String name;
}