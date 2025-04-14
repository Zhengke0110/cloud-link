package fun.timu.cloud.net.common.util;

import fun.timu.cloud.net.common.model.LoginUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class JWTUtil {
    private static Logger logger = LoggerFactory.getLogger(JWTUtil.class);


    /**
     * 主题
     */
    private static final String SUBJECT = "cloud";

    /**
     * 加密密钥
     */
    private static final String SECRET = "cloud.net168";

    /**
     * 令牌前缀
     */
    private static final String TOKNE_PREFIX = "cloud-link";


    /**
     * token过期时间，7天
     */
    private static final long EXPIRED = 1000 * 60 * 60 * 24 * 7;


    /**
     * 生成JSON Web Token (JWT)
     *
     * @param loginUser 登录用户对象，包含用户相关信息
     * @return 生成的JWT字符串
     *
     * 此方法负责根据登录用户信息创建一个JWT它首先检查登录用户对象是否为空，
     * 如果为空则抛出空指针异常然后，它使用Jwts.builder()来构建JWT，在这个过程中，
     * 它将用户信息作为payload的一部分，并设置令牌的过期时间最后，它使用指定的算法
     * 和密钥对令牌进行签名并返回生成的JWT字符串
     */
    public static String geneJsonWebTokne(LoginUser loginUser) {

        // 检查登录用户对象是否为空
        if (loginUser == null) {
            throw new NullPointerException("对象为空");
        }

        // 构建JWT，设置主题、用户信息、签发时间、过期时间和签名算法
        String token = Jwts.builder().setSubject(SUBJECT)
                // 配置payload
                .claim("head_img", loginUser.getHeadImg()).claim("account_no", loginUser.getAccountNo()).claim("username", loginUser.getUsername()).claim("mail", loginUser.getMail()).claim("phone", loginUser.getPhone()).claim("auth", loginUser.getAuth())
                .setIssuedAt(new Date()).setExpiration(new Date(CommonUtil.getCurrentTimestamp() + EXPIRED)).signWith(SignatureAlgorithm.HS256, SECRET).compact();

        // 在令牌前添加前缀
        token = TOKNE_PREFIX + token;
        return token;
    }


    /**
     * 解密jwt
     *
     * @param token 待解密的jwt字符串
     * @return 解密成功返回Claims对象，包含jwt中的声明，解密失败返回null
     */
    public static Claims checkJWT(String token) {

        try {
            // 使用SECRET作为签名密钥，解析传入的token并获取jwt的body部分
            final Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKNE_PREFIX, "")).getBody();
            return claims;
        } catch (Exception e) {

            // 记录jwt解密失败的日志
            logger.error("jwt 解密失败");
            return null;
        }

    }
}