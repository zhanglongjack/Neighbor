package com.neighbor.app.users.entity;

import java.util.Date;

import com.neighbor.common.util.PageTools;

public class UserInfo {
    private Long uId;

    private String name;

    private String wechatNumber;

    private String password;
    private String newPwd;

    private Integer uLevel;

    private Long phone;

    private String entryDate;

    private Date createDate;

	private PageTools pageTools;
	
    public Long getuId() {
        return uId;
    }

    public void setuId(Long uId) {
        this.uId = uId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWechatNumber() {
        return wechatNumber;
    }

    public void setWechatNumber(String wechatNumber) {
        this.wechatNumber = wechatNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPwd() {
		return newPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}

	public Integer getuLevel() {
		return uLevel;
	}

	public void setuLevel(Integer uLevel) {
		this.uLevel = uLevel;
	}

	public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    
	public boolean isAdmin() {
		return this.uLevel==0;
	}
	
	public PageTools getPageTools() {
		return pageTools;
	}

	public void setPageTools(PageTools pageTools) {
		this.pageTools = pageTools;
	}

	@Override
	public String toString() {
		return String.format(
				"UserInfo [uId=%s, name=%s, wechatNumber=%s, password=%s, uLevel=%s, phone=%s, entryDate=%s, createDate=%s]",
				uId, name, wechatNumber, password, uLevel, phone, entryDate, createDate);
	}

    
}