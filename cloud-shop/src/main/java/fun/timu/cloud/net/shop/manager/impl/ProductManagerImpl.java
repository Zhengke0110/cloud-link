package fun.timu.cloud.net.shop.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import fun.timu.cloud.net.shop.manager.ProductManager;
import fun.timu.cloud.net.shop.mapper.ProductMapper;
import fun.timu.cloud.net.shop.model.DO.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductManagerImpl implements ProductManager {
    private static Logger logger = LoggerFactory.getLogger(ProductManager.class);

    private final ProductMapper productMapper;

    public ProductManagerImpl(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }


    /**
     * 重写父类或接口的list方法
     * 该方法用于获取产品列表
     *
     * @return 返回一个Product对象列表，包含所有产品信息
     */
    @Override
    public List<Product> list() {
        // 使用MyBatis-Plus提供的方法，查询并返回所有产品信息
        return productMapper.selectList(null);
    }

    /**
     * 根据产品ID获取产品详情
     *
     * @param productId 产品的唯一标识符
     * @return 返回Product对象，如果找不到则返回null
     */
    @Override
    public Product findDetailById(long productId) {
        // 使用MyBatis-Plus的查询功能，根据产品ID查询数据库中的产品信息
        return productMapper.selectOne(new QueryWrapper<Product>().eq("id", productId));
    }
}
