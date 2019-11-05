package cn.itcast.web.controller.cargo;

import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ContractExample;
import cn.itcast.domain.system.Dept;
import cn.itcast.domain.system.User;
import cn.itcast.service.cargo.ContractService;
import cn.itcast.web.controller.BaseController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 购销合同
 */
@Controller
@RequestMapping("/cargo/contract")
public class ContractController extends BaseController{

    /**
     * 远程获取合同的业务对象
     */
    @Reference
    private ContractService contractService;


    /**
     * 分页展示合同
     *   方法请求地址：http://localhost:8080/cargo/contract/list.do
     *   方法的参数：pageNum=1&pageSize=5
     *   方法的返回值/跳转的页面：/WEB-INF/pages/cargo/contract/contract-list.jsp
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int pageNum,
                       @RequestParam(defaultValue = "5") int pageSize){
        //1.（租用）登录企业的ID
        String companyId = getLoginCompanyId();

        //2.创建Example，把条件封装到Example中
        ContractExample contractExample = new ContractExample();

        //添加按照创建时间倒序
        contractExample.setOrderByClause("create_time desc");

        ContractExample.Criteria criteria = contractExample.createCriteria();
        criteria.andCompanyIdEqualTo(companyId); // 根据企业ID查询

        /**
         * 细粒度权限控制代码
         *   -- 1) 用户的degree=4普通用户， 只查询自己创建的购销合同
                 SELECT * FROM co_contract WHERE create_by='登陆用户id'
             -- 2) 用户的degree=3部门管理者， 可以看到本部门员工创建的购销合同
                 SELECT * FROM co_contract WHERE create_dept='登陆用户的部门id'
         */
        //获取登录用户对象
        User loginUser = getLoginUser();

        //用户的degree=4普通用户， 只查询自己创建的购销合同
        if(loginUser.getDegree()==4){
            criteria.andCreateByEqualTo(loginUser.getId()); // create_by='登陆用户id'
        }else if(loginUser.getDegree()==3){
            //用户的degree=3部门管理者， 可以看到本部门员工创建的购销合同
            criteria.andCreateDeptEqualTo(loginUser.getDeptId());
        }else if(loginUser.getDegree()==2){
            //大部门经理（很多子部门）:可以查看本部门及子部门员工创建的购销合同的数据
            PageInfo<Contract> pageInfo = contractService.findPageByDeptId(loginUser.getDeptId(), pageNum, pageSize);
            request.setAttribute("pageInfo",pageInfo);
            return "cargo/contract/contract-list";
        }

        //3.调用业务，获取PageInfo
        PageInfo<Contract> pageInfo = contractService.findByPage(contractExample, pageNum, pageSize);

        //4.把PageInfo存入request
        request.setAttribute("pageInfo",pageInfo);

        //3.返回页面
        return "cargo/contract/contract-list";
    }


    /**
     * 进入合同添加页面
     *   方法请求地址：http://localhost:8080/cargo/contract/toAdd.do
     *   方法的参数：无
     *   方法的返回值/跳转的页面：/WEB-INF/pages/cargo/contract/contract-add.jsp
     */
    @RequestMapping("/toAdd")
    public String toAdd(){
        return "cargo/contract/contract-add";
    }

    /**
     * 保存合同信息（添加、更新）
     *   方法请求地址：http://localhost:8080/cargo/contract/edit.do
     *   方法的参数：Dept对象
     *   方法的返回值/跳转的页面：重定向到部门列表（redirect:/cargo/contract/list.do）
     */
    @RequestMapping("/edit")
    public String edit(Contract contract){

        //获取当前登录企业的ID和名称
        String companyId = getLoginCompanyId();
        String compayName = getLoginCompanyName();

        //把企业数据封装到Contract对象中
        contract.setCompanyId(companyId);
        contract.setCompanyName(compayName);

        //给合同设置创建人，创建人的部门
        contract.setCreateBy(getLoginUser().getId());
        contract.setCreateDept(getLoginUser().getDeptId());

        //1.判断是否存在ID
        if(StringUtil.isEmpty(contract.getId())){
            //1.1 不存在，添加
            contractService.save(contract);
        }else{
            //1.2 存在，更新
            contractService.update(contract);
        }

        return "redirect:/cargo/contract/list.do";
    }

    /**
     * 进入修改合同页面
     *    方法请求地址：http://localhost:8080/cargo/contract/toUpdate.do
     *   方法的参数：id=1
     *   方法的返回值/跳转的页面：/WEB-INF/pages/cargo/contract/contract-update.jsp
     *
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        //1.查询合同
        Contract contract = contractService.findById(id);
        //2.存入request
        request.setAttribute("contract",contract);
        return "cargo/contract/contract-update";
    }


    /**
     * 合同删除
     *   方法请求地址：http://localhost:8080/cargo/contract/delete.do
     *   方法的参数：id=1
     *   方法的返回值/跳转的页面：redirect:/cargo/contract/list.do
     */
    @RequestMapping("/delete")
    public String delete(String id){
        //1.调用业务，删除部门
        contractService.delete(id);
        //3.返回result的Map对象
        return "redirect:/cargo/contract/list.do";
    }

    /**
     * 查询合同
     *   方法请求地址：http://localhost:8080/cargo/contract/toView.do
     *   方法的参数：id=1
     *   方法的返回值/跳转的页面：/WEB-INF/pages/cargo/contract/contract-view.jsp
     */
    @RequestMapping("/toView")
    public String toView(String id){
        //1.查询合同详情
        Contract contract = contractService.findById(id);
        //2.存入request
        request.setAttribute("contract",contract);
        return "cargo/contract/contract-view";
    }

    /**
     * 提交合同
     *   方法请求地址：http://localhost:8080/cargo/contract/submit.do
     *   方法的参数：id=1
     *   方法的返回值/跳转的页面：redirect:/cargo/contract/list.do
     */
    @RequestMapping("/submit")
    public String submit(String id){
        //需求：把合同的state从0改为1
        Contract contract = new Contract();
        contract.setId(id);
        //修改状态
        contract.setState(1);
        contractService.update(contract);

        return "redirect:/cargo/contract/list.do";
    }

    /**
     * 取消合同
     *    法请求地址：http://localhost:8080/cargo/contract/cancel.do
     *   方法的参数：id=1
     *   方法的返回值/跳转的页面：redirect:/cargo/contract/list.do
     */
    @RequestMapping("/cancel")
    public String cancel(String id){
        //需求：把合同的state从1改为0
        Contract contract = new Contract();
        contract.setId(id);
        //修改状态
        contract.setState(0);
        contractService.update(contract);

        return "redirect:/cargo/contract/list.do";
    }
}
