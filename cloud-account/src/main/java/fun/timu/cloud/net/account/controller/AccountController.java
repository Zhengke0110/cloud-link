package fun.timu.cloud.net.account.controller;

import fun.timu.cloud.net.account.controller.request.AccountLoginRequest;
import fun.timu.cloud.net.account.controller.request.AccountRegisterRequest;
import fun.timu.cloud.net.account.service.AccountService;
import fun.timu.cloud.net.account.service.FileService;
import fun.timu.cloud.net.common.enums.BizCodeEnum;
import fun.timu.cloud.net.common.util.JsonData;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户控制器相关接口
 */
@RestController
@RequestMapping("/api/account/v1")
public class AccountController {

    private final FileService fileService;
    private final AccountService accountService;

    public AccountController(FileService fileService, AccountService accountService) {
        this.fileService = fileService;
        this.accountService = accountService;
    }

    /**
     * 文件上传接口，最大默认1M
     * 该方法负责处理用户图像的上传请求，包括对文件的格式、扩展名等进行判断
     * 使用@PostMapping注解限定HTTP的POST请求，路径为"upload"
     *
     * @param file 用户上传的文件，通过@RequestPart注解指定文件参数名为"file"
     * @return 返回一个JsonData对象，包含上传结果信息
     */
    @PostMapping("upload")
    public JsonData uploadUserImg(@RequestPart("file") MultipartFile file) {

        // 调用文件服务的上传方法，尝试上传用户图像
        String result = fileService.uploadUserImg(file);

        // 根据上传结果构建并返回相应的JsonData对象
        // 如果上传成功，调用buildSuccess方法构建成功响应，传入文件路径或其它结果信息
        // 如果上传失败，调用buildResult方法构建失败响应，传入相应的错误码
        return result != null ? JsonData.buildSuccess(result) : JsonData.buildResult(BizCodeEnum.FILE_UPLOAD_USER_IMG_FAIL);
    }


    /**
     * 用户注册
     * <p>
     * 此方法处理用户注册请求，接收注册信息并返回注册结果
     * 使用PostMapping注解限定HTTP请求方法为POST，路径为"register"
     * 使用RequestBody注解将HTTP请求的JSON格式请求体转换为AccountRegisterRequest对象
     *
     * @param registerRequest 包含用户注册信息的请求对象
     * @return 返回表示注册结果的JsonData对象
     */
    @PostMapping("register")
    public JsonData register(@RequestBody AccountRegisterRequest registerRequest) {
        JsonData jsonData = accountService.register(registerRequest);
        return jsonData;
    }


    /**
     * 用户登录
     * <p>
     * 此方法处理用户的登录请求它期望接收一个AccountLoginRequest对象作为参数，
     * 该对象中包含了登录所需的信息，例如用户名和密码这些信息用于验证用户身份
     * 登录成功后，将返回一个包含成功信息的JsonData对象；如果登录失败，则返回一个包含错误信息的JsonData对象
     *
     * @param request 包含用户登录信息的请求对象
     * @return 包含登录结果信息的JsonData对象
     */
    @PostMapping("login")
    public JsonData login(@RequestBody AccountLoginRequest request) {
        JsonData jsonData = accountService.login(request);
        return jsonData;
    }

    /**
     * 获取账户详情信息
     * <p>
     * 通过GET请求调用此方法，以获取账户的详细信息该方法不接受任何参数，
     * 并返回一个包含账户详情的JsonData对象主要用于前端展示账户详细信息的页面
     *
     * @return 返回包含账户详情信息的JsonData对象
     */
    @GetMapping("detail")
    public JsonData detail() {
        // 调用AccountService的detail方法获取账户详情信息
        JsonData jsonData = accountService.detail();
        // 返回账户详情信息
        return jsonData;
    }


}
