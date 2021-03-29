package com.zsr.controller;

import com.zsr.pojo.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class JDBCController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    //查询所有员工
    @RequestMapping("/emps")
    public String employeeList(Model model) {
        String sql = "SELECT * FROM employee";
        List<Employee> employees = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Employee.class));
        model.addAttribute("emps", employees);
        return "emp/list";
    }

    @GetMapping("/add")
    public String add(Model model) {
        //将所有的部门名称添加到departments中,用于前端接收
        ArrayList<String> departments = new ArrayList<>();
        departments.add("技术部");
        departments.add("市场部");
        departments.add("调研部");
        departments.add("后勤部");
        departments.add("运营部");
        model.addAttribute("departments", departments);
        return "emp/add";//返回到添加员工页面
    }

    //添加员工
    @PostMapping("/add")
    public String addEmp(Employee employee) {
        Integer id = employee.getId();
        String lastName = employee.getLastName();
        String email = employee.getEmail();
        Integer gender = employee.getGender();
        String departmentName = employee.getDepartmentName();
        Date date = employee.getDate();
        String sql = "INSERT INTO employee VALUES(?,?,?,?,?,?)";
        jdbcTemplate.update(sql, new Object[]{id, lastName, email, gender, departmentName, date});
        return "redirect:/emps";//重定向到/emps请求,显示list页面,展示所有员工
    }

    //修改员工,restful风格接收参数
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        //查询指定id的员工,添加到empByID中,用于前端接收
        String sql = "SELECT * FROM employee WHERE id = " + String.valueOf(id);
        List<Employee> employees = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Employee.class));
        Employee employeeByID = employees.get(0);
        model.addAttribute("empByID", employeeByID);
        //查出所有的部门信息,添加到departments中,用于前端接收
        //将所有的部门名称添加到departments中,用于前端接收
        ArrayList<String> departments = new ArrayList<>();
        departments.add("技术部");
        departments.add("市场部");
        departments.add("调研部");
        departments.add("后勤部");
        departments.add("运营部");
        model.addAttribute("departments", departments);
        return "/emp/edit";//返回到编辑员工页面
    }

    @PostMapping("/edit")
    public String editEmp(Employee employee) {
        Integer id = employee.getId();
        String lastName = employee.getLastName();
        String email = employee.getEmail();
        Integer gender = employee.getGender();
        String departmentName = employee.getDepartmentName();
        Date date = employee.getDate();
        String sql = "UPDATE employee SET lastName = ?, email = ?, gender = ?, departmentName = ?, date = ? WHERE id = ?";
        jdbcTemplate.update(sql, new Object[]{lastName, email, gender, departmentName, date, id});
        return "redirect:/emps";
    }

    //删除员工
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        String sql = "DELETE FROM employee WHERE id = ?";
        jdbcTemplate.update(sql, id);
        return "redirect:/emps";//重定向到/emps请求,显示list页面,展示所有员工
    }
}
