package com.golf.golfnation.game.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nguyenngocfu on 6/17/15.
 */
public class MyGame {
    public static List<MyGame> initFakeData() {
        List<MyGame> games = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            games.add(new MyGame());
        }
        return games;
    }
}
