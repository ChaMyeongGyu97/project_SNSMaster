package com.codemaster.project_snsmaster.controller;

import com.codemaster.project_snsmaster.service.IF_GroupService;
import com.codemaster.project_snsmaster.vo.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;

@Controller
public class GroupController {
    @Autowired
    IF_GroupService gservice;

    @RequestMapping(value = "/groupinput", method = RequestMethod.GET)//그룹에서 게시글 작성
    public String groupinput(@RequestParam int gno, Model model) {
        System.out.println(gno);
        model.addAttribute("gno", gno);
        return "groupinput_test";
    }

    @RequestMapping(value = "/groupinputSave", method = RequestMethod.POST)//그룹에서 게시글 작성
    public String groupinputSave(@ModelAttribute GroupPostVO gpvo, HttpSession session) throws Exception {
        gpvo.setId((String) session.getAttribute("userid"));
        System.out.println(gpvo.toString());
        gservice.insert(gpvo);
        return "redirect:gpList?gno=" + gpvo.getGno();
    }

    @RequestMapping(value = "/grupinput", method = RequestMethod.GET)//그룹에서 동행 모집글 작성
    public String grupinput(@RequestParam int gno, Model model) {
        System.out.println(gno);
        model.addAttribute("gno", gno);
        return "groupinput_test";
    }

    @RequestMapping(value = "/joininputSave", method = RequestMethod.POST)//그룹에서 모임동행 모집글 작성
    public String joininputSave(@ModelAttribute G_joinVO gjvo, HttpSession session) throws Exception {
        gjvo.setId((String) session.getAttribute("userid"));
        System.out.println(gjvo.toString());
        gservice.gjinsert(gjvo);
        return "redirect:gpList?gno=" + gjvo.getGno();
    }

    @RequestMapping(value = "/membergroupinput", method = RequestMethod.GET)//그룹만들기
    public String membergroupinput() {
        return "membergroup_test";
    }

    @RequestMapping(value = "/membergroupinputSave", method = RequestMethod.POST)//그룹만들기
    public String membergroupinputSave(@ModelAttribute MemberGroupVO mgvo, HttpSession session) throws Exception {
        mgvo.setG_id((String) session.getAttribute("userid"));
        System.out.println(mgvo.toString());
        gservice.mginsert(mgvo);
        return "membergroup_test";
    }

    @RequestMapping(value = "/groupjoin", method = RequestMethod.GET) //그룹에 가입
    public String groupjoin(@ModelAttribute GroupJoinVO gjvo, HttpSession session) throws Exception {
        gjvo.setId((String) session.getAttribute("userid"));
        System.out.println(gjvo.getGno());
        System.out.println(gjvo.getId());
        //session.getAttribute("userid");
        gservice.joinsert(gjvo);
        return "groupjoin_test";
    }

    @RequestMapping(value = "/groupjoinmember", method = RequestMethod.GET)//그룹 가입 그룹장이 수락
    public String groupjoinmember(@ModelAttribute GroupJoinVO gjvo) throws Exception {
        System.out.println(gjvo);
        System.out.println(gjvo.getGno());
        gservice.gjminsert(gjvo);
        gservice.gjmdelete(gjvo);
        return "redirect:/groupmy?gno=" + gjvo.getGno();
    }

    @RequestMapping(value = "/g_memberjoin", method = RequestMethod.GET)//그룹에서 모임동행글 신청
    public String memberjoin(@ModelAttribute G_memberVO gmvo, HttpSession session) throws Exception {
        gmvo.setId((String) session.getAttribute("userid"));
        System.out.println(gmvo.getGno());
        System.out.println(gmvo.getMo_no());
        System.out.println(gmvo.getId());

        gservice.gmjinsert(gmvo);
        // return "redirect:gpList?gno=" + gmvo.getGno();
        return "redirect:gList";
    }

    @GetMapping("/gList")//그룹 전체보기
    public String gList(Model model, HttpSession session) throws Exception {
        String id = (String) session.getAttribute("userid");
        model.addAttribute("id", id);
        List<MemberGroupVO> gList = gservice.gList();
        model.addAttribute("gList", gList);
        return "groupall_test";
    }

    @GetMapping("/gsearch")// 그룹 전체보기 검색
    public String search(@RequestParam("search") String search, @RequestParam String g_region, Model model) throws Exception {
        System.out.println(g_region);
        System.out.println(search);
        HashMap<String, String> param = new HashMap<>();
        param.put("search", search);
        param.put("g_region", g_region);
        model.addAttribute("gList", gservice.search(param));
        return "groupall_test";
    }

