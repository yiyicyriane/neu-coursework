/*
用户在注册时系统会自动生成一个唯一的id,用于后期系统和数据管理；
用户在注册时还需自己定义一个userid, 类似于wechat id,系统要检测保证唯一性，一旦确定无法再次更改。
除此之外还需要name,phone number, password, profile picture,但是这些注册之后都可以更改
 */
//删除id, phone number,
package com.chat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor              // a constructor with all args
public class User {
    @NonNull private final String userId; //user creates their userid when sign up, unique and cannot change after being created
    @NonNull private String name;
    @NonNull private String password;
    @NonNull private String profilePicture;
}
