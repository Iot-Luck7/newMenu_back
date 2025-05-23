package com.iot7.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "menu")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MENU_ID")
    private Long menuId;

    @Column(name = "MENU_NAME")
    private String menuName;

    @Column(name = "INGREDIENTS")
    private String ingredients;

    @Column(name = "PRICE")
    private int price;

    @Column(name = "CLICK_COUNT")
    private Long clickCount;

    @Column(name = "CALORIE")
    private Float calorie;

    @Column(name = "CATEGORY")
    private String category;

    @ManyToOne
    @JoinColumn(name = "BUSINESS_ID")
    private BusinessUser businessUser;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "IMAGE_URL")
    private String image;

    @Column(name = "DIET_YN")
    private String dietYn;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Column(name = "AVERAGE_RATING")
    private Float averageRating;

    //localMenu를 가져오기 위한 Menu 연결
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<MenuPos> menuPosList = new ArrayList<>();


}
