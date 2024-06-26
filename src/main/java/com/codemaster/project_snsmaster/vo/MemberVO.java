package com.codemaster.project_snsmaster.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@ToString
public class MemberVO {

    private String id;
    private String email;
    private String pw;
    private String region;
    private String name;
    private boolean state;
    private String file_name;
    private String grade;

}
