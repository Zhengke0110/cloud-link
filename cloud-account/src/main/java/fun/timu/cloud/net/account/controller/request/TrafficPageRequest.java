package fun.timu.cloud.net.account.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrafficPageRequest {

    private int page;

    private int size;
}
