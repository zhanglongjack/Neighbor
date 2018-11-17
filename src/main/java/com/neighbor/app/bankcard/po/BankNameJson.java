package com.neighbor.app.bankcard.po;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;

@Component
public class BankNameJson {
    private static final Logger logger = LoggerFactory.getLogger(BankNameJson.class);
    public JSONObject json;

    @Value(value="classpath:bankname.json")
    private Resource resource;


    @PostConstruct
    public void init() {
        try {
            if(json==null){
                logger.info(">>> init bank name json");
                String bankname = IOUtils.toString(resource.getInputStream(), Charset.forName("UTF-8"));
                json = JSON.parseObject(bankname);
            }
        } catch (Exception e) {
            logger.info("init bank name json ERROR <<< "+e.getMessage(),e);
        }
    }

    public String getBankName(String bankCode){
        if(json==null)return null;
        return json.getString(bankCode);
    }


}
