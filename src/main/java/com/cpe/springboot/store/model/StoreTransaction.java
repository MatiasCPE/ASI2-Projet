package com.cpe.springboot.store.model;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class StoreTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private Integer userId;
	private Integer cardId;
	private Integer storeId;

	private float amount;
	private StoreAction action;
    private java.sql.Timestamp timeSt;
	
	public StoreTransaction() {
		this.timeSt=new Timestamp(System.currentTimeMillis());
	}

	public StoreTransaction( Integer userId, Integer cardId,Integer storeId,float amount, StoreAction action) {
		super();
		this.userId = userId;
		this.cardId = cardId;
		this.storeId = storeId;
		this.amount = amount;
		this.action = action;
		this.timeSt=new Timestamp(System.currentTimeMillis());
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getCardId() {
		return cardId;
	}

	public void setCardId(Integer cardId) {
		this.cardId = cardId;
	}

	public StoreAction getAction() {
		return action;
	}

	public void setAction(StoreAction action) {
		this.action = action;
	}

	public java.sql.Timestamp getTimeSt() {
		return timeSt;
	}

	public void setTimeSt(java.sql.Timestamp sqlTimestamp) {
		this.timeSt = sqlTimestamp;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}
}