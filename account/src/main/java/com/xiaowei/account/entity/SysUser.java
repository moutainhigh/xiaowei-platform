package com.xiaowei.account.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

/**
 * 用户实体
 */
@Data
@Table(name = "sys_user")
@Entity
public class SysUser extends BaseEntity {

    /**
     * 登录名称
     */
    private String loginName;

    /**
     * 真实名称
     */
    private String nickName;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 盐
     */
    private String salt;

    /**
     * 手机号码
     */
    @Column(unique = true)
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户状态  0代表正常  1代表禁用  2代表删除
     */
    private Integer status;

    /**
     * 用户和角色多对多
     */
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="sys_user_role",
        joinColumns={@JoinColumn(name="USER_ID")},
        inverseJoinColumns={@JoinColumn(name="ROLE_ID")})
    @JsonIgnore
    private List<SysRole> roles;

    /**
     * 用户的公司
     */
    @ManyToOne(targetEntity = Company.class)
    @JoinColumn(name = "company_id")
    @Fetch(FetchMode.JOIN)
    private Company company;

    /**
     * 用户的部门
     */
    @ManyToOne(targetEntity = Department.class)
    @JoinColumn(name = "department_id")
    @Fetch(FetchMode.JOIN)
    private Department department;

    /**
     * 用户的岗位
     */
    @ManyToOne(targetEntity = Post.class)
    @JoinColumn(name = "post_id")
    @Fetch(FetchMode.JOIN)
    private Post post;


}
