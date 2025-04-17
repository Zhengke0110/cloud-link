package fun.timu.cloud.net.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import fun.timu.cloud.net.shop.mapper.ProductMapper;
import fun.timu.cloud.net.shop.model.DO.Product;
import fun.timu.cloud.net.shop.service.ProductService;
import org.springframework.stereotype.Service;

/**
 * @author zhengke
 * @description 针对表【product】的数据库操作Service实现
 * @createDate 2025-04-17 20:40:43
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product>
        implements ProductService {

}




