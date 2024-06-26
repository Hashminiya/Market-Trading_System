package DomainLayer.Market.Notification;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {
    private long id;
    private String recipientId;
    private String content;
}