    @GetMapping("/gjsearch")//그룹 동행 모집글 검색
    public String gjsearch
            (HttpSession session, RedirectAttributes redirect,
             @RequestParam("gno") String gno, @RequestParam String search,
             @RequestParam String g_region, Model model) throws Exception {
        String id = (String) session.getAttribute("userid");
        model.addAttribute("id", id);
        HashMap<String, String> param = new HashMap<>();
        System.out.println(gno);
        System.out.println(search);
        System.out.println(g_region);
        List<GroupPostVO> gpList = gservice.gpList(gno);
        param.put("gno", gno);
        param.put("search", search);
        param.put("g_region", g_region);
        model.addAttribute("gpList", gpList);
        model.addAttribute("gjList", gservice.gjsearch(param));
        System.out.println(gservice.gjsearch(param));
        model.addAttribute("gno",gno);
        //redirect.addAttribute("gList",gservice.gpsearch(param));
        //return "redirect:/gpList?gno="+ gno;
        return "gpall_test";
    }

    @GetMapping("/gpsearch")//그룹 게시글 검색
    public String gpsearch
            (HttpSession session,
             @RequestParam("gno") String gno,@RequestParam String search,
             @RequestParam String idsearch, Model model,HttpServletRequest request)throws Exception{
        String id = (String) session.getAttribute("userid");
        model.addAttribute("id", id);
        HashMap<String, String> param = new HashMap<>();
        System.out.println(gno);
        System.out.println(search);
        System.out.println(idsearch);
        List<G_joinVO> gjList = gservice.gjList(gno);
        param.put("gno", gno);
        param.put("search", search);
        param.put("idsearch", idsearch);
        model.addAttribute("gjList", gjList);
        model.addAttribute("gpList", gservice.gpsearch(param));
        model.addAttribute("gno",gno);
        String referer=request.getHeader("referer");
        model.addAttribute("referer", referer);
        System.out.println(gservice.gpsearch(param).size());
        return "gpall_test";
    }


    @GetMapping("/gpList")//그룹에 들어가면 그 그룹에 관한 게시글 보기
    public String gpList(@RequestParam("gno") String gno, Model model, HttpSession session,HttpServletRequest request) throws Exception {
        String id = (String) session.getAttribute("userid");
        model.addAttribute("id", id);
        System.out.println(gno);
        model.addAttribute("gno", gno);
        List<GroupPostVO> gpList = gservice.gpList(gno);
        List<G_joinVO> gjList = gservice.gjList(gno);
        System.out.println(gpList);
        model.addAttribute("gpList", gpList);
        //int count =gservice.countselect(mo_no);
        // model.addAttribute("")
        System.out.println(gjList);
        model.addAttribute("gjList", gjList);
        String referer=request.getHeader("referer");
        model.addAttribute("referer", referer);
        return "gpall_test";
    }

    @GetMapping("/groupmy")//그룹 가입 신청 한 사람 보기
    public String groupmy(@RequestParam("gno") String gno, Model model) throws Exception {
        System.out.println(gno);
        model.addAttribute("gno", gno);
        List<GroupJoinVO> gjoList = gservice.gjoList(gno);
        model.addAttribute("gjoList", gjoList);
        System.out.println(gjoList);
        return "groupmy_test";
    }


    @RequestMapping(value = "/group", method = RequestMethod.GET)
    public String home() {
        return "grouphome_test";
    }

    @RequestMapping(value = "/delgroup", method = RequestMethod.GET)//게시글 삭제
    public String delgroup(@RequestParam("gno") String gno, @RequestParam("g_no") int g_no,HttpServletRequest request) throws Exception {
        System.out.println(g_no);
        System.out.println(gno);
        gservice.delete(g_no);
        String referer=request.getHeader("referer");
        return "redirect:" +referer;
        //return redirect 할때 그룹 게시글이 있는 화면으로 넘어 갈려고 할때 ?gno="+gno 쓰면
        //삭제한 게시글이 있던 그룹으로 화면을 보여준다
    }

