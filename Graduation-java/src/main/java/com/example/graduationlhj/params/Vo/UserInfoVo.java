package com.example.graduationlhj.params.Vo;



import com.example.graduationlhj.entity.Role;
import lombok.Data;


@Data
public class UserInfoVo {
    private String nickName;

    private String avatar;

    private Role role;

    private String sex;

    private Integer age;

    private String phoneNumber;

    private String email;

    private String address;
}
