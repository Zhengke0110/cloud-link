package fun.timu.cloud.net.shop.manager;

import fun.timu.cloud.net.shop.model.DO.Product;

import java.util.List;

public interface ProductManager {
    List<Product> list();

    Product findDetailById(long productId);
}
