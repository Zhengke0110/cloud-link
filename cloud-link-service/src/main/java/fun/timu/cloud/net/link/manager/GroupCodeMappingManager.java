package fun.timu.cloud.net.link.manager;

import fun.timu.cloud.net.common.enums.ShortLinkStateEnum;
import fun.timu.cloud.net.link.model.DO.GroupCodeMapping;

import java.util.Map;

/**
 * GroupCodeMapping管理接口，用于处理GroupCodeMapping的相关操作
 */
public interface GroupCodeMappingManager {

    /**
     * 根据映射ID、账户编号和组ID查找GroupCodeMapping详情
     *
     * @param mappingId 映射ID
     * @param accountNo 账户编号
     * @param groupId   组ID
     * @return 返回GroupCodeMapping对象，如果找不到则返回null
     */
    GroupCodeMapping findByGroupIdAndMappingId(Long mappingId, Long accountNo, Long groupId);

    /**
     * 新增GroupCodeMapping记录
     *
     * @param groupCodeMappingDO GroupCodeMapping数据对象
     * @return 返回新增记录的影响行数
     */
    int add(GroupCodeMapping groupCodeMappingDO);

    /**
     * 根据短链码删除GroupCodeMapping记录
     *
     * @param groupCodeMappingDO GroupCodeMapping数据对象，包含短链码信息
     * @return 返回删除记录的影响行数
     */
    int del(GroupCodeMapping groupCodeMappingDO);

    /**
     * 分页查找GroupCodeMapping记录
     *
     * @param page      当前页码
     * @param size      每页记录数
     * @param accountNo 账户编号
     * @param groupId   组ID
     * @return 返回包含分页结果的Map对象
     */
    Map<String, Object> pageShortLinkByGroupId(Integer page, Integer size, Long accountNo, Long groupId);

    /**
     * 更新GroupCodeMapping的状态
     *
     * @param accountNo          账户编号
     * @param groupId            组ID
     * @param shortLinkCode      短链码
     * @param shortLinkStateEnum 短链码状态枚举
     * @return 返回更新记录的影响行数
     */
    int updateGroupCodeMappingState(Long accountNo, Long groupId, String shortLinkCode, ShortLinkStateEnum shortLinkStateEnum);

    /**
     * 根据短链码和组ID查找GroupCodeMapping记录是否存在
     *
     * @param shortLinkCode 短链码
     * @param groupId       组ID
     * @param accountNo     账户编号
     * @return 返回GroupCodeMapping对象，如果找不到则返回null
     */
    GroupCodeMapping findByCodeAndGroupId(String shortLinkCode, Long groupId, Long accountNo);

    /**
     * 更新GroupCodeMapping记录
     *
     * @param groupCodeMappingDO GroupCodeMapping数据对象
     * @return 返回更新记录的影响行数
     */
    int update(GroupCodeMapping groupCodeMappingDO);
}
