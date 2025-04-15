package fun.timu.cloud.net.link.controller;

import fun.timu.cloud.net.common.util.JsonData;
import fun.timu.cloud.net.link.model.VO.DomainVO;
import fun.timu.cloud.net.link.service.DomainService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/domain/v1")
public class DomainController {
    private final DomainService domainService;

    public DomainController(DomainService domainService) {
        this.domainService = domainService;
    }

    /**
     * 列举全部可用域名列表
     * @return
     */
    @GetMapping("list")
    public JsonData listAll(){

        List<DomainVO> list = domainService.listAll();

        return JsonData.buildSuccess(list);

    }
}
