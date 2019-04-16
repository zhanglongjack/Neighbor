package com.neighbor.app.game.controller;

import com.neighbor.app.api.common.ErrorCodeDesc;
import com.neighbor.app.game.entity.GameRule;
import com.neighbor.app.game.service.GameService;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.PageTools;
import com.neighbor.common.util.ResponseResult;
import com.neighbor.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping(value = "/gameRuleList")
@SessionAttributes("user")
@Controller
public class GameRuleListController {
    private static final Logger logger = LoggerFactory.getLogger(GameRuleListController.class);
    @Autowired
    private GameService gameService;


    @RequestMapping(value = "/gameRuleSelectListModal.ser")
    public String gameRuleSelectListModal(Integer id, Model model) throws Exception {
        logger.debug("GroupSelectListModal request:" + id + ",model:" + model);
        model.addAttribute("gameId", id);

        logger.debug("gameRuleSelectListModal model : " + model);

        return "page/gameRule/SelectListModal";
    }

    @RequestMapping(value = "/gameRulePage.ser")
    @ResponseBody
    public Map<String,Object> gameRulePage(GameRule gameRule, @ModelAttribute("user") UserInfo user) throws Exception {
        logger.debug("gameRulePage request:" + gameRule + " user info ==="+user);
        if(gameRule!=null&& StringUtil.isEmpty(gameRule.getRuleSubType())){
            gameRule.setRuleSubType(null);
        }
        Long size = gameService.selectRulePageTotalCount(gameRule);
        List<GameRule> ciList = gameService.selectPageRuleByObjectForList(gameRule);
        logger.debug("gameRulePage result list info =====:" + ciList);

        Map<String,Object> mv = new HashMap<String,Object>();
        mv.put("rows", ciList);
        mv.put("total", size);
//		mv.put("queryObject", queryObject);
        return mv;
    }


    @RequestMapping(value = "/primaryModalView.ser")
    public String primaryModalView(Long gameId, String modifyModel, Model model) throws Exception {
        logger.debug("primaryModalView request:" + gameId + ",model:" + model);
        model.addAttribute("modifyModel", modifyModel);
        GameRule gameRule = new GameRule();
       /* if(id!=null){
            gameRule = gameService.selectRuleByPrimaryKey(id);
        }*/
        gameRule.setGameId(gameId);
        model.addAttribute("modifyInfo", gameRule);
        logger.debug("primaryModalView model : " + model);

        return "page/gameRule/ModifyModal";
    }

    @RequestMapping(value = "/pageView.ser")
    @ResponseBody
    public Map<String, Object> pageView(UserInfo userInfo, GameRule gameRule, PageTools pageTools                                         ) {
        logger.debug("GameListController request userInfo: " +userInfo);
        Long size = gameService.selectRulePageTotalCount(gameRule);
        pageTools.setTotal(size);
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("pageTools", pageTools);
        return result;
    }

   @RequestMapping(value = "/loadPage.ser")
    public ModelAndView loadPage(  GameRule gameRule, PageTools pageTools) throws Exception {
        logger.debug("loadPage page info ===" + pageTools+" gameRule ==="+gameRule);

       gameRule.setPageTools(pageTools);
       if(gameRule!=null&& StringUtil.isEmpty(gameRule.getRuleSubType())){
           gameRule.setRuleSubType(null);
       }
        ModelAndView mv = new ModelAndView("page/gameRule/Content :: container-fluid");
        Long size = gameService.selectRulePageTotalCount(gameRule);
        pageTools.setTotal(size);
        List<GameRule> ciList = gameService.selectPageRuleByObjectForList(gameRule);
        logger.debug("loadPage game rule result list info =====:" + ciList);

        mv.addObject("resultList", ciList);
        mv.addObject("pageTools", pageTools);
        mv.addObject("queryObject", gameRule);

        return mv;
    }

    @RequestMapping(value="/editGameRule.ser")
    @ResponseBody
    public ResponseResult editGameRule(@ModelAttribute("user") UserInfo user, GameRule gameRule){
        logger.info("edit userInfo:{}",user);
        logger.info("edit gameRule:{}",gameRule);
        Map<String,Object> map = new HashMap<String,Object>();
        int num = 0;
        try {
            gameService.updateRuleByPrimaryKeySelective(gameRule);
            map.put("success", true);
            num=1;

        } catch (Exception e) {
            map.put("success", false);
            logger.error(e.getMessage(),e);
        }
        map.put("editNumber", num);
        return new ResponseResult();
    }

    @RequestMapping(value="/addGameRule.ser")
    @ResponseBody
    public ResponseResult addGameRule(GameRule gameRule, @ModelAttribute("user") UserInfo user) throws Exception{
        logger.info("addGameRule request:{}",user);
        logger.info("addGameRule request:{}",gameRule);
        if(!user.isAdmin()){
            throw new Exception("很抱歉，您还没有权限~");
        }
        gameRule.setCreateTime(new Date());
        if(StringUtil.isNotEmpty(gameRule.getRuleCode())&&gameRule.getRuleCode().indexOf("-")!=-1){
            String[] code = gameRule.getRuleCode().split("-");
            gameRule.setSchemeCode(StringUtil.generateGameRuleNum(code[0],code[1],Integer.valueOf(gameRule.getRuleSubType())));
        }else{
            gameRule.setSchemeCode(gameRule.getRuleCode());
        }
        gameService.insertRuleSelective(gameRule);
        return new ResponseResult();
    }

    @RequestMapping(value = "/del.ser", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult del(Long id) {
        logger.info("del begin .. id = "+id+"....");

        ResponseResult result = new ResponseResult();
        try {

            gameService.deleteRuleByPrimaryKey(id);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setErrorCode(ErrorCodeDesc.failed.getValue());
        }

        return result;
    }
}
