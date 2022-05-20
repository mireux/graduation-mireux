package com.example.graduationlhj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.graduationlhj.common.lang.Result;
import com.example.graduationlhj.entity.Role;
import com.example.graduationlhj.entity.User;
import com.example.graduationlhj.mapper.UserMapper;
import com.example.graduationlhj.params.LoginUser;
import com.example.graduationlhj.params.Vo.UserInfoVo;
import com.example.graduationlhj.params.Vo.UserVo;
import com.example.graduationlhj.params.param.LoginParam;
import com.example.graduationlhj.params.param.PasswordParam;
import com.example.graduationlhj.params.param.UserInfoParam;
import com.example.graduationlhj.service.StudyreportService;
import com.example.graduationlhj.service.UserService;
import com.example.graduationlhj.utils.JwtUtil;
import com.example.graduationlhj.utils.QiniuUtils;
import com.example.graduationlhj.utils.RedisCache;
import com.example.graduationlhj.utils.UserUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author lhj
 * @since 2022-01-08
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final AuthenticationManager authenticationManager;

    private final RedisCache redisCache;

    private final UserMapper userMapper;

    private final StudyreportService studyreportService;

    private final QiniuUtils qiniuUtils;


    @Autowired
    public UserServiceImpl(AuthenticationManager authenticationManager, RedisCache redisCache, UserMapper userMapper, StudyreportService studyreportService, QiniuUtils qiniuUtils) {
        this.authenticationManager = authenticationManager;
        this.redisCache = redisCache;
        this.userMapper = userMapper;
        this.studyreportService = studyreportService;
        this.qiniuUtils = qiniuUtils;
    }

    /**
     * 登录操作的实现类
     * 1. 首先验证 验证码是否正确 如果不正确直接返回
     * 2. 调用自定义的登录接口 调用ProviderManager的authenticate方法进行认证，如果认证通过则生成jwt
     * 3. 将token存入redis中，缓存
     */
    @Override
    public Result login(LoginParam loginParam) {
        // 先获取Cookie中携带的uuid 根据uuid获取存在redis中的验证码
        String uuid = loginParam.getUuid();
        String correctCode = redisCache.getCacheObject(uuid).toString();
        // 校验token
        if (StringUtils.isBlank(correctCode)) {
            return new Result(5001, "验证码不存在");
        } else if (!correctCode.equals(loginParam.getCode())) {
            return new Result(5002, "验证码错误");
        }
        // 调用自定义的登录接口 调用ProviderManager的方法进行认证 如果认证通过生成jwt
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginParam.getUsername(), loginParam.getPassword());
        Authentication authenticate = null;
        try {
            authenticate = authenticationManager.authenticate(authenticationToken);
        } catch (LockedException e) {
            return new Result(500, "账号已停用");
        } catch (InternalAuthenticationServiceException e) {
            return new Result(500,"登录失败");
        } catch (BadCredentialsException e) {
            return new Result(500,"用户名或者密码错误");
        }

        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        // 获取用户ID去生成一个token
        String userId = loginUser.getUser().getId().toString();
        // 生成token
        String token = JwtUtil.createJWT(userId);
        //将数据存入redis 根据Id查找和放入
        redisCache.setCacheObject("login:" + userId, loginUser, 1, TimeUnit.DAYS);
        // 返回前端以KEY-VALUE形式
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        return new Result(200, "登陆成功", map);
    }

    /**
     * 获取用户信息
     * 1. 我们在校验token的过滤器中已经将用户信息存放到SecurityContextHolder中，所以在这我们直接获取存放在这里的用户信息就可以了
     * 2.除了基本信息 我们还需要通过role表去查询用户的权限信息 前端需要权限信息去动态的路由
     */
    @Override
    public Result getUserInfo() {
        User user = UserUtils.getUserInfo();
        // 获取用户的权限信息 根据id查找Role 前端动态生成路由所需要的东西
        Role role = userMapper.getRoleByUserId(user.getId());
        // 把需要的内容复制到UserInfoVo之中传给前端
        UserInfoVo userInfoVo = copyToVo(user, role);
        return new Result(200, "", userInfoVo);
    }

    /**
     * 更新用户信息
     * 1. 从token中获取我们的用户Id从而在redis中找到我们原先存放的数据
     * 2. 往数据库中进行更新操作
     * 3. 更新完毕后需要更新我们在redis中的缓存数据
     * TODO redis和数据库之间会有数据不一致的情况
     */
    @Override
    @Transactional
    public Result updateUserInfoById(UserInfoParam userInfoParam, String token) {

        try {
            // 1. 从token中获取到当前操作用户的userId
            Claims claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject();
            // 2. 从redis中获取到我们原先存放的User数据
            LoginUser loginUser = redisCache.getCacheObject("login:" + userId);
            User user = loginUser.getUser();
            // 准备更新操作
            UpdateUser(user, userInfoParam);
            // 3. 进行更新操作
            if (userMapper.updateById(user) < 0) {
                return new Result(4001, "更新失败");
            }
            loginUser.setUser(user);
            // 5. 将新的数据写入redis数据库中
            redisCache.setCacheObject("login:" + userId, loginUser, 1, TimeUnit.DAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Result(200, "更新成功");
    }

    /**
     * 更新用户头像操作
     */
    @Override
    public Result updateAvatarById(String token) {
        String ImageUrl = null;
        try {
            // 通过token拿到用户Id
            Claims claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject();
            // 通过用户Id拿到我们存储在redis中的头像信息
            ImageUrl = redisCache.getCacheObject("Avatar:" + userId);
            LoginUser loginUser = redisCache.getCacheObject("login:" + userId);
            User user = loginUser.getUser();
            user.setAvatar(ImageUrl);
            System.out.println(user);
            if (userMapper.updateById(user) < 0) {
                return new Result(4001, "更新头像失败");
            }
            loginUser.setUser(user);
            redisCache.setCacheObject("login:" + userId, loginUser, 1, TimeUnit.DAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(200, "更新头像成功", ImageUrl);
    }

    /**
     * 取消上传头像
     * 主要是删除redis中的缓存
     * 删除七牛云图库中的头像
     */
    @Override
    public Result CancelUpdateAvatar(String token) {
        String userId = null;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 从redis中获取数据
        String ImageUrl = redisCache.getCacheObject("Avatar:" + userId);
        // 删除图库中的头像
        if (!StringUtils.isBlank(ImageUrl)) {
            qiniuUtils.deleteOnQn(ImageUrl);
            // 删除redis的缓存
            redisCache.deleteObject("Avatar:" + userId);
        }
        return new Result(200, "");
    }

    @Override
    public Result getAllUser() {
        List<UserVo> userVoList = userMapper.getAllUser();
        if (userVoList == null || userVoList.isEmpty()) {
            return new Result(500, "获取学生列表失败");
        }
        return new Result(200, "", userVoList);
    }


    /**
     * 添加用户
     * @param user 用户信息
     * @return
     */
    @Override
    public Result InsertUser(User user) {
        // 获取当前管理员信息
        User userInfo = UserUtils.getUserInfo();
        // 头像默认
        user.setAvatar("http://badwomen.asia/6dd34e05-0e07-49dc-bd14-51dd1f1a0886.gif");
        user.setCreateBy(userInfo.getId());
        user.setUpdateBy(userInfo.getId());
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setStatus("0");
        user.setDelFlag(0);
        user.setRoleId(2L);
        user.setUserType("0");
        // 加密密码
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        if (userMapper.insert(user) < 0) {
            return new Result(500, "添加失败");
        }
        // 添加个人学习报告
        if (!studyreportService.createStudyReport(user.getId())) {
            return new Result(500, "添加失败");
        }
        return new Result(200, "添加成功");
    }

    /**
     * 改变用户状态
     *
     * @param id     用户id
     * @param status 用户状态
     * @return
     */
    @Override
    public Result changeTheStatus(Long id, String status) {
        // 获取当前管理员信息
        User userInfo = UserUtils.getUserInfo();
        // 根据Id寻找到对应的用户
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getId, id);
        User user = userMapper.selectOne(lambdaQueryWrapper);
        user.setStatus(status);
        user.setUpdateBy(userInfo.getId());
        user.setUpdateTime(LocalDateTime.now());
        if (userMapper.updateById(user) < 0) {
            return new Result(500, "修改状态失败");
        }
        return new Result(200, "修改状态成功");
    }

    /**
     * 删除用户
     *
     * @param id 用户id
     * @return
     */
    @Override
    public Result deleteUser(Long id) {
        // 获取当前管理员信息
        User userInfo = UserUtils.getUserInfo();
        // 根据Id寻找到对应的用户
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getId, id);
        User user = userMapper.selectOne(lambdaQueryWrapper);
        user.setDelFlag(1);
        user.setUpdateBy(userInfo.getId());
        user.setUpdateTime(LocalDateTime.now());
        if (userMapper.updateById(user) < 0) {
            return new Result(500, "删除失败");
        }
        return new Result(200, "删除成功");
    }

    /**
     * 修改密码
     *
     * @param passwordParam
     * @return
     */
    @Override
    public Result changePass(PasswordParam passwordParam) {
        /**
         * 1. 先比对旧密码是否相同
         * 2. 比对新密码和重复密码是否相同（前端虽然有验证 后端为了保险再验证一遍）
         * 3. 比对新密码和旧密码是否相同
         * 3. 将新密码写入数据库
         * 4. 删除redis中的缓存 相当于需要重新登录
         */
        User user = UserUtils.getUserInfo();
        String password = user.getPassword();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (!bCryptPasswordEncoder.matches(passwordParam.getOldPass(), password)) {
            return new Result(50003, "旧密码错误");
        }
        else if (!passwordParam.getNewPass().equals(passwordParam.getCheckPass())) {
            return new Result(50003, "两次输入密码不一致");
        }
        else if(passwordParam.getOldPass().equals(passwordParam.getNewPass())) {
            return new Result(50003,"新旧密码一致，请重试");
        }
        String encode = bCryptPasswordEncoder.encode(passwordParam.getNewPass());
        //更新进数据库
        user.setPassword(encode);
        if (userMapper.updateById(user) < 0) {
            return new Result(50002,"更新失败");
        }
        // 删除redis中的缓存
        redisCache.deleteObject("login:"+user.getId());
        return new Result(200,"更新成功");
    }


    /**
     * 登出操作
     * 1. 从SecurityContextHolder中获取用户数据
     * 2. 根据用户数据删除redis中的数据
     */
    @Override
    public Result logout() {
        // 从SecurityContextHolder中获取用户信息 然后删除 Redis中存放的用户的值
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        // 删除redis中的用户信息
        redisCache.deleteObject("login:" + userId);
        return new Result(200, "退出登录成功");
    }

    // 将后端数据赋值到Vo中,Vo是前端所需要的数据
    public UserInfoVo copyToVo(User user, Role role) {
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(user, userInfoVo);
        userInfoVo.setRole(role);
        return userInfoVo;
    }


    // 更新操作
    public void UpdateUser(User user, UserInfoParam userInfoParam) {
        BeanUtils.copyProperties(userInfoParam, user);
        user.setUpdateBy(user.getId());
        user.setUpdateTime(LocalDateTime.now());
    }
}
