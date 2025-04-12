package fun.timu.cloud.net.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @TableName account
 */
@TableName(value = "account")
@Data
public class Account implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     *
     */
    private Long accountNo;

    /**
     * 头像
     */
    private String headImg;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 密码
     */
    private String pwd;

    /**
     * 盐，用于个人敏感信息处理
     */
    private String secret;

    /**
     * 邮箱
     */
    private String mail;

    /**
     * 用户名
     */
    private String username;

    /**
     * 认证级别，DEFAULT，REALNAME，ENTERPRISE，访问次数不一样
     */
    private String auth;

    /**
     *
     */
    private Date gmtCreate;

    /**
     *
     */
    private Date gmtModified;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}