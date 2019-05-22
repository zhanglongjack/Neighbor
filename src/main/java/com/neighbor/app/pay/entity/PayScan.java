package com.neighbor.app.pay.entity;

public class PayScan {
    private String store_id;
    private Long total;
    private String nonce_str;
    private String body;
    private String out_trade_no;

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

	@Override
	public String toString() {
		return String.format("PayScan [store_id=%s, total=%s, nonce_str=%s, body=%s, out_trade_no=%s]", store_id, total,
				nonce_str, body, out_trade_no);
	}
    
    
}
