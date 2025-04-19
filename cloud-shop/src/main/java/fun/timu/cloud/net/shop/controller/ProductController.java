package fun.timu.cloud.net.shop.controller;

import fun.timu.cloud.net.common.util.JsonData;
import fun.timu.cloud.net.shop.model.VO.ProductVO;
import fun.timu.cloud.net.shop.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/product/v1")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 查看商品列表接口
     *
     * 此方法响应GET请求，提供商品列表数据
     * 它没有输入参数，因为GET请求的性质不需要用户发送数据到服务器
     * 返回的是一个JsonData对象，其中包含了从productService获取的商品列表
     * 使用List<ProductVO>类型来存储商品数据，ProductVO代表商品的视图对象，用于数据传输
     * 使用JsonData.buildSuccess方法构建返回对象，确保客户端能够接收到成功状态和商品列表数据
     */
    @GetMapping("list")
    public JsonData list() {
        List<ProductVO> list = productService.getList();
        return JsonData.buildSuccess(list);
    }


    /**
     * 查看商品详情
     *
     * 此方法通过商品ID获取并返回商品的详细信息它使用GET请求来访问资源，
     * 并以JSON格式返回商品信息
     *
     * @param productId 商品ID，用于标识特定的商品
     * @return 返回一个JsonData对象，其中包含商品的详细信息
     */
    @GetMapping("detail/{productId}")
    public JsonData detail(@PathVariable("productId") long productId) {

        // 根据商品ID调用服务层方法，获取商品详细信息
        ProductVO productVO = productService.findDetailById(productId);

        // 构建并返回包含商品详细信息的成功响应
        return JsonData.buildSuccess(productVO);
    }


}
