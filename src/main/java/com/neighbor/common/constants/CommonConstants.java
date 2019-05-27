package com.neighbor.common.constants;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.neighbor.app.game.constants.RuleTypeDesc;
import com.neighbor.app.game.entity.GameRule;
import com.neighbor.app.game.service.GameService;
import com.neighbor.common.dictionary.entity.Dictionary;
import com.neighbor.common.dictionary.service.DictionaryService;
 

@Component
public class CommonConstants implements ApplicationListener<ContextRefreshedEvent> {
	public static final Map<String, Map<String, String>> dictionaryMap = new HashMap<String, Map<String, String>>();
	static {
//		custOrderStatusMap = CustOrderStatus.getValues();
	}

	@Autowired
	private DictionaryService dictionaryService;
	@Autowired
	private GameService gameService;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (dictionaryMap.size() == 0) {
			List<Dictionary> dictionList = dictionaryService.selectBySelective(null);
			for (Dictionary dict : dictionList) {
				if (dict.getStatus() != 1) {
					continue;
				}

				if (!dictionaryMap.containsKey(dict.getBizCode())) {
					dictionaryMap.put(dict.getBizCode(), new LinkedHashMap<String, String>());
				}

				dictionaryMap.get(dict.getBizCode()).put(dict.getCode(), dict.getName());
			}
			
			List<GameRule> gameRuleList = gameService.ruleCommissionRecord(null, RuleTypeDesc.rebate.getValue()); // 中奖金额 
			for(GameRule rule : gameRuleList){
				String id = rule.getGameId()+"-"+RuleTypeDesc.rebate.getValue();
				if (!dictionaryMap.containsKey(id)) {
					dictionaryMap.put(id, new LinkedHashMap<String, String>());
				}
				dictionaryMap.get(id).put(rule.getRuleCode(),rule.getRuleValue());
			}
		}
	}
	
	public String getAdAccountType(String code){
		return getDictionarysByKey("AdAccountType").get(code);
	}
	
	public Map<String, String> getAdAccountTypeMap(){
		return getDictionarysByKey("AdAccountType");
	}
	
	public Map<String, String> getDictionarysByKey(String bizCode){
		return dictionaryMap.get(bizCode);
	}
	
	public String getDictionarysBy(String bizCode,String code){
		Assert.isTrue(bizCode!=null&&code!=null, "业务代码和字典代码不允许为空");
		return dictionaryMap.get(bizCode).get(code);
	}
	
	public String getGameRuleCommissionBy(long gameId,RuleTypeDesc ruleType,String code){
		Assert.isTrue(code!=null&&ruleType!=null, "规则类型和规则代码不允许为空");
		return dictionaryMap.get(gameId+"-"+ruleType.getValue()).get(code);
	}

	public void updateDictionaryBy(String bizCode, String code,String value) {
		String msg = String.format("业务代码[%s],字典代码[%s],更新值[%s]均不允许为空", bizCode,code,value);
		Assert.isTrue(bizCode!=null&&code!=null&&value!=null, msg);
		dictionaryMap.get(bizCode).put(code, value);
	}
}
