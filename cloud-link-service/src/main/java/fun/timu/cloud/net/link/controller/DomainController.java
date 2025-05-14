package fun.timu.cloud.net.link.controller;

import fun.timu.cloud.net.common.util.JsonData;
import fun.timu.cloud.net.link.model.VO.DomainVO;
import fun.timu.cloud.net.link.service.DomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * 域名管理控制器接口
 */
@RestController
@RequestMapping("/api/domain/v1")
public class DomainController {
    private final DomainService domainService;

    public DomainController(DomainService domainService) {
        this.domainService = domainService;
    }

    /**
     * 列举全部可用域名列表
     * <p>
     * 该方法通过GET请求获取系统中全部可用的域名列表，并以JSON格式返回
     * 主要用于前端展示或系统间数据交互
     *
     * @return 包含全部域名信息的列表，通过JsonData对象封装
     */
    @GetMapping("list")
    public JsonData listAll() {

        // 调用domainService的listAll方法获取全部域名列表
        List<DomainVO> list = domainService.listAll();

        // 使用JsonData封装域名列表并返回成功响应
        return JsonData.buildSuccess(list);
    }

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * 处理测试请求，主要用于验证短链码和账户号的关联
     * 此方法通过执行Lua脚本在Redis中检查和设置短链码与账户号的映射关系
     *
     * @param code      短链码，用于识别特定的资源或信息
     * @param accountNo 账户号，代表用户账户的唯一标识
     * @return 返回一个包含执行结果的JsonData对象，表示操作的状态
     */
    @GetMapping("test")
    public JsonData test(@RequestParam(name = "code") String code, @RequestParam(name = "accountNo") Long accountNo) {

        // Lua脚本用于在Redis中执行复杂的逻辑
        // key1是短链码，ARGV[1]是accountNo,ARGV[2]是过期时间
        String script = "if redis.call('EXISTS',KEYS[1])==0 then redis.call('set',KEYS[1],ARGV[1]); redis.call('expire',KEYS[1],ARGV[2]); return 1;" + " elseif redis.call('get',KEYS[1]) == ARGV[1] then return 2;" + " else return 0; end;";

        // 执行Lua脚本，检查短链码是否存在，如果不存在则设置短链码和账户号，并设置过期时间
        // 如果短链码存在且关联的账户号相同，则返回2；如果关联的账户号不同，则返回0
        Long result = redisTemplate.execute(new DefaultRedisScript<>(script, Long.class), Arrays.asList(code), accountNo, 100);

        // 返回操作结果
        return JsonData.buildSuccess(result);
    }

}
