package com.neighbor.app.game.service.impl;

import com.neighbor.app.api.common.ErrorCodeDesc;
import com.neighbor.app.game.constants.RuleTypeDesc;
import com.neighbor.app.game.dao.GameRuleMapper;
import com.neighbor.app.game.entity.GameRule;
import com.neighbor.app.game.service.GameService;
import com.neighbor.common.util.PageTools;
import com.neighbor.common.util.ResponseResult;
import com.neighbor.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

@Service
public class GameServiceImpl implements GameService {
    private static final Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);
    @Autowired
    private GameRuleMapper gameRuleMapper;

    @Override
    public int deleteRuleByPrimaryKey(Long id) {
        return gameRuleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insertRuleSelective(GameRule record) {
        return gameRuleMapper.insertSelective(record);
    }

    @Override
    public GameRule selectRuleByPrimaryKey(Long id) {
        return gameRuleMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateRuleByPrimaryKeySelective(GameRule record) {
        return gameRuleMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Long selectRulePageTotalCount(GameRule record) {
        return gameRuleMapper.selectPageTotalCount(record);
    }

    @Override
    public List<GameRule> selectPageRuleByObjectForList(GameRule record) {
        return gameRuleMapper.selectPageByObjectForList(record);
    }

    @Override
    public List<GameRule> selectRuleAll() {
        return gameRuleMapper.selectAll();
    }

    @Override
    public GameRule ruleMatching(GameRule gameRule) {
        if(gameRule.getRuleType()==null||StringUtils.isEmpty(gameRule.getMatchingParam())||!StringUtil.isNumeric(gameRule.getMatchingParam())){
            return null;
        }
        PageTools pageTools = new PageTools();
        pageTools.setIndex(1);
        pageTools.setPageSize(1);
        if(RuleTypeDesc.award.getValue()==gameRule.getRuleType()){
            //中奖
            gameRule.setPageTools(pageTools);
            gameRule.setRuleCode(gameRule.getMatchingParam());
            List<GameRule> gameRules = gameRuleMapper.selectPageByObjectForList(gameRule);
            if(gameRules!=null&&gameRules.size()>0){
                return gameRules.get(0);
            }
            pageTools.setPageSize(999999);
            gameRule.setPageTools(pageTools);
            gameRule.setRuleCode(null);
            List<GameRule> gameRuleList = gameRuleMapper.selectPageByObjectForList(gameRule);
            if(gameRuleList!=null&&gameRuleList.size()>0){
                for(GameRule rule : gameRuleList){
                    if(rule.getRuleCode().indexOf("-")!=-1){
                        String[] arr = rule.getRuleCode().split("-");
                        BigDecimal begin = new BigDecimal(arr[0]);
                        BigDecimal end = new BigDecimal(arr[1]);
                        BigDecimal param = new BigDecimal(gameRule.getMatchingParam());
                        if(param.compareTo(begin)>=0&&param.compareTo(end)<=0){
                            return rule;
                        }
                    }
                }
            }

        }else if(RuleTypeDesc.thunder.getValue()==gameRule.getRuleType()){
            //中雷
            gameRule.setPageTools(pageTools);
            gameRule.setRuleCode(gameRule.getMatchingParam());
            List<GameRule> gameRules = gameRuleMapper.selectPageByObjectForList(gameRule);
            if(gameRules!=null&&gameRules.size()>0){
                return gameRules.get(0);
            }
        }
        return null;
    }


    @Override
    public ResponseResult ruleRecord(GameRule gameRule) {
        logger.info("查询游戏规则列表...");
        ResponseResult result = new ResponseResult();
        Long total = gameRuleMapper.selectPageTotalCount(gameRule);
        List<GameRule> pageList = gameRuleMapper.selectPageByObjectForList(gameRule);
        PageTools pageTools = gameRule.getPageTools();
        pageTools.setTotal(total);
        result.addBody("resultList", pageList);
        result.addBody("pageTools", pageTools);
        return result;
    }

}
