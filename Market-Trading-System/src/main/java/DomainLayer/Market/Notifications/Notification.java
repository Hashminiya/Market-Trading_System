package DomainLayer.Market.Notifications;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Notification {
    @Id
    private Long id;
    private String recipient;
    private String content;
    private Date date;
}
