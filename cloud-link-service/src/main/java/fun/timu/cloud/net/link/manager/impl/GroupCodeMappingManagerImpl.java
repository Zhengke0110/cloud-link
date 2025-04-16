package fun.timu.cloud.net.link.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fun.timu.cloud.net.common.enums.ShortLinkStateEnum;
import fun.timu.cloud.net.link.manager.GroupCodeMappingManager;
import fun.timu.cloud.net.link.mapper.GroupCodeMappingMapper;
import fun.timu.cloud.net.link.model.DO.GroupCodeMapping;
import fun.timu.cloud.net.link.model.VO.GroupCodeMappingVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GroupCodeMappingManagerImpl implements GroupCodeMappingManager {
    private static Logger logger = LoggerFactory.getLogger(GroupCodeMappingManager.class);
    private final GroupCodeMappingMapper groupCodeMappingMapper;

    public GroupCodeMappingManagerImpl(GroupCodeMappingMapper groupCodeMappingMapper) {
        this.groupCodeMappingMapper = groupCodeMappingMapper;
    }

    /**
     * 根据组ID和映射ID查找群组代码映射
     *
     * @param mappingId 映射ID，用于标识特定的映射关系
     * @param accountNo 账户编号，用于标识特定的账户
     * @param groupId   组ID，用于标识特定的组
     * @return 返回找到的GroupCodeMapping对象，如果没有找到则返回null
     */
    @Override
    public GroupCodeMapping findByGroupIdAndMappingId(Long mappingId, Long accountNo, Long groupId) {
        // 使用MyBatis-Plus的QueryWrapper构建查询条件
        GroupCodeMapping groupCodeMappingDO = groupCodeMappingMapper.selectOne(new QueryWrapper<GroupCodeMapping>()
                .eq("id", mappingId).eq("account_no", accountNo)
                .eq("group_id", groupId));

        return groupCodeMappingDO;
    }

    /**
     * 插入新的群组代码映射
     *
     * @param groupCodeMappingDO 群组代码映射对象，包含群组代码映射的相关信息
     * @return 返回插入操作的影响行数，成功插入返回1，否则返回0
     */
    @Override
    public int add(GroupCodeMapping groupCodeMappingDO) {
        return groupCodeMappingMapper.insert(groupCodeMappingDO);
    }

    /**
     * 删除群组代码映射
     * 通过设置del标志为1来软删除记录
     *
     * @param shortLinkCode 短链接代码，用于定位要删除的记录
     * @param accountNo     账户编号，用于定位要删除的记录
     * @param groupId       群组ID，用于定位要删除的记录
     * @return 返回受影响的行数，表示删除操作影响的记录数
     */
    @Override
    public int del(String shortLinkCode, Long accountNo, Long groupId) {
        // 更新群组代码映射表中匹配指定条件的记录，将del字段设置为1，表示已删除
        int rows = groupCodeMappingMapper.update(null, new UpdateWrapper<GroupCodeMapping>()
                .eq("code", shortLinkCode).eq("account_no", accountNo)
                .eq("group_id", groupId).set("del", 1));

        return rows;
    }

    /**
     * 根据组ID分页查询短链接信息
     *
     * @param page      页码
     * @param size      每页记录数
     * @param accountNo 账号编号
     * @param groupId   组ID
     * @return 返回包含分页信息的Map对象，包括总记录数、总页数和当前页数据
     */
    @Override
    public Map<String, Object> pageShortLinkByGroupId(Integer page, Integer size, Long accountNo, Long groupId) {

        // 创建Page对象用于分页查询
        Page<GroupCodeMapping> pageInfo = new Page<>(page, size);

        // 执行分页查询，筛选出符合账号编号和组ID条件的记录
        Page<GroupCodeMapping> groupCodeMappingDOPage = groupCodeMappingMapper.selectPage(pageInfo, new QueryWrapper<GroupCodeMapping>().eq("account_no", accountNo)
                .eq("group_id", groupId));

        // 创建Map对象存储分页查询结果
        Map<String, Object> pageMap = new HashMap<>(3);

        // 将总记录数放入Map
        pageMap.put("total_record", groupCodeMappingDOPage.getTotal());
        // 将总页数放入Map
        pageMap.put("total_page", groupCodeMappingDOPage.getPages());
        // 将当前页的数据转换并放入Map
        pageMap.put("current_data", groupCodeMappingDOPage.getRecords()
                .stream().map(obj -> beanProcess(obj)).collect(Collectors.toList()));

        // 返回包含分页信息的Map对象
        return pageMap;
    }

    /**
     * 更新群组代码映射的状态
     *
     * @param accountNo          用户账号编号，用于定位特定用户的群组代码映射
     * @param groupId            群组ID，用于指定哪个群组的代码映射需要更新
     * @param shortLinkCode      短链接代码，用于识别特定的群组代码映射记录
     * @param shortLinkStateEnum 短链接状态枚举，表示新的状态值
     * @return 返回受影响的行数，用于确认更新操作是否成功
     */
    @Override
    public int updateGroupCodeMappingState(Long accountNo, Long groupId, String shortLinkCode, ShortLinkStateEnum shortLinkStateEnum) {
        // 执行更新操作，使用MyBatis-Plus的UpdateWrapper构建更新条件
        int rows = groupCodeMappingMapper.update(null, new UpdateWrapper<GroupCodeMapping>()
                .eq("code", shortLinkCode).eq("account_no", accountNo)
                .eq("group_id", groupId).set("state", shortLinkStateEnum.name()));

        return rows;
    }

    /**
     * 根据代码、组ID和账户号查找组代码映射
     *
     * @param shortLinkCode 短链接代码，用于标识特定的链接
     * @param groupId       组ID，用于标识特定的组
     * @param accountNo     账户号，用于标识特定的用户账户
     * @return 返回找到的GroupCodeMapping对象，如果未找到则返回null
     * <p>
     * 此方法通过组合查询条件来定位特定的组代码映射，旨在确保返回的结果是唯一的
     * 它主要用于处理与组相关的链接操作，确保链接与正确的组和账户相关联
     */
    @Override
    public GroupCodeMapping findByCodeAndGroupId(String shortLinkCode, Long groupId, Long accountNo) {
        // 使用MyBatis-Plus的QueryWrapper构建查询条件
        GroupCodeMapping groupCodeMappingDO = groupCodeMappingMapper.selectOne(new QueryWrapper<GroupCodeMapping>()
                .eq("code", shortLinkCode)
                .eq("account_no", accountNo)
                .eq("group_id", groupId));

        // 返回查询结果
        return groupCodeMappingDO;
    }


    /**
     * 将GroupCodeMapping实体类转换为VO类
     * 该方法主要用于将数据库实体类转换为视图对象（VO），以便在不同层次之间传递数据
     * 使用Spring框架提供的BeanUtils.copyProperties方法来复制属性，减少手动设置属性的繁琐
     *
     * @param groupCodeMappingDO 数据库实体类GroupCodeMapping的实例，包含从数据库获取的数据
     * @return 返回一个GroupCodeMappingVO实例，其中填充了从groupCodeMappingDO复制的属性
     */
    private GroupCodeMappingVO beanProcess(GroupCodeMapping groupCodeMappingDO) {
        // 创建一个新的GroupCodeMappingVO对象
        GroupCodeMappingVO groupCodeMappingVO = new GroupCodeMappingVO();
        // 使用Spring的BeanUtils工具类将groupCodeMappingDO中的属性复制到groupCodeMappingVO中
        BeanUtils.copyProperties(groupCodeMappingDO, groupCodeMappingVO);

        // 返回填充好属性的GroupCodeMappingVO对象
        return groupCodeMappingVO;
    }
}
