package com.neighbor.app.bankcard.service.impl;

import com.alibaba.fastjson.JSON;
import com.neighbor.app.api.common.ErrorCodeDesc;
import com.neighbor.app.bankcard.dao.BankCardMapper;
import com.neighbor.app.bankcard.entity.BankCard;
import com.neighbor.app.bankcard.po.BankNameJson;
import com.neighbor.app.bankcard.constants.CardTypeDesc;
import com.neighbor.app.bankcard.po.ValidBankCard;
import com.neighbor.app.bankcard.service.BankCardService;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.constants.EnvConstants;
import com.neighbor.common.exception.ParamsCheckException;
import com.neighbor.common.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BankCardServiceImpl implements BankCardService {

    private static final Logger logger = LoggerFactory.getLogger(BankCardServiceImpl.class);

    @Autowired
    private BankCardMapper bankCardMapper;

    @Autowired
    private Environment env;

    @Autowired
    private BankNameJson bankNameJson;

    @Override
    public ResponseResult addCard(UserInfo user, BankCard bankCard) throws Exception {
        ResponseResult result = new ResponseResult();
        BankCard record = new BankCard();
        record.setuId(user.getId());
        record.setBankCardNo(bankCard.getBankCardNo());
        Long data =  bankCardMapper.selectPageTotalCount(record);
        if(data>0){
            throw new ParamsCheckException(ErrorCodeDesc.failed.getValue(),"该银行卡已添加过，请勿重复添加!");
        }
        String respStr = HttpClientUtils.doGet(String.format(env.getProperty(EnvConstants.ALI_BANK_CARD_URL),bankCard.getBankCardNo())) ;
        ValidBankCard validBankCard = JSON.parseObject(respStr,ValidBankCard.class);
        if(validBankCard==null||validBankCard.getValidated()==null||!validBankCard.getValidated().booleanValue()){
            throw new ParamsCheckException(ErrorCodeDesc.failed.getValue(),"暂不支持该银行卡!");
        }
        if(!CardTypeDesc.DC.toString().equals(validBankCard.getCardType())){
            throw new ParamsCheckException(ErrorCodeDesc.failed.getValue(),"只允许绑定储蓄卡!");
        }
        bankCard.setCardType(validBankCard.getCardType());
        bankCard.setCardTypeName(CardTypeDesc.getDesByValue(bankCard.getCardType()));
        bankCard.setBankCode(validBankCard.getBank());
        bankCard.setBankName(bankNameJson.getBankName(bankCard.getBankCode()));
        bankCard.setuId(user.getId());
        int len = bankCard.getBankCardNo().length();
        bankCard.setBankCardEndNo(bankCard.getBankCardNo().substring(len-4));
        String[] dateStr = DateUtils.formatDateStr(new Date(),DateFormateType.LANG_FORMAT).split(" ");
        bankCard.setBindDate(dateStr[0]);
        bankCard.setBindTime(dateStr[1]);
        bankCardMapper.insertSelective(bankCard);
        logger.info(bankCard.toString());
        return result;
    }

    @Override
    public ResponseResult listRecord(UserInfo user, BankCard bankCard) throws Exception {
        logger.info("查询银行卡列表...");
        ResponseResult result = new ResponseResult();
        bankCard.setuId(user.getId());
        Long total = bankCardMapper.selectPageTotalCount(bankCard);
        List<BankCard> pageList = bankCardMapper.selectPageByObjectForList(bankCard);
        PageTools pageTools = bankCard.getPageTools();
        pageTools.setTotal(total);
        result.addBody("resultList", pageList);
        result.addBody("pageTools", pageTools);
        return result;
    }

}
