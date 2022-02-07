package com.example.graduationlhj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author lhj
 * @since 2022-01-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("roleId")
    private Long roleId;

    /**
     * 用户名
     */
    @TableField("userName")
    private String userName;

    /**
     * 昵称
     */
    @TableField("nickName")
    private String nickName;

    /**
     * 密码
     */
    private String password;

    /**
     * 账号状态（0正常 1停用）
     */
    private String status;

    /**
     * 年龄
     */
    private Integer age;
    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 用户性别（0男，1女，2未知）
     */
    private String sex;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 地址
     */
    private String address;

    /**
     * 用户类型（0管理员，1普通用户）
     */
    @TableField("userType")
    private String userType;

    /**
     * 创建人的用户id
     */
    @TableField("createBy")
    private Long createBy;

    /**
     * 创建时间
     */
    @TableField("createTime")
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField("updateBy")
    private Long updateBy;

    /**
     * 更新时间
     */
    @TableField("updateTime")
    private LocalDateTime updateTime;

    /**
     * 删除标志（0代表未删除，1代表已删除）
     */
    @TableField("delFlag")
    private Integer delFlag;


}
