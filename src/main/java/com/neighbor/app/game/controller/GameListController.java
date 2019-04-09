package com.neighbor.app.game.controller;

import com.alibaba.fastjson.JSON;
import com.neighbor.app.api.common.ErrorCodeDesc;
import com.neighbor.app.game.entity.Game;
import com.neighbor.app.game.entity.GameRule;
import com.neighbor.app.game.service.GameService;
import com.neighbor.app.group.entity.Group;
import com.neighbor.app.recharge.constants.RechargeStatusDesc;
import com.neighbor.app.recharge.entity.Recharge;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.PageTools;
import com.neighbor.common.util.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping(value = "/gameList")
@SessionAttributes("user")
@Controller
public class GameListController {
    private static final Logger logger = LoggerFactory.getLogger(GameListController.class);
    @Autowired
    private GameService gameService;

    @RequestMapping(value = "/primaryModalView.ser")
    public String primaryModalView(Long id, String modifyModel, Model model) throws Exception {
        logger.debug("primaryModalView request:" + id + ",model:" + model);
        model.addAttribute("modifyModel", modifyModel);
        Game game = new Game();
        game.setGameType(0L);
        if(id!=null){
            game = gameService.selectByPrimaryKey(id);
        }
        model.addAttribute("modifyInfo", game);
        logger.debug("primaryModalView model : " + model);

        return "page/game/ModifyModal";
    }

    @RequestMapping(value = "/pageView.ser")
    @ResponseBody
    public Map<String, Object> pageView(UserInfo userInfo, Game game, PageTools pageTools                                         ) {
        logger.debug("GameListController request userInfo: " +userInfo);
        Long size = gameService.selectPageTotalCount(game);
        pageTools.setTotal(size);
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("pageTools", pageTools);
        return result;
    }

   @RequestMapping(value = "/loadPage.ser")
    public ModelAndView loadPage(UserInfo userInfo, PageTools pageTools,
                                 Game game) throws Exception {
        logger.debug("loadPage User request:" + userInfo + " page info ===" + pageTools+" Game ==="+game);


        userInfo.setPageTools(pageTools);
        ModelAndView mv = new ModelAndView("page/game/Content :: container-fluid");
        Long size = gameService.selectPageTotalCount(game);
        pageTools.setTotal(size);
        List<Game> ciList = gameService.selectPageByObjectForList(game);
        logger.debug("loadPage User result list info =====:" + ciList);

        mv.addObject("resultList", ciList);
        mv.addObject("pageTools", pageTools);
        mv.addObject("queryObject", game);

        return mv;
    }

    @RequestMapping(value="/editGame.ser")
    @ResponseBody
    public ResponseResult editGame(@ModelAttribute("user") UserInfo user, Game game){
        logger.info("edit userInfo:{}",user);
        logger.info("edit recharge:{}",game);
        Map<String,Object> map = new HashMap<String,Object>();
        int num = 0;
        try {
            gameService.updateByGame(game);
            map.put("success", true);
            num=1;

        } catch (Exception e) {
            map.put("success", false);
            logger.error(e.getMessage(),e);
        }
        map.put("editNumber", num);
        return new ResponseResult();
    }

    @RequestMapping(value="/addGame.ser")
    @ResponseBody
    public ResponseResult addGame(Game game, @ModelAttribute("user") UserInfo user) throws Exception{
        logger.info("addGame request:{}",user);
        logger.info("addGame request:{}",game);
        if(!user.isAdmin()){
            throw new Exception("很抱歉，您还没有权限~");
        }
        game.setCreateTime(new Date());
        gameService.insertSelective(game);
        return new ResponseResult();
    }
}
