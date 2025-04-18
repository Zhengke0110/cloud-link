package fun.timu.cloud.net.shop.manager.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fun.timu.cloud.net.shop.manager.ProductOrderManager;
import fun.timu.cloud.net.shop.mapper.ProductOrderMapper;
import fun.timu.cloud.net.shop.model.DO.ProductOrder;
import fun.timu.cloud.net.shop.model.VO.ProductOrderVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ProductOrderManagerImpl implements ProductOrderManager {
    private final ProductOrderMapper productOrderMapper;

    public ProductOrderManagerImpl(ProductOrderMapper productOrderMapper) {
        this.productOrderMapper = productOrderMapper;
    }

    /**
     * 添加产品订单
     * <p>
     * 此方法通过调用productOrderMapper的insert方法来插入一个新的产品订单到数据库
     * 它接收一个ProductOrder类型的参数，代表要添加的产品订单信息
     * 返回值是int类型，表示插入操作影响的行数
     *
     * @param productOrderDO 要插入的产品订单对象
     * @return 插入操作影响的行数
     */
    @Override
    public int add(ProductOrder productOrderDO) {
        return productOrderMapper.insert(productOrderDO);
    }

    /**
     * 根据外部交易号和账户号查找产品订单
     *
     * @param outTradeNo 外部交易号，用于标识特定的交易
     * @param accountNo  账户号，用于关联特定的用户账户
     * @return 返回找到的产品订单对象，如果未找到则返回null
     */
    @Override
    public ProductOrder findByOutTradeNoAndAccountNo(String outTradeNo, Long accountNo) {

        // 查询未删除的产品订单，使用外部交易号和账户号作为查询条件
        ProductOrder productOrderDO = productOrderMapper.selectOne(new QueryWrapper<ProductOrder>().eq("out_trade_no", outTradeNo).eq("account_no", accountNo).eq("del", 0));

        return productOrderDO;
    }

    /**
     * 更新订单支付状态
     *
     * @param outTradeNo 订单外部交易号，用于标识特定的交易
     * @param accountNo  用户账户编号，用于确定是哪个用户的订单
     * @param newState   新的订单状态，用于更新订单的支付状态
     * @param oldState   旧的订单状态，用于验证订单的当前状态是否符合更新要求
     * @return 返回受影响的行数，用于判断更新操作是否成功
     */
    @Override
    public int updateOrderPayState(String outTradeNo, Long accountNo, String newState, String oldState) {
        // 更新订单状态，条件是订单的外部交易号、用户账户编号和当前状态匹配
        // 如果匹配成功，则将订单状态更新为新的状态
        int rows = productOrderMapper.update(null, new UpdateWrapper<ProductOrder>().eq("out_trade_no", outTradeNo).eq("account_no", accountNo).eq("state", oldState).set("state", newState));

        // 返回受影响的行数，作为更新操作结果的反馈
        return rows;
    }


    /**
     * 根据账户编号和订单状态分页查询订单信息
     *
     * @param page      当前页码
     * @param size      每页记录数
     * @param accountNo 账户编号
     * @param state     订单状态
     * @return 返回包含订单信息的Map对象，包括总记录数、总页数和当前页数据
     */
    @Override
    public Map<String, Object> page(int page, int size, Long accountNo, String state) {

        // 初始化分页对象
        Page<ProductOrder> pageInfo = new Page<>(page, size);

        IPage<ProductOrder> orderDOIPage;

        // 根据订单状态查询订单
        if (!StringUtils.isBlank(state)) {
            // 如果状态不为空，根据账户编号和订单状态查询
            orderDOIPage = productOrderMapper.selectPage(pageInfo, new QueryWrapper<ProductOrder>().eq("account_no", accountNo).eq("state", state).eq("del", 0));
        } else {
            // 如果状态为空，仅根据账户编号查询
            orderDOIPage = productOrderMapper.selectPage(pageInfo, new QueryWrapper<ProductOrder>().eq("account_no", accountNo).eq("del", 0));
        }

        // 获取查询结果列表
        List<ProductOrder> orderDOIPageRecords = orderDOIPage.getRecords();

        // 将查询结果转换为视图对象列表
        List<ProductOrderVO> productOrderVOList = orderDOIPageRecords.stream().map(obj -> {
            ProductOrderVO productOrderVO = new ProductOrderVO();
            BeanUtils.copyProperties(obj, productOrderVO);
            return productOrderVO;
        }).collect(Collectors.toList());

        // 创建存储分页信息的Map对象
        Map<String, Object> pageMap = new HashMap<>(3);
        pageMap.put("total_record", orderDOIPage.getTotal());
        pageMap.put("total_page", orderDOIPage.getPages());
        pageMap.put("current_data", productOrderVOList);

        // 返回分页信息Map对象
        return pageMap;
    }

    /**
     * 删除指定的订单
     * 通过更新订单的删除状态来实现软删除
     *
     * @param productOrderId 订单ID，用于定位要删除的订单
     * @param accountNo      账户编号，用于确保操作者有权访问该订单
     * @return 返回受影响的行数，用于确认删除操作是否成功
     */
    @Override
    public int del(Long productOrderId, Long accountNo) {
        // 更新订单信息，设置订单的删除状态为1，表示已删除
        int rows = productOrderMapper.update(null, new UpdateWrapper<ProductOrder>().eq("id", productOrderId).eq("account_no", accountNo).set("del", 1));

        // 返回受影响的行数
        return rows;
    }

}
