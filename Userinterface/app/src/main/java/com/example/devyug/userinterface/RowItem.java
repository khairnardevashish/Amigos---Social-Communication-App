package com.example.devyug.userinterface;

/**
 * Created by Aylar-HP on 05/01/2016.
 */
public class RowItem {

    private String member_name;
    private String profile_pic_id;

    public RowItem(String member_name, String profile_pic_id) {

        this.member_name = member_name;
        this.profile_pic_id = profile_pic_id;

    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getProfile_pic_id() {
        return profile_pic_id;
    }

    public void setProfile_pic_id(String profile_pic_id) {
        this.profile_pic_id = profile_pic_id;
    }

}
