package fun.timu.cloud.net.link.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import fun.timu.cloud.net.link.manager.ShortLinkManager;
import fun.timu.cloud.net.link.mapper.ShortLinkMapper;
import fun.timu.cloud.net.link.model.DO.ShortLink;
import fun.timu.cloud.net.link.service.LinkGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShortLinkManagerImpl implements ShortLinkManager {
    private static Logger logger = LoggerFactory.getLogger(ShortLinkManager.class);

    private final ShortLinkMapper shortLinkMapper;

    public ShortLinkManagerImpl(ShortLinkMapper shortLinkMapper) {
        this.shortLinkMapper = shortLinkMapper;
    }

    /**
     * 添加短链接信息到数据库
     *
     * 此方法通过调用ShortLinkMapper接口的insert方法，将ShortLink对象中的信息插入到数据库中
     * 由于此方法简单明了，且只涉及一个操作，因此在此不做过多的代码逻辑解释
     *
     * @param shortLinkDO ShortLink数据对象，包含待插入数据库的短链接信息
     * @return 返回插入操作影响的行数，通常为1表示成功，0表示失败
     */
    @Override
    public int addShortLink(ShortLink shortLinkDO) {
        return shortLinkMapper.insert(shortLinkDO);
    }

    /**
     * 根据短链接代码查询短链接对象
     *
     * @param shortLinkCode 短链接代码，用于查询对应的短链接对象
     * @return 返回查询到的短链接对象，如果没有找到，则返回null
     */
    @Override
    public ShortLink findByShortLinCode(String shortLinkCode) {
        // 使用MyBatis-Plus的QueryWrapper构建查询条件，查询code字段等于shortLinkCode的ShortLink对象
        ShortLink shortLinkDO = shortLinkMapper.selectOne(new QueryWrapper<ShortLink>().eq("code", shortLinkCode));
        return shortLinkDO;
    }

    /**
     * 删除短链接
     * 通过短链接代码和账户编号来删除短链接信息
     *
     * @param shortLinkCode 短链接代码，用于定位要删除的短链接
     * @param accountNo 账户编号，用于确认短链接的拥有者
     * @return 返回受影响的行数，表示删除操作影响的记录数
     */
    @Override
    public int del(String shortLinkCode, Long accountNo) {

        // 创建ShortLink对象，并设置删除标志为1，表示该短链接已被删除
        ShortLink shortLinkDO = new ShortLink();
        shortLinkDO.setDel(1);

        // 执行更新操作，将匹配指定短链接代码和账户编号的短链接的删除标志设置为1
        // 这里使用了MyBatis-Plus的QueryWrapper来构建查询条件
        int rows = shortLinkMapper.update(shortLinkDO, new QueryWrapper<ShortLink>().eq("code", shortLinkCode).eq("account_no", accountNo));

        // 返回受影响的行数
        return rows;
    }
}
