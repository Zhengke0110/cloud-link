package fun.timu.cloud.net.link.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.timu.cloud.net.link.manager.ShortLinkManager;
import fun.timu.cloud.net.link.mapper.ShortLinkMapper;
import fun.timu.cloud.net.link.model.DO.ShortLink;
import fun.timu.cloud.net.link.model.VO.ShortLinkVO;
import fun.timu.cloud.net.link.service.ShortLinkService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @author zhengke
 * @description 针对表【short_link】的数据库操作Service实现
 * @createDate 2025-04-15 11:23:41
 */
@Service
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLink> implements ShortLinkService {

    private final ShortLinkManager shortLinkManager;

    public ShortLinkServiceImpl(ShortLinkManager shortLinkManager) {
        this.shortLinkManager = shortLinkManager;
    }


    /**
     * 解析短链接代码
     *
     * 本方法通过短链接代码查询对应的短链接信息，如果找到则返回该信息，否则返回null
     * 主要用于处理短链接的解析请求，将短链接代码转换为可访问的短链接信息
     *
     * @param shortLinkCode 短链接代码，用于查询对应的短链接信息
     * @return ShortLinkVO 如果找到对应的短链接信息则返回，否则返回null
     */
    @Override
    public ShortLinkVO parseShortLinkCode(String shortLinkCode) {

        // 根据短链接代码查询数据库中的短链接对象
        ShortLink shortLinkDO = shortLinkManager.findByShortLinCode(shortLinkCode);
        // 如果查询结果为空，则直接返回null
        if (shortLinkDO == null) {
            return null;
        }
        // 创建一个短链接视图对象，用于封装查询到的短链接信息
        ShortLinkVO shortLinkVO = new ShortLinkVO();
        // 将短链接对象的属性复制到视图对象中
        BeanUtils.copyProperties(shortLinkDO, shortLinkVO);
        // 返回填充好的短链接视图对象
        return shortLinkVO;
    }
}




