package com.xiaolvche.cloudconnection.controller;


import com.xiaolvche.cloudconnection.bean.Conversation;
import com.xiaolvche.cloudconnection.service.ConversationService;
import com.xiaolvche.cloudconnection.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * @author liaoxh
 * @create 2021-05-08-14:26
 */
@Controller
public class ConnectionContoler {
    @Autowired
    ConversationService conversationService;
    @RequestMapping("/")
    @ResponseBody
    public String index(){

        System.out.println("又一个访问主页");
        List<Conversation> list = conversationService.list();
        return list.toString();
    }

   @RequestMapping("/kefu")
   public String h(){
       System.out.println("又一个访问主页");
       return "kefu";
   }

    @RequestMapping("/kehu")
    public String kehu(HttpServletRequest request){
        System.out.println("又一个访问主页");
        String ip  =  request.getHeader( " x-forwarded-for " );
        if (ip  ==   null   ||  ip.length()  ==   0   ||   " unknown " .equalsIgnoreCase(ip))  {
            ip  =  request.getHeader( " Proxy-Client-IP " );
        }
        if (ip  ==   null   ||  ip.length()  ==   0   ||   " unknown " .equalsIgnoreCase(ip))  {
            ip  =  request.getHeader( " WL-Proxy-Client-IP " );
        }
        if (ip  ==   null   ||  ip.length()  ==   0   ||   " unknown " .equalsIgnoreCase(ip))  {
            ip  =  request.getRemoteAddr();
        }

        System.out.println(ip+"remote:"+request.getRemotePort()+" local:"+request.getLocalPort()+" server:"+request.getServerPort());
        return "bus_user";
    }

    @RequestMapping("/hello")
    public String hello(){
        return "index";
    }

    @RequestMapping("/im/{id}")
    public ModelAndView mytest(){
       System.out.println("欢迎光临~");
        ModelAndView view = new ModelAndView("haha");
        return view;
    }
    @RequestMapping("/kf")
    public String kf(){
        return "coniee";
    }

    @GetMapping(value="/test1")
    @ResponseBody
    public Object test1(){
       /* HashMap<String,Object> map = new HashMap<>();
        map.put("code", "200");
        map.put("message", "welcome!");
        map.put("userId",123);
        map.put("wechat","lys20191020");
        map.put("comment","返利机器人");*/
        //int i = 10/0;
       // return map;
        return ResponseVO.error(500,"code is bad" );
    }



}
