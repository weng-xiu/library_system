package com.yx.controller;

import com.github.pagehelper.PageInfo;
import com.yx.po.Admin;
import com.yx.service.AdminService;
import com.yx.utils.DataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

@Controller //将类标识为控制层组件
public class AdminController {

    /**
     * @Component：将类标识为普通组件
     * @Controller：将类标识为控制层组件
     * @Service：将类标识为业务层组件
     * @Repository：将类标识为持久层组件
     *
     * 通过注解+扫描所配置的bean的id，默认值为类的小驼峰，即类名的首字母为小写的结果
     * 可以通过标识组件的注解的value属性值设置bean的自定义的id
     *
     * @Autowired:实现自动装配功能的注解
     * 1、@Autowired注解能够标识的位置
     * a>标识在成员变量上，此时不需要设置成员变量的set方法
     * b>标识在set方法上
     * c>标识在为当前成员变量赋值的有参构造上
     *
     * 2、@Autowired注解的原理
     * a>默认通过byType的方式，在IOC容器中通过类型匹配某个bean为属性赋值
     * b>若有多个类型匹配的bean，此时会自动转换为byName的方式实现自动装配的效果
     * 即将要赋值的属性的属性名作为bean的id匹配某个bean为属性赋值
     * c>若byType和byName的方式都无妨实现自动装配，即IOC容器中有多个类型匹配的bean
     * 且这些bean的id和要赋值的属性的属性名都不一致，此时抛异常：NoUniqueBeanDefinitionException
     * d>此时可以在要赋值的属性上，添加一个注解@Qualifier
     * 通过该注解的value属性值，指定某个bean的id，将这个bean为属性赋值
     *
     * 注意：若IOC容器中没有任何一个类型匹配的bean，此时抛出异常：NoSuchBeanDefinitionException
     * 在@Autowired注解中有个属性required，默认值为true，要求必须完成自动装配
     * 可以将required设置为false，此时能装配则装配，无法装配则使用属性的默认值
     */
    @Autowired
    private AdminService adminService;

    @GetMapping("/adminIndex")//get请求的映射-->@GetMapping
    public String adminIndex(){
        return "admin/adminIndex";
    }

    /**
     * 查询管理员全部
     * @param admin
     * @param pageNum
     * @param limit
     * @return
     */
    @RequestMapping("/adminAll")
    @ResponseBody
    public DataInfo queryAdminAll(Admin admin, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "15") Integer limit){
        //pageNum:显示第几页，默认第一页；limit:显示多少条，默认15条
        PageInfo<Admin> pageInfo = adminService.queryAdminAll(admin,pageNum,limit);
        return DataInfo.ok("成功",pageInfo.getTotal(),pageInfo.getList());
    }

    /**
     * 添加页面的跳转
     * @return
     */
    @GetMapping("/adminAdd")
    public String adminAdd(){
        return "admin/adminAdd";
    }

    /**
     * 添加提交
     * @param admin
     * @return
     */
    @RequestMapping("/addAdminSubmit")
    @ResponseBody
    public DataInfo addBookSubmit(Admin admin){
        adminService.addAdminSubmit(admin);
        return DataInfo.ok();
    }

    /**
     * 根据id查询
     */
    @GetMapping("/queryAdminById")
    public String queryAdminById(Integer id, Model model){
        model.addAttribute("id",id);
        return "admin/updateAdmin";
    }

    /**
     * 修改提交
     */
    @RequestMapping("/updatePwdSubmit")
    @ResponseBody
    public DataInfo updatePwdSubmit(Integer id,String oldPwd,String newPwd){
        Admin admin = adminService.queryAdminById(id);//根据id查询对象
        if (!oldPwd.equals(admin.getPassword())){
            return DataInfo.fail("输入的旧密码错误");
        }else{
            admin.setPassword(newPwd);
            adminService.updateAdminSubmit(admin);//数据库修改
            return DataInfo.ok();
        }
    }

    /**
     * 删除
     */
    @RequestMapping("/deleteAdminByIds")
    @ResponseBody
    public DataInfo deleteAdminByIds(String ids){
        List<String> list = Arrays.asList(ids.split(","));
        adminService.deleteAdminByIds(list);
        return DataInfo.ok();
    }

}
