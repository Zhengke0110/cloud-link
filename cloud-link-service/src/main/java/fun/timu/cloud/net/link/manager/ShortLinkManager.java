package fun.timu.cloud.net.link.manager;

import fun.timu.cloud.net.link.model.DO.ShortLink;

/**
 * 短链接管理接口，提供短链接的添加、查询和删除功能
 */
public interface ShortLinkManager {

    /**
     * 添加短链接信息
     *
     * @param shortLinkDO 短链接对象，包含短链接的相关信息
     * @return 添加成功的短链接数量，通常为1，失败则为0
     */
    int addShortLink(ShortLink shortLinkDO);

    /**
     * 通过短链接码查询短链接信息
     *
     * @param shortLinkCode 短链接码，用于标识和查找短链接
     * @return 对应短链接码的短链接对象，如果不存在则返回null
     */
    ShortLink findByShortLinCode(String shortLinkCode);

    /**
     * 删除指定的短链接
     *
     * @param shortLinkCode 短链接码，用于标识要删除的短链接
     * @param accountNo 执行删除操作的账户编号，用于权限验证
     * @return 删除成功的短链接数量，通常为1，失败则为0
     */
    int del(String shortLinkCode,Long accountNo);
}
