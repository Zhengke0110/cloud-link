package fun.timu.cloud.net.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.timu.cloud.net.shop.model.DO.Product;
import fun.timu.cloud.net.shop.model.VO.ProductVO;

import java.util.List;

/**
 * @author zhengke
 * @description 针对表【product】的数据库操作Service
 * @createDate 2025-04-17 20:40:43
 */
public interface ProductService extends IService<Product> {

    List<ProductVO> getList();

    ProductVO findDetailById(long productId);
}
