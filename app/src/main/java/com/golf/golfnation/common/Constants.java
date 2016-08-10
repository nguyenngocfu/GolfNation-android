package com.golf.golfnation.common;

/**
 * Created by nguyenngocfu on 7/3/15.
 */
public interface Constants {

    public static final String ROOT_API = "http://dynobranding.com/client/golfnation/api/";
    public static final String SIGN_IN_URL = ROOT_API + "signin.php?user_name=%s&password=%s";
    public static final String SOCIAL_SIGNIN_URL = ROOT_API + "signin.php?user_name=%s&sign_up_type=%d";
    public static final String REGISTER_URL = ROOT_API + "signup.php?sign_up_type=0&email=%s&password=%s";
    public static final String USER_PROFILE_URL = ROOT_API + "user_profile.php?user_id=%s";

    public static final String SOCIAL_REGISTER_URL = ROOT_API + "signup.php?sign_up_type=%d&email=%s";
    public static final String FORGOT_PASSWORD_URL = ROOT_API + "forgotpassword.php?email=%s";
    public static final String RESET_PASSWORD_URL = ROOT_API + "reset_password.php?email=%s&oldpassword=%s&password=%s";
    public static final String UPLOAD_USER_PROFILE__URL = ROOT_API + "user_photo_upload.php";
    public static final String UPLOAD__IMAGE_URL = ROOT_API + "game_photo_upload.php";
    public static final String UPDATE_PROFILE_URL = ROOT_API + "update_profile.php?user_id=%s&name=%s&email=%s&username=%s&location=%s&zip_code=%s&home_course=%s&handicap=%s";
    public static final String CREATE_GAME_URL = ROOT_API + "create_game.php?user_id=%s&game_name=%s&golf_course=%s&game_type=%s&cost=%s&e_date=%s&e_time=%s&details=%s&phone=%s&password=%s&player=%s";
    public static final String GAME_DETAIL_URL = ROOT_API + "game_details.php?game_id=%s";
    public static final String SEARCH_GAMES_URL = ROOT_API +  "search_games.php?keyword=%s";
    public static final String GAMES_JOIN_URL = ROOT_API + "my_games_join.php?user_id=%s";
    //public static final String GAM
    //public static final String JOIN_GAME_URL = ROOT_API + "game_join.php?game_id=%s&user_id=%s";
    public static final String JOIN_GAME_URL = ROOT_API + "game_join.php?game_id=%s&user_id=%s&transaction_id=%s";
    public static final String JOIN_STATUS_URL = ROOT_API + "join_status.php?game_id=%s&user_id=%s";
    public static final String GAME_MEMBER_URL = ROOT_API + "game-users.php?game_id=%s";
    public static final String MANAGEDGAMES_URL = ROOT_API + "my_games.php?user_id=%s";
    public static final String MYGAMES_URL = ROOT_API + "game-users.php?game_id=%s";
    public static final String ALL_GAMES_URL = ROOT_API + "all_games.php";
    public static final String ADD_ORDER_URL = ROOT_API + "add_order.php?game_id=%s&user_id=%s&creator_id=%s&cost=%s&status=Done&transaction_id=%s";
    public static final String ORDER_LIST_URL = ROOT_API + "order_list.php?user_id=%s";
    public static final String MANAGE_WINNER_URL = ROOT_API + "game-winner.php?game_id=%s&winner_id=%s&winner_price=%s";

    //public static final String COURSE_LIST_URL = ROOT_API + "dropdown_golf_course.php";
    //public static final String COURSE_LIST_URL = ROOT_API + "dropdown_golf_course.php?city_name=%s";
    public static final String COURSE_LIST_URL = ROOT_API + "dropdown_golf_course.php?city_name=%s&state_name=%s";
    public static final String TYPE_LIST_URL = ROOT_API + "dropdown_game_type.php";
    public static final String STATE_LIST_URL = ROOT_API + "dropdown_state.php";
    //public static final String CITY_LIST_URL = ROOT_API + "dropdown_city.php?state_id=%s";
    public static final String CITY_LIST_URL = ROOT_API + "dropdown_city.php?state_name=%s";
    public static final String TERM_OF_USE = "www.golfnationapp.com/terms.html";

    public interface Status {
        public static final String STATUS_OK = "000";
    }

    public interface Key {
        public static final String GAME_DETAIL = "game_detail";
        public static final String LOGIN = "login";
        public static final String USER_ID = "user_id";
    }

    public interface Format {
        public static final String DATE_DISPLAY_FORMAT = "MM/dd/yy";
        public static final String DATE_API_FORMAT = "yyyy-MM-dd";
        public static final String TIME_DISPLAY_FORMAT = "hh:mm aa";
        public static final String TIME_API_FORMAT = "hh:mm";
    }
}
