package school.hei.tsinjo.repository.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import school.hei.tsinjo.model.Help;
import school.hei.tsinjo.repository.jpa.model.JEvent;

@Component
@AllArgsConstructor
public class JHelpMapper {

    private final JPaymentMapper jPaymentMapper;
    private final JUserMapper jUserMapper;

    public Help toDomain(JEvent jEvent) {
        return new Help(
                jEvent.getId(),
                jPaymentMapper.toDomain(jEvent.getPayment()),
                jUserMapper.toDomain(jEvent.getUser()),
                jEvent.getCreationInstant(),
                jEvent.getComment()
        );
    }

    public JEvent toEntity(Help help) {
        return new JEvent(
                help.getId(),
                jUserMapper.toEntity(help.getUser()),
                jPaymentMapper.toEntity(help.getPayment()),
                help.getCreationInstant(),
                help.getComment()
        );
    }
}