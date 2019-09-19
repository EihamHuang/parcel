package com.parcel.data;

import cn.bmob.v3.BmobObject;

public class Evaluation extends BmobObject {
    private Integer star;   // 星级1~5
    private String review;  // 评论
    private MyUser reviewer;    // 评论者
    private MyUser receiver;    // 被评论者

    public Integer getStar() {
        return star;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public String getReview() { return review; }

    public void setReview(String review) { this.review = review; }

    public MyUser getReviewer() { return reviewer; }

    public void setReviewer(MyUser reviewer) { this.reviewer = reviewer; }

    public MyUser getReceiver() { return receiver; }

    public void setReceiver(MyUser receiver) { this.receiver = receiver; }

}