    @RequestMapping(value = "/gjdel", method = RequestMethod.GET)//그룹에서 모임동행글 삭제
    public String gjdel(@RequestParam("mo_no") int mo_no, @RequestParam("gno") String gno,HttpServletRequest request) throws Exception {
        System.out.println(mo_no);
        gservice.gjdelete(mo_no);
        String referer=request.getHeader("referer");
        return "redirect:"+referer;
    }


    @RequestMapping(value = "/delmembergroup", method = RequestMethod.GET)//그룹 삭제
    public String delmembergroup(@RequestParam("gno") int gno) throws Exception {
        System.out.println(gno);
        gservice.mdelete(gno);
        return "redirect:/gList";
    }

    @RequestMapping(value = "/delgroupjoinmember", method = RequestMethod.GET)//그룹 가입 신청 그룹장이 거부
    public String delgroupjoinmember(@RequestParam("wait_no") int wait_no, @RequestParam("gno") int gno) throws Exception {
        System.out.println(wait_no);
        System.out.println(gno);
        gservice.dgjmdelete(wait_no);
        return "redirect:/groupmy?gno=" + gno;
    }

    @GetMapping(value = "/modno")//수정할글 화면에 출력
    public String modno(@RequestParam("g_no") int g_no, Model model, HttpServletRequest request) throws Exception {
        GroupPostVO gpvo = gservice.modno(g_no);
        System.out.println("수정된 글 정보 확인");
        System.out.println(gpvo.toString());
        model.addAttribute("gpvo", gpvo);
        String referer = request.getHeader("Referer");
        model.addAttribute("referer", referer);

        return "modall_test";
    }

    @PostMapping(value = "/modnosave")//수정글 저장
    public String modnoSave(@ModelAttribute GroupPostVO gpvo, HttpSession session,String referer) throws Exception {

        gpvo.setId((String) session.getAttribute("userid"));
        System.out.println(gpvo.toString());
        System.out.println(gpvo.getG_no());
        System.out.println(gpvo.getId());
        gservice.modnoSaver(gpvo);
        //return "redirect:/gpList?gno=" + gpvo.getGno();
        return "redirect:"+referer;
    }

    @GetMapping(value = "/gmjoinmod")//그룹 모임동행글 수정
    public String gmjoinmod(@RequestParam("mo_no") int mo_no, Model model, HttpServletRequest request) throws Exception {
        G_joinVO gjvo = gservice.gmjoinmod(mo_no);
        System.out.println(gjvo.toString());
        model.addAttribute("gjvo", gjvo);
        String referer = request.getHeader("Referer");
        model.addAttribute("referer", referer);
        return "gmjoinmod_test";
    }

    @PostMapping(value = "/gmjoinmodsave")//그룹 모임동행글 수정
    public String gmjoinmodsave(@ModelAttribute G_joinVO gjvo, HttpSession session,String referer) throws Exception {
        gjvo.setId((String) session.getAttribute("userid"));
        System.out.println(gjvo.toString());
        System.out.println(gjvo.getGno());
        System.out.println(gjvo.getId());
        gservice.gmjoinmodSave(gjvo);
        return "redirect:" +referer;
    }

    @GetMapping(value = "/grouppost")//그룹에서 게시글 자세히 보기
    public String grouppost(@RequestParam("g_no") String g_no, Model model, HttpSession session) throws Exception {
        GroupPostVO gpvo = gservice.gpselect(g_no);
        String id = (String) session.getAttribute("userid");
        model.addAttribute("id", id);
        model.addAttribute("gpvo", gpvo);
        List<GroupPostCommentVO> cmList = gservice.cmList(g_no);
        model.addAttribute("cmList", cmList);
        return "grouppost_test";
    }

    @RequestMapping(value = "/groupcommentSave", method = RequestMethod.POST)//그룹에서 게시글 작성
    public String groupcommentSave(@ModelAttribute GroupPostCommentVO gcvo, HttpSession session) throws Exception {
        gcvo.setId((String) session.getAttribute("userid"));
        System.out.println(gcvo.toString());
        System.out.println(gcvo.getG_no());
        gservice.gcinsert(gcvo);
        return "redirect:grouppost?g_no=" + gcvo.getG_no();
    }

    @GetMapping(value = "/commentdel")// 그룹 뎃글 삭제
    public String commentdel(@RequestParam("c_no") int c_no, @RequestParam("g_no") String g_no) throws Exception {
        System.out.println(c_no);
        System.out.println(g_no);
        gservice.commentdel(c_no);
        return "redirect:grouppost?g_no=" + g_no;
    }
}

