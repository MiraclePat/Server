package com.miraclepat.pat.entity;

import com.miraclepat.pat.constant.ImgType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter //필수
@Setter //선택
@ToString //나중에 나오는 필드들에 대해 문자열을 자동으로 만들어줌
@NoArgsConstructor//빈생성자
public class PatImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //mysql
    @Column(name = "pat_img_id")
    private Long id;

    private String imgName; //관리할 때 사용할 이름

    private String oriImgName; //원래 이미지 이름

    private String imgUrl;  //이미지 경로

    @Enumerated(EnumType.STRING)
    private ImgType imgType; //어떤 이미지인가?

    @ManyToOne(fetch = FetchType.LAZY) //다대일 단방향
    @JoinColumn(name = "pat_id")
    private Pat pat;

    //변경감지
    public void updateImg(String oriImgName, String imgName, String imgUrl) {
        this.imgName = imgName;
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
    }
}
