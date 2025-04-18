package fun.timu.cloud.net.shop.manager;

import fun.timu.cloud.net.shop.model.DO.Product;

import java.util.List;

public interface ProductManager {
    /**
     * 查询商品列表
     *
     * @return
     */
    List<Product> list();

    /**
     * 查询商品详情
     *
     * @param productId
     * @return
     */
    Product findDetailById(long productId);
}
