package fun.timu.cloud.net.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import fun.timu.cloud.net.shop.manager.ProductManager;
import fun.timu.cloud.net.shop.mapper.ProductMapper;
import fun.timu.cloud.net.shop.model.DO.Product;
import fun.timu.cloud.net.shop.model.VO.ProductVO;
import fun.timu.cloud.net.shop.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhengke
 * @description 针对表【product】的数据库操作Service实现
 * @createDate 2025-04-17 20:40:43
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product>
        implements ProductService {

    private final ProductManager productManager;

    public ProductServiceImpl(ProductManager productManager) {
        this.productManager = productManager;
    }

    /**
     * 获取产品列表
     *
     * 本方法通过调用productManager的list方法获取产品列表，然后使用流(Stream)对每个产品对象进行转换处理，
     * 最终返回一个转换后的产品视图对象列表（ProductVO）
     *
     * @return 转换后的产品视图对象列表
     */
    @Override
    public List<ProductVO> getList() {
        // 获取产品列表
        List<Product> list = productManager.list();

        // 使用流对产品列表进行转换，每个产品对象转换为产品视图对象
        List<ProductVO> collect = list.stream().map(obj -> beanProcess(obj)).collect(Collectors.toList());

        // 返回转换后的视图对象列表
        return collect;
    }

    /**
     * 根据产品ID查找产品详细信息
     * 此方法覆盖自上级类或接口，旨在提供获取产品详细信息的功能
     * 主要解决了如何通过产品ID来获取经过处理的产品视图对象（ProductVO）的问题
     *
     * @param productId 产品ID，用于查询特定产品的详细信息
     * @return 返回一个ProductVO对象，包含了产品的详细视图信息
     */
    @Override
    public ProductVO findDetailById(long productId) {

        // 通过产品ID从产品管理器中获取产品详细信息
        Product productDO = productManager.findDetailById(productId);

        // 将获取到的产品信息对象转换为产品视图对象
        ProductVO productVO = beanProcess(productDO);

        // 返回处理后的产品视图对象
        return productVO;
    }


    /**
     * 将Product实体类转换为ProductVO视图类
     *
     * 此方法的主要作用是实现Product实体类到ProductVO视图类的数据转换
     * 它利用Apache Commons BeanUtils库中的copyProperties方法进行属性复制
     * 这种方式可以简化代码，避免手动一一赋值，提高代码的可维护性和可读性
     *
     * @param productDO Product实体类对象，包含从数据库中查询到的product数据
     * @return 返回一个ProductVO对象，包含了与Product实体类相同数据的视图对象
     */
    private ProductVO beanProcess(Product productDO) {
        // 创建一个ProductVO对象，用于存储转换后的数据
        ProductVO productVO = new ProductVO();

        // 使用Apache Commons BeanUtils工具类中的copyProperties方法进行属性复制
        // 从productDO对象复制属性到productVO对象，简化了数据转换过程
        BeanUtils.copyProperties(productDO, productVO);

        // 返回转换后的ProductVO对象
        return productVO;
    }

}




