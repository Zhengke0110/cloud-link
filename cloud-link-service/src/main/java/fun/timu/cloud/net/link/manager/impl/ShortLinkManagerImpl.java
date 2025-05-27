package fun.timu.cloud.net.link.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
     * <p>
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
     * 删除短链接信息
     * 通过设置del标志为1来软删除记录
     *
     * @param shortLinkDO 包含短链接信息的数据对象，包括code和accountNo
     * @return 返回受影响的行数，表示删除操作成功更新的记录数
     */
    @Override
    public int del(ShortLink shortLinkDO) {
        // 构建更新条件，根据code和accountNo查找记录，并设置del标志为1，实现软删除
        int rows = shortLinkMapper.update(null, new UpdateWrapper<ShortLink>()
            .eq("code", shortLinkDO.getCode())
            .eq("account_no", shortLinkDO.getAccountNo())
            .set("del", 1));
        return rows;
    }

    /**
     * 更新短链接信息
     *
     * @param shortLinkDO 包含更新信息的短链接数据对象
     * @return 返回受影响的行数，表示更新操作是否成功
     */
    @Override
    public int update(ShortLink shortLinkDO) {
        // 更新短链接信息，只更新标题和域名，其他信息保持不变
        int rows = shortLinkMapper.update(null, new UpdateWrapper<ShortLink>()
                .eq("code", shortLinkDO.getCode()) // 根据短链接代码进行匹配
                .eq("del", 0) // 确保短链接未被删除
                .eq("account_no", shortLinkDO.getAccountNo()) // 根据账户编号进行匹配
                .set("title", shortLinkDO.getTitle()) // 更新标题
                .set("domain", shortLinkDO.getDomain())); // 更新域名


        return rows;
    }

}
