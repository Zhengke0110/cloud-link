package fun.timu.cloud.net.account.manager;

import fun.timu.cloud.net.account.model.DO.TrafficTask;

/**
 * 接口TrafficTaskManager用于管理流量任务的增删查操作
 */
public interface TrafficTaskManager {

    /**
     * 添加一个新的流量任务
     *
     * @param trafficTaskDO 流量任务对象，包含任务的相关信息
     * @return 返回添加任务的影响行数，通常为1表示成功，0表示失败
     */
    int add(TrafficTask trafficTaskDO);

    /**
     * 根据任务ID和账户编号查找流量任务
     *
     * @param id        流量任务的唯一标识符
     * @param accountNo 账户编号，用于限定查询范围
     * @return 返回找到的流量任务对象，如果未找到则返回null
     */
    TrafficTask findByIdAndAccountNo(Long id, Long accountNo);

    /**
     * 根据任务ID和账户编号删除流量任务
     *
     * @param id        流量任务的唯一标识符
     * @param accountNo 账户编号，用于限定删除范围
     * @return 返回删除任务的影响行数，通常为1表示成功，0表示失败
     */
    int deleteByIdAndAccountNo(Long id, Long accountNo);

}
