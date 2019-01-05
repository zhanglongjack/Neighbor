package com.neighbor.app.game.controller;

import com.neighbor.app.bankcard.entity.BankCard;
import com.neighbor.app.game.constants.RuleTypeDesc;
import com.neighbor.app.game.dao.GameRuleMapper;
import com.neighbor.app.game.entity.Game;
import com.neighbor.app.game.entity.GameRule;
import com.neighbor.app.game.service.GameService;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.PageTools;
import com.neighbor.common.util.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/game")
@SessionAttributes("user")
@Controller
public class GameController {
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);
    @Autowired
    private GameService gameService;

    @RequestMapping(value = "/ruleMatching.req", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult ruleMatching(GameRule gameRule) throws Exception {
        logger.info("ruleMatching request gameRule >>>> " + gameRule);
        ResponseResult result = new ResponseResult();
        result.addBody("gameRule",gameService.ruleMatching(gameRule));
        return result;
    }


    @RequestMapping(value = "/ruleRecord.req", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult ruleRecord(GameRule gameRule, PageTools pageTools) throws Exception {
        logger.info("ruleRecord request gameRule >>>> " + gameRule);
        logger.info("ruleRecord request pageTools >>>> " + pageTools);
        gameRule.setPageTools(pageTools);
        ResponseResult result = gameService.ruleRecord(gameRule);
        return result;
    }
}
